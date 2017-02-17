package com.lyric.android.app.third.retrofit.multiple;

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
public class FileResponseBody extends ResponseBody {
    private final ResponseBody responseBody;
    private final FileCallback callback;
    private BufferedSource bufferedSource;
    /**
     * 文件保存路径
     */
    private String filePath;
    /**
     * 下载文件名
     */
    private String fileName;

    public FileResponseBody(ResponseBody responseBody, FileCallback callback) {
        this.responseBody = responseBody;
        this.callback = callback;
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
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(convert(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source convert(Source source) {
        return new ForwardingSource(source) {
            long currentSize = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                final long bytesRead = super.read(sink, byteCount);
                currentSize += bytesRead != -1 ? bytesRead : 0;
                Observable.just(callback)
                        .filter(new Func1<FileCallback, Boolean>() {
                            @Override
                            public Boolean call(FileCallback fileCallback) {
                                return fileCallback != null;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<FileCallback>() {
                            @Override
                            public void call(FileCallback callback) {
                                callback.onProgress(currentSize, responseBody.contentLength(), bytesRead == -1);
                            }
                        });
                return bytesRead;
            }
        };
    }
}