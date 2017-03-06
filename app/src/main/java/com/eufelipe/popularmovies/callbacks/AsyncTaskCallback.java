package com.eufelipe.popularmovies.callbacks;


/**
 * @description : Interface com os métodos de retorno de uma classe AsyncTask
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public interface AsyncTaskCallback {
    /**
     * @description: Este método será chamado quando ocorrer um sucesso na Task
     * @param result
     */
    void onAsyncTaskSuccess(String result);

    /**
     * @description : Este método será chamado quando ocorrer uma falha da Task
     */
    void onAsyncTaskError();
}
