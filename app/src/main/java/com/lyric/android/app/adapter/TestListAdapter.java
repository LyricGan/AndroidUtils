package com.lyric.android.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyric.android.app.R;
import com.lyric.android.library.adapter.ListAdapter;

import java.util.List;

/**
 * @author lyric
 * @description
 * @time 2016/3/12 17:23
 */
public class TestListAdapter extends ListAdapter<String> {

    public TestListAdapter(Context context) {
        super(context);
    }

    public TestListAdapter(Context context, String[] arrays) {
        super(context, arrays);
    }

    public TestListAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.view_item_index_list, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_title.setText(mDataList.get(position));
        return convertView;
    }

    private static class ViewHolder {
        private TextView tv_title;
    }
}
