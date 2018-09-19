package com.lyric.android.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.CookieHandler;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * WebView Compat
 *
 * @author lyricgan
 */
public class WebViewCompat extends WebView {
    private static final String[] FILTER_METHODS = { "getClass", "hashCode", "notify", "notifyAll", "equals", "toString", "wait" };
    private static final String VAR_ARG_PREFIX = "arg";
    private static final String MSG_PROMPT_HEADER = "MyApp:";
    private static final String KEY_INTERFACE_NAME = "obj";
    private static final String KEY_FUNCTION_NAME = "func";
    private static final String KEY_ARG_ARRAY = "args";

    private HashMap<String, Object> mJsInterfaceMap = new HashMap<>();
    private String mJavaScriptString;
    private LinkedList<String> mOverrideUrls = new LinkedList<>();

    public WebViewCompat(Context context) {
        this(context, null);
    }

    public WebViewCompat(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebViewCompat(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public void removeJavascriptInterface(@NonNull String name) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            removeJavascriptInterface(this, name);
        } else {
            mJsInterfaceMap.remove(name);
            mJavaScriptString = null;
            injectJavascriptInterfaces();
        }
    }

    @Override
    public void setWebChromeClient(WebChromeClient webChromeClient) {
        super.setWebChromeClient(new WebChromeClientDelegate(webChromeClient));
    }

    @Override
    public void setWebViewClient(WebViewClient webViewClient) {
        super.setWebViewClient(new WebViewClientDelegate(webViewClient, mOverrideUrls));
    }

    @Override
    public void loadUrl(String url) {
        if (isValidUrl(url)) {
            super.loadUrl(url);
        }
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (isValidUrl(url)) {
            super.loadUrl(url, additionalHttpHeaders);
        }
    }

    protected void initWebSettings() {
        WebSettings settings = getSettings();
        // 删除掉安卓系统自带的searchBoxJavaBridge_java接口后门漏洞
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            removeJavascriptInterface(this, "searchBoxJavaBridge_");
        }
        initWebSettings(settings, getContext());
    }

    public void initWebSettings(WebSettings settings, Context context) {
        settings.setAllowFileAccess(false);
        setAllowAccessFromFileUrls(settings, false);

        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(context.getCacheDir().getAbsolutePath());
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        }
        settings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            settings.setDatabasePath(context.getDir("database", Context.MODE_PRIVATE).getPath());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        } else {
            setDisplayZoomControlsGone(this);
        }
        setWebDebuggingEnabled(false);
    }

    public void setAllowAccessFromFileUrls(WebSettings settings, boolean allowed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(allowed);
            settings.setAllowUniversalAccessFromFileURLs(allowed);
        }
    }

    private void setDisplayZoomControlsGone(View view) {
        try {
            Field field = WebView.class.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController controller = new ZoomButtonsController(view);
            controller.getZoomControls().setVisibility(View.GONE);
            field.set(view, controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setWebDebuggingEnabled(boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(enabled);
        }
    }

    public List<WebHistoryItem> getWebHistoryItems() {
        WebBackForwardList webBackForwardList = copyBackForwardList();
        if (webBackForwardList == null) {
            return null;
        }
        int size = webBackForwardList.getSize();
        if (size <= 0) {
            return null;
        }
        List<WebHistoryItem> webHistoryItems = new ArrayList<>();
        WebHistoryItem webHistoryItem;
        for (int i = 0; i < size; i++) {
            webHistoryItem = webBackForwardList.getItemAtIndex(i);
            if (webHistoryItem == null) {
                continue;
            }
            webHistoryItems.add(webHistoryItem);
        }
        return webHistoryItems;
    }

    public boolean onBackPressed() {
        return onBackPressed(mOverrideUrls, true);
    }

    private boolean onBackPressed(LinkedList<String> overrideUrls, boolean isCanGoBack) {
        if (overrideUrls != null && !overrideUrls.isEmpty()) {
            String url = overrideUrls.removeLast();
            if (!isValidUrl(url)) {
                return onBackPressed(overrideUrls, false);
            }
            loadUrl(url);
            if (TextUtils.equals(url, getUrl())) {
                goBack();
                return onBackPressed(overrideUrls, false);
            }
            return true;
        }
        if (canGoBack()) {
            if (isCanGoBack) {
                goBack();
            }
            return true;
        }
        return false;
    }

    public void onDestroy() {
        try {
            clearHistory();
            loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            ViewParent viewParent = getParent();
            if (viewParent != null) {
                ((ViewGroup) viewParent).removeView(this);
            }
            removeAllViews();
            destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidUrl(String url) {
        return URLUtil.isNetworkUrl(url);
    }

    public static String getCookie(String url) {
        return CookieManager.getInstance().getCookie(url);
    }

    public static void setCookie(String url, String value) {
        CookieManager.getInstance().setCookie(url, value);
    }

    public static void removeCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        String cookieString = cookieManager.getCookie(url);
        if (TextUtils.isEmpty(cookieString)) {
            return;
        }
        for (String cookie : cookieString.split("; ")) {
            cookieManager.setCookie(url, cookie.split("=")[0] + "=");
        }
        flushCookie();
    }

    public static void removeCookies(boolean isSessionOnly, ValueCallback<Boolean> callback) {
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (isSessionOnly) {
                cookieManager.removeSessionCookies(callback);
            } else {
                cookieManager.removeAllCookies(callback);
            }
        } else {
            if (isSessionOnly) {
                cookieManager.removeSessionCookie();
            } else {
                cookieManager.removeAllCookie();
            }
        }
        flushCookie();
    }

    public static void flushCookie() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }

    public Object removeJavascriptInterface(WebView webView, String params) {
        try {
            Method method = WebView.class.getMethod("removeJavascriptInterface", new Class[] { String.class });
            return method.invoke(webView, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    private static class WebChromeClientDelegate extends WebChromeClient {
        private WebChromeClient mWebChromeClient;

        private WebChromeClientDelegate(WebChromeClient client) {
            this.mWebChromeClient = client;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            injectJavascriptInterfaces(view);
            mWebChromeClient.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (view instanceof WebViewCompat) {
                if (((WebViewCompat) view).handleJsInterface(view, url, message, defaultValue, result)) {
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mWebChromeClient.onShowCustomView(view, requestedOrientation, callback);
            }
        }

        @Override
        public void onHideCustomView() {
            mWebChromeClient.onHideCustomView();
        }

        @Override
        public void onRequestFocus(WebView view) {
            mWebChromeClient.onRequestFocus(view);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return mWebChromeClient.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
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
        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize,
                                            long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
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

        @Override
        public void onPermissionRequest(PermissionRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebChromeClient.onPermissionRequest(request);
            }
        }

        @Override
        public void onPermissionRequestCanceled(PermissionRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebChromeClient.onPermissionRequestCanceled(request);
            }
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

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return mWebChromeClient.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }
            return false;
        }

        private void injectJavascriptInterfaces(WebView view) {
            if (view instanceof WebViewCompat) {
                ((WebViewCompat) view).injectJavascriptInterfaces();
            }
        }
    }

    private static class WebViewClientDelegate extends WebViewClient {
        private WebViewClient mWebViewClient;
        private LinkedList<String> mOverrideUrls;

        private WebViewClientDelegate(WebViewClient client, LinkedList<String> overrideUrls) {
            this.mWebViewClient = client;
            this.mOverrideUrls = overrideUrls;
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
            if (mOverrideUrls != null) {
                if (!mOverrideUrls.contains(url)) {
                    mOverrideUrls.add(url);
                }
            }
            return mWebViewClient.shouldOverrideUrlLoading(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return mWebViewClient.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mWebViewClient.onPageCommitVisible(view, url);
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                return mWebViewClient.shouldInterceptRequest(view, url);
            }
            return null;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return mWebViewClient.shouldInterceptRequest(view, request);
            }
            return null;
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
            mWebViewClient.onTooManyRedirects(view, cancelMsg, continueMsg);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mWebViewClient.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mWebViewClient.onReceivedError(view, request, error);
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mWebViewClient.onReceivedHttpError(view, request, errorResponse);
            }
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            mWebViewClient.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            mWebViewClient.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWebViewClient.onReceivedClientCertRequest(view, request);
            }
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                mWebViewClient.onReceivedLoginRequest(view, realm, account, args);
            }
        }

        private void injectJavascriptInterfaces(WebView view) {
            if (view instanceof WebViewCompat) {
                ((WebViewCompat) view).injectJavascriptInterfaces();
            }
        }
    }

    public static class WebkitCookieHandler extends CookieHandler {

        @Override
        public void put(URI uri, Map<String, List<String>> headers) {
            if (uri == null || headers == null) {
                return;
            }
            String url = uri.toString();
            for (String headerKey : headers.keySet()) {
                if (headerKey == null || !(headerKey.equalsIgnoreCase("set-cookie2") || headerKey.equalsIgnoreCase("set-cookie"))) {
                    continue;
                }
                for (String headerValue : headers.get(headerKey)) {
                    CookieManager.getInstance().setCookie(url, headerValue);
                }
            }
        }

        @Override
        public Map<String, List<String>> get(URI uri, Map<String, List<String>> headers) {
            if (uri == null || headers == null) {
                return Collections.emptyMap();
            }
            String cookie = CookieManager.getInstance().getCookie(uri.toString());
            if (cookie != null) {
                return Collections.singletonMap("Cookie", Collections.singletonList(cookie));
            } else {
                return Collections.emptyMap();
            }
        }
    }
}
