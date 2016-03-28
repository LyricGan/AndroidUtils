package com.lyric.android.library.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author ganyu
 * @description to create image
 * @time 16/1/16 下午11:02
 */
public class ImageBuilder {
    private Context mContext;

    public ImageBuilder(Context context) {
        this.mContext = context;
    }

    public Bitmap getBitmap(String fileName) {
        Bitmap bitmap = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);

            try {
                fileInputStream.close();
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap getBitmap(int resourceId) {
        return BitmapFactory.decodeResource(mContext.getResources(), resourceId);
    }
}
