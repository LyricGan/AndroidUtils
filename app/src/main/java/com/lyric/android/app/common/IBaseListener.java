package com.lyric.android.app.common;

import android.os.Bundle;
import android.view.View;

/**
 * 基础接口类
 *
 * @author lyricgan
 */
public interface IBaseListener {

    void onCreatePrepare(Bundle savedInstanceState);

    void onCreateExtras(Bundle bundle);

    int getLayoutId();

    void onCreateTitleBar(BaseTitleBar titleBar, Bundle savedInstanceState);

	void onCreateContentView(View view, Bundle savedInstanceState);

    void onCreateData(Bundle savedInstanceState);

}