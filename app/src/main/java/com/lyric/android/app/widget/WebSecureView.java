package com.lyric.android.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 自定义WebView，修复系统WebView部分漏洞
 * @author lyricgan
 * @time 2017/2/20 18:15
 */
public abstract class WebSecureView extends WebView {
    private static final String[] FILTER_METHODS = { "getClass", "hashCode", "notify", "notifyAll", "equals", "toString", "wait" };
    private static final String VAR_ARG_PREFIX = "arg";
    private static final String MSG_PROMPT_HEADER = "MyApp:";
    private static final String KEY_INTERFACE_NAME = "obj";
    private static final String KEY_FUNCTION_NAME = "func";
    private static final String KEY_ARG_ARRAY = "args";

    private HashMap<String, Object> mJsInterfaceMap = new HashMap<>();
    private String mJavaScriptString;

    public WebSecureView(Context context) {
        this(context, null);
    }

    public WebSecureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebSecureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebSettings();
    }

    @Override
    public void addJavascriptInterface(Object object, String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        mJsInterfaceMap.put(name, object);
    }

    @Override
    public void removeJavascriptInterface(String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            removeJavascriptInterface(this, name);
        } else {
            mJsInterfaceMap.remove(name);
            mJavaScriptString = null;
            injectJavascriptInterfaces();
        }
    }

    protected void initWebSettings() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        // 删除掉安卓系统自带的searchBoxJavaBridge_java接口后门漏洞
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            removeJavascriptInterface(this, "searchBoxJavaBridge_");
        }
        initWebSettings(settings);
    }

    public abstract void initWebSettings(WebSettings settings);

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        super.setWebChromeClient(new SecureWebChromeClient(client));
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(new SecureWebViewClient(client));
    }

    public boolean handleJsInterface(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        String prefix = MSG_PROMPT_HEADER;
        if (!message.startsWith(prefix)) {
            return false;
        }
        String jsonStr = message.substring(prefix.length());
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            String interfaceName = jsonObj.getString(KEY_INTERFACE_NAME);
            String methodName = jsonObj.getString(KEY_FUNCTION_NAME);
            JSONArray argsArray = jsonObj.getJSONArray(KEY_ARG_ARRAY);
            Object[] args = null;
            if (null != argsArray) {
                int count = argsArray.length();
                if (count > 0) {
                    args = new Object[count];
                    for (int i = 0; i < count; ++i) {
                        args[i] = argsArray.get(i);
                    }
                }
            }
            if (invokeJSInterfaceMethod(result, interfaceName, methodName, args)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.cancel();
        return false;
    }

    /**
     * 执行JS方法
     * @param result JsPromptResult对象
     * @param interfaceName 接口名
     * @param methodName 方法名
     * @param args 参数
     * @return true or false
     */
    private boolean invokeJSInterfaceMethod(JsPromptResult result, String interfaceName, String methodName, Object[] args) {
        boolean succeed = false;
        final Object obj = mJsInterfaceMap.get(interfaceName);
        if (null == obj) {
            result.cancel();
            return false;
        }
        Class<?>[] parameterTypes = null;
        int count = 0;
        if (args != null) {
            count = args.length;
        }
        if (count > 0) {
            parameterTypes = new Class[count];
            for (int i = 0; i < count; ++i) {
                parameterTypes[i] = getClassFromJsonObject(args[i]);
            }
        }
        try {
            Method method = obj.getClass().getMethod(methodName, parameterTypes);
            Object returnObj = method.invoke(obj, args);
            boolean isVoid = returnObj == null || returnObj.getClass() == void.class;
            String returnValue = isVoid ? "" : returnObj.toString();
            result.confirm(returnValue);
            succeed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.cancel();
        return succeed;
    }

    private Class<?> getClassFromJsonObject(Object object) {
        Class<?> cls = object.getClass();
        // JS对象只支持int boolean string三种类型
        if (cls == Integer.class) {
            cls = Integer.TYPE;
        } else if (cls == Boolean.class) {
            cls = Boolean.TYPE;
        } else {
            cls = String.class;
        }
        return cls;
    }

    /**
     * 注入Javascript接口
     */
    public void injectJavascriptInterfaces() {
        if (TextUtils.isEmpty(mJavaScriptString)) {
            mJavaScriptString = genJavascriptInterfacesString();
        }
        loadUrl(mJavaScriptString);
    }

    private String genJavascriptInterfacesString() {
        if (mJsInterfaceMap.size() == 0) {
            return null;
        }
        Iterator<Map.Entry<String, Object>> iterator = mJsInterfaceMap.entrySet().iterator();
        StringBuilder script = new StringBuilder();
        script.append("javascript:(function JsAddJavascriptInterface_(){");
        try {
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                createJsMethod(entry.getKey(), entry.getValue(), script);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        script.append("})()");
        return script.toString();
    }

    /**
     * 拼接js代码，过滤掉JS访问调用Java程序时的一些方法
     * @param interfaceName 接口名称
     * @param object Object
     * @param script 用来拼接字符串
     */
    private void createJsMethod(String interfaceName, Object object, StringBuilder script) {
        if (TextUtils.isEmpty(interfaceName) || (null == object) || (null == script)) {
            return;
        }
        Class<?> objClass = object.getClass();
        script.append("if(typeof(window.").append(interfaceName).append(")!='undefined'){");
        script.append("}else {");
        script.append("    window.").append(interfaceName).append("={");

        // Add methods
        Method[] methods = objClass.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            // 过滤掉Object类的相关方法，比如：getClass()方法，因为在JS中就是通过getClass()方法来得到Runtime实例
            if (filterMethods(methodName)) {
                continue;
            }
            script.append("        ").append(methodName).append(":function(");

            // 添加方法的参数
            int argCount = method.getParameterTypes().length;
            if (argCount > 0) {
                int maxCount = argCount - 1;
                for (int i = 0; i < maxCount; ++i) {
                    script.append(VAR_ARG_PREFIX).append(i).append(",");
                }
                script.append(VAR_ARG_PREFIX).append(argCount - 1);
            }
            script.append(") {");

            // Add implementation
            if (method.getReturnType() != void.class) {
                script.append("            return ").append("prompt('").append(MSG_PROMPT_HEADER).append("'+");
            } else {
                script.append("            prompt('").append(MSG_PROMPT_HEADER).append("'+");
            }
            // Begin JSON
            script.append("JSON.stringify({");
            script.append(KEY_INTERFACE_NAME).append(":'").append(interfaceName).append("',");
            script.append(KEY_FUNCTION_NAME).append(":'").append(methodName).append("',");
            script.append(KEY_ARG_ARRAY).append(":[");
            // 添加参数到JSON串中
            if (argCount > 0) {
                int max = argCount - 1;
                for (int i = 0; i < max; i++) {
                    script.append(VAR_ARG_PREFIX).append(i).append(",");
                }
                script.append(VAR_ARG_PREFIX).append(max);
            }
            // End JSON
            script.append("]})");
            // End prompt
            script.append(");");
            // End function
            script.append("        }, ");
        }
        // End of obj
        script.append("    };");
        // End of if or else
        script.append("}");
    }

    private boolean filterMethods(String methodName) {
        for (String method : FILTER_METHODS) {
            if (method.equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 移除Javascript接口
     * @param webView WebView
     * @param params 反射用到的参数
     * @return 返回值
     */
    public static Object removeJavascriptInterface(WebView webView, String params) {
        try {
            Method method = WebView.class.getMethod("removeJavascriptInterface", new Class[] { String.class });
            return method.invoke(webView, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否为可信任的链接
     * @param url 链接地址
     * @return true or false
     */
    public static boolean isDependabilitySite(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        url = url.toLowerCase();
        if (url.startsWith("file://")) {
            return true;
        }
        String host = Uri.parse(url).getHost().toLowerCase();
        if (!TextUtils.isEmpty(host)) {
            if (host.startsWith("http://") || host.startsWith("https://")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加js处理的WebChromeClient
     */
    private static class SecureWebChromeClient extends WebChromeClient {
        private WebChromeClient mWebChromeClient;

        private SecureWebChromeClient(WebChromeClient client) {
            this.mWebChromeClient = client;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            injectJavascriptInterfaces(view);
            mWebChromeClient.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (view instanceof WebSecureView) {
                if (((WebSecureView) view).handleJsInterface(view, url, message, defaultValue, result)) {
                    return true;
                }
            }
            return mWebChromeClient.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            injectJavascriptInterfaces(view);
            mWebChromeClient.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            mWebChromeClient.onReceivedIcon(view, icon);
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            mWebChromeClient.onReceivedTouchIconUrl(view, url, precomposed);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            mWebChromeClient.onShowCustomView(view, callback);
        }

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            mWebChromeClient.onShowCustomView(view, requestedOrientation, callback);
        }

        @Override
        public void onHideCustomView() {
            mWebChromeClient.onHideCustomView();
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return mWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public void onRequestFocus(WebView view) {
            mWebChromeClient.onRequestFocus(view);
        }

        @Override
        public void onCloseWindow(WebView window) {
            mWebChromeClient.onCloseWindow(window);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return mWebChromeClient.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return mWebChromeClient.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return mWebChromeClient.onJsBeforeUnload(view, url, message, result);
        }

        @Override
        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
            mWebChromeClient.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
        }

        @Override
        public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
            mWebChromeClient.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            mWebChromeClient.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            mWebChromeClient.onGeolocationPermissionsHidePrompt();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPermissionRequest(PermissionRequest request) {
            mWebChromeClient.onPermissionRequest(request);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            mWebChromeClient.onPermissionRequestCanceled(request);
        }

        @Override
        public boolean onJsTimeout() {
            return mWebChromeClient.onJsTimeout();
        }

        @Override
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            mWebChromeClient.onConsoleMessage(message, lineNumber, sourceID);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return mWebChromeClient.onConsoleMessage(consoleMessage);
        }

        @Override
        public Bitmap getDefaultVideoPoster() {
            return mWebChromeClient.getDefaultVideoPoster();
        }

        @Override
        public View getVideoLoadingProgressView() {
            return mWebChromeClient.getVideoLoadingProgressView();
        }

        @Override
        public void getVisitedHistory(ValueCallback<String[]> callback) {
            mWebChromeClient.getVisitedHistory(callback);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            return mWebChromeClient.onShowFileChooser(webView, filePathCallback, fileChooserParams);
        }

        private void injectJavascriptInterfaces(WebView view) {
            if (view instanceof WebSecureView) {
                ((WebSecureView) view).injectJavascriptInterfaces();
            }
        }
    }

    /**
     * 添加js处理的WebViewClient
     */
    private static class SecureWebViewClient extends WebViewClient {
        private WebViewClient mWebViewClient;

        private SecureWebViewClient(WebViewClient client) {
            this.mWebViewClient = client;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            injectJavascriptInterfaces(view);
            mWebViewClient.onLoadResource(view, url);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            injectJavascriptInterfaces(view);
            mWebViewClient.doUpdateVisitedHistory(view, url, isReload);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            injectJavascriptInterfaces(view);
            mWebViewClient.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            injectJavascriptInterfaces(view);
            mWebViewClient.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return mWebViewClient.shouldOverrideUrlLoading(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return mWebViewClient.shouldOverrideUrlLoading(view, request);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onPageCommitVisible(WebView view, String url) {
            mWebViewClient.onPageCommitVisible(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            return mWebViewClient.shouldInterceptRequest(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return mWebViewClient.shouldInterceptRequest(view, request);
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
            mWebViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            mWebViewClient.onReceivedError(view, request, error);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            mWebViewClient.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            mWebViewClient.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            mWebViewClient.onReceivedSslError(view, handler, error);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            mWebViewClient.onReceivedClientCertRequest(view, request);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            mWebViewClient.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return mWebViewClient.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            mWebViewClient.onUnhandledKeyEvent(view, event);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            mWebViewClient.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
            mWebViewClient.onReceivedLoginRequest(view, realm, account, args);
        }

        private void injectJavascriptInterfaces(WebView view) {
            if (view instanceof WebSecureView) {
                ((WebSecureView) view).injectJavascriptInterfaces();
            }
        }
    }
}
