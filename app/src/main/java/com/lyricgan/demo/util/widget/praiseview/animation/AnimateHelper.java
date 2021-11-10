package com.lyricgan.demo.util.widget.praiseview.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

public class AnimateHelper {
    private static final long DURATION = BaseViewAnimator.DURATION;
    private static final long NO_DELAY = 0;

    public static AnimationComposer with(BaseViewAnimator animator) {
        return new AnimationComposer(animator);
    }

    public static AnimationComposer withPulse() {
        return new AnimationComposer(new PulseAnimator());
    }

    public static AnimationComposer withSlideOutUp() {
        return new AnimationComposer(new SlideOutUpAnimator());
    }

    public static final class AnimationComposer {
        private List<Animator.AnimatorListener> callbacks = new ArrayList<Animator.AnimatorListener>();
        private BaseViewAnimator animator;
        private long duration = DURATION;
        private long delay = NO_DELAY;
        private Interpolator interpolator;
        private View target;

        private AnimationComposer(BaseViewAnimator animator) {
            this.animator = animator;
        }

        public AnimationComposer duration(long duration) {
            this.duration = duration;
            return this;
        }

        public AnimationComposer delay(long delay) {
            this.delay = delay;
            return this;
        }

        public AnimationComposer interpolate(Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public AnimationComposer withListener(Animator.AnimatorListener listener) {
            callbacks.add(listener);
            return this;
        }

        public AnimManager playOn(View target) {
            this.target = target;
            return new AnimManager(play(), this.target);
        }

        private BaseViewAnimator play() {
            animator.setTarget(target);
            animator.setDuration(duration)
                    .setInterpolator(interpolator)
                    .setStartDelay(delay);

            if (callbacks.size() > 0) {
                for (Animator.AnimatorListener callback : callbacks) {
                    animator.addAnimatorListener(callback);
                }
            }
            animator.animate();
            return animator;
        }
    }

    public static final class AnimManager {
        private BaseViewAnimator animator;
        private View target;

        private AnimManager(BaseViewAnimator animator, View target) {
            this.target = target;
            this.animator = animator;
        }

        public boolean isStarted() {
            return animator.isStarted();
        }

        public boolean isRunning() {
            return animator.isRunning();
        }

        public void stop(boolean reset) {
            animator.cancel();
            if (reset) {
                animator.reset(target);
            }
        }
    }

    public static class PulseAnimator extends BaseViewAnimator {
        @Override
        public void prepare(View target) {
            getAnimatorAgent().playTogether(ObjectAnimator.ofFloat(target, "scaleY", 1, 1.2f, 1),
                    ObjectAnimator.ofFloat(target, "scaleX", 1, 1.2f, 1)
            );
        }
    }

    public static class SlideOutUpAnimator extends BaseViewAnimator {
        @Override
        public void prepare(View target) {
            ViewGroup parent = (ViewGroup) target.getParent();
            getAnimatorAgent().playTogether(ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                    ObjectAnimator.ofFloat(target, "translationY", 0, -parent.getHeight() / 2)
            );
        }
    }

}
