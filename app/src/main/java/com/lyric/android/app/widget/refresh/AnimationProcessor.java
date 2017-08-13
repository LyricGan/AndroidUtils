package com.lyric.android.app.widget.refresh;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import java.util.LinkedList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AnimationProcessor implements IAnimationRefresh, IAnimationOverScroll {
    private GraceRefreshLayout.CoreProcessor coreProcessor;
    private static final float animFraction = 1f;
    // 动画的变化率
    private DecelerateInterpolator decelerateInterpolator;

    public AnimationProcessor(GraceRefreshLayout.CoreProcessor coProcessor) {
        this.coreProcessor = coProcessor;
        decelerateInterpolator = new DecelerateInterpolator(8);
    }

    private boolean scrollHeadLocked = false;
    private boolean scrollBottomLocked = false;

    public void scrollHeaderByMove(float moveY) {
        float offsetY = decelerateInterpolator.getInterpolation(moveY / coreProcessor.getMaxHeadHeight() / 2) * moveY / 2;
        // 禁止下拉刷新时下拉不显示头部
        if (coreProcessor.isPureScrollModeOn() || (!coreProcessor.enableRefresh() && !coreProcessor.isOverScrollTopShow())) {
            if (coreProcessor.getHeader().getVisibility() != GONE) {
                coreProcessor.getHeader().setVisibility(GONE);
            }
        } else {
            if (coreProcessor.getHeader().getVisibility() != VISIBLE) {
                coreProcessor.getHeader().setVisibility(VISIBLE);
            }
        }
        if (scrollHeadLocked && coreProcessor.isEnableKeepIView()) {
            coreProcessor.getHeader().setTranslationY(offsetY - coreProcessor.getHeader().getLayoutParams().height);
        } else {
            coreProcessor.getHeader().setTranslationY(0);
            coreProcessor.getHeader().getLayoutParams().height = (int) Math.abs(offsetY);
            coreProcessor.getHeader().requestLayout();
            coreProcessor.onPullingDown(offsetY);
        }

        if (!coreProcessor.isOpenFloatRefresh()) {
            coreProcessor.getTargetView().setTranslationY(offsetY);
            translateExHead((int) offsetY);
        }
    }

    public void scrollFooterByMove(float moveY) {
        float offsetY = decelerateInterpolator.getInterpolation(moveY / coreProcessor.getMaxBottomHeight() / 2) * moveY / 2;
        if (coreProcessor.isPureScrollModeOn() || (!coreProcessor.enableLoadmore() && !coreProcessor.isOverScrollBottomShow())) {
            if (coreProcessor.getFooter().getVisibility() != GONE) {
                coreProcessor.getFooter().setVisibility(GONE);
            }
        } else {
            if (coreProcessor.getFooter().getVisibility() != VISIBLE) {
                coreProcessor.getFooter().setVisibility(VISIBLE);
            }
        }
        if (scrollBottomLocked && coreProcessor.isEnableKeepIView()) {
            coreProcessor.getFooter().setTranslationY(coreProcessor.getFooter().getLayoutParams().height - offsetY);
        } else {
            coreProcessor.getFooter().setTranslationY(0);
            coreProcessor.getFooter().getLayoutParams().height = (int) Math.abs(offsetY);
            coreProcessor.getFooter().requestLayout();
            coreProcessor.onPullingUp(-offsetY);
        }
        coreProcessor.getTargetView().setTranslationY(-offsetY);
    }

    public void dealPullDownRelease() {
        if (!coreProcessor.isPureScrollModeOn() && coreProcessor.enableRefresh() && getVisibleHeadHeight() >= coreProcessor.getHeadHeight() - coreProcessor.getTouchSlop()) {
            animationHeaderToRefresh();
        } else {
            animationHeaderBack(false);
        }
    }

    public void dealPullUpRelease() {
        if (!coreProcessor.isPureScrollModeOn() && coreProcessor.enableLoadmore() && getVisibleFootHeight() >= coreProcessor.getBottomHeight() - coreProcessor.getTouchSlop()) {
            animationFooterToLoad();
        } else {
            animationFooterBack(false);
        }
    }

    private int getVisibleHeadHeight() {
        return (int) (coreProcessor.getHeader().getLayoutParams().height + coreProcessor.getHeader().getTranslationY());
    }

    private int getVisibleFootHeight() {
        return (int) (coreProcessor.getFooter().getLayoutParams().height - coreProcessor.getFooter().getTranslationY());
    }

    private void transHeader(float offsetY) {
        coreProcessor.getHeader().setTranslationY(offsetY - coreProcessor.getHeader().getLayoutParams().height);
    }

    private void transFooter(float offsetY) {
        coreProcessor.getFooter().setTranslationY(coreProcessor.getFooter().getLayoutParams().height - offsetY);
    }

    private boolean isAnimHeadToRefresh = false;

    /**
     * 1.满足进入刷新的条件或者主动刷新时，把Head位移到刷新位置（当前位置 ~ HeadHeight）
     */
    public void animationHeaderToRefresh() {
        isAnimHeadToRefresh = true;
        animLayoutByTime(getVisibleHeadHeight(), coreProcessor.getHeadHeight(), animHeadUpListener, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                isAnimHeadToRefresh = false;

                if (coreProcessor.getHeader().getVisibility() != VISIBLE) {
                    coreProcessor.getHeader().setVisibility(VISIBLE);
                }

                coreProcessor.setRefreshVisible(true);
                if (coreProcessor.isEnableKeepIView()) {
                    if (!scrollHeadLocked) {
                        coreProcessor.setRefreshing(true);
                        coreProcessor.onRefresh();
                        scrollHeadLocked = true;
                    }
                } else {
                    coreProcessor.setRefreshing(true);
                    coreProcessor.onRefresh();
                }
            }
        });
    }

    private boolean isAnimHeadBack = false;

    /**
     * 2.动画结束或不满足进入刷新状态的条件，收起头部（当前位置 ~ 0）
     */
    public void animationHeaderBack(final boolean isFinishRefresh) {
        isAnimHeadBack = true;
        if (isFinishRefresh && scrollHeadLocked && coreProcessor.isEnableKeepIView()) {
            coreProcessor.setPrepareFinishRefresh(true);
        }
        animLayoutByTime(getVisibleHeadHeight(), 0, animHeadUpListener, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimHeadBack = false;
                coreProcessor.setRefreshVisible(false);
                if (isFinishRefresh && scrollHeadLocked && coreProcessor.isEnableKeepIView()) {
                    coreProcessor.getHeader().getLayoutParams().height = 0;
                    coreProcessor.getHeader().requestLayout();
                    coreProcessor.getHeader().setTranslationY(0);
                    scrollHeadLocked = false;
                    coreProcessor.setRefreshing(false);
                    coreProcessor.resetHeaderView();
                }
            }
        });
    }

    private boolean isAnimBottomToLoad = false;

    /**
     * 3.满足进入加载更多的条件或者主动加载更多时，把Footer移到加载更多位置（当前位置 ~ BottomHeight）
     */
    public void animationFooterToLoad() {
        isAnimBottomToLoad = true;
        animLayoutByTime(getVisibleFootHeight(), coreProcessor.getBottomHeight(), animBottomUpListener, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimBottomToLoad = false;

                if (coreProcessor.getFooter().getVisibility() != VISIBLE) {
                    coreProcessor.getFooter().setVisibility(VISIBLE);
                }

                coreProcessor.setLoadVisible(true);
                if (coreProcessor.isEnableKeepIView()) {
                    if (!scrollBottomLocked) {
                        coreProcessor.setLoadingMore(true);
                        coreProcessor.onLoadMore();
                        scrollBottomLocked = true;
                    }
                } else {
                    coreProcessor.setLoadingMore(true);
                    coreProcessor.onLoadMore();
                }
            }
        });
    }

    private boolean isAnimBottomBack = false;

    /**
     * 4.加载更多完成或者不满足进入加载更多模式的条件时，收起尾部（当前位置 ~ 0）
     */
    public void animationFooterBack(final boolean isFinishLoading) {
        isAnimBottomBack = true;
        if (isFinishLoading && scrollBottomLocked && coreProcessor.isEnableKeepIView()) {
            coreProcessor.setPrepareFinishLoadMore(true);
        }
        animLayoutByTime(getVisibleFootHeight(), 0, new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                //列表中加载到内容时滚动List
                if (!RefreshUtils.isViewToBottom(coreProcessor.getTargetView(), coreProcessor.getTouchSlop())) {
                    int dy = getVisibleFootHeight() - height;
                    //可以让TargetView滚动dy高度，但这样两个方向上滚动感觉画面闪烁，改为dy/2是为了消除闪烁
                    if (dy > 0) {
                        if (coreProcessor.getTargetView() instanceof RecyclerView)
                            RefreshUtils.scrollAViewBy(coreProcessor.getTargetView(), dy);
                        else RefreshUtils.scrollAViewBy(coreProcessor.getTargetView(), dy / 2);
                    }
                }

                //decorate the AnimatorUpdateListener
                animBottomUpListener.onAnimationUpdate(animation);
            }
        }, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimBottomBack = false;

                coreProcessor.setLoadVisible(false);
                if (isFinishLoading) {
                    if (scrollBottomLocked && coreProcessor.isEnableKeepIView()) {
                        coreProcessor.getFooter().getLayoutParams().height = 0;
                        coreProcessor.getFooter().requestLayout();
                        coreProcessor.getFooter().setTranslationY(0);
                        scrollBottomLocked = false;
                        coreProcessor.resetBottomView();
                        coreProcessor.setLoadingMore(false);
                    }
                }
            }
        });
    }

    private boolean isAnimHeadHide = false;

    /**
     * 5.当刷新处于可见状态，向上滑动屏幕时，隐藏刷新控件
     *
     * @param vy 手指向上滑动速度
     */
    public void animationHeaderHideByVy(int vy) {
        if (isAnimHeadHide) return;
        isAnimHeadHide = true;
        vy = Math.abs(vy);
        if (vy < 5000) vy = 8000;
        animLayoutByTime(getVisibleHeadHeight(), 0, 5 * Math.abs(getVisibleHeadHeight() * 1000 / vy), animHeadUpListener, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimHeadHide = false;
                coreProcessor.setRefreshVisible(false);
                if (!coreProcessor.isEnableKeepIView()) {
                    coreProcessor.setRefreshing(false);
                    coreProcessor.onRefreshCanceled();
                    coreProcessor.resetHeaderView();
                }
            }
        });
    }

    private boolean isAnimBottomHide = false;

    /**
     * 6.当加载更多处于可见状态时，向下滑动屏幕，隐藏加载更多控件
     *
     * @param vy 手指向下滑动的速度
     */
    public void animationFooterHideByVy(int vy) {
        if (isAnimBottomHide) return;
        isAnimBottomHide = true;
        vy = Math.abs(vy);
        if (vy < 5000) vy = 8000;
        animLayoutByTime(getVisibleFootHeight(), 0, 5 * getVisibleFootHeight() * 1000 / vy, animBottomUpListener, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimBottomHide = false;
                coreProcessor.setLoadVisible(false);
                if (!coreProcessor.isEnableKeepIView()) {
                    coreProcessor.setLoadingMore(false);
                    coreProcessor.onLoadmoreCanceled();
                    coreProcessor.resetBottomView();
                }
            }
        });
    }

    private boolean isAnimOsTop = false;
    private boolean isOverScrollTopLocked = false;

    /**
     * 7.执行顶部越界  To executive cross-border springback at the top.
     * 越界高度height ∝ vy/computeTimes，此处采用的模型是 height=A*(vy + B)/computeTimes
     *
     * @param vy           满足越界条件的手指滑动速度  the finger sliding speed on the screen.
     * @param computeTimes 从满足条件到滚动到顶部总共计算的次数 Calculation times from sliding to top.
     */
    public void animOverScrollTop(float vy, int computeTimes) {
        if (isOverScrollTopLocked) return;
        isOverScrollTopLocked = true;
        isAnimOsTop = true;
        coreProcessor.setStatePTD();
        int oh = (int) Math.abs(vy / computeTimes / 2);
        final int overHeight = oh > coreProcessor.getOsHeight() ? coreProcessor.getOsHeight() : oh;
        final int time = overHeight <= 50 ? 115 : (int) (0.3 * overHeight + 100);
        animLayoutByTime(getVisibleHeadHeight(), overHeight, time, overScrollTopUpListener, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (scrollHeadLocked && coreProcessor.isEnableKeepIView() && coreProcessor.showRefreshingWhenOverScroll()) {
                    animationHeaderToRefresh();
                    isAnimOsTop = false;
                    isOverScrollTopLocked = false;
                    return;
                }
                animLayoutByTime(overHeight, 0, 2 * time, overScrollTopUpListener, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimOsTop = false;
                        isOverScrollTopLocked = false;
                    }
                });
            }
        });
    }

    private boolean isAnimOsBottom = false;
    private boolean isOverScrollBottomLocked = false;

    /**
     * 8.执行底部越界
     *
     * @param vy           满足越界条件的手指滑动速度
     * @param computeTimes 从满足条件到滚动到顶部总共计算的次数
     */
    public void animOverScrollBottom(float vy, int computeTimes) {
        if (isOverScrollBottomLocked) {
            return;
        }
        coreProcessor.setStatePBU();
        int oh = (int) Math.abs(vy / computeTimes / 2);
        final int overHeight = oh > coreProcessor.getOsHeight() ? coreProcessor.getOsHeight() : oh;
        final int time = overHeight <= 50 ? 115 : (int) (0.3 * overHeight + 100);
        if (!scrollBottomLocked && coreProcessor.autoLoadMore()) {
            coreProcessor.startLoadMore();
        } else {
            isOverScrollBottomLocked = true;
            isAnimOsBottom = true;
            animLayoutByTime(0, overHeight, time, overScrollBottomUpListener, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (scrollBottomLocked && coreProcessor.isEnableKeepIView() && coreProcessor.showLoadingWhenOverScroll()) {
                        animationFooterToLoad();
                        isAnimOsBottom = false;
                        isOverScrollBottomLocked = false;
                        return;
                    }
                    animLayoutByTime(overHeight, 0, 2 * time, overScrollBottomUpListener, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isAnimOsBottom = false;
                            isOverScrollBottomLocked = false;
                        }
                    });
                }
            });
        }
    }

    private AnimatorUpdateListener animHeadUpListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            if (scrollHeadLocked && coreProcessor.isEnableKeepIView()) {
                transHeader(height);
            } else {
                coreProcessor.getHeader().getLayoutParams().height = height;
                coreProcessor.getHeader().requestLayout();
                coreProcessor.getHeader().setTranslationY(0);
                coreProcessor.onPullDownReleasing(height);
            }
            if (!coreProcessor.isOpenFloatRefresh()) {
                coreProcessor.getTargetView().setTranslationY(height);
                translateExHead(height);
            }
        }
    };

    private AnimatorUpdateListener animBottomUpListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            if (scrollBottomLocked && coreProcessor.isEnableKeepIView()) {
                transFooter(height);
            } else {
                coreProcessor.getFooter().getLayoutParams().height = height;
                coreProcessor.getFooter().requestLayout();
                coreProcessor.getFooter().setTranslationY(0);
                coreProcessor.onPullUpReleasing(height);
            }
            coreProcessor.getTargetView().setTranslationY(-height);
        }
    };

    private AnimatorUpdateListener overScrollTopUpListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            if (coreProcessor.isOverScrollTopShow()) {
                if (coreProcessor.getHeader().getVisibility() != VISIBLE) {
                    coreProcessor.getHeader().setVisibility(VISIBLE);
                }
            } else {
                if (coreProcessor.getHeader().getVisibility() != GONE) {
                    coreProcessor.getHeader().setVisibility(GONE);
                }
            }
            if (scrollHeadLocked && coreProcessor.isEnableKeepIView()) {
                transHeader(height);
            } else {
                coreProcessor.getHeader().setTranslationY(0);
                coreProcessor.getHeader().getLayoutParams().height = height;
                coreProcessor.getHeader().requestLayout();
                coreProcessor.onPullDownReleasing(height);
            }

            coreProcessor.getTargetView().setTranslationY(height);
            translateExHead(height);
        }
    };

    private AnimatorUpdateListener overScrollBottomUpListener = new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int height = (int) animation.getAnimatedValue();
            if (coreProcessor.isOverScrollBottomShow()) {
                if (coreProcessor.getFooter().getVisibility() != VISIBLE) {
                    coreProcessor.getFooter().setVisibility(VISIBLE);
                }
            } else {
                if (coreProcessor.getFooter().getVisibility() != GONE) {
                    coreProcessor.getFooter().setVisibility(GONE);
                }
            }
            if (scrollBottomLocked && coreProcessor.isEnableKeepIView()) {
                transFooter(height);
            } else {
                coreProcessor.getFooter().getLayoutParams().height = height;
                coreProcessor.getFooter().requestLayout();
                coreProcessor.getFooter().setTranslationY(0);
                coreProcessor.onPullUpReleasing(height);
            }

            coreProcessor.getTargetView().setTranslationY(-height);
        }
    };

    private void translateExHead(int offsetY) {
        if (!coreProcessor.isExHeadLocked()) coreProcessor.getExHead().setTranslationY(offsetY);
    }

    public void animLayoutByTime(int start, int end, long time, AnimatorUpdateListener listener, AnimatorListener animatorListener) {
        ValueAnimator va = ValueAnimator.ofInt(start, end);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(listener);
        va.addListener(animatorListener);
        va.setDuration(time);
        va.start();
//        offerToQueue(va);
    }

    public void animLayoutByTime(int start, int end, long time, AnimatorUpdateListener listener) {
        ValueAnimator va = ValueAnimator.ofInt(start, end);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(listener);
        va.setDuration(time);
        va.start();
//        offerToQueue(va);
    }

    public void animLayoutByTime(int start, int end, AnimatorUpdateListener listener, AnimatorListener animatorListener) {
        ValueAnimator va = ValueAnimator.ofInt(start, end);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(listener);
        va.addListener(animatorListener);
        va.setDuration((int) (Math.abs(start - end) * animFraction));
        va.start();
//        offerToQueue(va);
    }

    //just for test.
    private void offerToQueue(Animator animator) {
        if (animator == null) return;
        if (animQueue == null) {
            animQueue = new LinkedList<>();
        }
        animQueue.offer(animator);
        animator.addListener(new AnimatorListenerAdapter() {
            long startTime = 0;

            @Override
            public void onAnimationStart(Animator animation) {
                startTime = System.currentTimeMillis();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animQueue.poll();
                if (animQueue.size() > 0) {
                    animQueue.getFirst().start();
                }
            }
        });
        if (animQueue.size() == 1) {
            animator.start();
        }
    }

    private LinkedList<Animator> animQueue;
}
