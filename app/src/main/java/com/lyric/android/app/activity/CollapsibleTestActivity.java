package com.lyric.android.app.activity;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lyric.android.app.BaseCompatActivity;
import com.lyric.android.app.R;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.app.widget.TextView.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

public class CollapsibleTestActivity extends BaseCompatActivity {
    private List<String> mStringList;

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collapsible_test);

        ListView lv_text_list = (ListView) findViewById(R.id.lv_text_list);
        mStringList = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            String value = "iii" + i + "如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，" +
                    "如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，" +
                    "如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView。";
            if (i % 2 == 0) {
                value = "iii" + i + "通过自定义View组合封装，通过自定义View组合封装，通过自定义View组合封装，通过自定义View组合封装，通过自定义View组合封装，通过自定义View组合封装，通过自定义View组合封装。";
            } else if (i % 3 == 0) {
                value = "iii" + i + "自定义MoreTextView并获取这些属性的值，自定义MoreTextView并获取这些属性的值，自定义MoreTextView并获取这些属性的值，" +
                        "自定义MoreTextView并获取这些属性的值，自定义MoreTextView并获取这些属性的值，自定义MoreTextView并获取这些属性的值，自定义MoreTextView并获取这些属性的值。";
            }
            mStringList.add(value);
        }
        TextAdapter adapter = new TextAdapter();
        lv_text_list.setAdapter(adapter);
    }

    @Override
    public void updateTitleBar(TitleBar titleBar) {
        titleBar.setText("CollapsibleTest");
    }

    class TextAdapter extends BaseAdapter {
        private final SparseBooleanArray mCollapsedStatus;

        public TextAdapter() {
            mCollapsedStatus = new SparseBooleanArray();
        }

        @Override
        public int getCount() {
            return mStringList.size();
        }

        @Override
        public Object getItem(int position) {
            return mStringList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(CollapsibleTestActivity.this, R.layout.view_item_text_list, null);
                viewHolder.view_expandable = (ExpandableTextView) convertView.findViewById(R.id.view_expandable);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.view_expandable.setText(mStringList.get(position), mCollapsedStatus, position);
            return convertView;
        }

    }

    static class ViewHolder {
        private ExpandableTextView view_expandable;
    }

}
