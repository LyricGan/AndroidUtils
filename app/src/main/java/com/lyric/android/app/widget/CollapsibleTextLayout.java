package com.lyric.android.app.widget;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyric.android.app.R;

/**
 * 可折叠文字布局，全文、收起
 * @author lyricgan
 * @date 2017/12/5 14:21
 */
public class CollapsibleTextLayout extends RelativeLayout implements View.OnClickListener {
    // 默认不显示
    private static final int STATE_NONE = 0;
    // 展开状态
    private static final int STATE_OPEN = 1;
    // 收起状态
    private static final int STATE_CLOSE = 2;
    /** 默认限制行数 */
    private static final int MAX_LINES_DEFAULT = 3;

    private TextView tvTextContent;
    private TextView tvTextStatus;

    // 实际展示的行数
    private int mMaxLines = MAX_LINES_DEFAULT;
    // 是否初始化标识
    private boolean mFirstLoad;
    // 初始化标识位
    private boolean mInitFlag;
    // 是否点击标识
    private boolean mClicked;
    // 状态标识
    private int mStatus = STATE_NONE;

    private InnerRunnable mInnerRunnable = new InnerRunnable();
    private OnTextLayoutChangedListener mOnTextLayoutChangedListener;

    public CollapsibleTextLayout(Context context) {
        this(context, null);
    }

    public CollapsibleTextLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsibleTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_collapsible_text, this);
        tvTextContent = (TextView) view.findViewById(R.id.tv_text_content);
        tvTextStatus = (TextView) view.findViewById(R.id.tv_text_status);

        tvTextStatus.setVisibility(View.GONE);
        tvTextStatus.setOnClickListener(this);
    }

    public void setText(CharSequence text, int maxLines, TextExpendItem expendItem) {
        tvTextContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvTextContent.setText(text, TextView.BufferType.NORMAL);
        this.mMaxLines = maxLines;
        if (expendItem == null) {
            this.mFirstLoad = true;
            this.mInitFlag = false;
            this.mClicked = false;
            this.mStatus = STATE_NONE;
        } else {
            this.mFirstLoad = expendItem.isFirstLoad();
            this.mInitFlag = expendItem.isFlag();
            this.mClicked = expendItem.isClicked();
            this.mStatus = expendItem.getStatus();
        }
        setStatus(mStatus);
        requestLayout();
    }

    public void setText(CharSequence text) {
        tvTextStatus.setVisibility(View.GONE);
        tvTextContent.setMaxLines(Integer.MAX_VALUE);
        tvTextContent.setText(text, TextView.BufferType.NORMAL);
    }

    public void setOnTextLayoutChangedListener(OnTextLayoutChangedListener listener) {
        this.mOnTextLayoutChangedListener = listener;
    }

    @Override
    public void onClick(View v) {
        mClicked = true;
        mFirstLoad = false;
        mInitFlag = false;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mInitFlag) {
            mInitFlag = true;
            if (tvTextContent.getLineCount() <= mMaxLines) {
                tvTextContent.setMaxLines(mMaxLines);
                tvTextStatus.setVisibility(View.GONE);

                setStatus(STATE_NONE);
            } else {
                if (mStatus == STATE_NONE) {
                    mStatus = STATE_CLOSE;
                }
                post(mInnerRunnable);
            }
        }
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        if (mStatus != status) {
            if (mOnTextLayoutChangedListener != null) {
                mOnTextLayoutChangedListener.onChanged(mFirstLoad, mInitFlag, mClicked, mStatus);
            }
        }
        this.mStatus = status;
    }

    private void updateType(int status) {
        if (status == STATE_OPEN) {
            tvTextContent.setMaxLines(Integer.MAX_VALUE);
            tvTextStatus.setVisibility(View.VISIBLE);
            tvTextStatus.setText(R.string.status_collapse);

            setStatus(STATE_CLOSE);
        } else if (status == STATE_CLOSE) {
            tvTextContent.setMaxLines(mMaxLines);
            tvTextStatus.setVisibility(View.VISIBLE);
            tvTextStatus.setText(R.string.status_expand);

            setStatus(STATE_OPEN);
        }
    }

    private class InnerRunnable implements Runnable {
        @Override
        public void run() {
            if (mFirstLoad && !mClicked) {
                updateType(mStatus);
            } else if (mClicked) {
                updateType(mStatus);
                mClicked = !mClicked;
            }
        }
    }

    /**
     * 文本扩展实体类
     */
    public static class TextExpendItem {
        /** 首次加载标识 */
        private boolean firstLoad;
        /** 加载标识 */
        private boolean flag;
        /** 点击标识 */
        private boolean clicked;
        /** 状态标识 */
        private int status;

        public boolean isFirstLoad() {
            return firstLoad;
        }

        public void setFirstLoad(boolean firstLoad) {
            this.firstLoad = firstLoad;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public boolean isClicked() {
            return clicked;
        }

        public void setClicked(boolean clicked) {
            this.clicked = clicked;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    /**
     * 文本状态变化监听事件
     */
    public interface OnTextLayoutChangedListener {

        void onChanged(boolean firstLoad, boolean flag, boolean clicked, int status);
    }
}
