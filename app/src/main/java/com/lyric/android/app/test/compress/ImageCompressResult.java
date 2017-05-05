package com.lyric.android.app.test.compress;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片压缩实体类
 * @author lyricgan
 * @time 2016/9/7 15:25
 */
public class ImageCompressResult implements Parcelable {
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
