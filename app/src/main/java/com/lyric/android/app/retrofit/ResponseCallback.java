package com.lyric.android.app.retrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author lyricgan
 * @description 请求默认回调
 * @time 2016/7/26 17:55
 */
public abstract class ResponseCallback<T> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            T responseBody = response.body();
            if (responseBody == null) {
                onError(buildError(ResponseError.ERROR_DATA_EXCEPTION, "Response is null"));
                return;
            }
            onResponse(response.body());
        } else {
            onError(buildError(response.code(), response.message()));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        String errorMessage = t != null ? t.getMessage() : "";
        onError(buildError(ResponseError.ERROR_REQUEST_FAILED, errorMessage));
    }

    private ResponseError buildError(int errorCode, String errorMessage) {
        ResponseError responseError = new ResponseError();
        responseError.setCode(errorCode);
        responseError.setMessage(errorMessage);
        return responseError;
    }

    public abstract void onResponse(T response);

    public abstract void onError(ResponseError error);
}
