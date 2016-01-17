package com.lrc.baseand.preference;

/**
 * @author ganyu
 * @description preference dispatcher
 * @time 16/1/16 下午11:41
 */
public class PreferenceDispatcher extends Thread {
    private boolean mCancel = false;
    private PreferenceType mType;

    public PreferenceDispatcher(PreferenceType type) {
        this.mType = type;
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
                // judge preference type
                if (PreferenceType.READ == mType) {

                } else if (PreferenceType.WRITE == mType) {

                } else if (PreferenceType.REMOVE == mType) {

                } else if (PreferenceType.CLEAR == mType) {

                }
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
