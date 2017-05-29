package com.eufelipe.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {

    public static final String AUTHORITY = "com.eufelipe.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final String DATE_FORMAT = "dd/MM/yyyy";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI
                .buildUpon()
                .appendPath(PATH_MOVIES)
                .build();


        public static final Uri buildWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }

        /**
         * A verdade é que só seria necessario o campo de remoteId, porém
         * Estou montando o objeto completo para o caso de no futuro fazer
         * uma versão offline
         */


        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_REMOTE_ID = "remote_id";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANG = "original_language";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster_path";
        public static final String COLUMN_BACKDROP = "backdrop_path";
        public static final String COLUMN_POPULARY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_TAGLINE = "tagline";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_REMOTE_ID + " INTEGER DEFAULT 0, " +
                        COLUMN_ADULT + " INTEGER DEFAULT 0, " +
                        COLUMN_OVERVIEW + " TEXT," +
                        COLUMN_RELEASE_DATE + " TEXT," +
                        COLUMN_ORIGINAL_TITLE + " TEXT," +
                        COLUMN_ORIGINAL_LANG + " TEXT," +
                        COLUMN_TITLE + " TEXT," +
                        COLUMN_POSTER + " TEXT," +
                        COLUMN_BACKDROP + " TEXT," +
                        COLUMN_POPULARY + " REAL," +
                        COLUMN_VOTE_COUNT + " INTEGER," +
                        COLUMN_VOTE_AVERAGE + " REAL," +
                        COLUMN_VIDEO + " INTEGER DEFAULT 0," +
                        COLUMN_TAGLINE + " TEXT," +
                        COLUMN_RUNTIME + " INTEGER," +
                        COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0" +
                        ");";


    }
}
