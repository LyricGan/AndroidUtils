package com.lyric.android.app.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lyric.android.app.AndroidApplication;
import com.lyric.android.app.R;
import com.lyric.android.app.common.BaseFragment;
import com.lyric.android.app.common.Constants;
import com.lyric.android.app.utils.ToastUtils;
import com.lyric.android.app.widget.web.WebViewCompat;

/**
 * web fragment
 *
 * @author Lyric Gan
 */
public class WebFragment extends BaseFragment {
    private WebViewCompat mWebView;
    private ProgressBar mProgressBar;

    private String mUrl;

    public static WebFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(Constants.EXTRAS_URL, url);
        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateExtras(Bundle bundle) {
        super.onCreateExtras(bundle);
        mUrl = bundle.getString(Constants.EXTRAS_URL);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_web;
    }

    @Override
    public void onCreateContentView(View view, Bundle savedInstanceState) {
        mWebView = findViewById(R.id.web_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onWebLongClick(v);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (mProgressBar != null) {
                    if (newProgress >= 90) {
                        mWebView.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    } else {
                        mWebView.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onCreateData(Bundle savedInstanceState) {
        super.onCreateData(savedInstanceState);
        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mWebView != null) {
            return mWebView.onBackPressed();
        }
        return super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.onDestroy();
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

                        ToastUtils.show(AndroidApplication.getContext(), "图片地址为" + imageUrl);
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
