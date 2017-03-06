package com.eufelipe.popularmovies.tasks;

import android.os.AsyncTask;

import com.eufelipe.popularmovies.callbacks.AsyncTaskCallback;
import com.eufelipe.popularmovies.helpers.NetworkHelper;

import java.net.URL;


/**
 * @description : AsyncTask for Request Api The Movie Db
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class TheMovieDbAsyncTask extends AsyncTask<URL, Void, String> {

    AsyncTaskCallback callback;

    /**
     * @description : Ao instanciar esta classe, é obrigatorio passar um objeto AsyncTaskCallback
     * para realizar a função de callback do response da requisição
     * @param callback
     */
    public TheMovieDbAsyncTask(AsyncTaskCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(URL... urls) {

        URL url = urls[0];
        String response = NetworkHelper.requestFromURL(url);

        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            callback.onAsyncTaskSuccess(s);
            return;
        }

        callback.onAsyncTaskError();
    }

}
