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
    private List<TestDataEntity> textDataEntityList;

    @Override
    public void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collapsible_test);

        lv_text_list = (ListView) findViewById(R.id.lv_text_list);
        textDataEntityList = new ArrayList<>();
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
            TestDataEntity testDataEntity = new TestDataEntity();
            testDataEntity.setValue(value);
            textDataEntityList.add(testDataEntity);
        }
        TextAdapter adapter = new TextAdapter();
        lv_text_list.setAdapter(adapter);
    }

    class TextAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return textDataEntityList.size();
        }

        @Override
        public Object getItem(int position) {
            return textDataEntityList.get(position);
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
                viewHolder.view_collapsible = (CollapsibleTextView) convertView.findViewById(R.id.view_collapsible);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TextExpendEntity textExpendEntity = textDataEntityList.get(position).getTextExpendEntity();
            if (position % 2 == 0) {
                viewHolder.view_collapsible.setText(textDataEntityList.get(position).getValue(), 3, textExpendEntity);
            } else {
                viewHolder.view_collapsible.setText(textDataEntityList.get(position).getValue(), 5, textExpendEntity);
            }
            viewHolder.view_collapsible.setOnTextLayoutChangedListener(new OnTextLayoutChangedListener() {
                @Override
                public void onChanged(boolean firstLoad, boolean flag, boolean clicked, int status) {
                    TextExpendEntity textExpendEntity = new TextExpendEntity();
                    textExpendEntity.setFirstLoad(firstLoad);
                    textExpendEntity.setFlag(flag);
                    textExpendEntity.setClicked(clicked);
                    textExpendEntity.setStatus(status);
                    textDataEntityList.get(position).setTextExpendEntity(textExpendEntity);
                }
            });
            return convertView;
        }

    }

    static class ViewHolder {
        private CollapsibleTextView view_collapsible;
    }

    class TestDataEntity {
        private String value;
        private TextExpendEntity textExpendEntity;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public TextExpendEntity getTextExpendEntity() {
            return textExpendEntity;
        }

        public void setTextExpendEntity(TextExpendEntity textExpendEntity) {
            this.textExpendEntity = textExpendEntity;
        }
    }

}
