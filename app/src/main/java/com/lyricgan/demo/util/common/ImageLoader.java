package com.lyricgan.demo.util.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;

/**
 * image loader with glide
 *
 * @author Lyric Gan
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
        load(context, model, view, RequestOptions.placeholderOf(placeholderId));
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

    public <ResourceType> void load(Context context, Object model, ImageView view, RequestOptions requestOptions, Class<ResourceType> clazz, TransitionOptions<?, ? super ResourceType> transitionOptions) {
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
    public <ResourceType> void load(Context context, Object model, ImageView view, RequestOptions requestOptions, Class<ResourceType> clazz, TransitionOptions<?, ? super ResourceType> transitionOptions, RequestListener<ResourceType> listener) {
        getRequestBuilder(context, model, requestOptions, clazz, transitionOptions, listener).into(view);
    }

    public <ResourceType> Target<ResourceType> load(Context context, Object model, Target<ResourceType> target, RequestOptions requestOptions, Class<ResourceType> clazz, TransitionOptions<?, ? super ResourceType> transitionOptions, RequestListener<ResourceType> listener) {
        return getRequestBuilder(context, model, requestOptions, clazz, transitionOptions, listener).into(target);
    }

    private <ResourceType> RequestBuilder<ResourceType> getRequestBuilder(Context context, Object model, RequestOptions requestOptions, Class<ResourceType> clazz, TransitionOptions<?, ? super ResourceType> transitionOptions, RequestListener<ResourceType> listener) {
        RequestManager requestManager = getManager(context);
        RequestBuilder<ResourceType> requestBuilder = requestManager.as(clazz).load(model);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply(requestOptions);
        }
        if (transitionOptions != null) {
            requestBuilder = requestBuilder.transition(transitionOptions);
        }
        return requestBuilder.listener(listener);
    }

    public RequestManager getManager(Context context) {
        return Glide.with(context);
    }

    public RequestManager getManager(Activity activity) {
        return Glide.with(activity);
    }

    public Glide getLoader(Context context) {
        return Glide.get(context);
    }

    public File getPhotoCacheDir(Context context) {
        return Glide.getPhotoCacheDir(context);
    }

    public File getPhotoCacheDir(Context context, String cacheName) {
        return Glide.getPhotoCacheDir(context, cacheName);
    }

}
