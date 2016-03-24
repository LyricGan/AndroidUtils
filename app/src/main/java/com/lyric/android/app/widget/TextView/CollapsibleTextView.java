package com.lyric.android.app.widget.TextView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyric.android.app.R;

/**
 * @author lyric
 * @description 可折叠显示文字，全文、收起
 * @time 2016/2/23 15:45
 */
public class CollapsibleTextView extends RelativeLayout implements View.OnClickListener {
    private static final String TAG = CollapsibleTextView.class.getSimpleName();
    // 默认最大行数
    private static final int DEFAULT_MAX_LINE_COUNT = 3;
    // 默认不显示
    private static final int STATE_NONE = 0;
    // 展开状态
    private static final int STATE_OPEN = 1;
    // 收起状态
    private static final int STATE_CLOSE = 2;

    private TextView tv_text_content;
    private TextView tv_text_status;

    // 实际展示的行数
    private int mMaxLines = DEFAULT_MAX_LINE_COUNT;
    // 状态标识
    private int mStatus = STATE_NONE;
    private OnTextStatusChangedListener mOnTextStatusChangedListener;
    // 标识位
    private boolean mFlag;
    // 是否点击标识
    private boolean mClicked;
    // 是否初始化标识
    private boolean mInitFlag;

    public interface OnTextStatusChangedListener {

        void onChanged(int status);
    }

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
    }

    public void setText(CharSequence text, int maxLines, int status) {
        this.mMaxLines = maxLines;
        this.mStatus = status;
        mFlag = false;
        mClicked = false;
        mInitFlag = true;
        if (STATE_NONE == mStatus) {
            mStatus = STATE_CLOSE;
        }
        setStatus(mStatus);
        tv_text_content.setText(text);
    }

    public void setText(CharSequence text) {
        tv_text_status.setVisibility(View.GONE);
        tv_text_content.setMaxLines(Integer.MAX_VALUE);
        tv_text_content.setText(text);
    }

    public void setOnTextStatusChangedListener(OnTextStatusChangedListener listener) {
        mOnTextStatusChangedListener = listener;
    }

    @Override
    public void onClick(View v) {
        mFlag = false;
        mClicked = true;
        mInitFlag = false;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mFlag) {
            mFlag = true;
            if (tv_text_content.getLineCount() <= mMaxLines) {
                tv_text_content.setMaxLines(mMaxLines);
                tv_text_status.setVisibility(View.GONE);

                setStatus(STATE_NONE);
            } else {
                tv_text_content.setMaxLines(mMaxLines);
                tv_text_status.setVisibility(View.VISIBLE);
                post(mInnerRunnable);
            }
        }
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        if (mStatus != status) {
            if (mOnTextStatusChangedListener != null) {
                mOnTextStatusChangedListener.onChanged(mStatus);
            }
        }
        this.mStatus = status;
    }
    private Runnable mInnerRunnable = new Runnable() {
        @Override
        public void run() {
            if (mInitFlag && !mClicked) {
                updateType();
            } else if (mClicked) {
                updateType();
                mClicked = !mClicked;
            }
        }
    };

    private void updateType() {
        if (getStatus() == STATE_OPEN) {
            tv_text_content.setMaxLines(Integer.MAX_VALUE);
            tv_text_status.setText(R.string.collapse_pack_up);

            setStatus(STATE_CLOSE);
        } else if (getStatus() == STATE_CLOSE) {
            tv_text_content.setMaxLines(mMaxLines);
            tv_text_status.setText(R.string.collapse_full_text);

            setStatus(STATE_OPEN);
        }
    }

}
