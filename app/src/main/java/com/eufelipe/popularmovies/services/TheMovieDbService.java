package com.eufelipe.popularmovies.services;


import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.eufelipe.popularmovies.application.App;
import com.eufelipe.popularmovies.application.ListMovieCategory;
import com.eufelipe.popularmovies.callbacks.AsyncTaskCallback;
import com.eufelipe.popularmovies.callbacks.TheMovieDbCallback;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.models.MovieReview;
import com.eufelipe.popularmovies.models.MovieVideo;
import com.eufelipe.popularmovies.tasks.TheMovieDbAsyncTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * @description : class responsável pelas consultas na API The Movie DB
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class TheMovieDbService implements AsyncTaskCallback {

    private final String TAG = TheMovieDbService.class.getSimpleName();

    private final String THE_MOVIE_DB_API_URL = "Http://api.themoviedb.org/3/movie/";
    private final String THE_MOVIE_DB_ACTION_POPULAR = "popular";
    private final String THE_MOVIE_DB_ACTION_TOP_RATED = "top_rated";
    private final String THE_MOVIE_DB_ACTION_MOVIE = "movie";
    private final String THE_MOVIE_DB_ACTION_VIDEOS = "videos";
    private final String THE_MOVIE_DB_ACTION_REVIEWS = "reviews";
    private final String THE_MOVIE_DB_LANGUAGE = "pt-BR";

    public TheMovieDbCallback callback;


    public Integer page = 1;
    public Boolean isRequest = false;


    /**
     * @param callback
     * @description : Ao instanciar esta classe, é obrigatorio passar um objeto TheMovieDbCallback
     */
    public TheMovieDbService(TheMovieDbCallback callback) {
        this.callback = callback;
    }


    /**
     * FindAll by Category
     *
     * @param page
     * @param listMovieCategory
     * @description : Método responsável por iniciar a requisição assíncrona a API do The Movie Db
     */
    public void movies(Integer page, ListMovieCategory listMovieCategory) {
        if (isRequest) {
            return;
        }

        isRequest = true;
        this.page = page;

        String action = THE_MOVIE_DB_ACTION_POPULAR;

        if (listMovieCategory == ListMovieCategory.TOP_RATED) {
            action = THE_MOVIE_DB_ACTION_TOP_RATED;
        }
        startTask(action, getUrl(action, this.page), false);
    }


    /**
     * FindOne
     *
     * @param movieId
     * @description : Método para pegar as informações de 1 movie
     */
    public void movie(Integer movieId) {
        String action = movieId.toString();
        startTask(THE_MOVIE_DB_ACTION_MOVIE, getUrl(action), true);
    }


    /**
     * @param movieId
     */
    public void reviews(Integer movieId) {
        String action = String.format("%s/%s", movieId.toString(), THE_MOVIE_DB_ACTION_REVIEWS);
        startTask(action, getUrl(action), true);
    }

    /**
     * @param movieId
     */
    public void videos(Integer movieId) {
        String action = String.format("%s/%s", movieId.toString(), THE_MOVIE_DB_ACTION_VIDEOS);
        startTask(action, getUrl(action), true);
    }


    private void startTask(String action, URL url, boolean useParallelExecution) {

        Log.d(TAG, "----- Request : " + action + " -  " + url.toString());

        TheMovieDbAsyncTask task = new TheMovieDbAsyncTask(this, action);
        if (useParallelExecution) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } else {
            task.execute(url);
        }

    }

    /**
     * @param jsonString
     * @description : Retorno de sucesso da class TheMovieDbAsyncTask
     */
    @Override
    public void onAsyncTaskSuccess(String jsonString, String action) {
        isRequest = false;

        if (action.contains("/")) {
            String[] split = action.split("/");
            action = split[1];
        }

        switch (action) {
            case THE_MOVIE_DB_ACTION_POPULAR:
            case THE_MOVIE_DB_ACTION_TOP_RATED:

                List<Movie> movies = Movie.convertStringJsonForListOfMovie(jsonString);
                if (movies != null) {
                    callback.onRequestMovieSuccess(movies, this.page);
                } else {
                    callback.onRequestMoviesFailure(null, action);
                }

                break;
            case THE_MOVIE_DB_ACTION_MOVIE:
                Movie movie = Movie.convertStringJsonForMovie(jsonString);
                if (movie != null) {
                    callback.onRequestMovieSuccess(movie);
                } else {
                    callback.onRequestMoviesFailure(null, action);
                }
                break;
            case THE_MOVIE_DB_ACTION_REVIEWS:
                List<MovieReview> movieReviews = MovieReview.convertStringJsonForMovieReviews(jsonString);
                if (movieReviews != null) {
                    callback.onRequestMoviesReviewSuccess(movieReviews);
                } else {
                    callback.onRequestMoviesFailure(null, action);
                }
                break;


            case THE_MOVIE_DB_ACTION_VIDEOS:
                List<MovieVideo> movieVideos = MovieVideo.convertStringJsonForMovieVideo(jsonString);
                if (movieVideos != null) {
                    callback.onRequestMoviesVideosSuccess(movieVideos);
                } else {
                    callback.onRequestMoviesFailure(null, action);
                }
                break;
        }
    }


    /**
     * @description : Retorno de Falha ou Erro da class TheMovieDbAsyncTask
     */
    @Override
    public void onAsyncTaskError(String error, String action) {
        isRequest = false;
        callback.onRequestMoviesFailure(error, action);
    }

    /**
     * @param action
     * @return
     */

    private URL getUrl(String action) {
        return getUrl(action, null);
    }


    /**
     * @param action
     * @param page
     * @return
     * @description : Método helper para montar a url de consulta da API The Movie Db
     */

    private URL getUrl(String action, Integer page) {

        Boolean appendlanguage = false;

        if (action.equals(THE_MOVIE_DB_ACTION_POPULAR) ||
                action.equals(THE_MOVIE_DB_ACTION_TOP_RATED)) {
            appendlanguage = true;
        }

        Uri.Builder build = Uri.parse(THE_MOVIE_DB_API_URL).buildUpon();
        build.appendQueryParameter("api_key", App.getTheMovieDbKey());

        if (appendlanguage) {
            String languageCode = THE_MOVIE_DB_LANGUAGE;

            String language = Locale.getDefault().getLanguage();
            String country = Locale.getDefault().getCountry();

            if (language != null && country != null) {
                languageCode = String.format("%s-%s", language, country);
            }

            build.appendQueryParameter("language", languageCode);
        }

        if (page != null) {
            build.appendQueryParameter("page", String.valueOf(page));
        }

        if (action != null) {
            build.appendEncodedPath(action);
        }

        URL url = null;
        try {
            return new URL(build.build().toString());

        } catch (MalformedURLException e) {
            Log.d(TAG, "Erro ao montar URL");
        }

        Log.d(TAG, url.toString());

        return url;
    }
}
