package com.lyric.android.app.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseApp;
import com.lyric.android.app.widget.HeaderStickyListView;
import com.lyric.android.app.widget.HeaderStickyBaseAdapter;
import com.lyric.android.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class HeaderListViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HeaderStickyListView listView = new HeaderStickyListView(this);

        listView.setAdapter(new HeaderSectionListAdapter(this, createItemList()));
        setContentView(listView);
    }

    private List<ArrayList<String>> createItemList() {
        List<ArrayList<String>> itemList = new ArrayList<>();
        ArrayList<String> childItemList;
        for (int i = 0; i < 5; i++) {
            childItemList = new ArrayList<>();
            if (i == 0) {
                for (int j = 0; j < 1; j++) {
                    childItemList.add("Section " + i + " Row " + j);
                }
            } else if (i == 1) {
                for (int j = 0; j < 5; j++) {
                    childItemList.add("Section " + i + " Row " + j);
                }
            } else {
                for (int j = 0; j < 8; j++) {
                    childItemList.add("Section " + i + " Row " + j);
                }
            }
            itemList.add(childItemList);
        }
        return itemList;
    }

    private static class HeaderSectionListAdapter extends HeaderStickyBaseAdapter.HeaderStickyListAdapter<String> {
        private Context mContext;

        public HeaderSectionListAdapter(Context context, List<ArrayList<String>> itemList) {
            super(itemList);
            this.mContext = context;
        }

        @Override
        public View getRowView(int section, int row, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, android.R.layout.simple_list_item_1, null);
            }
            ((TextView) convertView).setText("Section " + section + " Row " + row);
            return convertView;
        }

        @Override
        public Object getRowItem(int section, int row) {
            return null;
        }

        @Override
        public boolean hasSectionHeaderView(int section) {
            return (section > 0);
        }

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, android.R.layout.simple_list_item_1, null);
                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.holo_darker_gray));
            }
            ((TextView) convertView.findViewById(android.R.id.text1)).setText("Header for section " + section);
            return convertView;
        }

        @Override
        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
            super.onRowItemClick(parent, view, section, row, id);

            ToastUtils.showLong(BaseApp.getContext(), "Section " + section + " Row " + row);
        }
    }

}
