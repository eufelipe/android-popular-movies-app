package com.eufelipe.popularmovies.callbacks;

import org.json.JSONObject;

/**
 * @description : Interface com os métodos de retorno e stage das requisições na Api The Movie Db
 * @author: Felipe Rosas <contato@eufelipe.com>
 */


public interface TheMovieDbCallback {
    /**
     * @description : Este método será chamado quando a requisição feitar tiver sucesso
     * @param json
     */
    void onRequestMoviesSuccess(JSONObject json);

    /**
     * @description: Este método será chamado quando ocorrer uma falha
     */
    void onRequestMoviesFailure();

    /**
     * @description : Este método será chamado para exibir ou esconder um ProgressBar
     * @param isShow
     */
    void onRequestMoviesProgress(Boolean isShow);
}
