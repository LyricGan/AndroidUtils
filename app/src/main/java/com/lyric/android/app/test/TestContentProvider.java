package com.lyric.android.app.test;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * ContentProvider
 * @author lyricgan
 * @time 2016/8/3 16:49
 */
public class TestContentProvider extends ContentProvider {
    private static final UriMatcher mUriMatcher;
    private static final int INDICATOR_GROUP = 1;
    private static final int INDICATOR_ITEM = 2;
    private static final String PATH = "path_test";
    private static HashMap<String, String> mProjectionMap;

    public static final String AUTHORITY = "com.provider.test";
    private DatabaseHelper mDatabaseHelper;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, PATH, INDICATOR_GROUP);
        mUriMatcher.addURI(AUTHORITY, PATH + "/#", INDICATOR_ITEM);

        mProjectionMap = new HashMap<>();
        mProjectionMap.put(ItemColumns._ID, ItemColumns._ID);
        mProjectionMap.put(ItemColumns.TITLE, ItemColumns.TITLE);
        mProjectionMap.put(ItemColumns.CONTENT, ItemColumns.CONTENT);
        mProjectionMap.put(ItemColumns.CREATE_DATE, ItemColumns.CREATE_DATE);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch(mUriMatcher.match(uri)) {
            case INDICATOR_GROUP: {
                // 设置查询的表
                queryBuilder.setTables(ItemColumns.TABLE_NAME);
                // 设置投影映射
                queryBuilder.setProjectionMap(mProjectionMap);
            }
                break;
            case INDICATOR_ITEM: {
                queryBuilder.setTables(ItemColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(mProjectionMap);
                queryBuilder.appendWhere(ItemColumns._ID + "=" + uri.getPathSegments().get(1));
            }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = ItemColumns.DEFAULT_ORDER_BY;
        } else {
            orderBy = sortOrder;
        }
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, orderBy);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case INDICATOR_GROUP: {
                return ItemColumns.CONTENT_TYPE;
            }
            case INDICATOR_ITEM: {
                return ItemColumns.CONTENT_ITEM_TYPE;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (mUriMatcher.match(uri) != INDICATOR_GROUP) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long rowID = db.insert(ItemColumns.TABLE_NAME, null, values);
        if (rowID > 0) {
            return ContentUris.withAppendedId(ItemColumns.CONTENT_URI, rowID);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int count;
        switch (mUriMatcher.match(uri)) {
            case INDICATOR_GROUP: {
                count = db.delete(ItemColumns.TABLE_NAME, selection, selectionArgs);
            }
                break;
            case INDICATOR_ITEM: {
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(ItemColumns.TABLE_NAME, ItemColumns._ID + "=" + rowId, null);
            }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI :" + uri);
        }
        // 更新数据时，通知其他ContentObserver
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int count;
        switch(mUriMatcher.match(uri)) {
            case INDICATOR_GROUP: {
                count = db.update(ItemColumns.TABLE_NAME, values, null, null);
            }
                break;
            case INDICATOR_ITEM: {
                String rowId = uri.getPathSegments().get(1);
                count = db.update(ItemColumns.TABLE_NAME, values, ItemColumns._ID + "=" + rowId, null);
            }
                break;
            default: {
                throw new IllegalArgumentException("Unknown URI : " + uri);
            }
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

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

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "test.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(ItemColumns.SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ItemColumns.TABLE_NAME);
            onCreate(db);
        }
    }
}
