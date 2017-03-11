package com.eufelipe.popularmovies.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.adapters.MovieReviewAdapter;
import com.eufelipe.popularmovies.adapters.MovieVideoAdapter;
import com.eufelipe.popularmovies.application.Constants;
import com.eufelipe.popularmovies.bases.BaseActivity;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.models.MovieReview;
import com.eufelipe.popularmovies.models.MovieVideo;
import com.eufelipe.popularmovies.services.TheMovieDbService;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;


public class MovieActivity extends BaseActivity {

    Movie mMovie;

    TheMovieDbService theMovieDbService = null;

    CollapsingToolbarLayout mCollapsingToolbar;
    ImageView mBackdropImageView;
    ImageView mPosterImageView;

    FloatingActionButton mFbFavorite;
    boolean isFavorite = false;


    /**
     * Infos
     */
    TextView mOverviewTextView;
    TextView mReleaseDateTextView;
    TextView mVoteAverageTextView;
    TextView mRuntimeTextView;
    LinearLayout mInfoView;
    ProgressBar mLoaderInfo;


    /**
     * Reviews
     */
    LinearLayout mReviewsView;
    RecyclerView mReviewsRecyclerView;
    MovieReviewAdapter mReviewAdapter;


    /**
     * Videos
     */
    LinearLayout mTrailersView;
    RecyclerView mTrailersRecyclerView;
    MovieVideoAdapter mVideoAdapter;

    Context mContext;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        mContext = getApplicationContext();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getParcelable(Constants.MOVIE_DATA_KEY) != null) {
            mMovie = bundle.getParcelable(Constants.MOVIE_DATA_KEY);
        } else {
            showToast(R.string.app_error_movie_invalid);
            finish();
        }

        show(mLoaderInfo, true);
        getTheMovieDbService().movie(mMovie.getId());
        getTheMovieDbService().videos(mMovie.getId());
        getTheMovieDbService().reviews(mMovie.getId());

        initializeToolbar(mMovie.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Butter Knife Faria uma maravilha nesse método (╯°□°）╯︵ ┻━┻


        mOverviewTextView = (TextView) findViewById(R.id.tv_overview);

        mFbFavorite = (FloatingActionButton) findViewById(R.id.fb_favorite);
        mFbFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFavorite = !isFavorite;
                setIconFloatingActionButton(isFavorite);
            }
        });


        mTrailersRecyclerView = (RecyclerView) findViewById(R.id.rv_trailers);
        mTrailersView = (LinearLayout) findViewById(R.id.ll_trailers);

        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.rv_reviews);
        mReviewsView = (LinearLayout) findViewById(R.id.ll_reviews);

        mInfoView = (LinearLayout) findViewById(R.id.ll_info);
        mLoaderInfo = (ProgressBar) findViewById(R.id.pb_info);

        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        mBackdropImageView = (ImageView) findViewById(R.id.iv_backdrop);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);

        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        mVoteAverageTextView = (TextView) findViewById(R.id.tv_vote_average);
        mRuntimeTextView = (TextView) findViewById(R.id.tv_runtime);

        bindViews();


    }

    private void bindViews() {

        Picasso.with(mContext)
                .load(mMovie.getBackdropImage())
                .into(backdropTarget);

        Picasso.with(mContext)
                .load(mMovie.getPosterImage())
                .into(mPosterImageView);

        mCollapsingToolbar.setTitle(mMovie.getTitle());

        setTextView(mOverviewTextView, mMovie.getOverview());

    }


    // Request de um Filme


    private void setIconFloatingActionButton(boolean isFavorite) {
        int on = R.drawable.ic_favorite_on;
        int off = R.drawable.ic_favorite_off;
        Drawable drawable = ContextCompat.getDrawable(mContext, isFavorite ? on : off);
        mFbFavorite.setImageDrawable(drawable);
    }

    private Target backdropTarget = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            mBackdropImageView.setImageBitmap(bitmap);

            // Firulas, firulas =P

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Palette palette = Palette.from(bitmap).generate();
                            int defaultColor = 0x000000;
                            final Integer vibrant = palette.getDarkVibrantColor(defaultColor);

                            if (vibrant != null) {
                                mCollapsingToolbar.setBackgroundColor(vibrant);
                                mCollapsingToolbar.setStatusBarScrimColor(palette.getDarkMutedColor(vibrant));
                                mCollapsingToolbar.setContentScrimColor(palette.getMutedColor(vibrant));
                            }
                        }
                    });
                }
            });
            thread.start();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };


    @Override
    public void onRequestMovieSuccess(Movie movie) {
        show(mLoaderInfo, false);
        mMovie = movie;
        setTextView(mRuntimeTextView, String.valueOf(mMovie.getRuntime()));
        setTextView(mReleaseDateTextView, mMovie.getReleaseDateDisplay());
        setTextView(mVoteAverageTextView, String.valueOf(mMovie.getVoteAverage()));

    }

    @Override
    public void onRequestMoviesVideosSuccess(List<MovieVideo> movieVideos) {
        show(mTrailersView, movieVideos.size() > 0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailersRecyclerView.setLayoutManager(linearLayoutManager);

        mVideoAdapter = new MovieVideoAdapter(movieVideos);
        mTrailersRecyclerView.setAdapter(mVideoAdapter);

    }

    @Override
    public void onRequestMoviesReviewSuccess(List<MovieReview> movieReviews) {
        show(mReviewsView, movieReviews.size() > 0);

        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReviewAdapter = new MovieReviewAdapter(movieReviews);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);
    }

    @Override
    public void onRequestMoviesFailure(String error, String action) {
        show(mLoaderInfo, false);
        showToast("Ocorreu um erro:  " + error + " - na action " + action);
    }


    @Override
    public void onDestroy() {
        Picasso.with(this).cancelRequest(backdropTarget);
        super.onDestroy();
    }


}
