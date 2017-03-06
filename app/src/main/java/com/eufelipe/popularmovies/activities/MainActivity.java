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
 * @description : Activity principal com o requisito de exibir uma lista de filmes populares
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class MainActivity extends BaseActivity implements TheMovieDbCallback {

    private static final int GRID_COLUMNS = 2;
    TheMovieDbService theMovieDbService = null;

    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;
    GridLayoutManager mGridLayoutManager;

    // página atual
    Integer page = 1;

    /**
     * Loader More
     */
    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mGridLayoutManager = new GridLayoutManager(this, GRID_COLUMNS);

        getTheMovieDbService().popular(this.page);

    }

    /**
     * Lazy load Instance
     *
     * @return
     */
    private TheMovieDbService getTheMovieDbService() {
        if (theMovieDbService == null) {
            theMovieDbService = new TheMovieDbService(this);
        }

        return theMovieDbService;
    }

    /**
     * @param movieList
     * @description : configura o adaptador pela primeira vez e inicia um Listener para load more
     */
    private void configureAdapter(List<Movie> movieList) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mMovieAdapter = new MovieAdapter(this, movieList);
        mRecyclerView.setAdapter(mMovieAdapter);

        // Listener for Loader More
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mGridLayoutManager.getChildCount();
                totalItemCount = mGridLayoutManager.getItemCount();
                pastVisiblesItems = mGridLayoutManager.findFirstVisibleItemPosition();
                
                if ((visibleItemCount + pastVisiblesItems + 1) >= totalItemCount) {
                    getTheMovieDbService().popular(page + 1);
                }
            }
        };

        mRecyclerView.addOnScrollListener(onScrollListener);

    }

    @Override
    public void onRequestMoviesSuccess(List<Movie> movieList, Integer page) {

        // Se a pagina que for enviada for igual, significa que é o primeiro load
        if (page == this.page) {
            configureAdapter(movieList);
            return;
        }

        // Se página enviada for diferente, então os items devem ser acrescentados no final do adapter...
        // E a this.page deve ser atualizada para o próximo request
        this.page = page;

        for (Movie movie : movieList) {
            mMovieAdapter.addItem(mMovieAdapter.getItemCount(), movie);
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