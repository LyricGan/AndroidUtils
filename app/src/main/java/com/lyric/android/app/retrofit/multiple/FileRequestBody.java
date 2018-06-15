package com.lyric.android.app.retrofit.multiple;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/28 17:21
 */
public class FileRequestBody extends RequestBody {
    private RequestBody requestBody;
    private FileCallback callback;
    private BufferedSink bufferedSink;

    public FileRequestBody(RequestBody requestBody, FileCallback callback) {
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
//                Observable.just(callback)
//                        .filter(new Func1<FileCallback, Boolean>() {
//                            @Override
//                            public Boolean call(FileCallback fileCallback) {
//                                return fileCallback != null;
//                            }
//                        })
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Action1<FileCallback>() {
//                            @Override
//                            public void call(FileCallback callback) {
//                                callback.onProgress(currentSize, totalSize, (currentSize == totalSize));
//                            }
//                        });
            }
        };
    }
}
