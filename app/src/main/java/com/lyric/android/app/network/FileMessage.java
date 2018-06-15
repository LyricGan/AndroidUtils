package com.lyric.android.app.network;

/**
 * @author lyricgan
 * @date 18/1/7 下午6:14
 */
public class FileMessage {
    private long totalSize;
    private long currentSize;
    private boolean isFinished;

    public FileMessage(long totalSize, long currentSize, boolean isFinished) {
        this.totalSize = totalSize;
        this.currentSize = currentSize;
        this.isFinished = isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }
}
