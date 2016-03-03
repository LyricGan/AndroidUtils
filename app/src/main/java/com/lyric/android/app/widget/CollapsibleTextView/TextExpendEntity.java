package com.lyric.android.app.widget.CollapsibleTextView;

/**
 * 文本扩展实体类
 */
public class TextExpendEntity {
    /**
     * 首次加载标识
     */
    private boolean firstLoad;
    /**
     * 加载标识
     */
    private boolean flag;
    /**
     * 点击标识
     */
    private boolean clicked;
    /**
     * 状态标识
     */
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
