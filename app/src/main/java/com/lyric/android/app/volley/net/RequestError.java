package com.lyric.android.app.volley.net;

/**
 * 网络请求异常封装类，继承 {@link Exception}
 */
public class RequestError extends Exception {
	private static final long serialVersionUID = 5044208484926569995L;
	public final NetworkResponse networkResponse;
	private long networkTimeMs;

	public RequestError() {
		networkResponse = null;
	}

	public RequestError(NetworkResponse response) {
		networkResponse = response;
	}

	public RequestError(String exceptionMessage) {
		super(exceptionMessage);
		networkResponse = null;
	}

	public RequestError(String exceptionMessage, Throwable reason) {
		super(exceptionMessage, reason);
		networkResponse = null;
	}

	public RequestError(Throwable cause) {
		super(cause);
		networkResponse = null;
	}

	public void setNetworkTimeMs(long networkTimeMs) {
		this.networkTimeMs = networkTimeMs;
	}

	public long getNetworkTimeMs() {
		return networkTimeMs;
	}
}
