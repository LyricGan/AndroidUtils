package com.lyric.android.app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.lyric.android.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eugeni on 16/10/2016.
 */
public class TabDigit extends View implements Runnable {
    private final static int LOWER_POSITION = 0;
    private final static int MIDDLE_POSITION = 1;
    private final static int UPPER_POSITION = 2;

    private int state = LOWER_POSITION;

    long mTime = -1;
    float mElapsedTime = 1000.0f;

    private Tab mTopTab;
    private Tab mBottomTab;
    private Tab mMiddleTab;

    private List<Tab> tabs = new ArrayList<>(3);

    private Matrix mProjectionMatrix = new Matrix();

    private int mAlpha = 0;
    private int mCornerSize;

    private Paint mNumberPaint;
    private Paint mDividerPaint;
    private Paint mBackgroundPaint;

    private Rect mTextMeasured = new Rect();

    private int mPadding = 0;
    private char[] mChars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private int mFrom;
    private int mTo;

    public TabDigit(Context context) {
        this(context, null);
    }

    public TabDigit(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabDigit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TabDigit(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        initPaints();

        int padding = -1;
        int textSize = -1;
        int cornerSize = -1;
        int textColor = 1;
        int backgroundColor = 1;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabDigit, 0, 0);
        final int num = ta.getIndexCount();
        for (int i = 0; i < num; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.TabDigit_numberSize) {
                textSize = ta.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.TabDigit_padding) {
                padding = ta.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.TabDigit_cornerSize) {
                cornerSize = ta.getDimensionPixelSize(attr, -1);
            } else if (attr == R.styleable.TabDigit_numberColor) {
                textColor = ta.getColor(attr, 1);
            } else if (attr == R.styleable.TabDigit_backgroundColor) {
                backgroundColor = ta.getColor(attr, 1);
            }
        }
        ta.recycle();

        if (padding > 0) {
            mPadding = padding;
        }
        if (textSize > 0) {
            mNumberPaint.setTextSize(textSize);
        }
        if (cornerSize > 0) {
            mCornerSize = cornerSize;
        }
        if (textColor < 1) {
            mNumberPaint.setColor(textColor);
        }
        if (backgroundColor < 1) {
            mBackgroundPaint.setColor(backgroundColor);
        }
        initTabs();
    }

    private void initPaints() {
        mNumberPaint = new Paint();
        mNumberPaint.setAntiAlias(true);
        mNumberPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNumberPaint.setColor(Color.WHITE);

        mDividerPaint = new Paint();
        mDividerPaint.setAntiAlias(true);
        mDividerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDividerPaint.setColor(Color.WHITE);
        mDividerPaint.setStrokeWidth(1);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Color.BLACK);
    }

    private void initTabs() {
        // top Tab
        mTopTab = new Tab();
        mTopTab.rotate(180);
        tabs.add(mTopTab);

        // bottom Tab
        mBottomTab = new Tab();
        tabs.add(mBottomTab);

        // middle Tab
        mMiddleTab = new Tab();
        tabs.add(mMiddleTab);

        setInternalChar(0);
    }

    public void setChar(int index) {
        setInternalChar(index);
        invalidate();
    }

    private void setInternalChar(int index) {
        for (Tab tab : tabs) {
            tab.setChar(index);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        calculateTextSize(mTextMeasured);

        int childWidth = mTextMeasured.width() + mPadding;
        int childHeight = mTextMeasured.height() + mPadding;
        measureTabs(childWidth, childHeight);

        int maxChildWidth = mMiddleTab.maxWith();
        int maxChildHeight = 2 * mMiddleTab.maxHeight();
        int resolvedWidth = resolveSize(maxChildWidth, widthMeasureSpec);
        int resolvedHeight = resolveSize(maxChildHeight, heightMeasureSpec);

        setMeasuredDimension(resolvedWidth, resolvedHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            setupProjectionMatrix();
        }
    }

    private void setupProjectionMatrix() {
        mProjectionMatrix.reset();
        int centerY = getHeight() / 2;
        int centerX = getWidth() / 2;
        MatrixHelper.translate(mProjectionMatrix, centerX, -centerY, 0);
    }

    private void measureTabs(int width, int height) {
        for (Tab tab : tabs) {
            tab.measure(width, height);
        }
    }

    private void drawTabs(Canvas canvas) {
        for (Tab tab : tabs) {
            tab.draw(canvas);
        }
    }

    private void drawDivider(Canvas canvas) {
        canvas.save();
        canvas.concat(mProjectionMatrix);
        canvas.drawLine(-canvas.getWidth() / 2, 0, canvas.getWidth() / 2, 0, mDividerPaint);
        canvas.restore();
    }

    private void calculateTextSize(Rect rect) {
        mNumberPaint.getTextBounds("8", 0, 1, rect);
    }

    public void setTextSize(int size) {
        mNumberPaint.setTextSize(size);
        requestLayout();
    }

    public int getTextSize() {
        return (int) mNumberPaint.getTextSize();
    }

    public void setPadding(int padding) {
        mPadding = padding;
        requestLayout();
    }

    /**
     * Sets chars that are going to be displayed.
     * Note: <b>That only one digit is allow per character.</b>
     *
     * @param chars
     */
    public void setChars(char[] chars) {
        mChars = chars;
    }

    public char[]  getChars() {
        return mChars;
    }

    public void setDividerColor(int color) {
        mDividerPaint.setColor(color);
    }

    public int getPadding(){
        return mPadding;
    }

    public void setTextColor(int color) {
        mNumberPaint.setColor(color);
    }

    public int getTextColor() {
        return mNumberPaint.getColor();
    }

    public void setCornerSize(int cornerSize) {
        mCornerSize = cornerSize;
        invalidate();
    }

    public int getCornerSize() {
        return mCornerSize;
    }

    public void setBackgroundColor(int color) {
        mBackgroundPaint.setColor(color);
    }

    public int getBackgroundColor() {
        return mBackgroundPaint.getColor();
    }

    public void start() {
        makeSureCycleIsClosed();
        mTime = System.currentTimeMillis();
        invalidate();
    }

    public void elapsedTime(float elapsedTime) {
        mElapsedTime = elapsedTime;
    }

    public void setFrom(int from) {
        this.mFrom = from;
    }

    public int getFrom() {
        return mFrom;
    }

    public void setTo(int to) {
        this.mTo = to;
    }

    public int getTo() {
        return mTo;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawTabs(canvas);
        drawDivider(canvas);
        ViewCompat.postOnAnimationDelayed(this, this, 40);
    }

    @Override
    public void run() {
        if (mTime == -1) {
            return;
        }
        switch (state) {
            case LOWER_POSITION: {
                nextBottomTab();
                break;
            }
            case MIDDLE_POSITION: {
                if (mAlpha > 90) {
                    nextMiddleTab();
                }
                break;
            }
            case UPPER_POSITION: {
                if (mAlpha >= 180) {
                    nextTopTab();
                }
                break;
            }
        }
        if (mTime != -1) {
            long delta = (System.currentTimeMillis() - mTime);
            mAlpha = (int) (180 * (1 - (1 * mElapsedTime - delta) / (1 * mElapsedTime)));
            mMiddleTab.rotate(mAlpha);
        }
        invalidate();
    }

    private void nextBottomTab(){
        mBottomTab.next();
        state = MIDDLE_POSITION;
    }

    private void nextMiddleTab() {
        mMiddleTab.next();
        state = UPPER_POSITION;
    }

    private void nextTopTab() {
        mTopTab.next();
        mTime = -1;
        state = LOWER_POSITION;
    }

    public void sync() {
        makeSureCycleIsClosed();
        invalidate();
    }

    private void makeSureCycleIsClosed() {
        if (mTime == -1) {
            return;
        }
        switch (state) {
            case LOWER_POSITION: {
                nextMiddleTab();
            }
            case UPPER_POSITION: {
                nextTopTab();
            }
        }
        mMiddleTab.rotate(180);
    }

    public class Tab {
        private final Matrix mModelViewMatrix = new Matrix();
        private final Matrix mModelViewProjectionMatrix = new Matrix();
        private final Matrix mRotationModelViewMatrix = new Matrix();

        private final RectF startBounds = new RectF();
        private final RectF endBounds = new RectF();
        private final Rect textBounds = new Rect();

        private int currIndex = 0;
        private int mAlpha;

        private Matrix measuredMatrixHeight = new Matrix();
        private Matrix measuredMatrixWidth = new Matrix();

        public void measure(int width, int height) {
            Rect area = new Rect(-width / 2, 0, width / 2, height / 2);
            startBounds.set(area);
            endBounds.set(area);
            endBounds.offset(0, -height / 2);

            calculateTextSize();
        }

        public int maxWith() {
            RectF rect = new RectF(startBounds);
            Matrix projectionMatrix = new Matrix();
            MatrixHelper.translate(projectionMatrix, startBounds.left, -startBounds.top, 0);
            measuredMatrixWidth.reset();
            measuredMatrixWidth.setConcat(projectionMatrix, MatrixHelper.ROTATE_X_90);
            measuredMatrixWidth.mapRect(rect);
            return (int) rect.width();
        }

        public int maxHeight() {
            RectF rect = new RectF(startBounds);
            Matrix projectionMatrix = new Matrix();
            measuredMatrixHeight.reset();
            measuredMatrixHeight.setConcat(projectionMatrix, MatrixHelper.ROTATE_X_0);
            measuredMatrixHeight.mapRect(rect);
            return (int) rect.height();
        }

        private void calculateTextSize() {
            mNumberPaint.getTextBounds("8", 0, 1, textBounds);
        }

        public void setChar(int index) {
            currIndex = index > mChars.length ? 0 : index;
        }

        public void next() {
            currIndex++;
            if (currIndex >= mChars.length) {
                currIndex = 0;
            }
        }

        public void rotate(int alpha) {
            mAlpha = alpha;
            MatrixHelper.rotateX(mRotationModelViewMatrix, alpha);
        }

        public void draw(Canvas canvas) {
            drawBackground(canvas);
            drawText(canvas);
        }

        private void drawBackground(Canvas canvas) {
            canvas.save();
            mModelViewMatrix.set(mRotationModelViewMatrix);
            applyTransformation(canvas, mModelViewMatrix);
            canvas.drawRoundRect(startBounds, mCornerSize, mCornerSize, mBackgroundPaint);
            canvas.restore();
        }

        private void drawText(Canvas canvas) {
            canvas.save();
            mModelViewMatrix.set(mRotationModelViewMatrix);
            RectF clip = startBounds;
            if (mAlpha > 90) {
                mModelViewMatrix.setConcat(mModelViewMatrix, MatrixHelper.MIRROR_X);
                clip = endBounds;
            }
            applyTransformation(canvas, mModelViewMatrix);
            canvas.clipRect(clip);
            canvas.drawText(Character.toString(mChars[currIndex]), 0, 1, -textBounds.centerX(), -textBounds.centerY(), mNumberPaint);
            canvas.restore();
        }

        private void applyTransformation(Canvas canvas, Matrix matrix) {
            mModelViewProjectionMatrix.reset();
            mModelViewProjectionMatrix.setConcat(mProjectionMatrix, matrix);
            mModelViewProjectionMatrix.setConcat(mProjectionMatrix, mModelViewMatrix);
            canvas.concat(mModelViewProjectionMatrix);
        }
    }

    public static class MatrixHelper {
        public static final Camera camera = new Camera();
        /**
         * Matrix with 180 degrees x rotation defined
         */
        public static final Matrix MIRROR_X = new Matrix();
        static {
            MatrixHelper.rotateX(MIRROR_X, 180);
        }

        /**
         * Matrix with 0 degrees x rotation defined
         */
        public static final Matrix ROTATE_X_0 = new Matrix();

        static {
            MatrixHelper.rotateX(ROTATE_X_0, 0);
        }

        /**
         * Matrix with 90 degrees x rotation defined
         */
        public static final Matrix ROTATE_X_90 = new Matrix();

        static {
            MatrixHelper.rotateX(ROTATE_X_90, 90);
        }

        public static void mirrorX(Matrix matrix) {
            rotateX(matrix, 180);
        }

        public static void rotateX(Matrix matrix, int alpha) {
            synchronized (camera) {
                camera.save();
                camera.rotateX(alpha);
                camera.getMatrix(matrix);
                camera.restore();
            }
        }

        public static void rotateZ(Matrix matrix, int alpha) {
            synchronized (camera) {
                camera.save();
                camera.rotateZ(alpha);
                camera.getMatrix(matrix);
                camera.restore();
            }
        }

        public static void translate(Matrix matrix, float dx, float dy, float dz) {
            synchronized (camera) {
                camera.save();
                camera.translate(dx, dy, dz);
                camera.getMatrix(matrix);
                camera.restore();
            }
        }

        public static void translateY(Matrix matrix, float dy) {
            synchronized (camera) {
                camera.save();
                camera.translate(0, dy, 0);
                camera.getMatrix(matrix);
                camera.restore();
            }
        }
    }

}
