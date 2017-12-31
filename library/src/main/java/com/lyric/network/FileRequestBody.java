package com.lyric.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 文件请求
 * @author lyricgan
 * @date 17/12/31 下午11:00
 */
public class FileRequestBody extends RequestBody {
    private RequestBody requestBody;
    private FileCallback fileCallback;
    private BufferedSink bufferedSink;

    public FileRequestBody(RequestBody requestBody, FileCallback fileCallback) {
        this.requestBody = requestBody;
        this.fileCallback = fileCallback;
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
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
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
            public void write(@NonNull Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (totalSize == 0) {
                    totalSize = contentLength();
                }
                currentSize += byteCount;
                if (fileCallback != null) {
                    fileCallback.onProgress(totalSize, currentSize, (currentSize == totalSize));
                }
            }
        };
    }
}
