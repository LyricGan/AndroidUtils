package com.lyric.android.app.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;

/**
 * 截图工具类
 * @author lyricgan
 * @date 2017/11/23 11:36
 */
public class SnapshotUtils {

    private SnapshotUtils() {
    }

    /**
     * 对页面进行截图
     * @param activity 指定activity
     * @param isContainsStatusBar 是否包含状态栏
     * @param startHeight 指定截图开始位置
     * @return 截图生成的bitmap
     */
    public static Bitmap snapShot(Activity activity, boolean isContainsStatusBar, int startHeight) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        int statusBarHeight = 0;
        if (!isContainsStatusBar) {
            Rect frameOutRect = new Rect();
            decorView.getWindowVisibleDisplayFrame(frameOutRect);
            statusBarHeight = frameOutRect.top;
        }
        int actualStartHeight = startHeight + statusBarHeight;
        Display display = activity.getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(decorView.getDrawingCache(), 0, actualStartHeight, screenWidth, screenHeight - actualStartHeight);
        decorView.setDrawingCacheEnabled(false);
        decorView.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 对指定视图进行截图
     * @param view 指定视图
     * @param exactlyWidth 指定宽度
     * @param exactlyHeight 指定高度
     * @return 截图生成的bitmap
     */
    public static Bitmap snapShot(View view, int exactlyWidth, int exactlyHeight) {
        if (view == null) {
            return null;
        }
        // 判断视图宽高是否需要重新测量
        if (view.getWidth() <= 0 || view.getHeight() <= 0) {
            int widthMeasureSpec;
            if (exactlyWidth <= 0) {
                widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            } else {
                widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(exactlyWidth, View.MeasureSpec.EXACTLY);
            }
            int heightMeasureSpec;
            if (exactlyHeight <= 0) {
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            } else {
                heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(exactlyHeight, View.MeasureSpec.EXACTLY);
            }
            view.measure(widthMeasureSpec, heightMeasureSpec);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            if (view.getWidth() <= 0 || view.getHeight() <= 0) {
                return null;
            }
        }
        return snapShot(view);
    }

    public static Bitmap snapShot(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getWidth(), view.getHeight());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 对两张图片进行上下拼接
     * @param topBitmap 顶部图片
     * @param bottomBitmap 底部图片
     * @return 拼接生成的图片
     */
    public static Bitmap composeBitmap(Bitmap topBitmap, Bitmap bottomBitmap) {
        if (topBitmap == null || bottomBitmap == null) {
            return null;
        }
        int width = topBitmap.getWidth();
        int totalHeight = topBitmap.getHeight() + bottomBitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(topBitmap, 0, 0, null);
        canvas.drawBitmap(bottomBitmap, 0, topBitmap.getHeight(), null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBitmap;
    }
}
