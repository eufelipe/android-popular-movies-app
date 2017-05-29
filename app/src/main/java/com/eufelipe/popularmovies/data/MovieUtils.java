package com.eufelipe.popularmovies.data;


import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.eufelipe.popularmovies.models.Movie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieUtils {

    public static List<Movie> parse(Cursor cursor) {

        List<Movie> movieList = new ArrayList<>();

        try {

            while (cursor.moveToNext()) {

                // Values
                final int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID));

                String remoteId = cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_REMOTE_ID));

                Integer isAdult = cursor.getInt(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ADULT));

                String overview = cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));

                String releaseDate = cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));

                String originalTitle = cursor.getString
                        (cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE));

                String originalLanguage = cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANG));

                String title = cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));

                String posterPath = cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));

                String backdropPath = cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP));

                Double popularity = cursor.getDouble(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARY));

                Integer voteCount = cursor.getInt(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT));

                Double voteAverage = cursor.getDouble(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));

                Integer video = cursor.getInt(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VIDEO));

                String tagline = cursor.getString(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TAGLINE));

                Integer runtime = cursor.getInt(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RUNTIME));

                Integer isFavorite = cursor.getInt(
                        cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IS_FAVORITE));

                Movie movie = new Movie();


                movie
                        .setId(id)
                        .setRemoteId(remoteId)
                        .setIsAdult(isAdult > 0)
                        .setOverview(overview)
                        .setTagline(tagline)
                        .setBackdrop(backdropPath)
                        .setOriginalTitle(originalTitle)
                        .setOriginalLanguage(originalLanguage)
                        .setTitle(title)
                        .setPoster(posterPath)
                        .setPopularity(popularity)
                        .setVoteCount(voteCount)
                        .setVoteAverage(voteAverage)
                        .setIsVideo(video > 0)
                        .setRuntime(runtime)
                        .setIsFavorite(isFavorite > 0);


                SimpleDateFormat sdf = new SimpleDateFormat(MovieContract.MovieEntry.DATE_FORMAT);
                sdf.setLenient(false);

                try {
                    Date date = sdf.parse(releaseDate);
                    movie.setReleaseDate(date);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                movieList.add(movie);

            }
        } finally {
            cursor.close();
        }

        return movieList;
    }


    public static ContentValues values(Movie movie) {

        ContentValues contentValues = new ContentValues();

        String remoteId = movie.getRemoteId();

        if (movie.getId() > 0) {
            Log.d("TAG", "values: " + movie.getId());
        }

        Integer isFavorite = movie.getIsFavorite() ? 1 : 0;
        Integer isAdult = movie.getAdult() ? 1 : 0;

        String originalTitle = movie.getOriginalTitle();
        String originalLanguage = movie.getOriginalLanguage();
        String overview = movie.getOverview();
        String title = movie.getTitle();
        String poster = movie.getPoster();
        String backdrop = movie.getBackdrop();
        Double popularity = movie.getPopularity();

        Integer voteCount = movie.getVoteCount();
        double voteAverage = movie.getVoteAverage();

        int runtime = movie.getRuntime();
        String tagline = movie.getTagline();


        Integer isVideo = movie.getIsVideo() ? 1 : 0;

        contentValues.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, isFavorite);
        contentValues.put(MovieContract.MovieEntry.COLUMN_ADULT, isAdult);
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANG, originalLanguage);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, poster);
        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP, backdrop);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POPULARY, popularity);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, voteCount);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
        contentValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, isVideo);
        contentValues.put(MovieContract.MovieEntry.COLUMN_TAGLINE, tagline);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RUNTIME, runtime);
        contentValues.put(MovieContract.MovieEntry.COLUMN_REMOTE_ID, remoteId);

        if (movie.getReleaseDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(MovieContract.MovieEntry.DATE_FORMAT);
            String releaseDate = sdf.format(movie.getReleaseDate());
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        }

        return contentValues;
    }

}
