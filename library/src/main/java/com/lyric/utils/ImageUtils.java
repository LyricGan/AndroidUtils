package com.lyric.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片工具类
 * 
 * @author lyricgan
 */
public class ImageUtils {

	private ImageUtils() {
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
	 * @param format 图片格式
	 * @return byte[]
	 */
	public static byte[] bitmapToBytes(Bitmap bitmap, Bitmap.CompressFormat format) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(format, 100, outputStream);
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
	 * @param format 图片格式
	 * @return byte[]
	 */
	public static byte[] drawableToBytes(Drawable drawable, Bitmap.CompressFormat format) {
        return bitmapToBytes(drawableToBitmap(drawable), format);
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
     * 获取图片（Bitmap）所占内存大小
     * @param bitmap Bitmap
     * @return 图片占用内存大小
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
     * 通过URI获取图片
     * @param context 上下文对象
     * @param uri 图片URI
     * @return Bitmap or null
     */
    public static Bitmap decodeBitmap(Context context, Uri uri) {
        if (null == context || uri == null) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            return decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(inputStream);
        }
        return null;
    }

    public static Bitmap decodeBitmap(Context context, int drawableId) {
        Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(drawableId);
        try {
            return decodeStream(inputStream);
        } finally {
            closeQuietly(inputStream);
        }
    }

    public static Bitmap decodeBitmap(String imageFilePath, int width, int height) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(imageFilePath);
            return decodeStream(inputStream, null, width, height);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(inputStream);
        }
        return null;
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap decodeStream(InputStream inputStream) {
        return decodeStream(inputStream, null, 0, 0);
    }

    public static Bitmap decodeStream(InputStream inputStream, Rect outPadding, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, outPadding, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, outPadding, options);
    }

    public static Bitmap decodeResource(Resources resources, int resId) {
        return decodeResource(resources, resId, 0, 0);
    }

    public static Bitmap decodeResource(Resources resources, int resId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd) {
        return decodeFileDescriptor(fd, null, 0, 0);
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd, Rect outPadding, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, outPadding, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, outPadding, options);
    }

    /**
     * 计算图片的缩放倍数
     * @param options BitmapFactory.Options
     * @param width 图片的宽
     * @param height 图片的高
     * @return 缩放倍数，缩放大小为原图的1/x * 1/x
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int width, int height) {
        int inSampleSize = 1;
        if (width <= 0 || height <= 0) {
            return inSampleSize;
        }
        final int outWidth = options.outWidth;
        final int outHeight = options.outHeight;
        if (outWidth > width || outHeight > height) {
            final int widthRatio = Math.round((float) outWidth / (float) width);
            final int heightRatio = Math.round((float) outHeight / (float) height);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        if (inSampleSize < 1) {
            inSampleSize = 1;
        }
        return inSampleSize;
    }

    /**
     * 压缩图片到指定大小，先通过宽高压缩再进行质量压缩
     * @param bitmap 图片
     * @param targetKbSize 图片预计内存大小，单位为KB
     * @param width 图片宽度
     * @param height 图片高度
     * @return Bitmap or null
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int targetKbSize, int width, int height) {
        if (bitmap == null || targetKbSize <= 0) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Bitmap newBitmap = decodeStream(inputStream, null, width, height);
        return compressBitmap(newBitmap, targetKbSize);
    }

    /**
     * 压缩图片到指定大小，通过质量压缩
     * @param bitmap 图片
     * @param targetKbSize 图片预计内存大小，单位为KB
     * @return Bitmap or null
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int targetKbSize) {
        if (bitmap == null || targetKbSize <= 0) {
            return null;
        }
        int quality = 100;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        while (outputStream.toByteArray().length > targetKbSize * 1024 && ((quality -= 10) > 0)) {
            outputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        }
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return BitmapFactory.decodeStream(inputStream, null, null);
    }

	public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            return null;
        }
		return scaleBitmap(bitmap, (float) newWidth / bitmap.getWidth(), (float) newHeight / bitmap.getHeight());
	}

	/**
	 * 按宽高缩放图片
	 * @param bitmap Bitmap
	 * @param scaleWidth 缩放宽度
	 * @param scaleHeight 缩放高度
	 * @return Bitmap
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap, float scaleWidth, float scaleHeight) {
		if (bitmap == null) {
			return null;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
	
	/**
	 * 对图片做灰度处理
	 * @param bitmap 需要修改的图片
	 * @return 去色后的图片
	 */
	public static Bitmap grayScaleBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap convertBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);

		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);
		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);

        Canvas canvas = new Canvas(convertBitmap);
        Paint paint = new Paint();
		paint.setColorFilter(colorMatrixFilter);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		
		return convertBitmap;
	}

    /**
     * 对图片做圆角处理
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @param color 圆边颜色
     * @return 圆角图片
     */
	public static Bitmap roundCornerBitmap(Bitmap bitmap, int pixels, int color) {
        if (bitmap == null) {
            return null;
        }
		Bitmap convertBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(convertBitmap);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, pixels, pixels, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		
		return convertBitmap;
	}
	
    /**
     * 对图片做高斯模糊
     * @param context 上下文
     * @param bitmap Bitmap
     * @param radius 模糊范围：(0, 25]
     * @return 高斯模糊后的图片
     */
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float radius) {
        if (context == null || bitmap == null) {
            return null;
        }
        if (radius <= 0 || radius > 25) {
            radius = 10.0f;
        }
        Bitmap convertBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            RenderScript renderScript = RenderScript.create(context);
            ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            Allocation allIn = Allocation.createFromBitmap(renderScript, bitmap);
            Allocation allOut = Allocation.createFromBitmap(renderScript, convertBitmap);
            // set the radius of the blur: 0 < radius <= 25
            intrinsicBlur.setRadius(radius);
            intrinsicBlur.setInput(allIn);
            intrinsicBlur.forEach(allOut);
            allOut.copyTo(convertBitmap);
            bitmap.recycle();
            renderScript.destroy();
        }
        return convertBitmap;
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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 不进行人脸检测
        activity.startActivityForResult(intent, requestCode);
    }
}
