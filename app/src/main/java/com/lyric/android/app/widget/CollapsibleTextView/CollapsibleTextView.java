package com.lyric.android.app.widget.CollapsibleTextView;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyric.android.app.R;
import com.lyric.android.library.log.Loggers;

/**
 * 可折叠显示文字，全文、收起
 */
public class CollapsibleTextView extends RelativeLayout implements View.OnClickListener {
    // 默认不显示
    private static final int STATE_NONE = 0;
    // 展开状态
    private static final int STATE_OPEN = 1;
    // 收起状态
    private static final int STATE_CLOSE = 2;

    private TextView tv_text_content;
    private TextView tv_text_status;

    // 实际展示的行数
    private int mMaxLines = 3;
    // 是否初始化标识
    private boolean mFirstLoad;
    // 标识位
    private boolean mFlag;
    // 是否点击标识
    private boolean mClicked;
    // 状态标识
    private int mStatus = STATE_NONE;

    private OnTextLayoutChangedListener mOnTextLayoutChangedListener;

    public CollapsibleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    public CollapsibleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public CollapsibleTextView(Context context) {
        super(context);
        initialize(context);
    }

    private void initialize(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_collapsible_text, this);
        tv_text_content = (TextView) view.findViewById(R.id.tv_text_content);
        tv_text_status = (TextView) view.findViewById(R.id.tv_text_status);

        tv_text_status.setVisibility(View.GONE);
        tv_text_status.setOnClickListener(this);
        ViewTreeObserver viewTreeObserver = tv_text_content.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int lineCount = tv_text_content.getLineCount();
                Loggers.e("ganyu", "lineCount:" + lineCount);
                return true;
            }
        });
    }

    public void setText(CharSequence text, int maxLines, TextExpendEntity textExpendEntity) {
        tv_text_content.setMovementMethod(LinkMovementMethod.getInstance());
        tv_text_content.setText(text, TextView.BufferType.NORMAL);
        this.mMaxLines = maxLines;
        if (textExpendEntity == null) {
            this.mFirstLoad = true;
            this.mFlag = false;
            this.mClicked = false;
            this.mStatus = STATE_NONE;
        } else {
            this.mFirstLoad = textExpendEntity.isFirstLoad();
            this.mFlag = textExpendEntity.isFlag();
            this.mClicked = textExpendEntity.isClicked();
            this.mStatus = textExpendEntity.getStatus();
        }
        if (STATE_NONE == mStatus) {
            mStatus = STATE_CLOSE;
        }
        setStatus(mStatus);
        requestLayout();
    }

    public void setText(CharSequence text) {
        tv_text_status.setVisibility(View.GONE);
        tv_text_content.setMaxLines(Integer.MAX_VALUE);
        tv_text_content.setText(text, TextView.BufferType.NORMAL);
    }

    public void setOnTextLayoutChangedListener(OnTextLayoutChangedListener listener) {
        this.mOnTextLayoutChangedListener = listener;
    }

    @Override
    public void onClick(View v) {
        mClicked = true;
        mFirstLoad = false;
        mFlag = false;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mFlag) {
            mFlag = true;
            if (tv_text_content.getLineCount() <= mMaxLines) {
                tv_text_content.setMaxLines(mMaxLines);
                tv_text_status.setVisibility(View.GONE);

                setStatus(STATE_NONE);
            } else {
                post(mInnerRunnable);
            }
        }
        if (mOnTextLayoutChangedListener != null) {
            mOnTextLayoutChangedListener.onChanged(mFirstLoad, mFlag, mClicked, mStatus);
        }
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    private InnerRunnable mInnerRunnable = new InnerRunnable();

    private class InnerRunnable implements Runnable {
        @Override
        public void run() {
            if (mFirstLoad && !mClicked) {
                updateType();
            } else if (mClicked) {
                updateType();
                mClicked = !mClicked;
            }
        }
    }

    private void updateType() {
        if (getStatus() == STATE_OPEN) {
            tv_text_content.setMaxLines(Integer.MAX_VALUE);
            tv_text_status.setVisibility(View.VISIBLE);
            tv_text_status.setText(R.string.pack_up);

            setStatus(STATE_CLOSE);
        } else if (getStatus() == STATE_CLOSE) {
            tv_text_content.setMaxLines(mMaxLines);
            tv_text_status.setVisibility(View.VISIBLE);
            tv_text_status.setText(R.string.full_text);

            setStatus(STATE_OPEN);
        }
    }

}
