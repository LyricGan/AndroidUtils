package com.lyricgan.library.network;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 文件响应类
 * @author Lyric Gan
 * @since 17/12/30 下午9:55
 */
public class FileResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private Handler handler;
    /** 文件保存路径 */
    private String filePath;
    /** 文件保存名称 */
    private String fileName;
    private BufferedSource bufferedSource;

    public FileResponseBody(ResponseBody responseBody, FileCallback fileCallback) {
        this.responseBody = responseBody;
        this.handler = new InnerHandler(fileCallback);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public MediaType contentType() {
        if (responseBody != null) {
            return responseBody.contentType();
        }
        return null;
    }

    @Override
    public long contentLength() {
        if (responseBody != null) {
            return responseBody.contentLength();
        }
        return 0;
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(new InnerForwardingSource(responseBody.source(), contentLength(), handler));
        }
        return bufferedSource;
    }

    private static class InnerForwardingSource extends ForwardingSource {
        long currentSize;
        long totalSize;
        Handler handler;

        InnerForwardingSource(Source delegate, long totalSize, Handler handler) {
            super(delegate);
            this.totalSize = totalSize;
            this.handler = handler;
        }

        @Override
        public long read(@NonNull Buffer sink, long byteCount) throws IOException {
            final long bytesRead = super.read(sink, byteCount);
            currentSize += bytesRead != -1 ? bytesRead : 0;

            if (handler != null) {
                handler.sendMessage(Message.obtain(handler, 0, new FileMessage(totalSize, currentSize, bytesRead == -1)));
            }
            return bytesRead;
        }
    }

    private static class InnerHandler extends WeakHandler<FileCallback> {

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
