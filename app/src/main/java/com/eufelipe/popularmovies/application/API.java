package com.eufelipe.popularmovies.application;


import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.callbacks.MoviesCallback;
import com.eufelipe.popularmovies.callbacks.ReviewsCallback;
import com.eufelipe.popularmovies.callbacks.VideosCallback;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    String ENDPOINT = "Http://api.themoviedb.org/3/movie/";

    @GET("popular")
    Call<MoviesCallback> popular(@Query("page") Integer page);

    @GET("top_rated")
    Call<MoviesCallback> topRated(@Query("page") Integer page);

    @GET("{movieId}")
    Call<Movie> movie(@Path("movieId") Integer movieId);

    @GET("{movieId}/reviews")
    Call<ReviewsCallback> reviews(@Path("movieId") Integer movieId);

    @GET("{movieId}/videos")
    Call<VideosCallback> videos(@Path("movieId") Integer movieId);

}
