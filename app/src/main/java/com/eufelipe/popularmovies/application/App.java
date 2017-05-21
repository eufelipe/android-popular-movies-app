package com.eufelipe.popularmovies.application;

import android.app.Application;
import android.content.Context;

import com.eufelipe.popularmovies.R;

import io.realm.Realm;

/**
 * @description : Class App responsável pela aplicação
 * @author: Felipe Rosas <contato@eufelipe.com>
 */

public class App extends Application {

    public static Context mGlobalContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mGlobalContext = getApplicationContext();
        Realm.init(mGlobalContext);
    }

    /**
     * @return string
     * TODO: É necessário criar um resource chamado secret.xml em values com uma string chamada api_key_the_movie_db para armazenar a Key da Api do The MoviesCallback Db
     * @description: Seguindo as recomendações do artigo Popular Movies App Implementation Guide
     * <https://docs.google.com/document/d/1ZlN1fUsCSKuInLECcJkslIqvpKlP7jWL2TP9m6UiA6I/pub?embedded=true#h.3omxhyonl2o1>
     * Neste projeto não costa os dados da API KEY do The MoviesCallback Db
     */
    public static String getTheMovieDbKey() {
        return mGlobalContext.getString(R.string.api_key_the_movie_db);
    }

}
