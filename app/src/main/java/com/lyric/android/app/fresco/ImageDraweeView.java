package com.lyric.android.app.fresco;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.lyric.android.app.base.BaseApplication;
import com.lyric.android.library.utils.DisplayUtils;

import java.io.File;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/6 19:18
 */
public class ImageDraweeView extends SimpleDraweeView {
    private static final int SMALL_SIZE;
    private static final int MIDDLE_SIZE;
    private static final int MAX_SIZE;
    private int mWidth;
    private int mHeight;

    static {
        SMALL_SIZE = DisplayUtils.dip2px(BaseApplication.getContext(), 72);
        MIDDLE_SIZE = DisplayUtils.dip2px(BaseApplication.getContext(), 144);
        MAX_SIZE = DisplayUtils.dip2px(BaseApplication.getContext(), 256);
    }

    public ImageDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public ImageDraweeView(Context context) {
        super(context);
    }

    public ImageDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ImageDraweeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void onMeasure() {
        mWidth = getWidth();
        mHeight = getHeight();
    }

    private void addRoundingParams() {
        GenericDraweeHierarchy hierarchy = getHierarchy();
        RoundingParams roundingParams = hierarchy.getRoundingParams();
        if (roundingParams == null) {
            roundingParams = RoundingParams.asCircle();
        }
        hierarchy.setRoundingParams(roundingParams);
    }

    private void setPlaceholderImage(int resId) {
        getHierarchy().setPlaceholderImage(resId, ScalingUtils.ScaleType.FIT_XY);
    }

    public void setImageUrl(String url, int resId) {
        setPlaceholderImage(resId);
        DraweeController controller = ((PipelineDraweeControllerBuilder) getControllerBuilder())
                .setLowResImageRequest(ImageRequest.fromUri(formateUri(url)))
                .setImageRequest(ImageRequest.fromUri(formateUri(url)))
                .setOldController(getController())
                .build();
        setController(controller);
    }

//    public void setImageUrl(String url, int resId) {
//        ControllerListener<ImageInfo> controllerListener = new BaseControllerListener<ImageInfo>() {
//            @Override
//            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable anim) {
//            }
//
//            @Override
//            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
//            }
//
//            @Override
//            public void onFailure(String id, Throwable throwable) {
//            }
//        };
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setControllerListener(controllerListener)
//                .setUri(formateUri(url))
//                .build();
//        getHierarchy().setPlaceholderImage(resId);
//        setController(controller);
//    }

    public void setAvatarUrl(String url, int resId) {
        addRoundingParams();
        setImageUrl(url, resId);
    }

    private Uri formateUri(String url) {
        return url.startsWith("http://") || url.startsWith("https://") ? Uri.parse(url) : Uri.fromFile(new File(url));
    }
}
