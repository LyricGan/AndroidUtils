package com.lyric.android.app.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lyric.android.app.R;
import com.lyric.android.app.BaseApp;
import com.lyric.android.app.BaseFragment;
import com.lyric.android.app.widget.TabDigitLayout;
import com.lyric.android.app.widget.chart.PieView;
import com.lyric.android.app.test.logger.Loggers;
import com.lyric.utils.ImageUtils;

import java.util.ArrayList;

/**
 * 视图测试页面
 * @author ganyu
 * @date 2017/7/25 14:57
 */
public class ViewTestFragment extends BaseFragment {
    private TabDigitLayout mTabDigitLayout;
    private ImageView imageCapture;
    private Bitmap mCaptureBitmap;

    @Override
    protected void initExtras(Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_test;
    }

    @Override
    protected void initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PieView pieView =  findViewById(R.id.pie_view);
        imageCapture =  findViewById(R.id.image_capture);

        ArrayList<PieView.PieData> dataList = new ArrayList<>();
        PieView.PieData data;
        for (int i = 0; i < 5; i++) {
            data = new PieView.PieData("i" + i, 100 + (i * 50));

            dataList.add(data);
        }
        pieView.setData(dataList);
        pieView.setStartAngle(0);

        mTabDigitLayout = findViewById(R.id.tab_digit_layout);
        mTabDigitLayout.setNumber(567890, 500L);

        findViewById(R.id.btn_start).setOnClickListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    protected void onViewClick(View v) {
        super.onViewClick(v);
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
    public void onDestroy() {
        super.onDestroy();
        if (mCaptureBitmap != null && !mCaptureBitmap.isRecycled()) {
            mCaptureBitmap.recycle();
            mCaptureBitmap = null;
        }
    }
}
