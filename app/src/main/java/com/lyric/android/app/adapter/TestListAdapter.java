package com.lyric.android.app.adapter;

import android.content.Context;

import com.lyric.android.app.R;

/**
 * @author lyric
 * @description
 * @time 2016/3/12 17:23
 */
public class TestListAdapter extends BaseListAdapter<String> {

    public TestListAdapter(Context context, String[] arrays) {
        super(context, arrays, R.layout.view_item_index_list);
    }

    @Override
    public void convert(BaseViewHolder viewHolder, int position, String item) {
        viewHolder.setText(R.id.tv_title, item);
    }
}
