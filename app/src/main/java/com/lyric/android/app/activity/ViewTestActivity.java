package com.lyric.android.app.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.lyric.android.app.BaseActivity;
import com.lyric.android.app.widget.MovedCircleView;

/**
 * @author lyric
 * @description
 * @time 2016/3/15 15:12
 */
public class ViewTestActivity extends BaseActivity {

    @Override
    public void onInitView(Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(params);

        MovedCircleView movedCircleView = new MovedCircleView(this);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewParams.gravity = Gravity.CENTER;
        movedCircleView.setLayoutParams(viewParams);
        linearLayout.addView(movedCircleView);

        setContentView(linearLayout);
    }

    public void getMemory() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long currentMemory = totalMemory - freeMemory;
        System.gc();
    }
}
