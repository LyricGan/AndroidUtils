package com.lyric.android.app.widget.webview.video;

import android.text.TextUtils;

public class JsInject {
	
	public static String fullScreenByJs(String url) {
		String refer = referParser(url);
		if (!TextUtils.isEmpty(refer)) {
			return "javascript:document.getElementsByClassName('" + refer
					+ "')[0].addEventListener('click',function(){onVideoPlay.onFullScreenClick();return false;});";
		} else {
			return "javascript:";
		}
	}

	public static String referParser(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
		if (url.contains("qq")) {
			return "tvp_fullscreen_button";
		} else if (url.contains("youku")) {
			return "x-zoomin";
		} else if (url.contains("bilibili")) {
			return "icon-widescreen";
		} else if (url.contains("le")) {
			return "hv_ico_screen";
		}
		return "";
	}
}
