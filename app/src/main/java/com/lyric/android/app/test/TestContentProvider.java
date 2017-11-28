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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * @author lyricgan
 * @time 2016/8/3 16:49
 */
public class TestContentProvider extends ContentProvider {
    private static final UriMatcher mUriMatcher;
    private static final int INDICATOR_GROUP = 1;
    private static final int INDICATOR_ITEM = 2;
    private static final String PATH = "path_test";
    private static HashMap<String, String> mProjectionMap;
    private DatabaseHelper mDatabaseHelper;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(TestDatabase.AUTHORITY, PATH, INDICATOR_GROUP);
        mUriMatcher.addURI(TestDatabase.AUTHORITY, PATH + "/#", INDICATOR_ITEM);

        mProjectionMap = new HashMap<>();
        mProjectionMap.put(TestDatabase.ItemColumns._ID, TestDatabase.ItemColumns._ID);
        mProjectionMap.put(TestDatabase.ItemColumns.TITLE, TestDatabase.ItemColumns.TITLE);
        mProjectionMap.put(TestDatabase.ItemColumns.CONTENT, TestDatabase.ItemColumns.CONTENT);
        mProjectionMap.put(TestDatabase.ItemColumns.CREATE_DATE, TestDatabase.ItemColumns.CREATE_DATE);
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
                queryBuilder.setTables(TestDatabase.ItemColumns.TABLE_NAME);
                // 设置投影映射
                queryBuilder.setProjectionMap(mProjectionMap);
            }
                break;
            case INDICATOR_ITEM: {
                queryBuilder.setTables(TestDatabase.ItemColumns.TABLE_NAME);
                queryBuilder.setProjectionMap(mProjectionMap);
                queryBuilder.appendWhere(TestDatabase.ItemColumns._ID + "=" + uri.getPathSegments().get(1));
            }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = TestDatabase.ItemColumns.DEFAULT_ORDER_BY;
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
                return TestDatabase.ItemColumns.CONTENT_TYPE;
            }
            case INDICATOR_ITEM: {
                return TestDatabase.ItemColumns.CONTENT_ITEM_TYPE;
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
        long rowID = db.insert(TestDatabase.ItemColumns.TABLE_NAME, null, values);
        if (rowID > 0) {
            return ContentUris.withAppendedId(TestDatabase.ItemColumns.CONTENT_URI, rowID);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int count;
        switch (mUriMatcher.match(uri)) {
            case INDICATOR_GROUP: {
                count = db.delete(TestDatabase.ItemColumns.TABLE_NAME, selection, selectionArgs);
            }
                break;
            case INDICATOR_ITEM: {
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(TestDatabase.ItemColumns.TABLE_NAME, TestDatabase.ItemColumns._ID + "=" + rowId, null);
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
                count = db.update(TestDatabase.ItemColumns.TABLE_NAME, values, null, null);
            }
                break;
            case INDICATOR_ITEM: {
                String rowId = uri.getPathSegments().get(1);
                count = db.update(TestDatabase.ItemColumns.TABLE_NAME, values, TestDatabase.ItemColumns._ID + "=" + rowId, null);
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

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, TestDatabase.DATABASE_NAME, null, TestDatabase.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TestDatabase.ItemColumns.SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TestDatabase.ItemColumns.TABLE_NAME);
            onCreate(db);
        }
    }
}
