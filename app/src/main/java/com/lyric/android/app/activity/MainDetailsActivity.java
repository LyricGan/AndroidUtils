package com.lyric.android.app.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.lyric.android.app.R;
import com.lyric.android.app.test.Test;

public class MainDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("我的课程");
    }

    public void onCheck(View view) {
        Snackbar.make(view, "连续点击应用将崩溃", Snackbar.LENGTH_SHORT).show();

        Test.getInstance().printMemoryInfo(this);
        Test.getInstance().newLeaky();
    }
}
