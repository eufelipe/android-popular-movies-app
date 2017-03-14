package com.eufelipe.popularmovies.bases;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eufelipe.popularmovies.R;
import com.eufelipe.popularmovies.application.ListMovieCategory;
import com.eufelipe.popularmovies.callbacks.TheMovieDbCallback;
import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.models.MovieReview;
import com.eufelipe.popularmovies.models.MovieVideo;

import java.util.List;


/**
 * @description : class abstract com métodos globais a todas as activities
 * @author: Felipe Rosas <contato@eufelipe.com>
 */
public abstract class BaseActivity extends AppCompatActivity implements TheMovieDbCallback {

    protected int title = R.string.popular_movies;

    private Toast mToast;
    private Toolbar mToolbar;


    /**
     * @param textView
     * @param value
     */
    protected void setTextView(TextView textView, String value) {
        if (textView == null || value == null) {
            return;
        }
        textView.setText(value);
    }


    /**
     * @param view
     * @param isShow
     */
    protected void show(View view, boolean isShow) {
        if (view == null) {
            return;
        }
        view.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }


    protected void setTitleToolbar(ListMovieCategory listMovieCategory) {
        if (mToolbar == null) {
            return;
        }

        int color = ContextCompat.getColor(getApplicationContext(), R.color.colorGrayDark);
        if (listMovieCategory == ListMovieCategory.TOP_RATED) {
            color = ContextCompat.getColor(getApplicationContext(), R.color.colorOrange);
        }
        mToolbar.setBackgroundColor(color);
        title = (listMovieCategory == ListMovieCategory.POPULAR ? R.string.popular_movies : R.string.top_rated_movies);
        mToolbar.setTitle(title);
    }


    protected void initializeToolbar(String title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar == null) {
            return;
        }
        setSupportActionBar(mToolbar);
        setTitle(title);
        mToolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
    }

    protected void initializeToolbar(int resourceTitle) {
        initializeToolbar(getString(resourceTitle));
    }


    protected void showToast(int resourceId) {
        showToast(getString(resourceId));
    }

    protected void showToast(String message) {
        if (message == null) {
            return;
        }

        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mToast.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestMovieSuccess(List<Movie> movies, Integer page) {

    }

    @Override
    public void onRequestMoviesFailure(String error, String action) {

    }


    @Override
    public void onRequestMovieSuccess(Movie movie) {

    }

    @Override
    public void onRequestMoviesVideosSuccess(List<MovieVideo> movieVideos) {

    }

    @Override
    public void onRequestMoviesReviewSuccess(List<MovieReview> movieReviews) {

    }
}