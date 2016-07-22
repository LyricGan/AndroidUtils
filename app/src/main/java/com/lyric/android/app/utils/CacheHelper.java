/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lyric.android.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 缓存工具类，用来缓存字符串，JsonObject、JsonArray、Bitmap、Drawable、序列化的java对象，和byte数据。
 * 可以配置缓存路径，缓存大小，缓存数量等。
 * 可以设置缓存超时时间，缓存超时自动失效，并被删除。
 */
public class CacheHelper {
    private static final String DEFAULT_CACHE_NAME = "CacheHelper";
    private static final int MAX_SIZE = 1000 * 1000 * 50; // 50 mb
    private static final int MAX_COUNT = Integer.MAX_VALUE; // 不限制存放数据的数量
    private static Map<String, CacheHelper> mInstanceMap = new HashMap<>();
    private CacheManager mCacheManager;

    private CacheHelper(File cacheDir, long maxSize, int maxCount) {
        if (!cacheDir.exists() && !cacheDir.mkdirs()) {
            throw new RuntimeException("can't make dirs in " + cacheDir.getAbsolutePath());
        }
        mCacheManager = new CacheManager(cacheDir, maxSize, maxCount);
    }

    public static CacheHelper get(Context context) {
        return get(context, DEFAULT_CACHE_NAME);
    }

    public static CacheHelper get(Context context, String cacheName) {
        File file = new File(context.getCacheDir(), cacheName);
        return get(file, MAX_SIZE, MAX_COUNT);
    }

    public static CacheHelper get(File cacheDir) {
        return get(cacheDir, MAX_SIZE, MAX_COUNT);
    }

    public static CacheHelper get(Context context, long maxSize, int maxCount) {
        File file = new File(context.getCacheDir(), DEFAULT_CACHE_NAME);
        return get(file, maxSize, maxCount);
    }

    public static CacheHelper get(File cacheDir, long maxSize, int maxCount) {
        CacheHelper helper = mInstanceMap.get(cacheDir.getAbsoluteFile() + myPid());
        if (helper == null) {
            helper = new CacheHelper(cacheDir, maxSize, maxCount);
            mInstanceMap.put(cacheDir.getAbsolutePath() + myPid(), helper);
        }
        return helper;
    }

    private static String myPid() {
        return "_" + android.os.Process.myPid();
    }

    /**
     * 保存String数据到缓存中
     *
     * @param key   保存的key
     * @param value 保存的String数据
     */
    public void put(String key, String value) {
        File file = mCacheManager.newFile(key);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file), 1024);
            out.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCacheManager.put(file);
        }
    }

    /**
     * 保存String数据到缓存中
     *
     * @param key      保存的key
     * @param value    保存的String数据
     * @param expireTime 保存的时间，单位：秒
     */
    public void put(String key, String value, int expireTime) {
        put(key, CacheTimeUtils.newStringWithDateInfo(expireTime, value));
    }

    /**
     * 读取String数据
     *
     * @param key
     * @return String 数据
     */
    public String getString(String key) {
        File file = mCacheManager.get(key);
        if (!file.exists())
            return null;
        boolean removeFile = false;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String readString = "";
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                readString += currentLine;
            }
            if (!CacheTimeUtils.isExpire(readString)) {
                return CacheTimeUtils.clearDateInfo(readString);
            } else {
                removeFile = true;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile) {
                remove(key);
            }
        }
    }

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的JSON数据
     */
    public void put(String key, JSONObject value) {
        put(key, value.toString());
    }

    /**
     * 保存 JSONObject数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的JSONObject数据
     * @param expireTime 保存的时间，单位：秒
     */
    public void put(String key, JSONObject value, int expireTime) {
        put(key, value.toString(), expireTime);
    }

    /**
     * 读取JSONObject数据
     *
     * @param key
     * @return JSONObject数据
     */
    public JSONObject getJSONObject(String key) {
        String JSONString = getString(key);
        try {
            return new JSONObject(JSONString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的JSONArray数据
     */
    public void put(String key, JSONArray value) {
        put(key, value.toString());
    }

    /**
     * 保存 JSONArray数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的JSONArray数据
     * @param expireTime 保存的时间，单位：秒
     */
    public void put(String key, JSONArray value, int expireTime) {
        put(key, value.toString(), expireTime);
    }

    /**
     * 读取JSONArray数据
     *
     * @param key
     * @return JSONArray数据
     */
    public JSONArray getJSONArray(String key) {
        String JSONString = getString(key);
        try {
            return new JSONArray(JSONString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的数据
     */
    public void put(String key, byte[] value) {
        File file = mCacheManager.newFile(key);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mCacheManager.put(file);
        }
    }

    /**
     * Cache for a stream
     *
     * @param key the file name.
     * @return OutputStream stream for writing data.
     * @throws FileNotFoundException if the file can not be created.
     */
    public OutputStream put(String key) throws FileNotFoundException {
        return new InnerFileOutputStream(mCacheManager.newFile(key));
    }

    /**
     * @param key the file name.
     * @return (InputStream or null) stream previously saved in cache.
     * @throws FileNotFoundException if the file can not be opened
     */
    public InputStream get(String key) throws FileNotFoundException {
        File file = mCacheManager.get(key);
        if (!file.exists())
            return null;
        return new FileInputStream(file);
    }

    /**
     * 保存 byte数据 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的数据
     * @param expireTime 保存的时间，单位：秒
     */
    public void put(String key, byte[] value, int expireTime) {
        put(key, CacheTimeUtils.newByteArrayWithDateInfo(expireTime, value));
    }

    /**
     * 获取 byte 数据
     *
     * @param key
     * @return byte 数据
     */
    public byte[] getBytes(String key) {
        RandomAccessFile randomAccessFile = null;
        boolean removeFile = false;
        try {
            File file = mCacheManager.get(key);
            if (!file.exists()) {
                return null;
            }
            randomAccessFile = new RandomAccessFile(file, "r");
            byte[] byteArray = new byte[(int) randomAccessFile.length()];
            randomAccessFile.read(byteArray);
            if (!CacheTimeUtils.isExpire(byteArray)) {
                return CacheTimeUtils.clearDateInfo(byteArray);
            } else {
                removeFile = true;
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (removeFile) {
                remove(key);
            }
        }
    }

    /**
     * 保存 Serializable数据到缓存中
     *
     * @param key   保存的key
     * @param value 保存的value
     */
    public void put(String key, Serializable value) {
        put(key, value, -1);
    }

    /**
     * 保存 Serializable数据到缓存中
     *
     * @param key      保存的key
     * @param value    保存的value
     * @param expireTime 保存的时间，单位：秒
     */
    public void put(String key, Serializable value, int expireTime) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            byte[] data = baos.toByteArray();
            if (expireTime != -1) {
                put(key, data, expireTime);
            } else {
                put(key, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取 Serializable数据
     *
     * @param key
     * @return Serializable 数据
     */
    public Object getObject(String key) {
        byte[] data = getBytes(key);
        if (data != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (bais != null)
                        bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (ois != null)
                        ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 保存bitmap到缓存中
     *
     * @param key   保存的key
     * @param value 保存的bitmap数据
     */
    public void put(String key, Bitmap value) {
        put(key, CacheTimeUtils.bitmap2Bytes(value));
    }

    /**
     * 保存bitmap到缓存中
     *
     * @param key      保存的key
     * @param value    保存的 bitmap 数据
     * @param expireTime 保存的时间，单位：秒
     */
    public void put(String key, Bitmap value, int expireTime) {
        put(key, CacheTimeUtils.bitmap2Bytes(value), expireTime);
    }

    /**
     * 读取 bitmap 数据
     *
     * @param key
     * @return bitmap 数据
     */
    public Bitmap getBitmap(String key) {
        if (getBytes(key) == null) {
            return null;
        }
        return CacheTimeUtils.bytes2Bitmap(getBytes(key));
    }

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key   保存的key
     * @param value 保存的drawable数据
     */
    public void put(String key, Drawable value) {
        put(key, CacheTimeUtils.drawable2Bitmap(value));
    }

    /**
     * 保存 drawable 到 缓存中
     *
     * @param key      保存的key
     * @param value    保存的 drawable 数据
     * @param expireTime 保存的时间，单位：秒
     */
    public void put(String key, Drawable value, int expireTime) {
        put(key, CacheTimeUtils.drawable2Bitmap(value), expireTime);
    }

    /**
     * 读取 Drawable 数据
     *
     * @param key
     * @return Drawable 数据
     */
    public Drawable getDrawable(String key) {
        if (getBytes(key) == null) {
            return null;
        }
        return CacheTimeUtils.bitmap2Drawable(CacheTimeUtils.bytes2Bitmap(getBytes(key)));
    }

    /**
     * 获取缓存文件
     *
     * @param key
     * @return value 缓存的文件
     */
    public File getFile(String key) {
        File file = mCacheManager.newFile(key);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    /**
     * 移除某个key
     *
     * @param key
     * @return 是否移除成功
     */
    public boolean remove(String key) {
        return mCacheManager.remove(key);
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        mCacheManager.clear();
    }

    /**
     * Provides a means to save a cached file before the data are available.
     * Since writing about the file is complete, and its close method is called,
     * its contents will be registered in the cache. Example of use:
     * <p>
     * CacheHelper cache = new CacheHelper(this) try { OutputStream stream =
     * cache.put("myFileName") stream.write("some bytes".getBytes()); // now
     * update cache! stream.close(); } catch(FileNotFoundException e){
     * e.printStackTrace() }
     */
    class InnerFileOutputStream extends FileOutputStream {
        File file;

        public InnerFileOutputStream(File file) throws FileNotFoundException {
            super(file);
            this.file = file;
        }

        public void close() throws IOException {
            super.close();
            mCacheManager.put(file);
        }
    }
}
