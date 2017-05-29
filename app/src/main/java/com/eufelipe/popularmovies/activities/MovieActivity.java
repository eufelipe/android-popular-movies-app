package com.eufelipe.popularmovies.activities;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MovieActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private Movie mMovie;

    private TheMovieDbService theMovieDbService = null;

    private boolean isFavorite = false;

    private MovieReviewAdapter mReviewAdapter;
    private MovieVideoAdapter mVideoAdapter;

    private Context mContext;


    @BindView(R.id.collapsing)
    CollapsingToolbarLayout mCollapsingToolbar;

    @BindView(R.id.iv_backdrop)
    ImageView mBackdropImageView;

    @BindView(R.id.iv_poster)
    ImageView mPosterImageView;

    @BindView(R.id.fb_favorite)
    FloatingActionButton mFbFavorite;

    /**
     * Infos
     */
    @BindView(R.id.tv_overview)
    TextView mOverviewTextView;

    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTextView;

    @BindView(R.id.tv_vote_average)
    TextView mVoteAverageTextView;

    @BindView(R.id.tv_runtime)
    TextView mRuntimeTextView;

    @BindView(R.id.ll_info)
    LinearLayout mInfoView;

    @BindView(R.id.pb_info)
    ProgressBar mLoaderInfo;

    /**
     * Reviews
     */
    @BindView(R.id.ll_reviews)
    LinearLayout mReviewsView;

    @BindView(R.id.rv_reviews)
    RecyclerView mReviewsRecyclerView;

    /**
     * Videos
     */
    @BindView(R.id.ll_trailers)
    LinearLayout mTrailersView;

    @BindView(R.id.rv_trailers)
    RecyclerView mTrailersRecyclerView;


    /**
     * Lazy load Instance
     *
     * @return
     */
    private TheMovieDbService getTheMovieDbService() {
        if (theMovieDbService == null) {
            theMovieDbService = new TheMovieDbService(this, getContentResolver());
        }

        return theMovieDbService;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        mContext = getApplicationContext();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getParcelable(Constants.MOVIE_DATA_KEY) != null) {
            mMovie = bundle.getParcelable(Constants.MOVIE_DATA_KEY);

            Cursor cursor = getTheMovieDbService().findMovieCursorById(mMovie.getRemoteId());
            if (cursor != null && cursor.moveToFirst()) {
                mMovie.setIsFavorite(true);
            }

        } else {
            showToast(R.string.app_error_movie_invalid);
            finish();
        }

        initializeToolbar(mMovie.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Integer remoteId = Integer.parseInt(mMovie.getRemoteId());

                getTheMovieDbService().movie(remoteId);
                getTheMovieDbService().videos(remoteId);
                getTheMovieDbService().reviews(remoteId);
            }

        });
        thread.start();

        show(mInfoView, false);
        updateImageViews();
        mCollapsingToolbar.setTitle(mMovie.getTitle());
        setTextView(mOverviewTextView, mMovie.getOverview());

        isFavorite = mMovie.getIsFavorite();
        setIconFloatingActionButton(isFavorite);

    }

    private void updateImageViews() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Picasso.with(mContext)
                                .load(mMovie.getBackdropImage())
                                .into(backdropTarget);

                        Picasso.with(mContext)
                                .load(mMovie.getPosterImage())
                                .into(mPosterImageView);

                    }
                });
            }
        });
        thread.start();

    }


    // Request de um Filme


    private void setIconFloatingActionButton(boolean isFavorite) {
        int on = R.drawable.ic_favorite_on;
        int off = R.drawable.ic_favorite_off;
        Drawable drawable = ContextCompat.getDrawable(mContext, isFavorite ? on : off);
        mFbFavorite.setImageDrawable(drawable);
    }

    private final Target backdropTarget = new Target() {
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
    public void onRequestMovieSuccess(final Movie movie) {

        show(mLoaderInfo, false);
        show(mInfoView, true);

        mMovie = movie;
        setTextView(mRuntimeTextView, mMovie.getRuntimeDisplay());
        setTextView(mReleaseDateTextView, mMovie.getReleaseDateDisplay());
        setTextView(mVoteAverageTextView, mMovie.getVoteAverageDisplay());

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
    public void onRequestMoviesFailure(String error) {
        show(mLoaderInfo, false);
        if (error != null) {
            showToast(error);
        }
    }


    @OnClick(R.id.fb_favorite)
    public void setIsFavorite() {
        isFavorite = !isFavorite;
        setIconFloatingActionButton(isFavorite);
        getTheMovieDbService().toggleFavoriteMovie(mMovie);
    }


    @Override
    public void onDestroy() {
        Picasso.with(this).cancelRequest(backdropTarget);
        super.onDestroy();
    }

}
