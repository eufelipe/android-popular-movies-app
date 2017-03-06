package com.eufelipe.popularmovies.callbacks;

import com.eufelipe.popularmovies.models.Movie;

import org.json.JSONObject;

import java.util.List;

/**
 * @description : Interface com os métodos de retorno e stage das requisições na Api The Movie Db
 * @author: Felipe Rosas <contato@eufelipe.com>
 */


public interface TheMovieDbCallback {
    /**
     * @param movies
     * @description : Este método será chamado quando a requisição feitar tiver sucesso
     */
    void onRequestMoviesSuccess(List<Movie> movies, Integer page);

    /**
     * @description: Este método será chamado quando ocorrer uma falha
     */
    void onRequestMoviesFailure();

    /**
     * @param isShow
     * @description : Este método será chamado para exibir ou esconder um ProgressBar
     */
    void onRequestMoviesProgress(Boolean isShow);
}
