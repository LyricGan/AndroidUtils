package com.lyric.android.app.widget.praiseview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

/**
 * 自定义点赞视图
 * @author Lyric Gan
 * @since 2018/9/19
 */
public class PraiseLayout extends RelativeLayout {
    private Random mRandom;
    private int[] mImageRes;
    private int mDrawableWidth;
    private int mDrawableHeight;
    private int mWidth;
    private int mHeight;
    private Interpolator[] mInterpolatorItems;

    public PraiseLayout(Context context) {
        this(context,null);
    }

    public PraiseLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PraiseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRandom = new Random();
        mInterpolatorItems = new Interpolator[]{new AccelerateDecelerateInterpolator(), new AccelerateInterpolator(),
                new DecelerateInterpolator(), new LinearInterpolator()};
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    public void setImageRes(@DrawableRes int... imageRes) {
        mImageRes = imageRes;
        if (imageRes.length > 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), imageRes[0]);
            if (drawable != null) {
                mDrawableWidth = drawable.getIntrinsicWidth();
                mDrawableHeight = drawable.getIntrinsicHeight();
            }
        }
    }

    public void show() {
        if (mImageRes == null || mImageRes.length == 0) {
            return;
        }
        addImage(mImageRes);
    }

    private void addImage(int[] imageRes) {
        int resId = imageRes[mRandom.nextInt(imageRes.length)];
        final ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(resId);
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setLayoutParams(params);
        addView(imageView);

        AnimatorSet animatorSet = startAnimator(imageView);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(imageView);
            }
        });
        animatorSet.start();
    }

    private AnimatorSet startAnimator(ImageView view) {
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 0.3f, 10.f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0.3f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0.3f, 1.0f);
        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0, mRandom.nextInt(60));

        AnimatorSet beginAnimatorSet = new AnimatorSet();
        beginAnimatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator, rotationAnimator);
        beginAnimatorSet.setDuration(350);
        beginAnimatorSet.setInterpolator(new BounceInterpolator());

        AnimatorSet allAnimatorSet = new AnimatorSet();
        allAnimatorSet.playSequentially(beginAnimatorSet, getBetheAnimator(view));
        return allAnimatorSet;
    }

    private Animator getBetheAnimator(final ImageView view) {
        // 起点，底部正中间，记得减去图片宽度的一半
        PointF p0 = new PointF(mWidth / 2 - mDrawableWidth / 2, mHeight - mDrawableHeight);
        // 控制点1 下半部分
        PointF p1 = new PointF(mRandom.nextInt(mWidth - mDrawableWidth), mRandom.nextInt(mHeight / 2) + mHeight / 2);
        // 控制点2 上半部分
        PointF p2 = new PointF(mRandom.nextInt(mWidth - mDrawableWidth), mRandom.nextInt(mHeight / 2));
        // 始点 防止露出来
        PointF p3 = new PointF(mRandom.nextInt(mWidth - mDrawableWidth), 0);

        BetheTypeEvaluator betheEvaluator = new BetheTypeEvaluator(p1, p2);
        ValueAnimator animator = ObjectAnimator.ofObject(betheEvaluator, p0, p3);
        animator.setDuration(2000);
        animator.setInterpolator(mInterpolatorItems[mRandom.nextInt(mInterpolatorItems.length)]);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                view.setX(pointF.x);
                view.setY(pointF.y);
                // 透明度
                float t = animation.getAnimatedFraction();
                view.setAlpha(1 - t + 0.3f);
            }
        });
        return animator;
    }

    /**
     * 自定义贝塞尔曲线动画
     * 三阶贝塞尔曲线公式：B(t)=P0*(1-t)*(1-t)*(1-t) + 3*P1*t*(1-t)*(1-t) + 3*P2*t*t*(1-t) + P3*t*t*t,t∈[0,1]
     *
     * t：为时间
     * p0：起点
     * p1：控制点1
     * p2：控制点2
     * p3：始点，终点
     */
    static class BetheTypeEvaluator implements TypeEvaluator<PointF> {
        // 控制点p1,p2
        private  PointF mP1;
        private  PointF mP2;

        BetheTypeEvaluator(PointF p1, PointF p2){
            mP1 = p1;
            mP2 = p2;
        }

        @Override
        public PointF evaluate(float t, PointF p0, PointF p3) {
            PointF pointF = new PointF();
            pointF.x = p0.x * (1 - t) * (1 - t) * (1 - t)
                    + 3 * mP1.x * t * (1 - t) * (1 - t)
                    + 3 * mP2.x * t * t * (1 - t)
                    + p3.x * t * t * t;
            pointF.y = p0.y * (1 - t) * (1 - t) * (1 - t)
                    + 3 * mP1.y * t * (1 - t) * (1 - t)
                    + 3 * mP2.y * t * t * (1 - t)
                    + p3.y * t * t * t;
            return pointF;
        }
    }
}
