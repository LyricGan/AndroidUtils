package com.lyric.android.app.retrofit.multiple;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/28 17:21
 */
public class ProgressRequestBody extends RequestBody {
    //实际的待包装请求体
    private RequestBody requestBody;
    //进度回调接口
    private MultipleApi.OnUploadCallback callback;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody, MultipleApi.OnUploadCallback callback) {
        this.requestBody = requestBody;
        this.callback = callback;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long currentSize = 0L;
            long totalSize = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (totalSize == 0) {
                    totalSize = contentLength();
                }
                currentSize += byteCount;
                Observable.just(callback)
                        .filter(new Func1<MultipleApi.OnUploadCallback, Boolean>() {
                            @Override
                            public Boolean call(MultipleApi.OnUploadCallback listener) {
                                return listener != null;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<MultipleApi.OnUploadCallback>() {
                            @Override
                            public void call(MultipleApi.OnUploadCallback listener) {
                                listener.onProgress(currentSize, totalSize, currentSize == totalSize);
                            }
                        });

            }
        };
    }
}
