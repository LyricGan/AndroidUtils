package com.lyricgan.util.sample;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lyricgan.util.ToastUtils;

/**
 * 主页面
 * @author Lyric Gan
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_test).setOnClickListener(v -> onTestClick());
    }

    private void onTestClick() {
        ToastUtils.showToast(this, R.string.test);
    }
}
