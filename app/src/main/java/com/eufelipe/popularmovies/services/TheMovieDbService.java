package com.eufelipe.popularmovies.services;


import android.net.Uri;
import android.util.Log;

import com.eufelipe.popularmovies.application.App;
import com.eufelipe.popularmovies.callbacks.AsyncTaskCallback;
import com.eufelipe.popularmovies.callbacks.TheMovieDbCallback;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.tasks.TheMovieDbAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @description : class responsável pelas consultas na API The Movie DB
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class TheMovieDbService implements AsyncTaskCallback {

    private final String TAG = TheMovieDbService.class.getSimpleName();

    private final String THE_MOVIE_DB_API_URL = "Http://api.themoviedb.org/3/movie/";
    private final String THE_MOVIE_DB_ACTION_POPULAR = "popular";

    public TheMovieDbCallback callback;


    public Integer page = 1;
    public Integer totalResults = 0;
    public Integer totalPages = 0;

    public Boolean isRequest = false;


    /**
     * @param callback
     * @description : Ao instanciar esta classe, é obrigatorio passar um objeto TheMovieDbCallback
     */
    public TheMovieDbService(TheMovieDbCallback callback) {
        this.callback = callback;
    }

    /**
     * @description : Método responsável por iniciar a requisição assíncrona a API do The Movie Db
     * @change : Inclusão do parametro page para paginação
     */
    public void popular(Integer page) {
        if (isRequest) {
            return;
        }

        isRequest = true;
        this.page = page;

        URL url = getUrl(THE_MOVIE_DB_ACTION_POPULAR, this.page);
        Log.d(TAG, url.toString());

        new TheMovieDbAsyncTask(this).execute(url);
    }

    /**
     * @param result
     * @description : Retorno de sucesso da class TheMovieDbAsyncTask
     */
    @Override
    public void onAsyncTaskSuccess(String result) {
        isRequest = false;
        JSONObject json = null;
        List<Movie> movies = new ArrayList<>();

        try {
            json = new JSONObject(result);
            JSONArray results = json.getJSONArray("results");

            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonObject = results.getJSONObject(i);
                    Movie movie = Movie.parse(jsonObject);
                    if (movie != null) {
                        movies.add(movie);
                    }
                }
            }

            callback.onRequestMoviesSuccess(movies, this.page);

        } catch (JSONException e) {
            e.printStackTrace();
            callback.onRequestMoviesFailure();
        }

    }

    /**
     * @description : Retorno de Falha ou Erro da class TheMovieDbAsyncTask
     */
    @Override
    public void onAsyncTaskError() {
        isRequest = false;
        callback.onRequestMoviesFailure();
    }

    /**
     * @param action
     * @param page
     * @return
     * @description : Método helper para montar a url de consulta da API The Movie Db
     * @change : Inclusão do parametro page para paginação
     */

    private URL getUrl(String action, Integer page) {

        Uri.Builder build = Uri.parse(THE_MOVIE_DB_API_URL).buildUpon();
        build.appendQueryParameter("api_key", App.getTheMovieDbKey());
        build.appendQueryParameter("page", String.valueOf(page));

        if (action != null) {
            build.appendEncodedPath(action);
        }

        URL url = null;
        try {
            return new URL(build.build().toString());

        } catch (MalformedURLException e) {
            Log.d(TAG, "Erro ao montar URL");
        }

        return url;
    }
}
