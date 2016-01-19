package com.lyric.android.library.preference;

/**
 * @author ganyu
 * @description preference type
 * @time 16/1/17 下午9:27
 */
public enum PreferenceType {
    READ(1),
    WRITE(2),
    REMOVE(3),
    CLEAR(4);

    final int mType;

    PreferenceType(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }
}
