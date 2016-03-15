package com.lyric.android.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyric.android.app.BaseActivity;
import com.lyric.android.app.BaseApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.adapter.TestListAdapter;
import com.lyric.android.library.utils.ActivityUtils;
import com.lyric.android.library.utils.ToastUtils;
import com.zys.brokenview.BrokenCallback;
import com.zys.brokenview.BrokenTouchListener;
import com.zys.brokenview.BrokenView;

/**
 * @author ganyu
 * @description
 * @time 2016/1/19 17:47
 */
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Override
    public void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ListView lv_index_list = (ListView) findViewById(R.id.lv_index_list);

        TestListAdapter adapter = new TestListAdapter(this, getResources().getStringArray(R.array.test_array));
        lv_index_list.setAdapter(adapter);

        lv_index_list.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: {// CollapsibleTest
                ActivityUtils.toActivity(this, CollapsibleTestActivity.class);
            }
                break;
            case 1: {// SpannableTest
                ActivityUtils.toActivity(this, SpannableTestActivity.class);
            }
                break;
            case 2: {// BrokenViewTest
                showBrokenView(view);
            }
                break;
            case 3: {// ViewTest
                ActivityUtils.toActivity(this, ViewTestActivity.class);
            }
                break;
            default:
                break;
        }
    }

    private void showBrokenView(View view) {
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
        view.setOnTouchListener(listener);
    }

}
