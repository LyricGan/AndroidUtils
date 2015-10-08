package com.lrc.baseand.utils;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.MeasureSpec;

/**
 * 图片工具类
 * 
 * @author ganyu
 * 
 */
public class ImageUtils {
	
	/**
	 * 从字节数组中获取图片
	 * @param bytes 字节数组
	 * @return
	 */
	public static Bitmap bytesToBitmap(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	/**
	 * 将Bitmap转换为字节数组
	 * @param bitmap 
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
	
	/**
	 * 从Drawable中获取图片
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {
			return null;
		}
		return ((BitmapDrawable) drawable).getBitmap();
	}
	
	/**
	 * 将Bitmap转换为Drawable
	 * @param bitmap
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }
	
	/**
	 * 将Bitmap转换为Drawable
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
		if (context == null) {
			return null;
		}
        return bitmap == null ? null : new BitmapDrawable(context.getResources(), bitmap);
    }
	
	/**
	 * 将Drawable转换为字节数组
	 * @param drawable
	 * @return
	 */
	public static byte[] drawableToBytes(Drawable drawable) {
        return bitmapToBytes(drawableToBitmap(drawable));
    }
	
	/**
	 * 将字节数组转换为Drawable
	 * @param bytes
	 * @return
	 */
	public static Drawable bytesToDrawable(byte[] bytes) {
        return bitmapToDrawable(bytesToBitmap(bytes));
    }
	
	/**
	 * 从视图中获取图片
	 * @param view 视图
	 * @return
	 */
	public static Bitmap getBitmap(View view) {
		int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		view.measure(measureSpec, measureSpec);
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.translate(-view.getScrollX(), -view.getScrollY());
		view.draw(canvas);
		view.setDrawingCacheEnabled(true);
		Bitmap newBitmap = view.getDrawingCache().copy(Bitmap.Config.ARGB_8888, true);
		view.destroyDrawingCache();
		return newBitmap;
	}
	
	/**
	 * 按宽高缩放图片
	 * @param bitmap
	 * @param newWidth 指定宽度
	 * @param newHeight 指定高度
	 * @return
	 */
	public static Bitmap scale(Bitmap bitmap, int newWidth, int newHeight) {
		return scale(bitmap, (float) newWidth / bitmap.getWidth(), (float) newHeight / bitmap.getHeight());
	}

	/**
	 * 按宽高缩放图片
	 * @param bitmap
	 * @param scaleWidth 缩放宽度
	 * @param scaleHeight 缩放高度
	 * @return
	 */
	public static Bitmap scale(Bitmap bitmap, float scaleWidth, float scaleHeight) {
		if (bitmap == null) {
			return null;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
	
	/**
	 * 图片去色,返回灰度图片
	 * @param bitmap 传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap grayBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(grayBitmap);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
		paint.setColorFilter(colorMatrixFilter);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		
		return grayBitmap;
	}

   /**
    * 去色同时加圆角
    * @param bitmap 原图
    * @param pixels 圆角弧度
    * @return 修改后的图片
    */
   public static Bitmap toGrayscale(Bitmap bitmap, int pixels) {
       return toRoundCorner(toGrayscale(bitmap), pixels);
   }
   
   /**
    * 把图片变成圆角
    * @param bitmap 需要修改的图片
    * @param pixels 圆角的弧度
    * @return 圆角图片
    */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		
		return output;
	}
	
	/**
	 * 获取图片（Bitmap）所占内存
	 * @param bitmap
	 * @return
	 */
	public static long getBitmapMemory(Bitmap bitmap) {
		if (bitmap == null || bitmap.isRecycled()) {
			return -1;
		}
		long size = 0;
		Bitmap.Config config = bitmap.getConfig();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (config == Bitmap.Config.ALPHA_8) {
			size = width * height * 1;
		} else if (config == Bitmap.Config.ARGB_4444) {
			size = width * height * 2;
		} else if (config == Bitmap.Config.ARGB_8888) {
			size = width * height * 4;
		} else if (config == Bitmap.Config.RGB_565) {
			size = width * height * 2;
		}
		return size;
	}

}
