package com.lyric.android.app.widget.image;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author lyric
 * @description
 * @time 2016/6/3 13:31
 */
public class NetworkImageView extends ImageView {

    public NetworkImageView(Context context) {
        super(context);
    }

    public NetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetworkImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageUrl(String url) {
        ImageLoader.load(getContext(), url, this);
    }

    public void setImageUri(Uri uri) {
        ImageLoader.load(getContext(), uri, this);
    }
}
