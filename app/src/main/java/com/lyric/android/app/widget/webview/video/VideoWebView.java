package com.lyric.android.app.widget.webview.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.Map;

/**
 * This class serves as a WebView to be used in conjunction with a
 * VideoWebChromeClient. It makes possible: - To detect the HTML5 video
 * ended event so that the VideoWebChromeClient can exit full-screen.
 *
 * Important notes: - Javascript is enabled by default and must not be disabled
 * with getSettings().setJavaScriptEnabled(false). - setWebChromeClient() must
 * be called before any loadData(), loadDataWithBaseURL() or loadUrl() method.
 *
 * @author Cristian Perez (http://cpr.name)
 *
 */
public class VideoWebView extends WebView {
	private VideoWebChromeClient videoWebChromeClient;
	private boolean addedJavascriptInterface;

	public VideoWebView(Context context) {
		this(context, null);
	}

	public VideoWebView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VideoWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		addedJavascriptInterface = false;
	}

	/**
	 * Indicates if the video is being displayed using a custom view (typically
	 * full-screen)
	 * 
	 * @return true it the video is being displayed using a custom view
	 *         (typically full-screen)
	 */
	public boolean isVideoFullscreen() {
		return videoWebChromeClient != null && videoWebChromeClient.isVideoFullscreen();
	}

	/**
	 * Pass only a VideoWebChromeClient instance.
	 */
	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void setWebChromeClient(WebChromeClient client) {
		getSettings().setJavaScriptEnabled(true);
		if (client instanceof VideoWebChromeClient) {
			this.videoWebChromeClient = (VideoWebChromeClient) client;
		}
		super.setWebChromeClient(client);
	}

	@Override
	public void loadData(String data, String mimeType, String encoding) {
		addJavascriptInterface();
		super.loadData(data, mimeType, encoding);
	}

	@Override
	public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
		addJavascriptInterface();
		super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
	}

	@Override
	public void loadUrl(String url) {
		addJavascriptInterface();
		super.loadUrl(url);
	}

	@Override
	public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
		addJavascriptInterface();
		super.loadUrl(url, additionalHttpHeaders);
	}

	private void addJavascriptInterface() {
		if (!addedJavascriptInterface) {
			// Add javascript interface to be called when the video ends (must be done before page load) noinspection all
			addJavascriptInterface(new JavascriptInterface(), "_VideoWebView");
			addedJavascriptInterface = true;
		}
	}

	public class JavascriptInterface {
		
		@android.webkit.JavascriptInterface
		public void notifyVideoEnd() {
			// This code is not executed in the UI thread, so we must force that to happen
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					if (videoWebChromeClient != null) {
						videoWebChromeClient.onHideCustomView();
					}
				}
			});
		}
	}
}