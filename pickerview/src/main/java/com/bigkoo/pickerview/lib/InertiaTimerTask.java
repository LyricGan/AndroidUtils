package com.bigkoo.pickerview.lib;

import java.util.TimerTask;

final class InertiaTimerTask extends TimerTask {
    private float dest;
    private final float velocityY;
    private final WheelView loopView;

    InertiaTimerTask(WheelView loopView, float velocityY) {
        super();
        this.loopView = loopView;
        this.velocityY = velocityY;
        this.dest = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (dest == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    dest = 2000F;
                } else {
                    dest = -2000F;
                }
            } else {
                dest = velocityY;
            }
        }
        if (Math.abs(dest) >= 0.0F && Math.abs(dest) <= 20F) {
            loopView.cancelFuture();
            loopView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((dest * 10F) / 1000F);
        loopView.totalScrollY = loopView.totalScrollY - i;
        if (!loopView.mLoop) {
            float itemHeight = loopView.itemHeight;
            float top = (-loopView.initPosition) * itemHeight;
            float bottom = (loopView.getItemsCount() - 1 - loopView.initPosition) * itemHeight;
            if (loopView.totalScrollY - itemHeight * 0.3 < top) {
                top = loopView.totalScrollY + i;
            } else if (loopView.totalScrollY + itemHeight * 0.3 > bottom) {
                bottom = loopView.totalScrollY + i;
            }
            if (loopView.totalScrollY <= top) {
                dest = 40F;
                loopView.totalScrollY = (int) top;
            } else if (loopView.totalScrollY >= bottom) {
                loopView.totalScrollY = (int) bottom;
                dest = -40F;
            }
        }
        if (dest < 0.0F) {
            dest = dest + 20F;
        } else {
            dest = dest - 20F;
        }
        loopView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }
}
