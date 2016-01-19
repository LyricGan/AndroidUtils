package com.lyric.android.library.preference;

/**
 * @author ganyu
 * @description preference dispatcher
 * @time 16/1/16 下午11:41
 */
public class PreferenceDispatcher<T> extends Thread {
    private boolean mCancel = false;
    private IPreference<T> mPreference;
    private PreferenceType mType;

    public PreferenceDispatcher(IPreference<T> preference, PreferenceType type) {
        this.mPreference = preference;
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
                if (PreferenceType.READ == mType) {

                } else if (PreferenceType.WRITE == mType) {

                } else if (PreferenceType.REMOVE == mType) {

                } else if (PreferenceType.CLEAR == mType) {
                    mPreference.clear();
                }
            } catch (InterruptedException e) {
                if (mCancel) {
                    return;
                }
                continue;
            }
        }
    }
}
