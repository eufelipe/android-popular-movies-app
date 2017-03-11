package com.eufelipe.popularmovies.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.adapters.MovieAdapter;
import com.eufelipe.popularmovies.application.Constants;
import com.eufelipe.popularmovies.application.ListMovieCategory;
import com.eufelipe.popularmovies.bases.BaseActivity;
import com.eufelipe.popularmovies.helpers.NetworkHelper;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.services.TheMovieDbService;

import java.util.List;


/**
 * @description : Activity principal com o requisito de exibir uma lista de filmes populares
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class MainActivity extends BaseActivity {

    final static String TAG = "MainActivity";

    Context mContext;

    TheMovieDbService theMovieDbService = null;

    RecyclerView mRecyclerView;
    MovieAdapter mMovieAdapter;
    GridLayoutManager mGridLayoutManager;

    RelativeLayout mLoaderView;
    RelativeLayout mErrorView;

    CoordinatorLayout mMainContentView;

    TextView mErrorMessageTextView;
    Button mErrorButton;

    // página atual
    Integer page = 1;

    ListMovieCategory listMovieCategory = ListMovieCategory.POPULAR;

    /**
     * Loader More
     */
    int pastVisiblesItems;
    int visibleItemCount;
    int totalItemCount;
    int firstVisibleItemPosition;

    final int LOADER_COLUMN = 1;
    final int MOVIE_COLUMN = 2;

    boolean isFirstRequest = true;

    int subtitle = R.string.popular_movies;

    /**
     * Restore
     */

    Parcelable mMovieListParceble;

    private boolean isEnableLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        initializeToolbar(R.string.app_name);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mErrorView = (RelativeLayout) findViewById(R.id.rl_not_internet);
        mLoaderView = (RelativeLayout) findViewById(R.id.rl_loader);
        mErrorMessageTextView = (TextView) findViewById(R.id.error_message);
        mErrorButton = (Button) findViewById(R.id.error_button);
        mMainContentView = (CoordinatorLayout) findViewById(R.id.main_content);

        mErrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTheMovieDbService().movies(page, listMovieCategory);
            }
        });

        if (savedInstanceState != null) {
            int subtitleSaved = savedInstanceState.getInt(Constants.SUBTITLE_STATE_KEY, -1);
            if (subtitleSaved > -1) {
                subtitle = subtitleSaved;
            }

            int order = savedInstanceState.getInt(Constants.MOVIE_ORDER_STATE_KEY, -1);
            if (order > -1) {
                listMovieCategory = ListMovieCategory.POPULAR;
                if (order == 2) {
                    listMovieCategory = ListMovieCategory.TOP_RATED;
                }
            }
        }

        int columns = getResources().getInteger(R.integer.grid_columns);
        mGridLayoutManager = new GridLayoutManager(this, columns);
        mLoaderView.setVisibility(View.VISIBLE);

        requestMoviesOnApi(page, listMovieCategory);
        setSubtitleToolbar(listMovieCategory);

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
     * @param page
     * @param listMovieCategory
     */
    private void requestMoviesOnApi(int page, ListMovieCategory listMovieCategory) {
        getTheMovieDbService().movies(page, listMovieCategory);
    }


    /**
     * @param movieList
     * @description : configura o adaptador pela primeira vez e inicia um Listener para load more
     */
    private void configureAdapter(List<Movie> movieList) {
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mMovieAdapter != null && mMovieAdapter.getItemViewType(position) == 1) {
                    return MOVIE_COLUMN;
                }
                return LOADER_COLUMN;
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mMovieAdapter = new MovieAdapter(this, movieList);
        mRecyclerView.setAdapter(mMovieAdapter);

        if (!isEnableLoadMore) {
            mMovieAdapter.setIsShowLoader(false);
        }

        // Listener for Loader More
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                firstVisibleItemPosition = mGridLayoutManager.findFirstVisibleItemPosition();

                visibleItemCount = mGridLayoutManager.getChildCount();
                totalItemCount = mGridLayoutManager.getItemCount();
                pastVisiblesItems = mGridLayoutManager.findFirstVisibleItemPosition();

                if (isFirstRequest) {
                    return;
                }

                // Habilitar o Load More caso trabalhe com banco de dados
                // Pq quando girar o device com muitos dados perde-se os registros
                if (!isEnableLoadMore) {
                    return;
                }

                // FIXME : Quando já rolou muito scroll, o rotate do celular perde os dados, resolver isso depois

                if ((visibleItemCount + pastVisiblesItems + 1) >= totalItemCount) {
                    mMovieAdapter.setIsShowLoader(true);

                    if (!NetworkHelper.isOnline(mContext)) {
                        showToast(R.string.app_error_not_internet);
                        return;
                    }

                    requestMoviesOnApi(page + 1, listMovieCategory);

                }
            }
        };

        mRecyclerView.addOnScrollListener(onScrollListener);

    }


    @Override
    public void onRequestMovieSuccess(List<Movie> movieList, Integer page) {

        mLoaderView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        isFirstRequest = false;

        if (mMovieAdapter != null && isEnableLoadMore) {
            mMovieAdapter.setIsShowLoader(false);
        }

        // Se a pagina que for enviada for igual, significa que é o primeiro load
        if (page == this.page) {
            configureAdapter(movieList);
            return;
        }

        // Se página enviada for diferente, então os items devem ser acrescentados no final do adapter...
        // E a this.page deve ser atualizada para o próximo movies
        this.page = page;

        if (mMovieAdapter != null) {
            for (Movie movie : movieList) {
                mMovieAdapter.addItem(mMovieAdapter.getItemCount(), movie);
            }
        }

    }

    @Override
    public void onRequestMoviesFailure(String error, String action) {

        mLoaderView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);

        if (mMovieAdapter != null) {
            mMovieAdapter.setIsShowLoader(false);
        }

        String errorMessage = getString(R.string.app_error_request_server);
        if (error != null) {
            errorMessage = error;
        }

        mErrorMessageTextView.setText(errorMessage);
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
                return actionMenu(ListMovieCategory.POPULAR);

            case R.id.action_top_rated_movies:
                return actionMenu(ListMovieCategory.TOP_RATED);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean actionMenu(ListMovieCategory order) {
        if (this.listMovieCategory == order) {
            showToast(getString(R.string.menu_already_ordered));
            return true;
        }

        mLoaderView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);

        setSubtitleToolbar(order);

        listMovieCategory = order;
        page = 1;
        mMovieAdapter.getItems().clear();
        mMovieAdapter.notifyDataSetChanged();
        requestMoviesOnApi(page, listMovieCategory);
        return true;
    }


    /**
     * @param savedInstanceState
     * @description : Quando a tela é rotacionada, é necessário salvar o estado do GridLayout
     */

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        mMovieListParceble = mGridLayoutManager.onSaveInstanceState();
        savedInstanceState.putParcelable(Constants.MOVIE_LIST_STATE_KEY, mMovieListParceble);
        savedInstanceState.putInt(Constants.SUBTITLE_STATE_KEY, subtitle);
        savedInstanceState.putInt(Constants.MOVIE_ORDER_STATE_KEY, listMovieCategory == ListMovieCategory.POPULAR ? 1 : 2);

        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * @param savedInstanceState
     * @description : Quando a view é redesenhada, é necessário recuperar o estado do GridLayout
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mMovieListParceble = savedInstanceState.getParcelable(Constants.MOVIE_LIST_STATE_KEY);
            subtitle = savedInstanceState.getInt(Constants.SUBTITLE_STATE_KEY);
            int order = savedInstanceState.getInt(Constants.MOVIE_ORDER_STATE_KEY);
            listMovieCategory = ListMovieCategory.POPULAR;
            if (order == 2) {
                listMovieCategory = ListMovieCategory.TOP_RATED;
            }
        }
    }

    /**
     * @description : Quando a View é redesenhada e existe conteudo no mMovieListParceble ele
     * é aplicado novamente na GridLayout
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mMovieListParceble != null && mGridLayoutManager != null) {
            mGridLayoutManager.onRestoreInstanceState(mMovieListParceble);
        }
    }

    public void setSubtitleToolbar(ListMovieCategory listMovieCategory) {
        if (mToolbar == null) {
            return;
        }

        int color = ContextCompat.getColor(mContext, R.color.colorPurple);
        if (listMovieCategory == ListMovieCategory.TOP_RATED) {
            color = ContextCompat.getColor(mContext, R.color.colorOrange);
        }
        mToolbar.setBackgroundColor(color);
        subtitle = (listMovieCategory == ListMovieCategory.POPULAR ? R.string.popular_movies : R.string.top_rated_movies);
        mToolbar.setSubtitle(subtitle);
    }

}