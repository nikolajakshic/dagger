package com.nikola.jakshic.truesight.data.local;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nikola.jakshic.truesight.data.local.DotaContract.DotaSubscriber;

public class DotaProvider extends ContentProvider {

    private DotaDbHelper dbHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int CODE_FAVORITE = 100;
    private static final int CODE_FAVORITE_ID = 101;

    static {
        sUriMatcher.addURI(DotaContract.CONTENT_AUTHORITY, DotaContract.PATH_ACC, CODE_FAVORITE);
        sUriMatcher.addURI(DotaContract.CONTENT_AUTHORITY, DotaContract.PATH_ACC + "/#", CODE_FAVORITE_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DotaDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE:
                cursor = dbHelper.getReadableDatabase().query(
                        DotaSubscriber.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case CODE_FAVORITE_ID:
                selection = DotaSubscriber.COLUMN_ACC_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                cursor = dbHelper.getReadableDatabase().query(
                        DotaSubscriber.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Can not perform query on this uri: " + uri);
        }

        if (cursor != null) cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri newUri;
        long rowsInserted;

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE:
                rowsInserted = dbHelper.getWritableDatabase().insert(
                        DotaSubscriber.TABLE_NAME,
                        null,
                        values);
                if (rowsInserted != -1)
                    newUri = ContentUris.withAppendedId(uri, rowsInserted);
                else
                    throw new SQLException("Failed to insert row into: " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Can not insert row on this uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return newUri;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String
            selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
            selectionArgs) {
        int deletedRows;

        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_ID:
                selection = DotaSubscriber.COLUMN_ACC_ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                deletedRows = dbHelper.getWritableDatabase().delete(
                        DotaSubscriber.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Can not performe delete on this uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return deletedRows;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
