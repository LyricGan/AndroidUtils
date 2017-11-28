package com.lyric.android.app.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lyric.android.app.BaseApp;
import com.lyric.android.app.BaseFragment;
import com.lyric.android.app.R;
import com.lyric.android.app.utils.LogUtils;
import com.lyric.android.app.utils.QRCodeUtils;
import com.lyric.android.app.widget.TabDigitLayout;
import com.lyric.android.app.widget.chart.ClashBar;
import com.lyric.android.app.widget.chart.HorizontalRatioBar;
import com.lyric.android.app.widget.chart.PieView;
import com.lyric.android.app.widget.chart.RingProgressBar;
import com.lyric.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * 视图测试页面
 * @author lyricgan
 * @date 2017/7/25 14:57
 */
public class ViewTestFragment extends BaseFragment {
    private final int[] mRedGradientColors = {0xffff0000, 0xffff6f43, 0xffff0000};
    private final int[] mBlueGradientColors = {0xff1fbbe9, 0xff59d7fc, 0xff1fbbe9};
    private final int[] mGreenGradientColors = {0xffb3c526, 0xff7fb72f, 0xffb3c526};

    private TabDigitLayout mTabDigitLayout;
    private ImageView imageCapture;
    private ImageView ivQrcodeImage;
    private Bitmap mCaptureBitmap;

    private ClashBar mClashBar;

    public static ViewTestFragment newInstance() {
        Bundle args = new Bundle();
        ViewTestFragment fragment = new ViewTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initExtras(Bundle args) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_view_test;
    }

    @Override
    protected void initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PieView pieView = findViewById(R.id.pie_view);
        imageCapture = findViewById(R.id.image_capture);
        ivQrcodeImage = findViewById(R.id.iv_qrcode_image);

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

        mClashBar = findViewById(R.id.clash_bar);
        findViewById(R.id.btn_clash_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testClashBar();
            }
        });

        findViewById(R.id.btn_ring_progress_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRingProgressBar();
            }
        });
        findViewById(R.id.btn_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQrCode();
            }
        });

        HorizontalRatioBar horizontalRatioBar = findViewById(R.id.horizontal_radio_bar);
        horizontalRatioBar.test();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        testClashBar();

        testRingProgressBar();
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

            Log.d(TAG, "memory1:" + ImageUtils.getBitmapMemory(mCaptureBitmap));
            Bitmap blurBitmap = ImageUtils.blurBitmap(BaseApp.getContext(), mCaptureBitmap, 10.0f);
            imageCapture.setImageBitmap(blurBitmap);
            Log.d(TAG, "memory2:" + ImageUtils.getBitmapMemory(blurBitmap));
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

    private void testClashBar() {
        float leftData = 37.5f + new Random().nextInt(1000);
        float rightData = 12.5f + new Random().nextInt(1000);
        mClashBar.setData(leftData, rightData);
        mClashBar.setOnClashBarUpdatedListener(new ClashBar.OnClashBarUpdatedListener() {
            @Override
            public void onChanged(float leftData, float rightData, float leftProgressData, float rightProgressData, boolean isFinished) {
                Log.d(TAG, "leftData:" + leftData + ",rightData:" + rightData + ",leftProgressData:" + leftProgressData
                        + ",rightProgressData:" + rightProgressData + ",isFinished:" + isFinished);
            }
        });
    }

    private void testRingProgressBar() {
        RingProgressBar ringProgressBar1 = findViewById(R.id.ring_progress_bar_1);
        RingProgressBar ringProgressBar2 = findViewById(R.id.ring_progress_bar_2);
        RingProgressBar ringProgressBar3 = findViewById(R.id.ring_progress_bar_3);

        ringProgressBar1.setAlwaysShowAnimation(true);
        ringProgressBar1.setSweepGradientColors(mRedGradientColors);
        ringProgressBar1.setProgress(12f, 100);

        ringProgressBar2.setAlwaysShowAnimation(true);
        ringProgressBar2.setSweepGradientColors(mBlueGradientColors);
        ringProgressBar2.setProgress(54f, 100);

        ringProgressBar3.setAlwaysShowAnimation(false);
        ringProgressBar3.setSweepGradientColors(mGreenGradientColors);
        ringProgressBar3.setProgress(64f, 100);
    }

    private void createQrCode() {
        showLoadingDialog();
        // /data/data/com.lyric.android.app/files/qr_test0001.jpg
        final String filePath = getContext().getFilesDir().getAbsolutePath() + File.separator + "qr_test0001.jpg";
        LogUtils.d("lyricgan", "filePath:" + filePath);
        // 二维码图片较大时，生成图片、保存文件的时间可能较长，因此放在新线程中
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap qrCodeBitmap = QRCodeUtils.createQRCodeBitmap("https://www.github.com", 400, 400,
                        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), filePath);
                if (qrCodeBitmap != null) {
                    if (ivQrcodeImage != null) {
                        ivQrcodeImage.post(new Runnable() {
                            @Override
                            public void run() {
                                ivQrcodeImage.setImageBitmap(qrCodeBitmap);

                                hideLoadingDialog();
                            }
                        });
                    }
                }
            }
        }).start();
    }
}
