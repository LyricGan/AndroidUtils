package com.lyric.android.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

/**
 * 解决TextView设置行间距导致与ImageSpan不对齐的问题
 * @author lyricgan
 * @time 2016/6/16 15:39
 */
public class TextCenterImageSpan extends ImageSpan {

    public TextCenterImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        // ascent: 字体最上端到基线的距离，为负值。descent：字体最下端到基线的距离，为正值。
        // x，要绘制的image的左边框到TextView左边框的距离。y，要替换的文字的基线坐标，即基线到TextView上边框的距离。
        // top，替换行的最顶部位置。bottom，替换行的最底部位置。注意，TextView中两行之间的行间距是属于上一行的，所以这里bottom是指行间隔的底部位置。
        // paint，画笔，包含了要绘制字体的度量信息。
        // 通过getDrawable获取要绘制的image，getBounds是获取包裹image的矩形框尺寸；
        // y + fm.descent得到字体的descent线坐标；
        // y + fm.ascent得到字体的ascent线坐标；
        // 两者相加除以2就是两条线中线的坐标；
        // drawable.getBounds().bottom是image的高度（试想把image放到原点），除以2即高度一半；
        // 前面得到的中线坐标减image高度的一半就是image顶部要绘制的目标位置；最后把目标坐标传递给canvas.translate函数就可以了。
        Drawable drawable = getDrawable();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2 - drawable.getBounds().bottom / 2;
        canvas.save();
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
