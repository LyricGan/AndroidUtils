package com.lyric.android.app.widget.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ZoomButtonsController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 安全的WebView，修复系统WebView部分漏洞
 * @author lyricgan
 * @time 2017/2/20 18:15
 */
public class SecureWebView extends WebView {
    private static final String[] FILTER_METHODS = { "getClass", "hashCode", "notify", "notifyAll", "equals", "toString", "wait" };
    private static final String VAR_ARG_PREFIX = "arg";
    private static final String MSG_PROMPT_HEADER = "MyApp:";
    private static final String KEY_INTERFACE_NAME = "obj";
    private static final String KEY_FUNCTION_NAME = "func";
    private static final String KEY_ARG_ARRAY = "args";

    private HashMap<String, Object> mJsInterfaceMap = new HashMap<>();
    private String mJavascriptString;

    public SecureWebView(Context context) {
        super(context);
    }

    public SecureWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SecureWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebSettings(this);
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
        if (Build.VERSION.SDK_INT >= 17) {
            removeJavascriptInterface(this, name);
        } else {
            mJsInterfaceMap.remove(name);
            mJavascriptString = null;
            injectJavascriptInterfaces();
        }
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
     * @param result
     * @param interfaceName 接口名
     * @param methodName 方法名
     * @param args 参数
     * @return
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
        if (TextUtils.isEmpty(mJavascriptString)) {
            mJavascriptString = genJavascriptInterfacesString();
        }
        loadUrl(mJavascriptString);
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
     * 拼接JS代码，过滤掉JS访问调用Java程序时的一些方法
     * @param interfaceName 接口名称
     * @param object
     * @param script
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

    public static void initWebSettings(WebView webView) {
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        WebSettings settings = webView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setUseWideViewPort(true);
        settings.setUserAgentString(null);
        settings.setSupportMultipleWindows(true);
        settings.setBuiltInZoomControls(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(false);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setNeedInitialFocus(false);
        settings.setSaveFormData(true);
        settings.setSavePassword(true);
        settings.setDatabasePath(webView.getContext().getDir("databases", 0).getPath());
        // 删除掉安卓系统自带的searchBoxJavaBridge_java接口后门漏洞
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            removeJavascriptInterface(webView, "searchBoxJavaBridge_");
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                Method method = WebSettings.class.getMethod("setDisplayZoomControls", new Class[] { Boolean.TYPE });
                method.invoke(settings, false);
            } else {
                Method method = WebView.class.getMethod("getZoomButtonsController");
                ZoomButtonsController zoom = (ZoomButtonsController) method.invoke(webView);
                zoom.getZoomControls().setLayoutParams(new FrameLayout.LayoutParams(0, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除Javascript接口
     * @param webView
     * @param param
     * @return
     */
    public static Object removeJavascriptInterface(WebView webView, String param) {
        try {
            Method method = WebView.class.getMethod("removeJavascriptInterface", new Class[] { String.class });
            return method.invoke(webView, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否为可信任的站点
     * @param url
     * @return
     */
    public static boolean isDependabilitySite(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        url = url.toLowerCase();
        if (url.startsWith("file://")) {
            return true;
        }
        if (url.startsWith("intent://")) {
            return false;
        }
        String host = Uri.parse(url).getHost().toLowerCase();
        if (!TextUtils.isEmpty(host)) {
            return true;
        }
        return false;
    }

    /**
     * 添加js处理的WebChromeClient
     */
    public static class SecureWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            injectJavascriptInterfaces(view);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            if (view instanceof SecureWebView) {
                if (((SecureWebView) view).handleJsInterface(view, url, message, defaultValue, result)) {
                    return true;
                }
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            injectJavascriptInterfaces(view);
            super.onReceivedTitle(view, title);
        }

        private void injectJavascriptInterfaces(WebView view) {
            if (view instanceof SecureWebView) {
                ((SecureWebView) view).injectJavascriptInterfaces();
            }
        }
    }

    /**
     * 添加js处理的WebViewClient
     */
    public static class SecureWebViewClient extends WebViewClient {

        @Override
        public void onLoadResource(WebView view, String url) {
            injectJavascriptInterfaces(view);
            super.onLoadResource(view, url);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            injectJavascriptInterfaces(view);
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            injectJavascriptInterfaces(view);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            injectJavascriptInterfaces(view);
            super.onPageFinished(view, url);
        }

        private void injectJavascriptInterfaces(WebView view) {
            if (view instanceof SecureWebView) {
                ((SecureWebView) view).injectJavascriptInterfaces();
            }
        }
    }
}
