package com.eufelipe.popularmovies.services;


import android.net.Uri;
import android.util.Log;

import com.eufelipe.popularmovies.application.App;
import com.eufelipe.popularmovies.callbacks.AsyncTaskCallback;
import com.eufelipe.popularmovies.callbacks.TheMovieDbCallback;
import com.eufelipe.popularmovies.tasks.TheMovieDbAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @description : class responsável pelas consultas na API The Movie DB
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class TheMovieDbService implements AsyncTaskCallback {

    private final String TAG = TheMovieDbService.class.getSimpleName();

    private final String THE_MOVIE_DB_API_URL = "Http://api.themoviedb.org/3/movie/";
    private final String THE_MOVIE_DB_ACTION_POPULAR = "popular";

    public TheMovieDbCallback callback;

    /**
     * @param callback
     * @description : Ao instanciar esta classe, é obrigatorio passar um objeto TheMovieDbCallback
     */
    public TheMovieDbService(TheMovieDbCallback callback) {
        this.callback = callback;
    }

    /**
     * @description : Método responsável por iniciar a requisição assíncrona a API do The Movie Db
     */
    public void popular() {
        URL url = getUrl(THE_MOVIE_DB_ACTION_POPULAR);
        Log.d(TAG, url.toString());

        new TheMovieDbAsyncTask(this).execute(url);
    }

    /**
     * @param result
     * @description : Retorno de sucesso da class TheMovieDbAsyncTask
     */
    @Override
    public void onAsyncTaskSuccess(String result) {

        JSONObject json = null;
        try {
            json = new JSONObject(result);
            callback.onRequestMoviesSuccess(json);

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
        callback.onRequestMoviesFailure();
    }

    /**
     * @param action
     * @return
     * @description : Método helper para montar a url de consulta da API The Movie Db
     */

    private URL getUrl(String action) {

        Uri.Builder build = Uri.parse(THE_MOVIE_DB_API_URL).buildUpon();
        build.appendQueryParameter("api_key", App.getTheMovieDbKey());

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
