package com.lrc.baseand.preference;

/**
 * @author ganyu
 * @description preference dispatcher
 * @time 16/1/16 下午11:41
 */
public class PreferenceDispatcher extends Thread {
    private boolean mCancel = false;

    public PreferenceDispatcher() {

    }

    public void cancel() {
        mCancel = true;
        interrupt();
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                //

                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (mCancel) {
                    continue;
                }
            }
        }
    }
}
