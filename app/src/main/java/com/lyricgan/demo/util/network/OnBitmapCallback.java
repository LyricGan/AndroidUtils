package com.lyricgan.demo.util.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import okhttp3.ResponseBody;
import okhttp3.internal.Util;

/**
 * Bitmap请求回调接口
 * @author Lyric Gan
 * @since 2017/12/28 11:36
 */
public abstract class OnBitmapCallback extends HttpResponseCallback<Bitmap> {

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
