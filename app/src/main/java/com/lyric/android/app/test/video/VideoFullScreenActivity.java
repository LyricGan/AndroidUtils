package com.lyric.android.app.test.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lyric.android.app.R;
import com.lyric.android.app.eventbus.EventBusUtils;

import org.greenrobot.eventbus.Subscribe;

public class VideoFullScreenActivity extends Activity {
    VideoPlayer mVideoPlayer;
    /**
     * 刚启动全屏时的播放状态
     */
    public static int STATE = -1;
    public static String URL;
    public static String TITLE;
    public static String THUMB;
    public static boolean manualQuit = false;
    protected static VideoSkin skin;
    static boolean start = false;

    static void toActivityFromNormal(Context context, int state, String url, String thumb, String title) {
        STATE = state;
        URL = url;
        THUMB = thumb;
        TITLE = title;
        start = false;
        Intent intent = new Intent(context, VideoFullScreenActivity.class);
        context.startActivity(intent);
    }

    public static void toActivity(Context context, String url, String thumb, String title) {
        STATE = VideoPlayer.CURRENT_STATE_NORMAL;
        URL = url;
        THUMB = thumb;
        TITLE = title;
        start = true;
        Intent intent = new Intent(context, VideoFullScreenActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_fullscreen);

        mVideoPlayer = (VideoPlayer) findViewById(R.id.video_player);
        if (skin != null) {
            mVideoPlayer.setSkin(skin.titleColor, skin.timeColor, skin.seekDrawable, skin.bottomControlBackground,
                    skin.enlargRecId, skin.shrinkRecId);
        }
        mVideoPlayer.setUpForFullscreen(URL, THUMB, TITLE);
        mVideoPlayer.setState(STATE);
        MediaManager.instance().setUuid(mVideoPlayer.uuid);
        manualQuit = false;
        if (start) {
            mVideoPlayer.ivStart.performClick();
        }

        EventBusUtils.register(this);
    }

    @Subscribe
    public void onEventMainThread(VideoEvents videoEvents) {
        if (videoEvents.type == VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN || videoEvents.type == VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        mVideoPlayer.quitFullScreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!manualQuit) {
            mVideoPlayer.isClickFullscreen = false;
            VideoPlayer.releaseAllVideos();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }
}
