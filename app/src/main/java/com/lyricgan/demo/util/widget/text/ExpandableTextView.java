package com.lyricgan.demo.util.widget.text;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyricgan.demo.util.R;

/**
 * 带展开、收起的布局
 */
public class ExpandableTextView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = ExpandableTextView.class.getSimpleName();
    // 默认最大行数
    private static final int MAX_COLLAPSED_LINES = 5;
    // 默认动画持续时间
    private static final int DEFAULT_ANIM_DURATION = 100;
    // 默认动画开始透明度渐变值
    private static final float DEFAULT_ANIM_ALPHA_START = 0.7f;

    protected TextView tv_expandable_text;
    protected TextView tv_expand_collapse;

    private boolean mRelayout;

    private boolean mCollapsed = true;
    private int mCollapsedHeight;
    private int mTextHeightWithMaxLines;
    private int mMaxCollapsedLines;
    private int mMarginBetweenTxtAndBottom;

    private String mExpandString;
    private String mCollapseString;

    private int mAnimationDuration;
    private float mAnimAlphaStart;
    private boolean mAnimating;

    /* Listener for callback */
    private OnExpandStateChangeListener mListener;
    /* For saving collapsed status when used in ListView */
    private SparseBooleanArray mCollapsedStatus;
    private int mPosition;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    private void initialize() {
        mMaxCollapsedLines = MAX_COLLAPSED_LINES;
        mAnimationDuration = DEFAULT_ANIM_DURATION;
        mAnimAlphaStart = DEFAULT_ANIM_ALPHA_START;
        mExpandString = getContext().getResources().getString(R.string.status_expand);
        mCollapseString = getContext().getResources().getString(R.string.status_collapse);

        setOrientation(LinearLayout.VERTICAL);
        setVisibility(GONE);
    }

    @Override
    public void onClick(View view) {
        if (tv_expand_collapse.getVisibility() != View.VISIBLE) {
            return;
        }
        mCollapsed = !mCollapsed;
        tv_expand_collapse.setText(mCollapsed ? mExpandString : mCollapseString);
        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }
        // mark that the animation is in progress
        mAnimating = true;

        Animation animation;
        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
        } else {
            animation = new ExpandCollapseAnimation(this, getHeight(), getHeight() +
                    mTextHeightWithMaxLines - tv_expandable_text.getHeight());
        }
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                applyAlphaAnimation(tv_expandable_text, mAnimAlphaStart);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                // 清除动画防止重复调用
                clearAnimation();
                mAnimating = false;
                // 调用监听事件
                if (mListener != null) {
                    mListener.onExpandStateChanged(tv_expandable_text, !mCollapsed);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        clearAnimation();
        startAnimation(animation);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // while an animation is in progress, intercept all the touch events to children to
        // prevent extra clicks during the animation
        return mAnimating;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 判断是否有变化
        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;

        // 默认完全展示，进行测量
        tv_expand_collapse.setVisibility(View.GONE);
        tv_expandable_text.setMaxLines(Integer.MAX_VALUE);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 判断文本总行数是否小于最大行数
        if (tv_expandable_text.getLineCount() <= mMaxCollapsedLines) {
            return;
        }
        // Saves the text height w/ max lines
        mTextHeightWithMaxLines = getRealTextViewHeight(tv_expandable_text);
        // Doesn't fit in collapsed mode. Collapse text view as needed. Show button.
        if (mCollapsed) {
            tv_expandable_text.setMaxLines(mMaxCollapsedLines);
        }
        tv_expand_collapse.setVisibility(View.VISIBLE);
        // 重新测量
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mCollapsed) {
            // Gets the margin between the TextView's bottom and the ViewGroup's bottom
            tv_expandable_text.post(mInnerRunnable);
            // Saves the collapsed height of this ViewGroup
            mCollapsedHeight = getMeasuredHeight();
        }
    }

    Runnable mInnerRunnable = new Runnable() {
        @Override
        public void run() {
            mMarginBetweenTxtAndBottom = getHeight() - tv_expandable_text.getHeight();
        }
    };

    public void setOnExpandStateChangeListener(OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    public void setText(CharSequence text) {
        mRelayout = true;
        tv_expandable_text.setText(text);
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setText(CharSequence text,  SparseBooleanArray collapsedStatus, int position) {
        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        boolean isCollapsed = collapsedStatus.get(position, true);
        clearAnimation();
        mCollapsed = isCollapsed;
        tv_expand_collapse.setText(mCollapsed ? mExpandString : mCollapseString);
        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    public CharSequence getText() {
        if (tv_expandable_text == null) {
            return "";
        }
        return tv_expandable_text.getText();
    }

    private void findViews() {
        View view = View.inflate(getContext(), R.layout.view_expandable_text, this);
        tv_expandable_text = (TextView) view.findViewById(R.id.tv_expandable_text);
        tv_expandable_text.setOnClickListener(this);
        tv_expand_collapse = (TextView) view.findViewById(R.id.tv_expand_collapse);
        tv_expand_collapse.setText(mCollapsed ? mExpandString : mCollapseString);
        tv_expand_collapse.setOnClickListener(this);
    }

    private static void applyAlphaAnimation(View view, float alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }
    }

    private static int getRealTextViewHeight(TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int)((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            tv_expandable_text.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);
            if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
                applyAlphaAnimation(tv_expandable_text, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart));
            }
            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize( int width, int height, int parentWidth, int parentHeight ) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds( ) {
            return true;
        }
    }

    public interface OnExpandStateChangeListener {
        /**
         * Called when the expand/collapse animation has been finished
         *
         * @param textView - TextView being expanded/collapsed
         * @param isExpanded - true if the TextView has been expanded
         */
        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }

}