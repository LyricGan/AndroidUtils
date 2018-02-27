package com.lyric.android.app.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.common.Constants;
import com.lyric.android.app.utils.ToastUtils;
import com.lyric.android.app.widget.WebLayout;
import com.lyric.common.BaseFragment;

/**
 * 网页显示页面
 * @author lyricgan
 */
public class WebFragment extends BaseFragment {
    private WebLayout mWebLayout;

    private String mUrl;

    public static WebFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(Constants.EXTRAS_URL, url);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onExtrasInitialize(Bundle bundle) {
        super.onExtrasInitialize(bundle);
        mUrl = bundle.getString(Constants.EXTRAS_URL);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void onContentViewInitialize(View view, Bundle savedInstanceState) {
        mWebLayout = findViewByIdRes(R.id.layout_web);

        WebView webView = mWebLayout.getWebView();
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onWebLongClick(v);
            }
        });
    }

    @Override
    public void onDataInitialize(Bundle savedInstanceState) {
        super.onDataInitialize(savedInstanceState);
        if (!TextUtils.isEmpty(mUrl)) {
            mWebLayout.loadUrl(mUrl);
        }
    }

    @Override
    public boolean onBackPressed() {
        return (mWebLayout != null && mWebLayout.onBackPressed()) || super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebLayout != null) {
            mWebLayout.destroy();
        }
    }

    private boolean onWebLongClick(View v) {
        WebView.HitTestResult result = ((WebView) v).getHitTestResult();
        if (result == null) {
            return false;
        }
        boolean callback = false;
        int type = result.getType();
        switch (type) {
            case WebView.HitTestResult.PHONE_TYPE:
                break;
            case WebView.HitTestResult.EMAIL_TYPE:
                break;
            case WebView.HitTestResult.GEO_TYPE:
                break;
            case WebView.HitTestResult.SRC_ANCHOR_TYPE:
                break;
            case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                break;
            case WebView.HitTestResult.IMAGE_TYPE:
                final String imageUrl = result.getExtra();
                if (TextUtils.isEmpty(imageUrl)) {
                    break;
                }
                showDialog(imageUrl);
                callback = true;
                break;
            default:
                break;
        }
        return callback;
    }

    private void showDialog(final String imageUrl) {
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage("确定保存图片到本地")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        ToastUtils.showShort(AndroidApplication.getContext(), "图片地址为" + imageUrl);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}
