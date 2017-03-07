package com.eufelipe.popularmovies.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.adapters.MovieAdapter;
import com.eufelipe.popularmovies.application.MovieOrder;
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

    final int GRID_COLUMNS_PORTRAT = 2;
    final int GRID_COLUMNS_LAND = 3;

    TheMovieDbService theMovieDbService = null;

    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;
    GridLayoutManager mGridLayoutManager;

    // página atual
    Integer page = 1;

    MovieOrder movieOrder = MovieOrder.POPULAR;

    /**
     * Loader More
     */
    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;


    /**
     * Restore
     */

    Parcelable mMovieListParceble;
    String MOVIE_LIST_STATE_KEY = "MOVIE_LIST_STATE_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mGridLayoutManager = new GridLayoutManager(this, GRID_COLUMNS_PORTRAT);
        getTheMovieDbService().request(this.page, movieOrder);

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


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * @param movieList
     * @description : configura o adaptador pela primeira vez e inicia um Listener para load more
     */
    private void configureAdapter(List<Movie> movieList) {
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mMovieAdapter != null && mMovieAdapter.getItemViewType(position) == mMovieAdapter.VIEW_TYPE_LOADER) {
                    return mMovieAdapter.VIEW_TYPE_ITEM;
                }
                return mMovieAdapter.VIEW_TYPE_LOADER;
            }
        });

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
                    mMovieAdapter.setIsShowLoader(true);
                    getTheMovieDbService().request(page + 1, movieOrder);
                }
            }
        };

        mRecyclerView.addOnScrollListener(onScrollListener);


    }

    @Override
    public void onRequestMoviesSuccess(List<Movie> movieList, Integer page) {
        if (mMovieAdapter != null) {
            mMovieAdapter.setIsShowLoader(false);
        }

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
        mRecyclerView.getRecycledViewPool().clear();

    }

    @Override
    public void onRequestMoviesFailure() {
        if (mMovieAdapter != null) {
            mMovieAdapter.setIsShowLoader(false);
        }
        Toast.makeText(this, "Ocorreu um erro", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestMoviesProgress(Boolean isShow) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.action_popular_movies:
                return actionMenu(MovieOrder.POPULAR);

            case R.id.action_top_rated_movies:
                return actionMenu(MovieOrder.TOP_RATED);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean actionMenu(MovieOrder item) {
        if (movieOrder == item) {
            showToast(getString(R.string.menu_already_ordered));
            return true;
        }

        movieOrder = item;
        page = 1;
        mMovieAdapter.getItems().clear();
        mMovieAdapter.notifyDataSetChanged();
        getTheMovieDbService().request(page, movieOrder);
        return true;
    }


    /**
     * @param outState
     * @description : Quando a tela é rotacionada, é necessário salvar o estado do GridLayout
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMovieListParceble = mGridLayoutManager.onSaveInstanceState();
        outState.putParcelable(MOVIE_LIST_STATE_KEY, mMovieListParceble);
    }

    /**
     * @param savedInstanceState
     * @description : Quando a view é redesenhada, é necessário recuperar o estado do GridLayout
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mMovieListParceble = savedInstanceState.getParcelable(MOVIE_LIST_STATE_KEY);
        }
    }

    /**
     * @description : Quando a View é redesenhada e existe conteudo no mMovieListParceble ele
     * é aplicado novamente na GridLayout
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mMovieListParceble != null) {
            mGridLayoutManager.onRestoreInstanceState(mMovieListParceble);
        }
    }
}