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
 * 文件请求类
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
            bufferedSink = Okio.buffer(new InnerForwardingSink(sink, contentLength(), fileCallback));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private static class InnerForwardingSink extends ForwardingSink {
        long currentSize;
        long totalSize;
        FileCallback fileCallback;

        InnerForwardingSink(Sink delegate, long totalSize, FileCallback fileCallback) {
            super(delegate);
            this.totalSize = totalSize;
            this.fileCallback = fileCallback;
        }

        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            currentSize += byteCount;

            if (fileCallback != null) {
                fileCallback.onProgress(totalSize, currentSize, (currentSize == totalSize));
            }
        }
    }
}
