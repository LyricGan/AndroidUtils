package com.lyric.android.app.fragment;

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
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
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
import com.lyric.android.app.common.BaseFragment;
import com.lyric.android.app.utils.LogUtils;
import com.lyric.android.app.utils.QRCodeUtils;
import com.lyric.android.app.utils.SnapshotUtils;
import com.lyric.android.app.widget.CircleProgressBar;
import com.lyric.android.app.widget.ClashBar;
import com.lyric.android.app.widget.HorizontalRatioBar;
import com.lyric.android.app.widget.PieView;
import com.lyric.android.app.widget.RingProgressBar;
import com.lyric.android.app.widget.TabDigitLayout;
import com.lyric.utils.ImageUtils;
import com.lyric.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * 视图测试页面
 * @author lyricgan
 * @date 2017/7/25 14:57
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
    public void onViewInitialize(View view, Bundle savedInstanceState) {
        PieView pieView = findViewWithId(R.id.pie_view);
        imageCapture = findViewWithId(R.id.image_capture);
        ivQrcodeImage = findViewWithId(R.id.iv_qrcode_image);

        ArrayList<PieView.PieData> dataList = new ArrayList<>();
        PieView.PieData data;
        for (int i = 0; i < 5; i++) {
            data = new PieView.PieData("i" + i, 100 + (i * 50));

            dataList.add(data);
        }
        pieView.setData(dataList);
        pieView.setStartAngle(0);

        mTabDigitLayout = findViewWithId(R.id.tab_digit_layout);
        mTabDigitLayout.setNumber(567890, 500L);

        findViewWithId(R.id.btn_start).setOnClickListener(this);

        mClashBar = findViewWithId(R.id.clash_bar);
        findViewWithId(R.id.btn_clash_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testClashBar();
            }
        });

        findViewWithId(R.id.btn_ring_progress_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRingProgressBar();
            }
        });
        findViewWithId(R.id.btn_qr_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQrCode();
            }
        });
        findViewWithId(R.id.btn_show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
            }
        });
        HorizontalRatioBar horizontalRatioBar = findViewWithId(R.id.horizontal_radio_bar);
        horizontalRatioBar.test();
    }

    @Override
    public void onDataInitialize(Bundle savedInstanceState) {
        testClashBar();

        testRingProgressBar();
    }

    @Override
    public void onViewClick(View v) {
        super.onViewClick(v);
        switch (v.getId()) {
            case R.id.btn_start:
                takeViewCapture(mTabDigitLayout);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        simulateProgress();
    }

    private void simulateProgress() {
        final CircleProgressBar lineProgressBar = findViewWithId(R.id.line_progress);
        final CircleProgressBar solidProgressBar = findViewWithId(R.id.solid_progress);
        final CircleProgressBar customProgressBar1 = findViewWithId(R.id.custom_progress1);
        final CircleProgressBar customProgressBar2 = findViewWithId(R.id.custom_progress2);
        final CircleProgressBar customProgressBar3 = findViewWithId(R.id.custom_progress3);
        final CircleProgressBar customProgressBar4 = findViewWithId(R.id.custom_progress4);
        final CircleProgressBar customProgressBar5 = findViewWithId(R.id.custom_progress5);
        final CircleProgressBar customProgressBar6 = findViewWithId(R.id.custom_progress6);

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
            Bitmap blurBitmap = ImageUtils.blurBitmap(AndroidApplication.getContext(), mCaptureBitmap, 10.0f);
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

    private void testRingProgressBar() {
        RingProgressBar ringProgressBar1 = findViewWithId(R.id.ring_progress_bar_1);
        RingProgressBar ringProgressBar2 = findViewWithId(R.id.ring_progress_bar_2);
        RingProgressBar ringProgressBar3 = findViewWithId(R.id.ring_progress_bar_3);

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

                                hideLoadingDialog();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private void spannableText() {
        TextView tv_spannable = findViewWithId(R.id.tv_spannable);
        TextView tv_spannable_keywords = findViewWithId(R.id.tv_spannable_keywords);
        TextView tv_spannable_keywords2 = findViewWithId(R.id.tv_spannable_keywords2);

        // 创建一个SpannableString对象
        SpannableString spannableString = new SpannableString("字体测试字体大小一半两倍前景色背景色正常粗体斜体粗斜体下划线删除线x1x2电话邮件网站短信彩信地图X轴综合/bot/bot");
        // 设置项目符号：第一个参数表示项目符号占用的宽度，第二个参数为项目符号的颜色
        spannableString.setSpan(new BulletSpan(android.text.style.BulletSpan.STANDARD_GAP_WIDTH, Color.GREEN), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体(default,default-bold,monospace,serif,sans-serif)
        spannableString.setSpan(new TypefaceSpan("monospace"), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TypefaceSpan("serif"), 2, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体大小（绝对值,单位：像素）
        spannableString.setSpan(new AbsoluteSizeSpan(20), 4, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(20, true), 6, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍
        spannableString.setSpan(new RelativeSizeSpan(0.5f), 8, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 0.5f表示默认字体大小的一半
        spannableString.setSpan(new RelativeSizeSpan(2.0f), 10, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 2.0f表示默认字体大小的两倍
        // 设置字体前景色、背景色
        spannableString.setSpan(new ForegroundColorSpan(Color.MAGENTA), 12, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new BackgroundColorSpan(Color.CYAN), 15, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体样式正常，粗体，斜体，粗斜体
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 18, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 20, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 22, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 24, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置下划线、删除线
        spannableString.setSpan(new UnderlineSpan(), 27, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StrikethroughSpan(), 30, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置上下标
        spannableString.setSpan(new SubscriptSpan(), 34, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 下标
        spannableString.setSpan(new SuperscriptSpan(), 36, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 上标

        // 超级链接（需要添加setMovementMethod方法附加响应）
        spannableString.setSpan(new URLSpan("tel:4155551212"), 37, 39, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 电话
        spannableString.setSpan(new URLSpan("mailto:webmaster@google.com"), 39, 41, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 邮件
        spannableString.setSpan(new URLSpan("http://www.baidu.com"), 41, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 网络
        spannableString.setSpan(new URLSpan("sms:4155551212"), 43, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 短信，使用sms:或者smsto:
        spannableString.setSpan(new URLSpan("mms:4155551212"), 45, 47, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //彩信   使用mms:或者mmsto:
        spannableString.setSpan(new URLSpan("geo:38.899533,-77.036476"), 47, 49, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);     //地图
        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体宽度的多少倍
        spannableString.setSpan(new ScaleXSpan(2.0f), 49, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 2.0f表示默认字体宽度的两倍，即X轴方向放大为默认字体的两倍，而高度不变

        // 设置字体（依次包括字体名称，字体大小，字体样式，字体颜色，链接颜色）
//        ColorStateList color = null;
//        ColorStateList linkColor = null;
//        try {
//            color = ColorStateList.createFromXml(getResources(), getResources().getXml(R.color.colorAccent));
//            linkColor = ColorStateList.createFromXml(getResources(), getResources().getXml(R.color.colorPrimary));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        spannableString.setSpan(new TextAppearanceSpan("monospace", android.graphics.Typeface.BOLD_ITALIC, 30, color, linkColor), 51, 53, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //设置图片
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        spannableString.setSpan(new ImageSpan(drawable), 53, 57, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ImageSpan(getActivity(), R.mipmap.ic_launcher, ImageSpan.ALIGN_BOTTOM), 57, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_spannable.setText(spannableString);
        tv_spannable.setMovementMethod(LinkMovementMethod.getInstance());

        String keywordString = "不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈，我也不知道啊" +
                "不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈，我也不知道啊" +
                "不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈，我也不知道啊";
        tv_spannable_keywords.setText(StringUtils.matcherText(keywordString, new String[]{"哈哈","不知道"}, getResources().getColor(R.color.colorPrimary)));

        tv_spannable_keywords2.setText(buildString(getActivity(), "回复", "小明", "世界是不平凡的，平凡的是你自己。"));
        tv_spannable_keywords2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public CharSequence buildString(Context context, String action, String name, String content) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(action);
        builder.append(" ");
        SpannableString spannableString = new SpannableString(name);
        spannableString.setSpan(new TextClickableSpan(new TextSpanClickImpl(context), name), 0, name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        builder.append(" ");
        builder.append(content);
        return builder;
    }

    private static class TextSpanClickImpl implements ITextSpanClickListener {
        private Context mContext;

        TextSpanClickImpl(Context context) {
            this.mContext = context;
        }

        @Override
        public void onClick(String value) {
        }
    }

    private static class TextClickableSpan extends ClickableSpan implements View.OnClickListener {
        private final ITextSpanClickListener mListener;
        private String mValue;

        TextClickableSpan(ITextSpanClickListener listener, String value) {
            mListener = listener;
            mValue = value;
        }

        @Override
        public void onClick(View widget) {
            if (mListener != null) {
                mListener.onClick(mValue);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(ContextCompat.getColor(AndroidApplication.getContext(), R.color.color_blue_loading));
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }
    }

    interface ITextSpanClickListener {

        void onClick(String value);
    }
}
