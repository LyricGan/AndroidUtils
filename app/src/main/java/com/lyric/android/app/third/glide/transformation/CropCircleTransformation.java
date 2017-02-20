package com.lyric.android.app.third.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 圆形图片转换工具类，可以设置环装边框
 */
public class CropCircleTransformation extends BitmapTransformation {
    private static final int DEFAULT_BACKGROUND_COLOR = 0xffe0e0e0;
    private Paint mRingPaint;
    /** 圆形边框宽度 */
    private float mRingWidth;

    public CropCircleTransformation(Context context) {
        super(context);
    }

    public CropCircleTransformation(Context context, int ringColor, float ringWidth) {
        super(context);
        mRingPaint = new Paint();
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setColor(ringColor);
        mRingPaint.setStrokeWidth(ringWidth);
        mRingPaint.setAntiAlias(true);
        mRingWidth = ringWidth;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return cropCircle(pool, toTransform);
    }

    private Bitmap cropCircle(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        int realSize = (int) (size + mRingWidth);
        // 创建空白的bitmap，可以在上面绘制各种图形
        Bitmap result = pool.get(realSize, realSize, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(realSize, realSize, Bitmap.Config.ARGB_8888);
        }
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        // 以result作为画布创建Canvas，选择原图的中心点，以中心点为中心绘制
        float r = size / 2f;
        float realR = realSize / 2f;
        Canvas canvas = new Canvas(result);
        Paint backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(DEFAULT_BACKGROUND_COLOR);
        canvas.drawCircle(realR, realR, realR, backgroundPaint);
        canvas.drawCircle(realR, realR, r, paint);
        // 绘制环形边框
        if (mRingPaint != null) {
            canvas.drawCircle(realR, realR, realR - (mRingWidth / 2), mRingPaint);
        }
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
