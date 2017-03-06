package com.eufelipe.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.adapters.MovieAdapter;
import com.eufelipe.popularmovies.bases.BaseActivity;
import com.eufelipe.popularmovies.callbacks.TheMovieDbCallback;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.services.TheMovieDbService;

import java.util.List;

/**
 * @description : Activity principal com o requesito de exibir uma lista de filmes populares
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class MainActivity extends BaseActivity implements TheMovieDbCallback {

    private static final int GRID_COLUMNS = 2;
    TheMovieDbService theMovieDbService = null;

    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;
    GridLayoutManager mGridLayoutManager;

    Integer page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mGridLayoutManager = new GridLayoutManager(this, GRID_COLUMNS);

        getTheMovieDbService().popular(this.page);

    }


    private TheMovieDbService getTheMovieDbService() {
        if (theMovieDbService == null) {
            theMovieDbService = new TheMovieDbService(this);
        }

        return theMovieDbService;
    }


    @Override
    public void onRequestMoviesSuccess(List<Movie> movieList) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mMovieAdapter = new MovieAdapter(this, movieList);
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    public void onRequestMoviesFailure() {
        Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestMoviesProgress(Boolean isShow) {

    }
}