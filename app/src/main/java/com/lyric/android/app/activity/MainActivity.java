package com.lyric.android.app.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lyric.android.app.BaseActivity;
import com.lyric.android.app.R;
import com.lyric.android.app.view.TitleBar;

import java.util.Arrays;

/**
 * @author ganyu
 * @description
 * @time 2016/1/19 17:47
 */
public class MainActivity extends BaseActivity {
    private static final String[] INDEX_ARRAY = { "Preference", "Database", "Network", "CacheManager", "DesignMode" };

    @Override
    public void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        TitleBar titleBar = (TitleBar) findViewById(R.id.view_title_bar);
        ListView lv_index_list = (ListView) findViewById(R.id.lv_index_list);

        titleBar.setTitle("APP");
        QuickAdapter<String> adapter = new QuickAdapter<String>(this, R.layout.view_item_index_list, Arrays.asList(INDEX_ARRAY)) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.tv_title, item);
            }
        };
        lv_index_list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
