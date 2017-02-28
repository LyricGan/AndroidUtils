package com.lyric.android.app.activity;

import android.os.Bundle;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseCompatActivity;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.app.widget.PieView;

import java.util.ArrayList;

/**
 * @author lyric
 * @description
 * @time 2016/3/15 15:12
 */
public class ViewTestActivity extends BaseCompatActivity {

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
    }

    @Override
    public void onTitleCreated(TitleBar titleBar) {
        titleBar.setText(ViewTestActivity.class.getSimpleName());
    }
}
