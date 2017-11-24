package com.lyric.android.app.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.CharacterSetECI;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * 二维码工具类
 *
 * @author lyricgan
 * @date 2017/11/23 12:59
 */
public class QRCodeUtils {

    private QRCodeUtils() {
    }

    /**
     * 创建二维码位图
     *
     * @param content 字符串内容(支持中文)
     * @param width 位图宽度(单位:px)
     * @param height 位图高度(单位:px)
     * @return Bitmap or null
     */
    public static Bitmap createQRCodeBitmap(String content, int width, int height) {
        return createQRCodeBitmap(content, width, height, 1);
    }

    /**
     * 创建二维码位图
     *
     * @param content 字符串内容(支持中文)
     * @param width 位图宽度(单位:px)
     * @param height 位图高度(单位:px)
     * @param margin 空白边距，要求>=0
     * @return Bitmap or null
     */
    public static Bitmap createQRCodeBitmap(String content, int width, int height, int margin) {
        return createQRCodeBitmap(content, width, height, "UTF-8", "H", margin + "", Color.BLACK, Color.WHITE);
    }

    /**
     * 创建二维码位图 (支持自定义配置和自定义样式)
     *
     * @param content 字符串内容
     * @param width 位图宽度,要求>=0(单位:px)
     * @param height 位图高度,要求>=0(单位:px)
     * @param characterSet 字符集/字符转码格式 (支持格式:{@link CharacterSetECI })。传null时,zxing源码默认使用 "ISO-8859-1"
     * @param errorCorrection 容错级别 (支持级别:{@link ErrorCorrectionLevel })。传null时,zxing源码默认使用 "L"
     * @param margin 空白边距 (可修改,要求:整型且>=0), 传null时,zxing源码默认使用"4"。
     * @param blackColor 黑色色块的自定义颜色值
     * @param whiteColor 白色色块的自定义颜色值
     * @return Bitmap or null
     */
    public static Bitmap createQRCodeBitmap(String content, int width, int height, String characterSet, String errorCorrection, String margin, int blackColor, int whiteColor) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        if (width < 0 || height < 0) {
            return null;
        }
        try {
            // 设置二维码相关配置，生成BitMatrix(位矩阵)对象
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            if (!TextUtils.isEmpty(characterSet)) {
                hints.put(EncodeHintType.CHARACTER_SET, characterSet);
            }
            if (!TextUtils.isEmpty(errorCorrection)) {
                hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection);
            }
            if (!TextUtils.isEmpty(margin)) {
                hints.put(EncodeHintType.MARGIN, margin);
            }
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            // 创建像素数组，并根据BitMatrix(位矩阵)对象为数组元素赋颜色值
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = blackColor; // 黑色色块像素设置
                    } else {
                        pixels[y * width + x] = whiteColor; // 白色色块像素设置
                    }
                }
            }
            // 创建Bitmap对象，根据像素数组设置Bitmap每个像素点的颜色值，之后返回Bitmap对象
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码Bitmap
     * @param content   内容
     * @param width  图片宽度
     * @param height 图片高度
     * @param logoBitmap 二维码中心的Logo图标（可以为null）
     * @param filePath  用于存储二维码图片的文件路径
     * @return 生成的二维码图片
     */
    public static Bitmap createQRCodeBitmap(String content, int width, int height, Bitmap logoBitmap, String filePath) {
        boolean isSuccess = QRCodeUtils.createQRCode(content, width, height, logoBitmap, filePath);
        if (isSuccess) {
            return BitmapFactory.decodeFile(filePath);
        }
        return null;
    }

    /**
     * 生成二维码Bitmap
     * @param content   内容
     * @param width  图片宽度
     * @param height 图片高度
     * @param logoBitmap 二维码中心的Logo图标（可以为null）
     * @param filePath  用于存储二维码图片的文件路径
     * @return 生成二维码及保存文件是否成功
     */
    public static boolean createQRCode(String content, int width, int height, Bitmap logoBitmap, String filePath) {
        try {
            Bitmap bitmap = createQRCodeBitmap(content, width, height);
            if (logoBitmap != null) {
                bitmap = composeLogo(bitmap, logoBitmap);
            }
            // 由于直接返回的bitmap没有任何压缩，内存消耗比较大，使用compress方法将bitmap保存到文件中再进行读取
            return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 在二维码中间添加Logo图案
     * @param srcBitmap 原图片
     * @param logoBitmap logo图片
     * @return Bitmap or null
     */
    private static Bitmap composeLogo(Bitmap srcBitmap, Bitmap logoBitmap) {
        if (srcBitmap == null) {
            return null;
        }
        if (logoBitmap == null) {
            return srcBitmap;
        }
        // 获取图片的宽高
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int logoWidth = logoBitmap.getWidth();
        int logoHeight = logoBitmap.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return srcBitmap;
        }
        // logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 0.2f / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(srcBitmap, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logoBitmap, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }
}
