package com.lyric.android.app.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.lyric.android.app.R;
import com.lyric.android.app.BaseApp;
import com.lyric.android.app.test.video.VideoListAdapter;
import com.lyric.android.app.test.video.VideoPlayer;
import com.lyric.android.app.third.glide.ImageLoader;
import com.lyric.android.app.widget.TitleBar;

/**
 * 视频列表页面
 * @author ganyu
 * @date 2017/7/25 16:03
 */
public class VideoListActivity extends BaseCompatActivity {

    @Override
    protected void onTitleCreated(TitleBar titleBar) {
    }

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_video_list);
        ListView listView = findViewById(R.id.listview);

        ImageLoader.initialize(BaseApp.getContext());
        listView.setAdapter(new VideoListAdapter(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayer.releaseAllVideos();
    }
}
