package com.lyric.android.app.utils;

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
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
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
     * 获取图片（Bitmap）所占内存大小，加载本地资源图片，占用内存为width * height * nTargetDensity/inDensity * nTargetDensity/inDensity * 一个像素所占的内存
     * @param bitmap Bitmap
     * @return 图片占用内存大小
     */
    public static long getBitmapSize(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return -1;
        }
        long count = 0;
        switch (bitmap.getConfig()) {
            case ALPHA_8:
                count = 1;
                break;
            case ARGB_4444:
            case RGB_565:
                count = 2;
                break;
            case ARGB_8888:
                count = 4;
                break;
        }
        return bitmap.getWidth() * bitmap.getHeight() * count;
    }

    public static long getByteCount(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        } else {
            return getBitmapSize(bitmap);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过URI获取图片
     * @param context 上下文对象
     * @param imageUri 图片URI
     * @return Bitmap or null
     */
    public static Bitmap decodeBitmap(Context context, Uri imageUri) {
        if (null == context || imageUri == null) {
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(imageUri);
            return decodeStream(inputStream, null, 0, 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(inputStream);
        }
        return null;
    }

    public static Bitmap decodeBitmap(String imagePath, int width, int height) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(imagePath);
            return decodeStream(inputStream, null, width, height);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(inputStream);
        }
        return null;
    }

    public static Bitmap decodeStream(InputStream inputStream, Rect outPadding, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, outPadding, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, outPadding, options);
    }

    public static Bitmap decodeResource(Resources resources, int resId, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
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
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        if (outWidth > width || outHeight > height) {
            int widthRatio = Math.round((float) outWidth / (float) width);
            int heightRatio = Math.round((float) outHeight / (float) height);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            if (inSampleSize < 1) {
                inSampleSize = 1;
            }
        }
        return inSampleSize;
    }

    /**
     * 压缩图片质量到指定大小
     * @param bitmap 原图
     * @param format 图片格式，png格式图片质量压缩无效
     * @param targetSize 指定大小，kb
     * @return 压缩后的图片
     * @see android.graphics.Bitmap.CompressFormat#JPEG
     * @see android.graphics.Bitmap.CompressFormat#PNG
     * @see android.graphics.Bitmap.CompressFormat#WEBP
     */
    public static Bitmap compressBitmap(Bitmap bitmap, Bitmap.CompressFormat format, int targetSize) {
        if (bitmap == null || targetSize <= 0) {
            return bitmap;
        }
        if (format == Bitmap.CompressFormat.PNG) {
            return bitmap;
        }
        int quality = 100;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, outputStream);
        while (outputStream.toByteArray().length > targetSize * 1024 && ((quality -= 10) > 0)) {
            outputStream.reset();
            bitmap.compress(format, quality, outputStream);
        }
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return BitmapFactory.decodeStream(inputStream, null, null);
    }

    /**
     * 获取图片旋转角度
     * @param imagePath 图片地址
     * @return 图片旋转角度
     */
    public static int readImageDegree(String imagePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

	/**
	 * 对图片做灰度处理
	 * @param bitmap 需要修改的图片
	 * @return 去色后的图片
     * @see Bitmap.Config#ALPHA_8
     * @see Bitmap.Config#RGB_565
     * @see Bitmap.Config#ARGB_4444
     * @see Bitmap.Config#ARGB_8888
	 */
	public static Bitmap getGrayScaleBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
		Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.RGB_565);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
		paint.setColorFilter(colorMatrixFilter);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		
		return newBitmap;
	}

    /**
     * 对图片做圆角处理
     * @param bitmap 需要修改的图片
     * @param pixels 圆角的弧度
     * @param color 圆边颜色
     * @return 圆角图片
     */
	public static Bitmap getRoundCornerBitmap(Bitmap bitmap, int pixels, int color) {
        if (bitmap == null) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, pixels, pixels, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return newBitmap;
    }

    /**
     * 对图片做高斯模糊
     * @param context 上下文
     * @param bitmap Bitmap
     * @param radius 模糊范围：(0, 25]
     * @return 高斯模糊后的图片
     */
    public static Bitmap getBlurBitmap(Context context, Bitmap bitmap, float radius) {
        if (context == null || bitmap == null) {
            return null;
        }
        if (radius <= 0 || radius > 25) {
            radius = 10.0f;
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            RenderScript renderScript = RenderScript.create(context);
            ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            Allocation allIn = Allocation.createFromBitmap(renderScript, bitmap);
            Allocation allOut = Allocation.createFromBitmap(renderScript, newBitmap);
            // set the radius of the blur: 0 < radius <= 25
            intrinsicBlur.setRadius(radius);
            intrinsicBlur.setInput(allIn);
            intrinsicBlur.forEach(allOut);
            allOut.copyTo(newBitmap);
            bitmap.recycle();
            renderScript.destroy();
        }
        return newBitmap;
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
     * @see android.widget.ImageView.ScaleType#CENTER
     * @see android.widget.ImageView.ScaleType#CENTER_CROP
     * @see android.widget.ImageView.ScaleType#CENTER_INSIDE
     * @see android.widget.ImageView.ScaleType#FIT_START
     * @see android.widget.ImageView.ScaleType#FIT_CENTER
     * @see android.widget.ImageView.ScaleType#FIT_END
     * @see android.widget.ImageView.ScaleType#FIT_XY
     * @see android.widget.ImageView.ScaleType#MATRIX
     */
    public static void crop(Activity activity, Uri uri, int outputX, int outputY, int requestCode) {
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
