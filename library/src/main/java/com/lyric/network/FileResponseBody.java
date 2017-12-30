package com.lyric.network;

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
 * @author lyricgan
 * @date 17/12/30 下午9:55
 */
public class FileResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private FileCallback fileCallback;
    private String filePath;
    private String fileName;
    private BufferedSource bufferedSource;

    public FileResponseBody(ResponseBody responseBody, FileCallback fileCallback) {
        this.responseBody = responseBody;
        this.fileCallback = fileCallback;
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
            bufferedSource = Okio.buffer(new InnerForwardingSource(responseBody.source(), responseBody, fileCallback));
        }
        return bufferedSource;
    }

    private static class InnerForwardingSource extends ForwardingSource {
        long currentSize = 0L;
        ResponseBody responseBody;
        FileCallback fileCallback;

        InnerForwardingSource(Source delegate, ResponseBody responseBody, FileCallback fileCallback) {
            super(delegate);
            this.responseBody = responseBody;
            this.fileCallback = fileCallback;
        }

        @Override
        public long read(@NonNull Buffer sink, long byteCount) throws IOException {
            final long bytesRead = super.read(sink, byteCount);
            currentSize += bytesRead != -1 ? bytesRead : 0;
            if (fileCallback != null) {
                fileCallback.onProgress(responseBody.contentLength(), currentSize, bytesRead == -1);
            }
            return bytesRead;
        }
    }
}
