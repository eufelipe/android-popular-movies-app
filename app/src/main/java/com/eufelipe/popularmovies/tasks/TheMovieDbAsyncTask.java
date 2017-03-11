package com.eufelipe.popularmovies.tasks;

import android.os.AsyncTask;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.application.App;
import com.eufelipe.popularmovies.callbacks.AsyncTaskCallback;
import com.eufelipe.popularmovies.helpers.NetworkHelper;

import java.net.URL;


/**
 * @description : AsyncTask for Request Api The Movie Db
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class TheMovieDbAsyncTask extends AsyncTask<URL, Void, String> {

    AsyncTaskCallback callback;
    String action;

    /**
     * @param callback
     * @description : Ao instanciar esta classe, é obrigatorio passar um objeto AsyncTaskCallback
     * para realizar a função de callback do response da requisição
     */
    public TheMovieDbAsyncTask(AsyncTaskCallback callback, String action) {
        this.callback = callback;
        this.action = action;
    }

    @Override
    protected String doInBackground(URL... urls) {

        if (!NetworkHelper.isOnline(App.mGlobalContext)) {
            return App.mGlobalContext.getString(R.string.app_error_not_internet);
        }

        URL url = urls[0];
        String response = NetworkHelper.requestFromURL(url);

        return response;
    }

    @Override
    protected void onPostExecute(String s) {

        String errorInternet = App.mGlobalContext.getString(R.string.app_error_not_internet);

        if (s != null && !s.equals(errorInternet)) {
            callback.onAsyncTaskSuccess(s, action);
            return;
        }

        String error = null;
        if (s != null && s.equals(errorInternet)) {
            error = errorInternet;
        }

        callback.onAsyncTaskError(error, action);
    }

}
