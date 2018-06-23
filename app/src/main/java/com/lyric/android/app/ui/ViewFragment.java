package com.lyric.android.app.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.utils.LogUtils;
import com.lyric.android.app.utils.QRCodeUtils;
import com.lyric.android.app.utils.SnapshotUtils;
import com.lyric.android.app.utils.SpannableUtils;
import com.lyric.android.app.utils.StringUtils;
import com.lyric.android.app.utils.ToastUtils;
import com.lyric.android.app.widget.CircleProgressBar;
import com.lyric.android.app.widget.ClashBar;
import com.lyric.android.app.widget.HorizontalRatioBar;
import com.lyric.android.app.widget.PieView;
import com.lyric.android.app.widget.RingProgressBar;
import com.lyric.android.app.widget.TabDigitLayout;
import com.lyric.android.app.widget.TextClickableSpan;
import com.lyric.android.app.common.BaseFragment;
import com.lyric.android.app.utils.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * 视图测试页面
 * @author lyricgan
 */
public class ViewFragment extends BaseFragment {
    private final int[] mRedGradientColors = {0xffff0000, 0xffff6f43, 0xffff0000};
    private final int[] mBlueGradientColors = {0xff1fbbe9, 0xff59d7fc, 0xff1fbbe9};
    private final int[] mGreenGradientColors = {0xffb3c526, 0xff7fb72f, 0xffb3c526};

    private TabDigitLayout mTabDigitLayout;
    private ImageView imageCapture;
    private ImageView ivQrcodeImage;
    private Bitmap mCaptureBitmap;

    private ClashBar mClashBar;

    public static ViewFragment newInstance() {
        Bundle args = new Bundle();
        ViewFragment fragment = new ViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_view;
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        PieView pieView = findViewByIdRes(R.id.pie_view);
        imageCapture = findViewByIdRes(R.id.image_capture);
        ivQrcodeImage = findViewByIdRes(R.id.iv_qrcode_image);

        ArrayList<PieView.PieData> dataList = new ArrayList<>();
        PieView.PieData data;
        for (int i = 0; i < 5; i++) {
            data = new PieView.PieData("i" + i, 100 + (i * 50));

            dataList.add(data);
        }
        pieView.setData(dataList);
        pieView.setStartAngle(0);

        mTabDigitLayout = findViewByIdRes(R.id.tab_digit_layout);
        mTabDigitLayout.setNumber(567890, 500L);

        findViewByIdRes(R.id.btn_start).setOnClickListener(this);

        mClashBar = findViewByIdRes(R.id.clash_bar);
        findViewByIdRes(R.id.btn_clash_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clashBar();
            }
        });

        findViewByIdRes(R.id.btn_ring_progress_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringProgressBar();
            }
        });
        findViewByIdRes(R.id.btn_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQrCode();
            }
        });
        HorizontalRatioBar horizontalRatioBar = findViewByIdRes(R.id.horizontal_radio_bar);
        horizontalRatioBar.test();
    }

    @Override
    public void onDataInitialize(Bundle savedInstanceState) {
        simulateProgress();

        clashBar();

        ringProgressBar();

        spannableText();
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

    private void simulateProgress() {
        final CircleProgressBar lineProgressBar = findViewByIdRes(R.id.line_progress);
        final CircleProgressBar solidProgressBar = findViewByIdRes(R.id.solid_progress);
        final CircleProgressBar customProgressBar1 = findViewByIdRes(R.id.custom_progress1);
        final CircleProgressBar customProgressBar2 = findViewByIdRes(R.id.custom_progress2);
        final CircleProgressBar customProgressBar3 = findViewByIdRes(R.id.custom_progress3);
        final CircleProgressBar customProgressBar4 = findViewByIdRes(R.id.custom_progress4);
        final CircleProgressBar customProgressBar5 = findViewByIdRes(R.id.custom_progress5);
        final CircleProgressBar customProgressBar6 = findViewByIdRes(R.id.custom_progress6);

        ValueAnimator animator = ValueAnimator.ofInt(0, 98);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                lineProgressBar.setProgress(progress);
                solidProgressBar.setProgress(progress);
                customProgressBar1.setProgress(progress);
                customProgressBar2.setProgress(progress);
                customProgressBar3.setProgress(progress);
                customProgressBar4.setProgress(progress);
                customProgressBar5.setProgress(progress);
                customProgressBar6.setProgress(progress);
            }
        });
        animator.setDuration(2000);
        animator.start();
    }

    private void takeViewCapture(View view) {
        mCaptureBitmap = SnapshotUtils.snapShot(view);
        if (mCaptureBitmap != null) {
            imageCapture.setImageBitmap(mCaptureBitmap);

            Log.d(TAG, "memory1:" + ImageUtils.getBitmapMemory(mCaptureBitmap));
            Bitmap blurBitmap = ImageUtils.getBlurBitmap(AndroidApplication.getContext(), mCaptureBitmap, 10.0f);
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

    private void clashBar() {
        final float leftData = 37.5f + new Random().nextInt(1000);
        final float rightData = 12.5f + new Random().nextInt(1000);
        mClashBar.setOnClashBarUpdatedListener(new ClashBar.OnClashBarUpdatedListener() {
            @Override
            public void onChanged(float leftData, float rightData, float leftProgressData, float rightProgressData, boolean isFinished) {
                Log.d(TAG, "leftData:" + leftData + ",rightData:" + rightData + ",leftProgressData:" + leftProgressData
                        + ",rightProgressData:" + rightProgressData + ",isFinished:" + isFinished);
            }
        });
        // 延迟加载，防止页面初始化未完成直接调用引起的卡顿问题
        mClashBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                mClashBar.setData(leftData, rightData, true);
            }
        }, 300L);
    }

    private void ringProgressBar() {
        RingProgressBar ringProgressBar1 = findViewByIdRes(R.id.ring_progress_bar_1);
        RingProgressBar ringProgressBar2 = findViewByIdRes(R.id.ring_progress_bar_2);
        RingProgressBar ringProgressBar3 = findViewByIdRes(R.id.ring_progress_bar_3);

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
        showLoading("");
        // /data/data/com.lyric.android.app/files/qr_test0001.jpg
        final String filePath = getContext().getFilesDir().getAbsolutePath() + File.separator + "qr_test0001.jpg";
        LogUtils.d(TAG, "filePath:" + filePath);
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

                                hideLoading();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void spannableText() {
        final String testString = "字体测试字体大小一半两倍前景色背景色正常粗体斜体粗斜体下划线删除线x1x2电话邮件网站短信彩信地图X轴综合/bot/bot";
        // 创建一个SpannableString对象
        SpannableString source = SpannableUtils.valueOf(testString);
        // 设置项目符号：第一个参数表示项目符号占用的宽度，第二个参数为项目符号的颜色
        SpannableUtils.setSubSpan(source, new BulletSpan(BulletSpan.STANDARD_GAP_WIDTH, Color.GREEN), 0, source.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体(default,default-bold,monospace,serif,sans-serif)
        SpannableUtils.setSubSpan(source, new TypefaceSpan("monospace"), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableUtils.setSubSpan(source, new TypefaceSpan("serif"), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体大小（绝对值,单位：像素）
        SpannableUtils.setSubSpan(source, new AbsoluteSizeSpan(20), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableUtils.setSubSpan(source, new AbsoluteSizeSpan(20, true), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍
        SpannableUtils.setSubSpan(source, new RelativeSizeSpan(0.5f), 8, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 0.5f表示默认字体大小的一半
        SpannableUtils.setSubSpan(source, new RelativeSizeSpan(2.0f), 10, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 2.0f表示默认字体大小的两倍
        // 设置字体前景色、背景色
        SpannableUtils.setSubSpan(source, new ForegroundColorSpan(Color.MAGENTA), 12, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableUtils.setSubSpan(source, new BackgroundColorSpan(Color.CYAN), 15, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体样式正常，粗体，斜体，粗斜体
        SpannableUtils.setSubSpan(source, new StyleSpan(android.graphics.Typeface.NORMAL), 18, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableUtils.setSubSpan(source, new StyleSpan(android.graphics.Typeface.BOLD), 20, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableUtils.setSubSpan(source, new StyleSpan(android.graphics.Typeface.ITALIC), 22, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableUtils.setSubSpan(source, new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 24, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置下划线、删除线
        SpannableUtils.setSubSpan(source, new UnderlineSpan(), 27, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableUtils.setSubSpan(source, new StrikethroughSpan(), 30, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置上下标
        SpannableUtils.setSubSpan(source, new SubscriptSpan(), 34, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 下标
        SpannableUtils.setSubSpan(source, new SuperscriptSpan(), 36, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 上标

        // 超级链接（需要添加setMovementMethod方法附加响应）
        SpannableUtils.setSubSpan(source, new URLSpan("tel:4155551212"), 37, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 电话
        SpannableUtils.setSubSpan(source, new URLSpan("mailto:webmaster@google.com"), 39, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 邮件
        SpannableUtils.setSubSpan(source, new URLSpan("http://www.baidu.com"), 41, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 网络
        SpannableUtils.setSubSpan(source, new URLSpan("sms:4155551212"), 43, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 短信，使用sms:或者smsto:
        SpannableUtils.setSubSpan(source, new URLSpan("mms:4155551212"), 45, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //彩信   使用mms:或者mmsto:
        SpannableUtils.setSubSpan(source, new URLSpan("geo:38.899533,-77.036476"), 47, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //地图
        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体宽度的多少倍
        SpannableString spannableString = SpannableUtils.setSubSpan(source, new ScaleXSpan(2.0f), 49, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 2.0f表示默认字体宽度的两倍，即X轴方向放大为默认字体的两倍，而高度不变

        // 设置字体（依次包括字体名称，字体大小，字体样式，字体颜色，链接颜色）
//        ColorStateList color = null;
//        ColorStateList linkColor = null;
//        try {
//            color = ColorStateList.createFromXml(getResources(), getResources().getXml(R.color.colorAccent));
//            linkColor = ColorStateList.createFromXml(getResources(), getResources().getXml(R.color.colorPrimary));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        spannableString.setSpan(new TextAppearanceSpan("monospace", Typeface.BOLD_ITALIC, 30, color, linkColor), 51, 53, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置图片
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        SpannableUtils.setSubSpan(source, new ImageSpan(drawable), 53, 57, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableUtils.setSubSpan(source, new ImageSpan(getActivity(), R.mipmap.ic_launcher, ImageSpan.ALIGN_BOTTOM), 57, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tvSpannable = findViewByIdRes(R.id.tv_spannable);
        tvSpannable.setText(source);
        tvSpannable.setMovementMethod(LinkMovementMethod.getInstance());

        TextView tvKeywords = findViewByIdRes(R.id.tv_spannable_keywords);
        tvKeywords.setText(buildString(getActivity(), "回复", "小明", "世界是不平凡的，平凡的是你自己。"));
        tvKeywords.setHighlightColor(Color.TRANSPARENT);
        tvKeywords.setMovementMethod(LinkMovementMethod.getInstance());

        String keywordString = "不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈，我也不知道啊" +
                "不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈，我也不知道啊" +
                "不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈，我也不知道啊";

        TextView tvKeywords2 = findViewByIdRes(R.id.tv_spannable_keywords2);
        tvKeywords2.setText(StringUtils.matcherText(keywordString, new String[]{"哈哈","不知道"}, getResources().getColor(R.color.colorPrimary)));
    }

    private CharSequence buildString(final Context context, String action, final String name, String content) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(action);
        builder.append(" ");
        SpannableString spannableString = new SpannableString(name);
        int textColor = ContextCompat.getColor(context, R.color.color_blue_default);
        spannableString.setSpan(new TextClickableSpan(textColor, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(context, name);
            }
        }), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        builder.append(" ");
        builder.append(content);
        return builder;
    }
}
