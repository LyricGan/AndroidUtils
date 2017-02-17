package com.lyric.android.app.test.video;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseApp;
import com.lyric.android.app.third.glide.ImageLoader;

public class VideoListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        ListView listView = (ListView) findViewById(R.id.listview);

        ImageLoader.initialize(BaseApp.getContext());
        listView.setAdapter(new VideoListAdapter(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        VideoPlayer.releaseAllVideos();
    }
}
