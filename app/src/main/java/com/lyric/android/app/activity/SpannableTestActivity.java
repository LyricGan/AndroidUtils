package com.lyric.android.app.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
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
import android.widget.TextView;

import com.lyric.android.app.BaseCompatActivity;
import com.lyric.android.app.R;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.library.utils.KeywordUtils;

public class SpannableTestActivity extends BaseCompatActivity {

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_spannable_test);
        TextView tv_spannable = (TextView) findViewById(R.id.tv_spannable);
        TextView tv_spannable_keywords = (TextView) findViewById(R.id.tv_spannable_keywords);

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
        spannableString.setSpan(new ImageSpan(this, R.mipmap.ic_launcher, ImageSpan.ALIGN_BOTTOM), 57, 61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_spannable.setText(spannableString);
        tv_spannable.setMovementMethod(LinkMovementMethod.getInstance());

        String keywordString = "不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈，我也不知道啊" +
                "不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈，我也不知道啊" +
                "不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈不是我说哈哈，我也不知道啊";
        tv_spannable_keywords.setText(KeywordUtils.matcherText(keywordString, new String[]{"哈哈","不知道"}, getResources().getColor(R.color.colorPrimary)));
    }

    @Override
    public void onTitleCreated(TitleBar titleBar) {
        titleBar.setText("SpannableTest");
    }
}
