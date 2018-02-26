package com.lyric.android.app.test.fresco;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.text.TextUtils;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.cache.disk.FileCache;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;

import javax.annotation.Nullable;

/**
 * 图片加载工具类<br/>
 * 使用前必须调用{@link #initialize(Context)}来初始化加载库，否则会引起空指针异常
 * @author lyricgan
 */
public class ImageLoader {

    public static void initialize(Context context) {
        Fresco.initialize(context);
    }

    public static void loadImage(SimpleDraweeView draweeView, String imageUrl, ControllerListener<ImageInfo> listener) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(listener)
                .setUri(imageUrl)
                .build();
        draweeView.setController(controller);
    }

    public static ImagePipelineFactory getImagePipelineFactory() {
        return Fresco.getImagePipelineFactory();
    }

    public static File getImageFile(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }
        FileCache fileCache = getImagePipelineFactory().getMainFileCache();
        FileBinaryResource resource = (FileBinaryResource) fileCache.getResource(new SimpleCacheKey(imageUrl));
        if (resource != null) {
            return resource.getFile();
        }
        return null;
    }

    public static class ImageControllerListener extends BaseControllerListener<ImageInfo> {

        public ImageControllerListener() {
            super();
        }

        @Override
        public void onSubmit(String id, Object callerContext) {
            super.onSubmit(id, callerContext);
        }

        @Override
        public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
        }

        @Override
        public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            super.onIntermediateImageSet(id, imageInfo);
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {
            super.onIntermediateImageFailed(id, throwable);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
        }

        @Override
        public void onRelease(String id) {
            super.onRelease(id);
        }
    }
}
