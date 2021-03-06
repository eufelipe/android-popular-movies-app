package com.eufelipe.popularmovies.services;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.application.App;
import com.eufelipe.popularmovies.application.ListMovie;
import com.eufelipe.popularmovies.application.TheMovieDb;
import com.eufelipe.popularmovies.bases.BaseService;
import com.eufelipe.popularmovies.callbacks.MoviesCallback;
import com.eufelipe.popularmovies.callbacks.ReviewsCallback;
import com.eufelipe.popularmovies.callbacks.VideosCallback;
import com.eufelipe.popularmovies.data.MovieContract;
import com.eufelipe.popularmovies.data.MovieUtils;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.models.MovieReview;
import com.eufelipe.popularmovies.models.MovieVideo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @description : class responsável pelas consultas na API The MoviesCallback DB e no banco Local
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class TheMovieDbService extends BaseService {

    private final String TAG = TheMovieDbService.class.getSimpleName();

    private final String THE_MOVIE_DB_LANGUAGE = "pt-BR";

    private final TheMovieDb callback;

    private Integer page = 1;
    private Boolean isRequest = false;

    private String errorNotNetwork = null;
    ContentResolver contentResolver = null;


    /**
     * @return
     */
    public Boolean getRequest() {
        return isRequest;
    }

    /**
     * @param request
     * @return
     */
    public void setRequest(Boolean request) {
        isRequest = request;
    }

    /**
     * @param page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * @return
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @param callback
     * @param contentResolver
     * @description : Ao instanciar esta classe, é obrigatorio passar um objeto TheMovieDb
     */
    public TheMovieDbService(TheMovieDb callback, ContentResolver contentResolver) {
        this.callback = callback;
        this.contentResolver = contentResolver;
        this.errorNotNetwork = App.mGlobalContext.getString(R.string.app_error_not_internet);
    }


    /**
     * FindAll by Category
     *
     * @param page
     * @param listMovieCategory
     * @description : Método responsável por iniciar a requisição assíncrona a API do The MoviesCallback Db
     */
    public void movies(Integer page, ListMovie.Category listMovieCategory) {
        if (getRequest()) {
            return;
        }
        setRequest(true);
        setPage(page);

        String language = getLanguage();

        if (listMovieCategory == ListMovie.Category.TOP_RATED) {
            Call<MoviesCallback> call = api(language).topRated(this.page);
            enqueue(call);

        } else if (listMovieCategory == ListMovie.Category.POPULAR) {
            Call<MoviesCallback> call = api(getLanguage()).popular(this.page);
            enqueue(call);

        } else if (listMovieCategory == ListMovie.Category.FAVORITE) {
            setRequest(false);
            callback.onRequestMovieSuccess(favorites(), 1);
        }
    }


    /**
     * Método responsável pels requisições de filmes popular e top_rated
     *
     * @param call
     */
    protected void enqueue(Call<MoviesCallback> call) {
        if (call == null) {
            return;
        }

        requestLogger(call, null);

        call.enqueue(new Callback<MoviesCallback>() {
            @Override
            public void onResponse(Call<MoviesCallback> call, Response<MoviesCallback> response) {
                setRequest(false);

                if (response.body() == null || response.body().getResults() == null) {
                    callback.onRequestMoviesFailure(null);
                }

                List<Movie> results = new ArrayList<>();
                for (Movie movie : response.body().getResults()) {
                    movie.setRemoteId(String.format("%d", movie.getId()));
                    results.add(movie);
                }

                callback.onRequestMovieSuccess(results, getPage());
            }

            @Override
            public void onFailure(Call<MoviesCallback> call, Throwable t) {
                setRequest(false);
                callback.onRequestMoviesFailure(errorNotNetwork);
            }
        });
    }


    /**
     * FindOne
     *
     * @param movieId
     * @description : Método para pegar as informações de 1 movie
     */
    public void movie(Integer movieId) {
        Call<Movie> call = api().movie(movieId);
        requestLogger(call, null);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {

                if (response.body() == null) {
                    callback.onRequestMoviesFailure(null);
                    return;
                }
                Movie movie = response.body();
                movie.setRemoteId(String.format("%d", movie.getId()));
                callback.onRequestMovieSuccess(movie);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                callback.onRequestMoviesFailure(errorNotNetwork);
            }
        });
    }


    /**
     * @param movieId
     */
    public void reviews(Integer movieId) {
        Call<ReviewsCallback> call = api().reviews(movieId);
        requestLogger(call, null);
        call.enqueue(new Callback<ReviewsCallback>() {
            @Override
            public void onResponse(Call<ReviewsCallback> call, Response<ReviewsCallback> response) {
                if (response.body() == null || response.body().getResults() == null) {
                    callback.onRequestMoviesFailure(null);
                    return;
                }
                List<MovieReview> results = response.body().getResults();

                callback.onRequestMoviesReviewSuccess(results);
            }

            @Override
            public void onFailure(Call<ReviewsCallback> call, Throwable t) {
                callback.onRequestMoviesFailure(null);
            }
        });
    }

    /**
     * @param movieId
     */
    public void videos(Integer movieId) {
        Call<VideosCallback> call = api().videos(movieId);
        requestLogger(call, null);
        call.enqueue(new Callback<VideosCallback>() {
            @Override
            public void onResponse(Call<VideosCallback> call, Response<VideosCallback> response) {
                if (response.body() == null || response.body().getResults() == null) {
                    callback.onRequestMoviesFailure(null);
                    return;
                }
                List<MovieVideo> results = response.body().getResults();
                callback.onRequestMoviesVideosSuccess(results);
            }

            @Override
            public void onFailure(Call<VideosCallback> call, Throwable t) {
                callback.onRequestMoviesFailure(null);
            }
        });
    }

    /**
     * Reliza um consulta no banco de movies pelo remote remoteId
     *
     * @param remoteId
     * @return
     */
    public Cursor findMovieCursorById(String remoteId) {
        return this.contentResolver.query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.COLUMN_REMOTE_ID + " = " + remoteId,
                null,
                null
        );
    }


    /**
     * @param movie
     * @description : Método para favoritar/desfavoritar um movie
     */
    public void toggleFavoriteMovie(Movie movie) {

        ContentValues contentValues = MovieUtils.values(movie);
        Cursor cursor = findMovieCursorById(movie.getRemoteId());

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID));
            this.contentResolver.delete(MovieContract.MovieEntry.buildWithId(id), null, null);
        } else {
            this.contentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        }
    }

    /**
     * Método para retornar os filmes salvos localmente como favoritos
     *
     * @return
     */

    public List<Movie> favorites() {

        Cursor cursor = this.contentResolver.query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        return MovieUtils.parse(cursor);
    }


    /**
     * @return
     */
    private String getLanguage() {
        String languageCode = THE_MOVIE_DB_LANGUAGE;

        String language = Locale.getDefault().getLanguage();
        String country = Locale.getDefault().getCountry();

        if (language != null && country != null) {
            languageCode = String.format("%s-%s", language, country);
        }
        return languageCode;
    }


}
