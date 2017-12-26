package com.lyric.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;

import java.io.File;

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

    public void load(Context context, Object model, ImageView view) {
        load(context, model, view, -1);
    }

    public void load(Context context, Object model, ImageView view, int placeholderId) {
        load(context, model, view, ImageRequestOptions.getRequestOptions(placeholderId));
    }

    public void load(Context context, Object model, ImageView view, RequestOptions requestOptions) {
        load(context, model, view, requestOptions, Drawable.class);
    }

    public void load(Context context, Object model, ImageView view, RequestOptions requestOptions, TransitionOptions<?, ? super Drawable> transitionOptions) {
        load(context, model, view, requestOptions, Drawable.class, transitionOptions, null);
    }

    public void load(Context context, Object model, ImageView view, RequestOptions requestOptions, TransitionOptions<?, ? super Drawable> transitionOptions, RequestListener<Drawable> listener) {
        load(context, model, view, requestOptions, Drawable.class, transitionOptions, listener);
    }

    public <ResourceType> void load(Context context, Object model, ImageView view, RequestOptions requestOptions, Class<ResourceType> clazz) {
        load(context, model, view, requestOptions, clazz, null, null);
    }

    public <ResourceType> void load(Context context, Object model, ImageView view, RequestOptions requestOptions, Class<ResourceType> clazz,
                                    TransitionOptions<?, ? super ResourceType> transitionOptions) {
        load(context, model, view, requestOptions, clazz, transitionOptions, null);
    }

    /**
     * 加载图片
     * @param context 上下文
     * @param model 加载资源模型
     * @param view 视图
     * @param requestOptions 请求参数
     * @param clazz 泛型类对象，例如Drawable.class
     * @param transitionOptions 过渡选项，例如交叉淡入
     * @param listener 加载监听事件
     * @param <ResourceType> 泛型类参数类型
     */
    public <ResourceType> void load(Context context, Object model, ImageView view, RequestOptions requestOptions, Class<ResourceType> clazz,
                                    TransitionOptions<?, ? super ResourceType> transitionOptions, RequestListener<ResourceType> listener) {
        RequestBuilder<ResourceType> requestBuilder = getRequestBuilder(context, model, requestOptions, clazz, transitionOptions, listener);
        requestBuilder.into(view);
    }

    private <ResourceType> RequestBuilder<ResourceType> getRequestBuilder(Context context, Object model, RequestOptions requestOptions, Class<ResourceType> clazz,
                                                                          TransitionOptions<?, ? super ResourceType> transitionOptions, RequestListener<ResourceType> listener) {
        RequestManager requestManager = Glide.with(context);
        RequestBuilder<ResourceType> requestBuilder = requestManager.as(clazz).load(model);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply(requestOptions);
        }
        if (transitionOptions != null) {
            requestBuilder = requestBuilder.transition(transitionOptions);
        }
        requestBuilder = requestBuilder.listener(listener);
        return requestBuilder;
    }

    public void clear(Context context, View view) {
        Glide.with(context).clear(view);
    }

    public void clear(Activity activity, View view) {
        Glide.with(activity).clear(view);
    }

    public void clear(Fragment fragment, View view) {
        Glide.with(fragment).clear(view);
    }

    public void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }

    public void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }

    public Context getContext(Context context) {
        return Glide.get(context).getContext();
    }

    public File getPhotoCacheDir(Context context) {
        return Glide.getPhotoCacheDir(context);
    }

    public File getPhotoCacheDir(Context context, String cacheName) {
        return Glide.getPhotoCacheDir(context, cacheName);
    }

    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    public void trimMemory(Context context, int level) {
        Glide.get(context).trimMemory(level);
    }

    public void clearDiskCache(Context context) {
        if (Util.isOnMainThread()) {
            return;
        }
        Glide.get(context).clearDiskCache();
    }
}
