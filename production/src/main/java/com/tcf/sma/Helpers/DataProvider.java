package com.tcf.sma.Helpers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.tcf.sma.Models.AppConstants;

import java.util.HashMap;

/**
 * Created by Zubair Soomro on 12/22/2016.
 */

public class DataProvider extends ContentProvider {
    static final String PROVIDER_NAME = AppConstants.CONTENT_PROVIDER_URI;
    //    static final String URL = "content://" + PROVIDER_NAME + "/" + DatabaseHelper.TABLE_IMAGES;
    //    public static final Uri CONTENT_URI = Uri.parse(URL);
    static final int images = 1;
    private static HashMap<String, String> values;
    private static UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        mUriMatcher.addURI(PROVIDER_NAME, DatabaseHelper.TABLE_IMAGES, images);
    }

    SQLiteDatabase db;
    DatabaseHelper dbhelper;

    @Override
    public boolean onCreate() {
        dbhelper = DatabaseHelper.getInstance(getContext());
        if (dbhelper != null) {
            return true;
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortorder) {
//        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
//        sqLiteQueryBuilder.setTables(DatabaseHelper.TABLE_IMAGES);
//        switch (mUriMatcher.match(uri)) {
//            case images:
//                sqLiteQueryBuilder.setProjectionMap(values);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown Uri " + uri);
//        }
//        Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortorder);
//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Cursor c = null;
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;

    }

    @Override
    public int delete(Uri uri, String selection, String[] whereArgs) {
//        int rowsDeleted = 0;
//        switch (mUriMatcher.match(uri)) {
//            case images:
//                rowsDeleted = db.delete(DatabaseHelper.TABLE_IMAGES, selection, whereArgs);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown Uri " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] whereArgs) {
//        int rowsUpdated = 0;
//        switch (mUriMatcher.match(uri)) {
//            case images:
//                rowsUpdated = db.update(DatabaseHelper.TABLE_IMAGES, contentValues, selection, whereArgs);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown Uri " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }
}
