package com.lyric.network;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.lyric.common.BaseHandler;

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
    private BufferedSink bufferedSink;
    private Handler handler;

    public FileRequestBody(RequestBody requestBody, FileCallback fileCallback) {
        this.requestBody = requestBody;
        this.handler = new InnerHandler(fileCallback);
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
            bufferedSink = Okio.buffer(new InnerForwardingSink(sink, contentLength(), handler));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private static class InnerForwardingSink extends ForwardingSink {
        long currentSize;
        long totalSize;
        Handler handler;

        InnerForwardingSink(Sink delegate, long totalSize, Handler handler) {
            super(delegate);
            this.totalSize = totalSize;
            this.handler = handler;
        }

        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            currentSize += byteCount;

            if (handler != null) {
                handler.sendMessage(Message.obtain(handler, 0, new FileMessage(totalSize, currentSize, totalSize == currentSize)));
            }
        }
    }

    private static class InnerHandler extends BaseHandler<FileCallback> {

        InnerHandler(FileCallback object) {
            super(object);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FileCallback fileCallback = get();
            if (fileCallback != null) {
                FileMessage fileMessage = (FileMessage) msg.obj;
                if (fileMessage != null) {
                    fileCallback.onProgress(fileMessage.getTotalSize(), fileMessage.getCurrentSize(), fileMessage.isFinished());
                }
            }
        }
    }
}
