package com.eufelipe.popularmovies.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.eufelipe.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;

public class MovieContentProvider extends ContentProvider {

    private MovieDbHelper mMovieDbHelper;

    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;

    private static final UriMatcher URI_MATCHER = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        initializeDb();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = getDb();

        int match = URI_MATCHER.match(uri);

        switch (match) {
            case MOVIES:
                Cursor cursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                notifyChange(uri);
                return cursor;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = getDb();

        int match = URI_MATCHER.match(uri);

        switch (match) {
            case MOVIES:
                long id = db.insert(TABLE_NAME, null, contentValues);

                if (id > 0) {
                    notifyChange(uri);
                    return ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                }

                throw new android.database.SQLException("Failed to insert row into " + uri);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] args) {

        final SQLiteDatabase db = getDb();

        int match = URI_MATCHER.match(uri);

        switch (match) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                int delete = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                if (delete != 0) {
                    notifyChange(uri);
                    return delete;
                }

                throw new android.database.SQLException("Failed to delete row into " + uri);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = getDb();

        int match = URI_MATCHER.match(uri);

        switch (match) {
            case MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                int update = db.update(TABLE_NAME, contentValues, "_id=?", new String[]{id});
                if (update != 0) {
                    notifyChange(uri);
                    return update;
                }
                throw new android.database.SQLException("Failed to delete row into " + uri);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private void initializeDb() {
        Context context = getContext();
        mMovieDbHelper = new MovieDbHelper(context);
    }

    private SQLiteDatabase getDb() {
        if (mMovieDbHelper == null) {
            initializeDb();
        }
        return mMovieDbHelper.getWritableDatabase();
    }

    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

}
