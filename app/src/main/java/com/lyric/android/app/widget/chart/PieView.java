package com.lyric.android.app.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * @author lyricgan
 * @description
 * @time 2016/11/16 14:22
 */
public class PieView extends View {
    private static final String TAG = PieView.class.getSimpleName();
    // 颜色表(注意: 此处定义颜色使用的是ARGB，带Alpha通道的)
    private int[] mDefaultColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    /** 饼状图初始绘制角度 */
    private float mStartAngle = 0;
    private ArrayList<PieData> mDataList;
    private int mWidth;
    private int mHeight;
    private Paint mPaint = new Paint();
    private RectF mRectF = new RectF();

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDataList == null || mDataList.isEmpty()) {
            return;
        }
        float currentStartAngle = mStartAngle;
        // 将画布坐标原点移动到中心位置
        canvas.translate(mWidth / 2, mHeight / 2);
        // 饼状图半径
        float r = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);
        mRectF.set(-r, -r, r, r);
        // 饼状图绘制区域
        for (int i = 0; i < mDataList.size(); i++) {
            PieData pieData = mDataList.get(i);
            mPaint.setColor(pieData.getColor());
            canvas.drawArc(mRectF, currentStartAngle, pieData.getAngle(), true, mPaint);
            currentStartAngle += pieData.getAngle();
        }
    }

    /**
     * 设置起始角度
     */
    public void setStartAngle(int startAngle) {
        this.mStartAngle = startAngle;
        invalidate();
    }

    public void setData(ArrayList<PieData> dataList) {
        this.mDataList = dataList;
        initTestData(dataList);
        invalidate();
    }

    private void initTestData(ArrayList<PieData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        float sumValue = 0;
        for (int i = 0; i < dataList.size(); i++) {
            PieData pie = dataList.get(i);
            sumValue += pie.getValue();
            int j = i % mDefaultColors.length;
            pie.setColor(mDefaultColors[j]);
        }
        float sumAngle = 0;
        for (int i = 0; i < dataList.size(); i++) {
            PieData pieData = dataList.get(i);
            float percentage = pieData.getValue() / sumValue;
            float angle = percentage * 360;
            pieData.setPercentage(percentage);
            pieData.setAngle(angle);
            sumAngle += angle;

            Log.d(TAG, "sumAngle:" + sumAngle);
        }
    }

    /**
     * 饼状图数据实体类
     */
    public static class PieData {
        /** 标题 */
        private String name;
        /** 数值 */
        private float value;
        /** 百分比 */
        private float percentage;
        /** 颜色 */
        private int color = 0;
        /** 角度 */
        private float angle = 0;

        public PieData(String name, float value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }
    }
}
