package com.lyric.android.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lyric.android.app.BaseActivity;
import com.lyric.android.app.BaseApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.test.ExecutorsTest;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.library.utils.ToastUtils;
import com.zys.brokenview.BrokenCallback;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

import java.util.Arrays;

/**
 * @author ganyu
 * @description
 * @time 2016/1/19 17:47
 */
public class MainActivity extends BaseActivity {
    private static final String[] INDEX_ARRAY = { "Preference", "Database", "Network", "CacheManager", "DesignMode", "BrokenView" };

    private ListView lv_index_list;

    @Override
    public void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        TitleBar titleBar = (TitleBar) findViewById(R.id.view_title_bar);
        lv_index_list = (ListView) findViewById(R.id.lv_index_list);

        titleBar.setTitle("APP");
        QuickAdapter<String> adapter = new QuickAdapter<String>(this, R.layout.view_item_index_list, Arrays.asList(INDEX_ARRAY)) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.tv_title, item);
            }
        };
        lv_index_list.setAdapter(adapter);
        lv_index_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {

                    }
                    break;
                    case 1: {// db

                    }
                    break;
                    case 2: {

                    }
                    break;
                    default:
                        break;
                }
                if (position == INDEX_ARRAY.length - 1) {
                    BrokenView brokenView = BrokenView.add2Window(MainActivity.this);
                    BrokenTouchListener listener = new BrokenTouchListener.Builder(brokenView)
                            .setComplexity(12)
                            .setBreakDuration(700)
                            .setFallDuration(2000)
                            .setPaint(null)
                            .build();
                    brokenView.setCallback(new BrokenCallback() {
                        @Override
                        public void onCancel(View v) {
                            super.onCancel(v);
                        }

                        @Override
                        public void onCancelEnd(View v) {
                            super.onCancelEnd(v);
                        }

                        @Override
                        public void onFalling(View v) {
                            super.onFalling(v);

                            ToastUtils.showLong(BaseApplication.getContext(), "falling...");
                        }

                        @Override
                        public void onFallingEnd(View v) {
                            super.onFallingEnd(v);

                            finish();
                        }

                        @Override
                        public void onRestart(View v) {
                            super.onRestart(v);
                        }

                        @Override
                        public void onStart(View v) {
                            super.onStart(v);
                        }
                    });
                    lv_index_list.setOnTouchListener(listener);
                }
            }
        });

        ExecutorsTest.start();
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
