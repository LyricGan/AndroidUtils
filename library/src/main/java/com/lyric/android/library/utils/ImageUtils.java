package com.lyric.android.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.View.MeasureSpec;
import android.webkit.WebView;
import android.widget.ScrollView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * 图片工具类
 * 
 * @author ganyu
 * 
 */
public class ImageUtils {

	ImageUtils() {
	}
	
	/**
	 * 从字节数组中获取图片
	 * @param bytes 字节数组
	 * @return Bitmap
	 */
	public static Bitmap bytesToBitmap(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	/**
	 * 将Bitmap转换为字节数组
	 * @param bitmap Bitmap
	 * @return byte[]
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
	 * @param drawable Drawable
	 * @return Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {
			return null;
		}
		return ((BitmapDrawable) drawable).getBitmap();
	}
	
	/**
	 * 将Bitmap转换为Drawable
	 * @param bitmap Bitmap
	 * @return Drawable
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(bitmap);
    }
	
	/**
	 * 将Bitmap转换为Drawable
	 * @param context Context
	 * @param bitmap Bitmap
	 * @return Drawable
	 */
	public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
		if (context == null) {
			return null;
		}
        return bitmap == null ? null : new BitmapDrawable(context.getResources(), bitmap);
    }
	
	/**
	 * 将Drawable转换为字节数组
	 * @param drawable Drawable
	 * @return byte[]
	 */
	public static byte[] drawableToBytes(Drawable drawable) {
        return bitmapToBytes(drawableToBitmap(drawable));
    }
	
	/**
	 * 将字节数组转换为Drawable
	 * @param bytes byte[]
	 * @return Drawable
	 */
	public static Drawable bytesToDrawable(byte[] bytes) {
        return bitmapToDrawable(bytesToBitmap(bytes));
    }
	
	/**
	 * 对指定的视图进行截图
	 * @param view 视图
	 * @return Bitmap
	 */
	public static Bitmap captureBitmap(View view) {
        if (view.getWidth() <= 0 || view.getHeight() <= 0) {
            int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.translate(-view.getScrollX(), -view.getScrollY());
		view.draw(canvas);
		view.setDrawingCacheEnabled(true);
		Bitmap newBitmap = view.getDrawingCache().copy(Config.ARGB_8888, true);
		view.destroyDrawingCache();
		return newBitmap;
	}

    /**
     * 对指定的视图进行截图
     * @param view 视图
     * @return Bitmap
     */
    public static Bitmap captureSimpleBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        // 启用DrawingCache并创建位图
        view.buildDrawingCache();
        // 创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        // 禁用DrawingCache否则会影响性能
        view.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 对ScrollView截图
     * @param scrollView ScrollView
     * @return Bitmap
     */
    public static Bitmap captureBitmap(ScrollView scrollView) {
        int height = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            height += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.WHITE);
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 对WebView截图
     * @param webView WebView
     * @return Bitmap
     */
    public static Bitmap captureBitmap(WebView webView) {
        float scale = webView.getScale();
        int height = (int) (webView.getContentHeight() * scale);
        final Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        final byte[] bytes = stream.toByteArray();
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

	/**
	 * 按宽高缩放图片
	 * @param bitmap Bitmap
	 * @param newWidth 指定宽度
	 * @param newHeight 指定高度
	 * @return Bitmap
	 */
	public static Bitmap scale(Bitmap bitmap, int newWidth, int newHeight) {
		return scale(bitmap, (float) newWidth / bitmap.getWidth(), (float) newHeight / bitmap.getHeight());
	}

	/**
	 * 按宽高缩放图片
	 * @param bitmap Bitmap
	 * @param scaleWidth 缩放宽度
	 * @param scaleHeight 缩放高度
	 * @return Bitmap
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
	public static Bitmap toGrayScale(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap grayBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
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
    * @param color 圆边颜色
    * @return 修改后的图片
    */
   public static Bitmap toGrayScale(Bitmap bitmap, int pixels, int color) {
       return toRoundCorner(toGrayScale(bitmap), pixels, color);
   }
   
    /**
     * 把图片变成圆角
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @param color 圆边颜色
     * @return 圆角图片
     */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels, int color) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, pixels, pixels, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		
		return output;
	}
	
	/**
	 * 获取图片（Bitmap）所占内存
	 * @param bitmap Bitmap
	 * @return long
	 */
	public static long getBitmapMemory(Bitmap bitmap) {
		if (bitmap == null || bitmap.isRecycled()) {
			return -1;
		}
		long size = 0;
		Config config = bitmap.getConfig();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		if (config == Config.ALPHA_8) {
			size = width * height;
		} else if (config == Config.ARGB_4444) {
			size = width * height * 2;
		} else if (config == Config.ARGB_8888) {
			size = width * height * 4;
		} else if (config == Config.RGB_565) {
			size = width * height * 2;
		}
		return size;
	}

    /**
     * 备注：android:scaleType用来控制图片进行resized/moved来匹配ImageView的size
     * CENTER/center 按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
     * CENTER_CROP/centerCrop 按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
     * CENTER_INSIDE/centerInside 将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽
     * FIT_CENTER/fitCenter 把图片按比例扩大/缩小到View的宽度，居中显示
     * FIT_END/fitEnd 把图片按比例扩大/缩小到View的宽度，显示在View的下部分位置
     * FIT_START/fitStart 把图片按比例扩大/缩小到View的宽度，显示在View的上部分位置
     * FIT_XY/fitXY 把图片 不按比例 扩大/缩小到View的大小显示
     * MATRIX/matrix 用矩阵来绘制，动态缩小放大图片来显示
     * @param activity Activity
     * @param uri Uri
     * @param outputX 裁剪宽度
     * @param outputY 裁剪高度
     * @param requestCode If >= 0, this code will be returned in onActivityResult() when the activity exits.
     */
    public static void crop(Activity activity, Uri uri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 不进行人脸检测
        activity.startActivityForResult(intent, requestCode);
    }

    public static Bitmap decodeBitmap(Context context, int drawableId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(drawableId), new Rect(), options);
    }

    /**
     * 解析本地图片
     * @param filePath 文件路径
     * @return Bitmap
     */
    public static Bitmap decodeBitmap(String filePath) {
        Bitmap bitmap = null;
        File file = new File(filePath);
        // 判断文件是否存在
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 1;
            options.inPreferredConfig = Config.RGB_565;
            try {
                bitmap = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 通过URI获取图片
     * @param context 上下文对象
     * @param uri 图片URI
     * @return Bitmap
     */
    public static Bitmap decodeBitmap(Context context, Uri uri) {
        if (null == context || uri == null) {
            return null;
        }
        Bitmap bitmap;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Config.RGB_565;
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 压缩图片大小
     * @param bitmap Bitmap
     * @param kbSize 图片大小KB
     * @return Bitmap
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int kbSize) {
        if (bitmap == null || kbSize < 0) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int quality = 90;
        while (baos.toByteArray().length > kbSize * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return BitmapFactory.decodeStream(bais, null, null);
    }

    /**
     * 压缩图片
     * @param bitmap 图片
     * @param kbSize 图片占内存大小
     * @return Bitmap
     */
    public static Bitmap compress(Bitmap bitmap, int kbSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length > 1024 * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        }
        ByteArrayInputStream byteArrayInStream = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(byteArrayInStream, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int outWidth = newOpts.outWidth;
        int outHeight = newOpts.outHeight;
        float defaultHeight = 800f;
        float defaultWidth = 480f;
        int inSampleSize = 1;
        if (outWidth > outHeight && outWidth > defaultWidth) {// 如果宽度大的话根据宽度固定大小缩放
            inSampleSize = (int) (newOpts.outWidth / defaultWidth);
        } else if (outWidth < outHeight && outHeight > defaultHeight) {// 如果高度高的话根据宽度固定大小缩放
            inSampleSize = (int) (newOpts.outHeight / defaultHeight);
        }
        if (inSampleSize <= 0) {
            inSampleSize = 1;
        }
        newOpts.inSampleSize = inSampleSize;
        byteArrayInStream = new ByteArrayInputStream(baos.toByteArray());
		Bitmap newBitmap = BitmapFactory.decodeStream(byteArrayInStream, null, newOpts);
        return compressBitmap(newBitmap, kbSize);
    }

    /**
     * 获取压缩后的本地图片
     * @param imagePath 本地图片地址
     * @param width 图片宽度
     * @param height 图片高度
     * @return Bitmap
     */
    public static Bitmap getCompressBitmap(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * 计算图片的缩放值
     * @param options BitmapFactory.Options
     * @param width 图片的宽
     * @param height 图片的高
     * @return inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int inSampleSize = 1;
        if (outWidth > width || outHeight > height) {
            final int widthRatio = Math.round((float) outWidth / (float) width);
            final int heightRatio = Math.round((float) outHeight / (float) height);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 对图片做高斯模糊
     * @param context Context
     * @param bitmap Bitmap
     * @param radius 模糊范围：[0, 25)
     * @return 高斯模糊后的图片
     */
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float radius) {
        if (context == null || bitmap == null) {
            return null;
        }
        if (radius <= 0 || radius > 25) {
            radius = 10.0f;
        }
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
            Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
            // set the radius of the blur: 0 < radius <= 25
            intrinsicBlur.setRadius(radius);
            intrinsicBlur.setInput(allIn);
            intrinsicBlur.forEach(allOut);
            allOut.copyTo(outBitmap);
            bitmap.recycle();
            rs.destroy();
        }
        return outBitmap;
    }

}
