package com.eufelipe.popularmovies.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.bases.BaseActivity;
import com.eufelipe.popularmovies.callbacks.TheMovieDbCallback;
import com.eufelipe.popularmovies.services.TheMovieDbService;

import org.json.JSONObject;

/**
 * @description : Activity principal com o requesito de exibir uma lista de filmes populares
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class MainActivity extends BaseActivity implements TheMovieDbCallback {

    TheMovieDbService theMovieDbService = null;

    TextView helloworld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloworld = (TextView) findViewById(R.id.helloworld);

        getTheMovieDbService().popular();

    }


    private TheMovieDbService getTheMovieDbService() {
        if (theMovieDbService == null) {
            theMovieDbService = new TheMovieDbService(this);
        }

        return theMovieDbService;
    }


    @Override
    public void onRequestMoviesSuccess(JSONObject json) {

        if (json != null) {
            String result = json.toString();
            helloworld.setText(result);
        }

    }

    @Override
    public void onRequestMoviesFailure() {
        Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestMoviesProgress(Boolean isShow) {

    }
}