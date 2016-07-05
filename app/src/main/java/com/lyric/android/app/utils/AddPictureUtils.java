package com.lyric.android.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.lyric.android.app.view.AddPicturePopup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 添加图片工具类
 * 
 * @author lyricgan
 * @created 2014-8-13
 * 
 */
public class AddPictureUtils {
	/** 返回码：拍照 */
	public static final int REQUEST_CODE_TAKE_PHOTO = 1 << 2;
	/** 返回码：相册*/
	public static final int REQUEST_CODE_PHOTO_ALBUM = 2 << 2;
    private static final String _DEFAULT_AVATAR_PATH = "user_avatar_default.jpg";

    private static AddPictureUtils mInstance;
    private Context mContext;
    private AddPicturePopup mPopupWindow;
    private String mAvatarPath;
    private Uri mAvatarUri;

    public static synchronized AddPictureUtils getInstance() {
        if (mInstance == null) {
            mInstance = new AddPictureUtils();
        }
        return mInstance;
    }

    public void initialize(Context context) {
        this.mContext = context;
        initializeDirectory(context);
        mAvatarPath = getDirectory(context) + File.separator + _DEFAULT_AVATAR_PATH;
        mAvatarUri = Uri.fromFile(new File(mAvatarPath));
    }

    public void showPopup(View view) {
        if (mPopupWindow == null) {
            mPopupWindow = new AddPicturePopup(mContext);
        }
        if (mPopupWindow.isShowing()) {
            return;
        }
        mPopupWindow.show(view);
    }

    public String getAvatarPath() {
        return this.mAvatarPath;
    }

    public Uri getAvatarUri() {
        return this.mAvatarUri;
    }

    public void setOnMenuClickListener(AddPicturePopup.OnMenuClickListener listener) {
        if (mPopupWindow == null) {
            mPopupWindow = new AddPicturePopup(mContext);
        }
        mPopupWindow.setOnMenuClickListener(listener);
    }

    private Intent getTakePhotoIntent(Uri photoUri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        return intent;
    }

    private Intent getPhotoAlbumIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    public void takePhoto(Activity activity, Uri photoUri) {
        activity.startActivityForResult(getTakePhotoIntent(photoUri), REQUEST_CODE_TAKE_PHOTO);
    }

    public void takePhoto(Fragment fragment, Uri photoUri) {
        fragment.startActivityForResult(getTakePhotoIntent(photoUri), REQUEST_CODE_TAKE_PHOTO);
    }

    public void openPhotoAlbum(Activity activity) {
        activity.startActivityForResult(getPhotoAlbumIntent(), REQUEST_CODE_PHOTO_ALBUM);
    }

    public void openPhotoAlbum(Fragment fragment) {
        fragment.startActivityForResult(getPhotoAlbumIntent(), REQUEST_CODE_PHOTO_ALBUM);
    }

	private boolean isSdCardExists() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

    public void destroy() {
        this.mContext = null;
        this.mPopupWindow = null;
    }
	
	private void initializeDirectory(Context context) {
        createDirectory(getDirectory(context));
    }

	private boolean createDirectory(String dirPath) {
        File file = new File(dirPath);
        return file.exists() || file.mkdirs();
    }

    private String getDirectory(Context context) {
        return getSdCardDirectory() + "/Android/data/" + context.getPackageName() + "/" + "cache";
    }

	/**
	 * 获取SD卡路径
	 * @return SD卡路径
	 */
	private String getSdCardDirectory() {
		File sdCardDirectory = null;
		// 判断SD卡是否存在
		if (isSdCardExists()) {
			// 获取根目录
			sdCardDirectory = Environment.getExternalStorageDirectory();
		}
		// 判断根目录是否为空
		if (sdCardDirectory != null) {
			return sdCardDirectory.toString();
		} else {
			return "";
		}
	}
	
	/**
	 * 删除图片文件
	 * @param dirPath 图片文件目录
	 * @param suffix 文件后缀名
	 */
	public void deletePicture(String dirPath, String suffix) {
		// 判断文件后缀名是否为空
		if (TextUtils.isEmpty(suffix)) {
			suffix = ".jpg";
		}
		File fileDir = new File(getDirectory(mContext) + "/" + dirPath);
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
	
	public Bitmap getBitmap(String filePath) {
		Bitmap bitmap = null;
		File file = new File(filePath);
		if (file.exists()) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Config.RGB_565;
			bitmap = BitmapFactory.decodeFile(filePath, options);
			if (bitmap == null) {
				file.delete();
			}
		}
		return bitmap;
	}

    private String createFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return filePath;
    }

    public Bitmap getBitmap(Intent data, int width, int height, String toPath) {
        Bitmap bitmap = null;
        Uri uri = data.getData();
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            // 获取图片文件路径
            String photoPath = cursor.getString(1);
            cursor.close();
            try {
                bitmap = getBitmap(photoPath, width, height, toPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
	
	/**
	 * 获取本地图片文件
	 * @param fromPath 图片来源路径
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @param toPath 图片另存路径
	 * @return Bitmap
	 * @throws IOException
	 */
	public Bitmap getBitmap(String fromPath, int width, int height, String toPath) throws IOException {
		Bitmap bitmap = null;
		if (width <= 0 || height <= 0) {
			return null;
		}
		File file = new File(fromPath);
		if (file.exists()) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getPath(), options);
			final int minSideLength = Math.min(width, height);
			options.inSampleSize = computeInSampleSize(options, minSideLength, width * height);
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Config.RGB_565;
            // 获取图片旋转角度，并处理图片旋转
			int degree = readPictureDegree(file.getAbsolutePath());
			bitmap = BitmapFactory.decodeFile(fromPath, options);
			if (degree > 0) {
				bitmap = rotateBitmap(degree, bitmap);
			}
			// 创建图片文件
			createFile(toPath);
			FileOutputStream outStream = new FileOutputStream(toPath);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream)) {
				outStream.flush();
				outStream.close();
			}
		}
		return bitmap;
	}

    /**
     * 将图片存入文件缓存
     * @param bitmap 图片
     * @param picturePath 图片路径
     */
    public void putBitmap(Bitmap bitmap, String picturePath) {
        if (bitmap == null || TextUtils.isEmpty(picturePath)) {
            return;
        }
        String dir = getDirectory(mContext);
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private int computeInSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
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
	 * 读取图片的旋转角度
	 * 
	 * @param path 图片绝对路径
	 * @return 图片旋转的角度
	 */
    private int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 
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
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param degree 旋转角度
	 * @param bitmap 需要旋转的图片
	 * @return Bitmap
	 */
    private Bitmap rotateBitmap(int degree, Bitmap bitmap) {
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
}
