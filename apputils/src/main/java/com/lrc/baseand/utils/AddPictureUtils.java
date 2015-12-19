package com.lrc.baseand.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lrc.baseand.R;

/**
 * 添加图片工具类
 * 
 * @author ganyu
 * @created 2014-8-13
 * 
 */
public class AddPictureUtils {
	private static final String TAG = AddPictureUtils.class.getSimpleName();
	/** 返回码：拍照 */
	public static final int REQUEST_CODE_TAKE_PHOTO = 1;
	/** 返回码：相册*/
	public static final int REQUEST_CODE_PHOTO_ALBUM = 2;
	/** 返回码：图片预览 */
	public static final int REQUEST_CODE_PREVIEW_PICTURE = 3;
	
	/**
	 * 调用系统相机拍照
	 * @param activity
	 * @param photoUri
	 */
	public static void takePhoto(Activity activity, Uri photoUri) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
	}
	
	/**
	 * 调用系统相册
	 * @param activity
	 */
	public static void openPhotoAlbum(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		activity.startActivityForResult(intent, REQUEST_CODE_PHOTO_ALBUM);
	}
	
	/**
	 * 获取添加图片视图
	 * @param onClickListener
	 * @return
	 */
	public static PopupWindow getAddPictureWindow(Context context, OnClickListener onClickListener) {
		View view_menu_add_picture = View.inflate(context, R.layout.view_menu_add_picture, null);
		PopupWindow addPictureWindow = new PopupWindow(view_menu_add_picture, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		TextView tv_take_photo = (TextView) view_menu_add_picture.findViewById(R.id.tv_take_photo);
		TextView tv_photo_album = (TextView) view_menu_add_picture.findViewById(R.id.tv_photo_album);
		Button btn_cancel = (Button) view_menu_add_picture.findViewById(R.id.btn_cancel);
		
		addPictureWindow.setBackgroundDrawable(new ColorDrawable(-0000));
		addPictureWindow.setOutsideTouchable(true);
		addPictureWindow.setFocusable(true);
		addPictureWindow.setAnimationStyle(R.style.PopupWindowAnimation);
		addPictureWindow.update();
		
		tv_take_photo.setOnClickListener(onClickListener);
		tv_photo_album.setOnClickListener(onClickListener);
		btn_cancel.setOnClickListener(onClickListener);
		
		return addPictureWindow;
	}
	
	/**
	 * 获取添加图片对话框
	 * 
	 * @param context
	 * @param onClickListener
	 * @return
	 */
	public static Dialog getAddPictureDialog(final Context context, OnClickListener onClickListener) {
		final Dialog dialog = new Dialog(context, R.style.AddPictureDialog);
		LinearLayout layout = (LinearLayout) View.inflate(context, R.layout.view_menu_add_picture, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		
		// 绑定控件
		TextView tv_take_photo = (TextView) layout.findViewById(R.id.tv_take_photo);
		TextView tv_photo_album = (TextView) layout.findViewById(R.id.tv_photo_album);
		Button btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
		
		Window window = dialog.getWindow();
		LayoutParams lp = window.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dialog.onWindowAttributesChanged(lp);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(layout);
		
		// 注册监听事件
		tv_take_photo.setOnClickListener(onClickListener);
		tv_photo_album.setOnClickListener(onClickListener);
		btn_cancel.setOnClickListener(onClickListener);
		
		return dialog;
	}
	
	/**
	 * 判断SD卡是否可用
	 * @return SD卡是否可用
	 */
	public static boolean isSdCardExists() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 将图片存入文件缓存
	 * @param context 上下文对象
	 * @param bitmap Bitmap对象
	 * @param picturePath 图片地址
	 */
	public static void putBitmap(Context context, Bitmap bitmap, String picturePath) {
		if (bitmap == null) {
			return;
		}
		String dir = getDirectory(context);
		File dirFile = new File(dir);
		// 判断文件目录是否存在
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(picturePath);
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			LogUtils.e(TAG, "FileNotFoundException:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LogUtils.e(TAG, "IOException:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得缓存目录
	 * @return 缓存目录
	 */
	public static String getDirectory(Context context) {
		return getSdCardDir() + "/Android/data/" + context.getPackageName() + "/" + "cache";
	}
	
	/**
	 * 创建目录
	 * @param dirPath
	 * @return
	 */
	public static String createDirectory(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return dirPath;
	}
	
	/**
	 * 创建文件
	 * @param dirPath
	 * @return
	 * @throws IOException 
	 */
	public static String createFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		return filePath;
	}
	
	/**
	 * 获取SD卡路径
	 * @return SD卡路径
	 */
	public static String getSdCardDir() {
		File sdCardDir = null;
		// 判断SD卡是否存在
		if (isSdCardExists()) {
			// 获取根目录
			sdCardDir = Environment.getExternalStorageDirectory();
		}
		// 判断根目录是否为空
		if (sdCardDir != null) {
			return sdCardDir.toString();
		} else {
			return "";
		}
	}
	
	/**
	 * 删除图片文件
	 * @param dirPath 图片文件目录
	 * @param suffix 文件后缀名
	 */
	public static void deletePicture(Context context, String dirPath, String suffix) {
		// 判断文件后缀名是否为空
		if (TextUtils.isEmpty(suffix)) {
			suffix = ".jpg";
		}
		File fileDir = new File(getDirectory(context) + "/" + dirPath);
		File[] fileArray = fileDir.listFiles();
		// 判断文件数组是否为空
		if (fileArray == null || fileArray.length <= 0) {
			return;
		}
		int length = fileArray.length;
		for (int i = 0; i < length; i++) {
			if (fileArray[i].getName().contains(suffix)) {
				fileArray[i].delete();
			}
		}
	}
	
	/**
	 * 获取本地图片
	 * @param path 本地图片路径
	 * @return
	 */
	public static Bitmap getBitmap(String path) {
		File file = new File(path);
		if (file.exists()) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getPath(), opts);
			opts.inSampleSize = 2;
			opts.inJustDecodeBounds = false;
			opts.inInputShareable = true;
			opts.inPurgeable = true;
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeFile(path, opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			if (bitmap == null) {
				file.delete();
			} else {
				return bitmap;
			}
		}
		return null;
	}
	
	/**
	 * 获取本地图片文件
	 * @param fromPath 图片来源路径
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @param toPath 图片另存路径
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getBitmap(String fromPath, int width, int height, String toPath) throws IOException {
		File file = new File(fromPath);
		if (null != file && file.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(file.getPath(), opts);
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			Bitmap bitmap = BitmapFactory.decodeFile(fromPath, opts);
			int degree = readPictureDegree(file.getAbsolutePath());
			Bitmap newBitmap = rotateBitmap(degree, bitmap);
			// 创建图片文件
			createFile(toPath);
			FileOutputStream outStream = new FileOutputStream(toPath);
			if (newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream)) {
				outStream.flush();
				outStream.close();
			}
			return newBitmap;
		}
		return null;
	}
	
	/**
	 * 计算缩放比例
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
	    int scaleSize;
	    double outWidth = options.outWidth;
	    double outHeight = options.outHeight;
	    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(outWidth * outHeight / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(outWidth / minSideLength), Math.floor(outHeight / minSideLength));
	    if (upperBound < lowerBound) {
	    	scaleSize = lowerBound;
	    }
	    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
	    	scaleSize = 1;
	    } else if (minSideLength == -1) {
	    	scaleSize = lowerBound;
	    } else {
	    	scaleSize = upperBound;
	    }
	    int inSampleSize;
	    if (scaleSize <= 8) {
	        inSampleSize = 1;
	        while (inSampleSize < scaleSize) {
	            inSampleSize <<= 1;
	        }
	    } else {
	        inSampleSize = (scaleSize + 7) / 8 * 8;
	    }
	    return inSampleSize;
	}
	
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
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
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
		Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
        		bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

}
