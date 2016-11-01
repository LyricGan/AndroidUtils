package com.bigkoo.pickerview.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.OnDismissListener;
import com.bigkoo.pickerview.R;

/**
 * Created by Sai on 15/11/22.
 * 精仿iOSPickerViewController控件
 */
public class BasePickerView {
    private Context context;
    protected ViewGroup contentContainer;
    private ViewGroup decorView;// activity的根View
    private ViewGroup rootView;

    private OnDismissListener onDismissListener;
    private boolean isDismissing;

    private Animation mOutAnimation;
    private Animation mInAnimation;
    private int mGravity = Gravity.BOTTOM;

    public BasePickerView(Context context) {
        this.context = context;

        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_basepickerview, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);
        contentContainer.setLayoutParams(params);

        mInAnimation = getInAnimation();
        mOutAnimation = getOutAnimation();
    }

    /**
     * show的时候调用
     * @param view 这个View
     */
    private void onAttached(View view) {
        decorView.addView(view);
        contentContainer.startAnimation(mInAnimation);
    }

    /**
     * 添加这个View到Activity的根视图
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        onAttached(rootView);
    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        View view = decorView.findViewById(R.id.outmost_container);
        return view != null;
    }

    public void dismiss() {
        if (isDismissing) {
            return;
        }
        // 消失动画
        mOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        // 从activity根视图移除
                        decorView.removeView(rootView);
                        isDismissing = false;
                        if (onDismissListener != null) {
                            onDismissListener.onDismiss(BasePickerView.this);
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        contentContainer.startAnimation(mOutAnimation);
        isDismissing = true;
    }

    public Animation getInAnimation() {
        return AnimationUtils.loadAnimation(context, getAnimationResource(this.mGravity, true));
    }

    public Animation getOutAnimation() {
        return AnimationUtils.loadAnimation(context, getAnimationResource(this.mGravity, false));
    }

    public BasePickerView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public BasePickerView setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(R.id.outmost_container);
        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        } else {
            view.setOnTouchListener(null);
        }
        return this;
    }

    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    public View findViewById(int id) {
        return contentContainer.findViewById(id);
    }

    /**
     * Get default animation resource when not defined by the user
     * @param gravity       the mGravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    public static int getAnimationResource(int gravity, boolean isInAnimation) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? R.anim.slide_in_bottom : R.anim.slide_out_bottom;
        }
        return -1;
    }
}
