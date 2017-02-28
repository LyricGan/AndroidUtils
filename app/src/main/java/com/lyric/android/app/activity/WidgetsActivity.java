package com.lyric.android.app.activity;

import android.os.Bundle;

import com.lyric.android.app.R;
import com.lyric.android.app.base.BaseCompatActivity;
import com.lyric.android.app.view.TitleBar;
import com.lyric.android.app.widget.viewpager.AutoScrollViewPager;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 组件类
 * @author lyricgan
 * @time 17/2/25
 */
public class WidgetsActivity extends BaseCompatActivity {
    private TabPageIndicator tab_page_indicator;
    private AutoScrollViewPager auto_view_pager;

    @Override
    public void onTitleCreated(TitleBar titleBar) {
        titleBar.setText("Widgets");
    }

    @Override
    public void onViewCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_widgets);
        tab_page_indicator = (TabPageIndicator) findViewById(R.id.tab_page_indicator);
        auto_view_pager = (AutoScrollViewPager) findViewById(R.id.auto_view_pager);

    }
}
