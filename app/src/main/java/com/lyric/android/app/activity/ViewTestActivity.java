package com.lyric.android.app.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseApp;
import com.lyric.android.app.base.BaseCompatActivity;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.app.widget.TabDigitLayout;
import com.lyric.android.app.widget.chart.PieView;
import com.lyric.android.library.logger.Loggers;
import com.lyric.android.library.utils.ImageUtils;

import java.util.ArrayList;

/**
 * @author lyric
 * @description
 * @time 2016/3/15 15:12
 */
public class ViewTestActivity extends BaseCompatActivity {
    private TabDigitLayout mTabDigitLayout;
    private ImageView imageCapture;
    private Bitmap mCaptureBitmap;

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_view_test);
        PieView pieView = (PieView) findViewById(R.id.pie_view);
        imageCapture = (ImageView) findViewById(R.id.image_capture);

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
                takeViewCapture(mTabDigitLayout);
                break;
            default:
                break;
        }
    }

    private void takeViewCapture(View view) {
        mCaptureBitmap = ImageUtils.captureSimpleBitmap(view);
        if (mCaptureBitmap != null) {
            imageCapture.setImageBitmap(mCaptureBitmap);

            Loggers.d("memory1:" + ImageUtils.getBitmapMemory(mCaptureBitmap));
            Bitmap blurBitmap = ImageUtils.blurBitmap(BaseApp.getContext(), mCaptureBitmap, 10.0f);
            imageCapture.setImageBitmap(blurBitmap);
            Loggers.d("memory2:" + ImageUtils.getBitmapMemory(blurBitmap));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCaptureBitmap != null && !mCaptureBitmap.isRecycled()) {
            mCaptureBitmap.recycle();
            mCaptureBitmap = null;
        }
    }
}
