package com.lyric.android.app.test.compress;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author lyricgan
 * @description 图片压缩工具类
 * @time 2016/9/7 15:25
 */
public class ImageCompressHelper {
    private static final int DEFAULT_OUT_WIDTH = 720;
    private static final int DEFAULT_OUT_HEIGHT = 1080;
    private static final int DEFAULT_MAX_FILE_SIZE = 1024;// KB
    private static final String DEFAULT_OUT_FILE_PATH = "compress/images";
    private static ImageCompressHelper mInstance = null;
    private Context mContext;
    private ImageCompressListener mCompressListener;

    private ImageCompressHelper(Context context) {
        this.mContext = context;
    }

    public static ImageCompressHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ImageCompressHelper.class) {
                if (mInstance == null) {
                    mInstance = new ImageCompressHelper(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public ImageCompressHelper withListener(ImageCompressListener listener) {
        this.mCompressListener = listener;
        return this;
    }

    private String getFilePathFromUri(String uri) {
        Uri pathUri = Uri.parse(uri);
        Cursor cursor = mContext.getContentResolver().query(pathUri, null, null, null, null);
        if (cursor == null) {
            return pathUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String str = cursor.getString(index);
            cursor.close();
            return str;
        }
    }

    public void compress(String srcImageUri) {
        this.compress(srcImageUri, DEFAULT_OUT_WIDTH, DEFAULT_OUT_HEIGHT, DEFAULT_MAX_FILE_SIZE, DEFAULT_OUT_FILE_PATH);
    }

    public void compress(String srcImageUri, int outWidth, int outHeight, int maxFileSize, String outFilePath) {
        new CompressTask().execute(srcImageUri, "" + outWidth, "" + outHeight, "" + maxFileSize, outFilePath);
    }

    /**
     * Can't compress a recycled bitmap
     *
     * @param srcImageUri 原始图片的uri路径
     * @param outWidth    期望的输出图片的宽度
     * @param outHeight   期望的输出图片的高度
     * @param maxFileSize 期望的输出图片的最大占用的存储空间
     * @param outFilePath 图片文件路径
     * @return 压缩后图片文件路径
     */
    public String execute(String srcImageUri, int outWidth, int outHeight, int maxFileSize, String outFilePath) {
        String srcImagePath = getFilePathFromUri(srcImageUri);
        //进行大小缩放来达到压缩的目的
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcImagePath, options);
        //根据原始图片的宽高比和期望的输出图片的宽高比计算最终输出的图片的宽和高
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        float maxWidth = outWidth;
        float maxHeight = outHeight;
        float srcRatio = srcWidth / srcHeight;
        float outRatio = maxWidth / maxHeight;
        float actualOutWidth = srcWidth;
        float actualOutHeight = srcHeight;
        if (srcWidth > maxWidth || srcHeight > maxHeight) {
            //如果输入比率小于输出比率,则最终输出的宽度以maxHeight为准()
            //比如输入比为10:20 输出比是300:10 如果要保证输出图片的宽高比和原始图片的宽高比相同,则最终输出图片的高为10
            //宽度为10/20 * 10 = 5  最终输出图片的比率为5:10 和原始输入的比率相同

            //同理如果输入比率大于输出比率,则最终输出的高度以maxHeight为准()
            //比如输入比为20:10 输出比是5:100 如果要保证输出图片的宽高比和原始图片的宽高比相同,则最终输出图片的宽为5
            //高度需要根据输入图片的比率计算获得 为5 / 20/10= 2.5  最终输出图片的比率为5:2.5 和原始输入的比率相同
            if (srcRatio < outRatio) {
                actualOutHeight = maxHeight;
                actualOutWidth = actualOutHeight * srcRatio;
            } else if (srcRatio > outRatio) {
                actualOutWidth = maxWidth;
                actualOutHeight = actualOutWidth / srcRatio;
            } else {
                actualOutWidth = maxWidth;
                actualOutHeight = maxHeight;
            }
        }
        options.inSampleSize = computeSampleSize(options, actualOutWidth, actualOutHeight);
        options.inJustDecodeBounds = false;
        Bitmap scaledBitmap = null;
        try {
            scaledBitmap = BitmapFactory.decodeFile(srcImagePath, options);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (scaledBitmap == null) {
            return null;// 压缩失败
        }
        // 生成最终输出的bitmap
        Bitmap actualOutBitmap = Bitmap.createScaledBitmap(scaledBitmap, (int) actualOutWidth, (int) actualOutHeight, true);
        if (actualOutBitmap != scaledBitmap) {
            scaledBitmap.recycle();
        }
        // 处理图片旋转问题
        actualOutBitmap = exifProcess(srcImagePath, actualOutBitmap);
        if (actualOutBitmap == null) {
            return null;
        }
        // 进行有损压缩
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        actualOutBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
        int baosLength = baos.toByteArray().length;
        while (baosLength / 1024 > maxFileSize) {// 循环判断如果压缩后图片是否大于maxMemorySize,大于继续压缩
            baos.reset();// 重置baos即让下一次的写入覆盖之前的内容
            quality = Math.max(0, quality - 10);// 图片质量每次减少10
            actualOutBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);// 将压缩后的图片保存到baos中
            baosLength = baos.toByteArray().length;
            if (quality == 0) {// 如果图片的质量已降到最低则，不再进行压缩
                break;
            }
        }
        actualOutBitmap.recycle();
        // 将bitmap保存到指定路径
        FileOutputStream fos = null;
        String filePath = getOutputFileName(srcImagePath, outFilePath);
        try {
            fos = new FileOutputStream(filePath);
            // 包装缓冲流，提高写入速度
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
            bufferedOutputStream.write(baos.toByteArray());
            bufferedOutputStream.flush();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

    private Bitmap exifProcess(String srcImagePath, Bitmap bitmap) {
        ExifInterface exif;
        try {
            exif = new ExifInterface(srcImagePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                matrix.postRotate(90);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                matrix.postRotate(180);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                matrix.postRotate(270);
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private int computeSampleSize(BitmapFactory.Options options, float reqWidth, float reqHeight) {
        float srcWidth = options.outWidth;//20
        float srcHeight = options.outHeight;//10
        int sampleSize = 1;
        if (srcWidth > reqWidth || srcHeight > reqHeight) {
            int withRatio = Math.round(srcWidth / reqWidth);
            int heightRatio = Math.round(srcHeight / reqHeight);
            sampleSize = Math.min(withRatio, heightRatio);
        }
        return sampleSize;
    }

    private String getOutputFileName(String srcFilePath, String outFilePath) {
        File srcFile = new File(srcFilePath);
        File file = new File(Environment.getExternalStorageDirectory().getPath(), outFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + File.separator + srcFile.getName());
    }

    private class CompressTask extends AsyncTask<String, Void, ImageCompressResult> {

        @Override
        protected ImageCompressResult doInBackground(String... params) {
            String srcPath = params[0];
            int outWidth = Integer.parseInt(params[1]);
            int outHeight = Integer.parseInt(params[2]);
            int maxFileSize = Integer.parseInt(params[3]);
            String outFilePath = params[4];
            ImageCompressResult compressResult = new ImageCompressResult();
            String outPutPath = null;
            try {
                outPutPath = ImageCompressHelper.this.execute(srcPath, outWidth, outHeight, maxFileSize, outFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            compressResult.setSrcPath(srcPath);
            compressResult.setOutPath(outPutPath);
            if (outPutPath == null) {
                compressResult.setStatus(ImageCompressResult.RESULT_ERROR);
            } else {
                compressResult.setStatus(ImageCompressResult.RESULT_OK);
            }
            return compressResult;
        }

        @Override
        protected void onPreExecute() {
            if (mCompressListener != null) {
                mCompressListener.onPreCompress();
            }
        }

        @Override
        protected void onPostExecute(ImageCompressResult compressResult) {
            if (mCompressListener != null) {
                mCompressListener.onPostCompress(compressResult);
            }
        }
    }
}
