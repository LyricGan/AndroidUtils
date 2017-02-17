package com.lyric.android.app.test.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lyric.android.app.R;

public class VideoListAdapter extends BaseAdapter {
    Context context;

    public VideoListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return VideoConstant.videoUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_video_list, null);
            viewHolder.jcVideoPlayer = (VideoPlayer) convertView.findViewById(R.id.videoplayer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.jcVideoPlayer.setUp(VideoConstant.videoUrls[position], VideoConstant.videoThumbs[position], VideoConstant.videoTitles[position]);
        return convertView;
    }

    class ViewHolder {
        VideoPlayer jcVideoPlayer;
    }
}
