package com.lyric.android.app.retrofit.multiple;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/28 17:21
 */
public class ProgressResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private final MultipleApi.OnDownloadCallback downloadCallback;
    private BufferedSource bufferedSource;

    /**
     * 文件保存路径
     */
    private String savePath;

    /**
     * 下载文件名
     */
    private String fileName;

    public ProgressResponseBody(ResponseBody responseBody, MultipleApi.OnDownloadCallback callback) {
        this.responseBody = responseBody;
        this.downloadCallback = callback;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {
            long currentSize = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                final long bytesRead = super.read(sink, byteCount);
                currentSize += bytesRead != -1 ? bytesRead : 0;
                Observable.just(downloadCallback)
                        .filter(new Func1<MultipleApi.OnDownloadCallback, Boolean>() {
                            @Override
                            public Boolean call(MultipleApi.OnDownloadCallback onDownloadCallback) {
                                return onDownloadCallback != null;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<MultipleApi.OnDownloadCallback>() {
                            @Override
                            public void call(MultipleApi.OnDownloadCallback onDownloadCallback) {
                                downloadCallback.onProgress(currentSize, responseBody.contentLength(), bytesRead == -1);
                            }
                        });
                return bytesRead;
            }
        };
    }
}