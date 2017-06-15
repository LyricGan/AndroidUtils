package com.lyric.android.app.activity;

import android.os.Bundle;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseCompatActivity;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.app.widget.TabDigitLayout;
import com.lyric.android.app.widget.chart.PieView;

import java.util.ArrayList;

/**
 * @author lyric
 * @description
 * @time 2016/3/15 15:12
 */
public class ViewTestActivity extends BaseCompatActivity {
    private TabDigitLayout mTabDigitLayout;

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_view_test);
        PieView pieView = (PieView) findViewById(R.id.pie_view);

        ArrayList<PieView.PieData> dataList = new ArrayList<>();
        PieView.PieData data;
        for (int i = 0; i < 5; i++) {
            data = new PieView.PieData("i" + i, 100 + (i * 50));

            dataList.add(data);
        }
        pieView.setData(dataList);
        pieView.setStartAngle(0);

        mTabDigitLayout = (TabDigitLayout) findViewById(R.id.tab_digit_layout);
        mTabDigitLayout.setNumber(567890, 500L);

        findViewById(R.id.btn_start).setOnClickListener(this);
    }

    @Override
    public void onTitleCreated(TitleBar titleBar) {
        titleBar.setText(ViewTestActivity.class.getSimpleName());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_start:
                mTabDigitLayout.setNumber(567890, 500L);
                break;
            default:
                break;
        }
    }
}
