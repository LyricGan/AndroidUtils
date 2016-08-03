package com.lyric.android.app.test;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author lyricgan
 * @description
 * @time 2016/8/3 16:58
 */
public class TestDatabase {
    public static final String AUTHORITY = "com.provider.test";
    public static final String DATABASE_NAME = "test.db";
    public static final int DATABASE_VERSION = 1;

    public static final class ItemColumns implements BaseColumns {
        public static final String TABLE_NAME = "test";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TABLE_NAME;

        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String CREATE_DATE = "create_date";

        public static final String DEFAULT_ORDER_BY = "create_date DESC";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY,"
                + TITLE + " VARCHAR(50),"
                + CONTENT + " TEXT,"
                + CREATE_DATE + " INTEGER"
                + ");" ;
    }
}
