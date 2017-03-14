package com.eufelipe.popularmovies.application;

import com.eufelipe.popularmovies.models.Movie;
import com.eufelipe.popularmovies.models.MovieReview;
import com.eufelipe.popularmovies.models.MovieVideo;

import java.util.List;

/**
 * @description : Interface com os métodos de retorno e stage das requisições na Api The MoviesCallback Db
 * @author: Felipe Rosas <contato@eufelipe.com>
 */


public interface TheMovieDb {
    /**
     * @param movies
     * @description : Este método será chamado quando a requisição feitar tiver sucesso
     */
    void onRequestMovieSuccess(List<Movie> movies, Integer page);


    /**
     * FindOne
     *
     * @param movie
     */
    void onRequestMovieSuccess(Movie movie);

    /**
     * FindAll Videos
     *
     * @param movieVideos
     */
    void onRequestMoviesVideosSuccess(List<MovieVideo> movieVideos);


    /**
     * Find All Reviews
     *
     * @param movieVideos
     */
    void onRequestMoviesReviewSuccess(List<MovieReview> movieVideos);


    /**
     * @description: Este método será chamado quando ocorrer uma falha
     */
    void onRequestMoviesFailure(String error);

}
