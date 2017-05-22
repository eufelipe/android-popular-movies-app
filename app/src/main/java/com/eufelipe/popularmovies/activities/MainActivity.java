package com.eufelipe.popularmovies.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.adapters.MovieAdapter;
import com.eufelipe.popularmovies.application.Constants;
import com.eufelipe.popularmovies.application.ListMovie;
import com.eufelipe.popularmovies.bases.BaseActivity;
import com.eufelipe.popularmovies.helpers.NetworkHelper;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.services.TheMovieDbService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @description : Activity principal com o requisito de exibir uma lista de filmes populares
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class MainActivity extends BaseActivity {

    final String TAG = getClass().getSimpleName();

    private Context mContext;
    private TheMovieDbService theMovieDbService = null;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;
    private GridLayoutManager mGridLayoutManager;

    @BindView(R.id.rl_loader)
    RelativeLayout mLoaderView;

    @BindView(R.id.rl_not_internet)
    RelativeLayout mErrorView;

    @BindView(R.id.main_content)
    CoordinatorLayout mMainContentView;

    @BindView(R.id.error_message)
    TextView mErrorMessageTextView;

    @BindView(R.id.error_button)
    Button mErrorButton;

    // página atual
    private Integer page = 1;

    private ListMovie.Category listMovieCategory = ListMovie.Category.POPULAR;

    /**
     * Loader More
     */
    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private int firstVisibleItemPosition;

    private final int LOADER_COLUMN = 1;
    private final int MOVIE_COLUMN = 2;

    private boolean isFirstRequest = true;


    /**
     * Restore
     */

    private Parcelable mMovieListParceble;
    private final boolean isEnableLoadMore = true;


    /**
     * Firula
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = getApplicationContext();

        initializeToolbar(R.string.app_name);

        if (savedInstanceState != null) {
            int titleSaved = savedInstanceState.getInt(Constants.TITLE_STATE_KEY, -1);
            if (titleSaved > -1) {
                title = titleSaved;
            }

            int order = savedInstanceState.getInt(Constants.MOVIE_ORDER_STATE_KEY, -1);
            listMovieCategory = ListMovie.getListMovieCategory(order);
        }

        mGridLayoutManager = new GridLayoutManager(this, numberOfColums());
        show(mLoaderView, true);

        requestMoviesOnApi(page, listMovieCategory);
        setTitleToolbar(listMovieCategory);

    }

    /**
     * Sugestão 3
     *
     * @return
     */
    private int numberOfColums() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumn = width / widthDivider;
        if (nColumn < 2) return 2;

        return nColumn;
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
    private void requestMoviesOnApi(int page, ListMovie.Category listMovieCategory) {
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

        if (listMovieCategory == ListMovie.Category.FAVORITE) {
            page = 1;
            mMovieAdapter.setIsShowLoader(false);
            return;
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

        show(mLoaderView, false);
        show(mErrorView, false);
        show(mRecyclerView, true);

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
            // Para de piscar o fadeIn
            mMovieAdapter.setIsShowAnimation(false);
        }
    }

    @Override
    public void onRequestMoviesFailure(String error) {

        show(mLoaderView, false);
        show(mRecyclerView, false);
        show(mErrorView, true);

        if (mMovieAdapter != null) {
            mMovieAdapter.setIsShowLoader(false);
        }

        String errorMessage = getString(R.string.app_error_request_server);
        if (error != null) {
            errorMessage = error;
        }

        setTextView(mErrorMessageTextView, errorMessage);
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
                return actionMenu(ListMovie.Category.POPULAR);

            case R.id.action_top_rated_movies:
                return actionMenu(ListMovie.Category.TOP_RATED);

            case R.id.action_favorite_movies:
                return actionMenu(ListMovie.Category.FAVORITE);
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean actionMenu(ListMovie.Category order) {
        if (this.listMovieCategory == order) {
            showToast(getString(R.string.menu_already_ordered));
            return true;
        }

        show(mLoaderView, true);
        show(mErrorView, false);
        show(mRecyclerView, false);

        setTitleToolbar(order);

        listMovieCategory = order;
        page = 1;
        if (mMovieAdapter != null) {
            mMovieAdapter.clear();
            mMovieAdapter.notifyDataSetChanged();
        }
        requestMoviesOnApi(page, listMovieCategory);
        return true;
    }

    /**
     * Try Again Request
     */

    @OnClick(R.id.error_button)
    public void tryAgain() {
        requestMoviesOnApi(page, listMovieCategory);
    }


    /**
     * @param savedInstanceState
     * @description : Quando a tela é rotacionada, é necessário salvar o estado do GridLayout
     */

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        mMovieListParceble = mGridLayoutManager.onSaveInstanceState();
        savedInstanceState.putParcelable(Constants.MOVIE_LIST_STATE_KEY, mMovieListParceble);
        savedInstanceState.putInt(Constants.TITLE_STATE_KEY, title);
        savedInstanceState.putInt(Constants.MOVIE_ORDER_STATE_KEY, ListMovie.getListMovieCategoryId(listMovieCategory));
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
            title = savedInstanceState.getInt(Constants.TITLE_STATE_KEY);
            int order = savedInstanceState.getInt(Constants.MOVIE_ORDER_STATE_KEY);
            this.listMovieCategory = ListMovie.getListMovieCategory(order);
        }
    }

    /**
     * @description : Quando a View é redesenhada e existe conteudo no mMovieListParceble ele
     * é aplicado novamente na GridLayout
     */
    @Override
    protected void onResume() {
        super.onResume();
//        getTheMovieDbService().deleteAll();

        if (mMovieListParceble != null && mGridLayoutManager != null) {
            mGridLayoutManager.onRestoreInstanceState(mMovieListParceble);
        }
    }


}