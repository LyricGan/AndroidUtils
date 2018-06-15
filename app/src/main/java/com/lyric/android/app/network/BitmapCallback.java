package com.lyric.android.app.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;

/**
 * Bitmap请求回调接口
 * @author lyricgan
 * @date 2017/12/28 11:36
 */
public abstract class BitmapCallback extends BaseResponseCallback<Bitmap> {

    @Override
    public Bitmap parseResponse(ResponseBody responseBody) {
        InputStream inputStream = null;
        try {
            inputStream = responseBody.byteStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(inputStream);
        }
        return null;
    }
}
