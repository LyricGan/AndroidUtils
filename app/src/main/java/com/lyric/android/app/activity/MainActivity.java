package com.lyric.android.app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lyric.android.app.base.BaseActivity;
import com.lyric.android.app.base.BaseApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.adapter.TestListAdapter;
import com.lyric.android.app.mvvm.view.LoginActivity;
import com.lyric.android.app.widget.brokenview.BrokenCallback;
import com.lyric.android.app.widget.brokenview.BrokenTouchListener;
import com.lyric.android.app.widget.brokenview.BrokenView;
import com.lyric.android.app.widget.dialog.LoadingDialog;
import com.lyric.android.library.utils.ActivityUtils;
import com.lyric.android.library.utils.ToastUtils;

/**
 * @author ganyu
 * @description
 * @time 2016/1/19 17:47
 */
public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
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
                ActivityUtils.jumpActivity(this, CollapsibleTestActivity.class);
            }
                break;
            case 1: {// SpannableTest
                ActivityUtils.jumpActivity(this, SpannableTestActivity.class);
            }
                break;
            case 2: {// BrokenViewTest
                showBrokenView(view);
            }
                break;
            case 3: {// ViewTest
                ActivityUtils.jumpActivity(this, ViewTestActivity.class);
            }
                break;
            case 4: {// LoadingTest
                ActivityUtils.jumpActivity(this, LoadingActivity.class);
            }
                break;
            case 5: {// LoginTest
                ActivityUtils.jumpActivity(this, LoginActivity.class);
            }
                break;
            case 6: {// CircleProgressBar
                ActivityUtils.jumpActivity(this, CircleProgressBarActivity.class);
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

                final LoadingDialog dialog = new LoadingDialog(MainActivity.this);
                dialog.setMessage("正在加载...");
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.cancel();
                    }
                }, 5000);
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
