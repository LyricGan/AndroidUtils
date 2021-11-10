package com.lyricgan.demo.util.widget.refresh;

public interface IAnimationRefresh {

    void scrollHeaderByMove(float moveY);

    void scrollFooterByMove(float moveY);

    void animationHeaderToRefresh();

    void animationHeaderBack(boolean isFinishRefresh);

    void animationHeaderHideByVy(int vy);

    void animationFooterToLoad();

    void animationFooterBack(boolean isFinishRefresh);

    void animationFooterHideByVy(int vy);
}
