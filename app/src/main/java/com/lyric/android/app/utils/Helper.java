package com.lyric.android.app.utils;

import android.os.Handler;

import java.util.ArrayList;

/**
 * @author lyricgan
 * @description just test
 * @time 2016/7/29 15:03
 */
public class Helper {
    private static final ThreadLocal<RunQueue> sRunQueues = new ThreadLocal<RunQueue>();

    public static RunQueue getRunQueue() {
        RunQueue rq = sRunQueues.get();
        if (rq != null) {
            return rq;
        }
        rq = new RunQueue();
        sRunQueues.set(rq);
        return rq;
    }

    /**
     * The run queue is used to enqueue pending work from Views when no Handler is
     * attached.  The work is executed during the next call to performTraversals on
     * the thread.
     */
    public static final class RunQueue {
        private final ArrayList<HandlerAction> mActions = new ArrayList<HandlerAction>();

        public void post(Runnable action) {
            postDelayed(action, 0);
        }

        public void postDelayed(Runnable action, long delayMillis) {
            HandlerAction handlerAction = new HandlerAction();
            handlerAction.action = action;
            handlerAction.delay = delayMillis;

            synchronized (mActions) {
                mActions.add(handlerAction);
            }
        }

        public void removeCallbacks(Runnable action) {
            final HandlerAction handlerAction = new HandlerAction();
            handlerAction.action = action;

            synchronized (mActions) {
                final ArrayList<HandlerAction> actions = mActions;

                while (actions.remove(handlerAction)) {
                    // Keep going
                }
            }
        }

        public void executeActions(Handler handler) {
            synchronized (mActions) {
                final ArrayList<HandlerAction> actions = mActions;
                final int count = actions.size();

                for (int i = 0; i < count; i++) {
                    final HandlerAction handlerAction = actions.get(i);
                    handler.postDelayed(handlerAction.action, handlerAction.delay);
                }

                actions.clear();
            }
        }

        private static class HandlerAction {
            Runnable action;
            long delay;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                HandlerAction that = (HandlerAction) o;
                return !(action != null ? !action.equals(that.action) : that.action != null);
            }

            @Override
            public int hashCode() {
                int result = action != null ? action.hashCode() : 0;
                result = 31 * result + (int) (delay ^ (delay >>> 32));
                return result;
            }
        }
    }
}
