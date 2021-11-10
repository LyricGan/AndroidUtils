package com.lyricgan.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片压缩工具类
 * @author Lyric Gan
 * @since 2016/9/7 15:25
 */
public class ImageCompressor {
    private static final int DEFAULT_OUT_WIDTH = 720;
    private static final int DEFAULT_OUT_HEIGHT = 1080;
    private static final int DEFAULT_MAX_FILE_SIZE = 1024;// KB
    private static final String DEFAULT_OUT_FILE_PATH = "compress/images";
    private ImageCompressListener mCompressListener;

    private ImageCompressor() {
    }

    private static final class ImageCompressorHolder {
        private static final ImageCompressor mInstance = new ImageCompressor();
    }

    public static ImageCompressor getInstance() {
        return ImageCompressorHolder.mInstance;
    }

    public ImageCompressor addListener(ImageCompressListener listener) {
        this.mCompressListener = listener;
        return this;
    }

    public void compress(String srcImagePath) {
        this.compress(srcImagePath, DEFAULT_OUT_WIDTH, DEFAULT_OUT_HEIGHT, DEFAULT_MAX_FILE_SIZE, DEFAULT_OUT_FILE_PATH);
    }

    public void compress(String srcImagePath, int outWidth, int outHeight, int maxFileSize, String outFilePath) {
        new CompressTask(this, mCompressListener).execute(srcImagePath, "" + outWidth, "" + outHeight, "" + maxFileSize, outFilePath);
    }

    /**
     * Can't compress a recycled bitmap
     *
     * @param srcImagePath 原始图片的路径
     * @param outWidth    期望的输出图片的宽度
     * @param outHeight   期望的输出图片的高度
     * @param maxFileSize 期望的输出图片的最大占用的存储空间
     * @param outFilePath 图片文件路径
     * @return 压缩后图片文件路径
     */
    public String execute(String srcImagePath, int outWidth, int outHeight, int maxFileSize, String outFilePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcImagePath, options);
        // 根据原始图片的宽高比和期望的输出图片的宽高比计算最终输出的图片的宽和高
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        float srcRatio = srcWidth / srcHeight;
        float outRatio = outWidth / outHeight;
        float actualOutWidth = srcWidth;
        float actualOutHeight = srcHeight;
        // 计算规则：
        // 如果输入比率小于输出比率，则最终输出的宽度以maxHeight为准
        // 例如输入比为10:20，输出比是300:10，如果要保证输出图片的宽高比和原始图片的宽高比相同，则最终输出图片的高为10，宽度为(10/20) * 10 = 5，最终输出图片的比率为5:10，和原始输入的比率相同。
        // 如果输入比率大于输出比率，则最终输出的高度以maxHeight为准
        // 例如输入比为20:10，输出比是5:100，如果要保证输出图片的宽高比和原始图片的宽高比相同，则最终输出图片的宽为5，高度需要根据输入图片的比率计算获得为5/(20/10)= 2.5，最终输出图片的比率为5:2.5 和原始输入的比率相同
        if (srcWidth > outWidth || srcHeight > outHeight) {
            if (srcRatio < outRatio) {
                actualOutHeight = outHeight;
                actualOutWidth = actualOutHeight * srcRatio;
            } else if (srcRatio > outRatio) {
                actualOutWidth = outWidth;
                actualOutHeight = actualOutWidth / srcRatio;
            } else {
                actualOutWidth = outWidth;
                actualOutHeight = outHeight;
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
        // 压缩失败则返回空
        if (scaledBitmap == null) {
            return null;
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
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
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
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
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
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        return (file.getAbsolutePath() + File.separator + srcFile.getName());
    }

    private static class CompressTask extends AsyncTask<String, Void, ImageCompressResult> {
        private ImageCompressor mCompressHelper;
        private ImageCompressListener mCompressListener;

        CompressTask(ImageCompressor compressHelper, ImageCompressListener listener) {
            this.mCompressHelper = compressHelper;
            this.mCompressListener = listener;
        }

        @Override
        protected ImageCompressResult doInBackground(String... params) {
            String srcPath = params[0];
            int outWidth = Integer.parseInt(params[1]);
            int outHeight = Integer.parseInt(params[2]);
            int maxFileSize = Integer.parseInt(params[3]);
            String outFilePath = params[4];
            String outPutPath = null;
            try {
                outPutPath = mCompressHelper.execute(srcPath, outWidth, outHeight, maxFileSize, outFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ImageCompressResult compressResult = new ImageCompressResult();
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

    /**
     * 图片压缩监听事件
     */
    public interface ImageCompressListener {

        void onPreCompress();

        void onPostCompress(ImageCompressResult result);
    }

    /**
     * 图片压缩实体类
     */
    public static class ImageCompressResult implements Parcelable {
        public static final int RESULT_OK = 0;
        public static final int RESULT_ERROR = 1;

        private int status = RESULT_OK;
        private String srcPath;
        private String outPath;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getSrcPath() {
            return srcPath;
        }

        public void setSrcPath(String srcPath) {
            this.srcPath = srcPath;
        }

        public String getOutPath() {
            return outPath;
        }

        public void setOutPath(String outPath) {
            this.outPath = outPath;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.status);
            dest.writeString(this.srcPath);
            dest.writeString(this.outPath);
        }

        public ImageCompressResult() {
        }

        protected ImageCompressResult(Parcel in) {
            this.status = in.readInt();
            this.srcPath = in.readString();
            this.outPath = in.readString();
        }

        public static final Parcelable.Creator<ImageCompressResult> CREATOR = new Parcelable.Creator<ImageCompressResult>() {
            @Override
            public ImageCompressResult createFromParcel(Parcel source) {
                return new ImageCompressResult(source);
            }

            @Override
            public ImageCompressResult[] newArray(int size) {
                return new ImageCompressResult[size];
            }
        };
    }
}
