package com.lyric.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

/**
 * 图片加载器
 *
 * @author lyricgan
 * @date 17/12/23 下午7:33
 */
public class ImageLoader {

    private ImageLoader() {
    }

    private static final class ImageLoaderHolder {
        private static final ImageLoader IMAGE_LOADER = new ImageLoader();
    }

    public static ImageLoader getInstance() {
        return ImageLoaderHolder.IMAGE_LOADER;
    }

    public RequestOptions getRequestOptions(int placeholderId, int errorId) {
        return RequestOptions.placeholderOf(placeholderId)
                .error(errorId);
    }

    public void load(Context context, String imageUrl, ImageView view) {
        load(context, imageUrl, view, null);
    }

    public void load(Context context, String imageUrl, ImageView view, RequestOptions requestOptions) {
        RequestManager requestManager = Glide.with(context);
        RequestBuilder<Drawable> requestBuilder = requestManager.load(imageUrl);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply(requestOptions);
        }
        requestBuilder.into(view);
    }
}
