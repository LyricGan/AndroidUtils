package com.lyric.android.app.test.cache;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lyricgan
 * @description
 * @time 2016/7/22 12:03
 */
public class CacheManager {
    private final AtomicLong mCacheSize;
    private final AtomicInteger mCacheCount;
    private final long mSizeLimit;
    private final int mCountLimit;
    private final Map<File, Long> mLastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());
    private File mCacheDir;

    public CacheManager(File cacheDir, long sizeLimit, int countLimit) {
        this.mCacheDir = cacheDir;
        this.mSizeLimit = sizeLimit;
        this.mCountLimit = countLimit;
        mCacheSize = new AtomicLong();
        mCacheCount = new AtomicInteger();
        calculateCacheSizeAndCacheCount();
    }

    private void calculateCacheSizeAndCacheCount() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = 0;
                int count = 0;
                File[] cachedFiles = mCacheDir.listFiles();
                if (cachedFiles != null) {
                    for (File cachedFile : cachedFiles) {
                        size += calculateSize(cachedFile);
                        count += 1;
                        mLastUsageDates.put(cachedFile, cachedFile.lastModified());
                    }
                    mCacheSize.set(size);
                    mCacheCount.set(count);
                }
            }
        }).start();
    }

    public void put(File file) {
        int currentCacheCount = mCacheCount.get();
        while (currentCacheCount + 1 > mCountLimit) {
            long freedSize = removeNext();
            mCacheSize.addAndGet(-freedSize);
            currentCacheCount = mCacheCount.addAndGet(-1);
        }
        mCacheCount.addAndGet(1);

        long valueSize = calculateSize(file);
        long currentCacheSize = mCacheSize.get();
        while (currentCacheSize + valueSize > mSizeLimit) {
            long freedSize = removeNext();
            currentCacheSize = mCacheSize.addAndGet(-freedSize);
        }
        mCacheSize.addAndGet(valueSize);

        long currentTime = System.currentTimeMillis();
        file.setLastModified(currentTime);
        mLastUsageDates.put(file, currentTime);
    }

    public File get(String key) {
        File file = newFile(key);
        long currentTime = System.currentTimeMillis();
        file.setLastModified(currentTime);
        mLastUsageDates.put(file, currentTime);

        return file;
    }

    public File newFile(String key) {
        return new File(mCacheDir, key.hashCode() + "");
    }

    public boolean remove(String key) {
        File file = get(key);
        return file.delete();
    }

    public void clear() {
        mLastUsageDates.clear();
        mCacheSize.set(0);
        File[] files = mCacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }

    private long removeNext() {
        if (mLastUsageDates.isEmpty()) {
            return 0;
        }
        Long oldestUsage = null;
        File mostLongUsedFile = null;
        Set<Map.Entry<File, Long>> entries = mLastUsageDates.entrySet();
        synchronized (mLastUsageDates) {
            for (Map.Entry<File, Long> entry : entries) {
                if (mostLongUsedFile == null) {
                    mostLongUsedFile = entry.getKey();
                    oldestUsage = entry.getValue();
                } else {
                    Long lastValueUsage = entry.getValue();
                    if (lastValueUsage < oldestUsage) {
                        oldestUsage = lastValueUsage;
                        mostLongUsedFile = entry.getKey();
                    }
                }
            }
        }
        long fileSize = calculateSize(mostLongUsedFile);
        if (mostLongUsedFile != null && mostLongUsedFile.delete()) {
            mLastUsageDates.remove(mostLongUsedFile);
        }
        return fileSize;
    }

    private long calculateSize(File file) {
        return file.length();
    }
}
