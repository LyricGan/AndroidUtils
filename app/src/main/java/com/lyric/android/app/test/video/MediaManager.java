package com.lyric.android.app.test.video;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

class MediaManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnVideoSizeChangedListener {
    public MediaPlayer mediaPlayer;
    private static MediaManager jcMediaManager;
    public String uuid = "";//这个是正在播放中的视频控件的uuid，
    private String prev_uuid = "";
    public int currentVideoWidth = 0;
    public int currentVideoHeight = 0;

    public static MediaManager instance() {
        if (jcMediaManager == null) {
            jcMediaManager = new MediaManager();
        }
        return jcMediaManager;
    }

    public MediaManager() {
        mediaPlayer = new MediaPlayer();
    }

    public void prepareToPlay(Context context, String url) {
        if (TextUtils.isEmpty(url)) return;
        try {
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(context, Uri.parse(url));
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_PREPARED));
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE));
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        VideoEvents videoEvents = new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_UPDATE_BUFFER);
        videoEvents.obj = percent;
        EventBus.getDefault().post(videoEvents);
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_SEEKCOMPLETE));
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void backUpUuid() {
        this.prev_uuid = this.uuid;
    }

    public void revertUuid() {
        this.uuid = this.prev_uuid;
        this.prev_uuid = "";
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        currentVideoWidth = mp.getVideoWidth();
        currentVideoHeight = mp.getVideoHeight();
        EventBus.getDefault().post(new VideoEvents().setType(VideoEvents.VE_MEDIAPLAYER_RESIZE));
    }

    public void clearWidthAndHeight() {
        currentVideoWidth = 0;
        currentVideoHeight = 0;
    }
}
