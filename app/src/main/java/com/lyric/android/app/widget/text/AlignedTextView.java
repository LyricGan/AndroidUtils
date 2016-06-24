package com.lyric.android.app.widget.text;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author lyric
 * @description 自定义TextView，保持文本左右对齐
 * @time 2016/1/7 0:03
 */
public class AlignedTextView extends TextView {
    private static final String TAG = AlignedTextView.class.getSimpleName();
    private static final String _BLANK = " ";
    private static final String _MULTI_BLANK = "  ";
    // 视图宽度
    private int mViewWidth;
    // 视图高度
    private int mViewHeight;

    public AlignedTextView(Context context) {
        super(context);
    }

    public AlignedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlignedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mViewWidth = getMeasuredWidth();
        CharSequence text = getText();
        // 初始化视图高度，防止重复调用setText()
        mViewHeight = 0;
        mViewHeight += getTextSize();
        Layout layout = getLayout();
        if (layout == null || (text instanceof Spannable)) {
            super.onDraw(canvas);
            return;
        }
        int textHeight = getLineHeight();
        int lineCount = layout.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, paint);
            String line = text.toString().substring(lineStart, lineEnd);
            if (i == lineCount - 1) {
                canvas.drawText(line, 0, mViewHeight, paint);
            } else {
                if (isCanScale(line)) {
                    drawScaledText(canvas, line, width);
                } else {
                    canvas.drawText(line, 0, mViewHeight, paint);
                }
                mViewHeight += textHeight;
            }
        }
    }

    private boolean isCanScale(String line) {
        return !TextUtils.isEmpty(line) && (line.charAt(line.length() - 1) != '\n');
    }

    /**
     * 判断是否为段落开头
     * @param line 文本
     * @return boolean
     */
    private boolean isParagraphStart(String line) {
        return line.length() > 2 && (line.charAt(0) == ' ' || line.charAt(0) == 12288) && (line.charAt(1) == ' ' || line.charAt(1) == 12288);
    }

    private void drawScaledText(Canvas canvas, String line, float lineWidth) {
        float blanksWidth = StaticLayout.getDesiredWidth(_BLANK, getPaint());
        float x = 0;
        if (isParagraphStart(line)) {
            String substring = line.substring(0, 2);
            float charsWidth = StaticLayout.getDesiredWidth(substring, getPaint());
            canvas.drawText(substring, x, mViewHeight, getPaint());
            x += charsWidth;
            line = line.substring(2);
        }
        // 判断是否包含多个空格，考虑英文会出现大量空格的情况
        if (line.contains(_MULTI_BLANK)) {
            int length = line.length();
            line = line.replaceAll(_MULTI_BLANK, _BLANK);
            x += blanksWidth * (length - line.length());
        }
        int charCount = line.length();
        float dis = (mViewWidth - lineWidth) / charCount;
        for (int i = 0; i < line.length(); i++) {
            String chars = String.valueOf(line.charAt(i));
            float charsWidth = StaticLayout.getDesiredWidth(chars, getPaint());
            canvas.drawText(chars, x, mViewHeight, getPaint());
            x += charsWidth + dis;
        }
    }

}
