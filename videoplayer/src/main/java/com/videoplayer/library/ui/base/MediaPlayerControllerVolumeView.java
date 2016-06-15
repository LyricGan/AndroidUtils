package com.videoplayer.library.ui.base;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.videoplayer.library.R;
import com.videoplayer.library.util.Constants;

/**
 * @author LIXIAOPENG
 * @Description 声音控制
 * @date 2015-6-2
 */
public class MediaPlayerControllerVolumeView extends RelativeLayout {

    private static final int MAX_PROGRESS = 100;

    private AudioManager mAudioManager;

    private Callback mCallback;

    private volatile boolean isShowUpdateProgress = false;

    private boolean isChangedFromOnKeyChange = false;
    private int mOldVolume = 0;

//    private MediaPlayerVolumeSeekBar.OnScreenShowListener mOnScreenShowListener;

    private static final int LEVEL_VOLUME = 100;
    private MediaPlayerBrightSeekBar mSeekBarVolumeProgress;

    public MediaPlayerControllerVolumeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public MediaPlayerControllerVolumeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MediaPlayerControllerVolumeView(Context context) {
        super(context);
        init(context);
    }

    public void setOnScreenShowListener(MediaPlayerVolumeSeekBar.OnScreenShowListener mOnScreenShowListener) {
        Log.d(Constants.LOG_TAG, " listener set in C V");
        if (mOnScreenShowListener != null) {
//			mSeekBarVolumeProgress
//					.setOnScreenShowListener(mOnScreenShowListener);
        }
    }

    public void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.blue_media_player_controller_volume_view, this);

        mSeekBarVolumeProgress = (MediaPlayerBrightSeekBar) root.findViewById(R.id.seekbar_volume_progress);
        mSeekBarVolumeProgress.setMax(MAX_PROGRESS);
        mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);


        mSeekBarVolumeProgress
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        if (isShowUpdateProgress) {
                            isShowUpdateProgress = false;
                            return;
                        }
                        if (isChangedFromOnKeyChange) {
                            isChangedFromOnKeyChange = false;
                            return;
                        }

                        float percentage = (float) progress / seekBar.getMax();

                        if (percentage < 0 || percentage > 1)
                            return;

                        if (mAudioManager != null) {

                            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                            int newVolume = (int) (percentage * maxVolume);
                            setVolume(newVolume);
                            if (mCallback != null) {
                                mCallback.onVolumeProgressChanged(mAudioManager, percentage);
                            }

                        }

                    }
                });

        performVolumeChange(getVolume() / 100);

    }

    private void setVolume(int volume) {
        if (null != mAudioManager) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            if (volume == 0) {
//                mMuteIv.setSelected(true);
            } else {
//                mMuteIv.setSelected(false);
            }
        }
    }

    private int getVolume() {
        if (null != mAudioManager) {
            return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        return 0;
    }

    public void update(AudioManager audioManager) {

        mAudioManager = audioManager;
        isChangedFromOnKeyChange = false;
        // 目前存在bug:手动调用setProgress thumb会移动至初始位置
        // 每次show的时候,更新下最新的Volume进度
        // 但是因为seekbar max值和audioManager max值存在很大差距,可能会导致show完后会跳跃比较大间距
        if (mAudioManager != null) {
            updateSeekBarVolumeProgress();
            isShowUpdateProgress = true;
        }
    }

    private void updateSeekBarVolumeProgress() {
        int curVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        float percentage = (float) curVolume / maxVolume;

//		final int progress = (int) (percentage * mSeekBarVolumeProgress
//				.getMax());
//		mSeekBarVolumeProgress.post(new Runnable() {
//			@Override
//			public void run() {
//				mSeekBarVolumeProgress.setProgress(progress);
//			}
//		});
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onVolumeProgressChanged(AudioManager audioManager, float percentage);
    }

    private int getOldVolume() {
        if (mOldVolume == 0) {
            mOldVolume = 1;
        }
        return mOldVolume;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int eventAction = event.getAction();
        int keyCode = event.getKeyCode();
        if (eventAction == KeyEvent.ACTION_DOWN
                && (keyCode == KeyEvent.KEYCODE_VOLUME_UP// 音量+
                || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {// 音量-
            isChangedFromOnKeyChange = true;
            updateSeekBarVolumeProgress();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnShowListener() {

    }


    // 记录一次有效手势滑动的总距离,改值是由于多次的delta累加而成,要大于一个基础阀值,才能真正实现效果
    private float mTotalDeltaVolumeDistance = 0;
    private float mTotalLastDeltaVolumePercentage = 0;

    public void onGestureVolumeChange(float deltaVolumeDistance, float totalVolumeDistance, AudioManager audioManager) {
        Log.d(Constants.LOG_TAG, "onGestureVolumeChange ....11...." + deltaVolumeDistance + "---" + totalVolumeDistance);
        mTotalDeltaVolumeDistance = mTotalDeltaVolumeDistance + deltaVolumeDistance;
        float minVolumeDistance = totalVolumeDistance / LEVEL_VOLUME;
        float minVolumePercentage = (float) 1 / audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (Math.abs(mTotalDeltaVolumeDistance) >= minVolumeDistance) {

            float deltaVolumePercentage = mTotalDeltaVolumeDistance / totalVolumeDistance;
            mTotalLastDeltaVolumePercentage = mTotalLastDeltaVolumePercentage + deltaVolumePercentage;

            int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float curVolumePercentage = (float) curVolume / maxVolume;

            int newVolume = curVolume;
            float newVolumePercentage = curVolumePercentage + mTotalLastDeltaVolumePercentage;

            if (mTotalLastDeltaVolumePercentage > 0 && mTotalLastDeltaVolumePercentage > minVolumePercentage) {
                mTotalLastDeltaVolumePercentage = 0;
                newVolume++;
            } else if (mTotalLastDeltaVolumePercentage < 0 && mTotalLastDeltaVolumePercentage < -minVolumePercentage) {
                mTotalLastDeltaVolumePercentage = 0;
                newVolume--;
            } else {
                return;
            }

            if (newVolume < 0) {
                newVolume = 0;
            } else if (newVolume > maxVolume) {
                newVolume = maxVolume;
            }

            if (newVolumePercentage < 0) {
                newVolumePercentage = 0.0f;
            } else if (newVolumePercentage > 1) {
                newVolumePercentage = 1.0f;
            }
            if (newVolumePercentage == 0.0) {
                newVolume = 0;
            } else if (newVolumePercentage > 0.0 && newVolumePercentage < 0.04) {
                newVolume = 1;
            } else if (newVolumePercentage >= 0.04 && newVolumePercentage < 0.10) {
                newVolume = 2;
            } else if (newVolumePercentage >= 0.10 && newVolumePercentage < 0.17) {
                newVolume = 3;
            } else if (newVolumePercentage >= 0.17 && newVolumePercentage < 0.23) {
                newVolume = 4;
            } else if (newVolumePercentage >= 0.23 && newVolumePercentage < 0.30) {
                newVolume = 5;
            } else if (newVolumePercentage >= 0.30 && newVolumePercentage < 0.38) {
                newVolume = 6;
            } else if (newVolumePercentage >= 0.38 && newVolumePercentage < 0.44) {
                newVolume = 7;
            } else if (newVolumePercentage >= 0.44 && newVolumePercentage < 0.51) {
                newVolume = 8;
            } else if (newVolumePercentage >= 0.51 && newVolumePercentage < 0.58) {
                newVolume = 9;
            } else if (newVolumePercentage >= 0.58 && newVolumePercentage < 0.64) {
                newVolume = 10;
            } else if (newVolumePercentage >= 0.64 && newVolumePercentage < 0.71) {
                newVolume = 11;
            } else if (newVolumePercentage >= 0.71 && newVolumePercentage < 0.79) {
                newVolume = 12;
            } else if (newVolumePercentage >= 0.79 && newVolumePercentage < 0.86) {
                newVolume = 13;
            } else if (newVolumePercentage >= 0.86 && newVolumePercentage < 0.92) {
                newVolume = 14;
            } else if (newVolumePercentage >= 0.92 && newVolumePercentage <= 1.0) {
                newVolume = 15;
            }

            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
            performVolumeChange(newVolumePercentage);

            mTotalDeltaVolumeDistance = 0;

        }

    }

    public void onGestureVolumeFinish() {

        mTotalDeltaVolumeDistance = 0;
        mTotalLastDeltaVolumePercentage = 0;

    }

    //声音改变图像
    public void performVolumeChange(float volumePercentage) {
        mSeekBarVolumeProgress.setProgress((int) (volumePercentage * 100));


//        mMuteIv.setImageLevel(level);
//        mTvVolumeProgress.setText(((int)(volumePercentage*100)) + "%");

//        show();

    }


}
