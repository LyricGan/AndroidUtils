package com.lyric.android.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lyric.android.app.BaseActivity;
import com.lyric.android.app.R;
import com.lyric.android.app.widget.CollapsibleTextView.CollapsibleTextView;
import com.lyric.android.app.widget.CollapsibleTextView.OnTextLayoutChangedListener;
import com.lyric.android.app.widget.CollapsibleTextView.TextExpendEntity;

import java.util.ArrayList;
import java.util.List;

public class CollapsibleTestActivity extends BaseActivity {
    private ListView lv_text_list;
    private List<String> stringList;
    private TextExpendEntity textExpendEntity;

    @Override
    public void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collapsible_test);

        lv_text_list = (ListView) findViewById(R.id.lv_text_list);
        stringList = new ArrayList<>();
        for (int i = 0; i < 20; i ++) {
            String value = "iii" + i + "如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView，如何写一个可以展开的TextView";
            stringList.add(value);
        }
        TextAdapter adapter = new TextAdapter();
        lv_text_list.setAdapter(adapter);
    }

    class TextAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return stringList.size();
        }

        @Override
        public Object getItem(int position) {
            return stringList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(CollapsibleTestActivity.this, R.layout.view_item_text_list, null);
                viewHolder.view_collapsible = (CollapsibleTextView) convertView.findViewById(R.id.view_collapsible);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.view_collapsible.setText(stringList.get(position), 4, textExpendEntity);
            viewHolder.view_collapsible.setOnTextLayoutChangedListener(new OnTextLayoutChangedListener() {
                @Override
                public void onChanged(boolean firstLoad, boolean flag, boolean clicked, int status) {
                    textExpendEntity = new TextExpendEntity();
                    textExpendEntity.setFirstLoad(firstLoad);
                    textExpendEntity.setFlag(flag);
                    textExpendEntity.setClicked(clicked);
                    textExpendEntity.setStatus(status);
                }
            });
            return convertView;
        }

    }

    static class ViewHolder {
        private CollapsibleTextView view_collapsible;
    }

}
