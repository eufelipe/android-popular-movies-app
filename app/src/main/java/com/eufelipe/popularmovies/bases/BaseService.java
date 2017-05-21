package com.eufelipe.popularmovies.bases;


import android.util.Log;

import com.eufelipe.popularmovies.application.API;
import com.eufelipe.popularmovies.application.App;
import com.eufelipe.popularmovies.migrations.ManageMigration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseService<T extends RealmObject> {

    private final String TAG = getClass().getSimpleName();

    private static final String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
    private Gson gson;
    private GsonConverterFactory gsonConverterFactory;

    private Class<T> currentModel;

    public BaseService() {
        this.currentModel = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * DataBase Configs
     */
    static RealmConfiguration realmConfig;
    static Realm realm;

    protected Realm realm() {
        if (realm == null) {
            realmConfig = new RealmConfiguration.Builder()
                    .schemaVersion(ManageMigration.MIGRATION_VERSION)
                    .migration(new ManageMigration().migrate())
                    .build();

            realm = Realm.getInstance(realmConfig);
        }
        return realm;
    }

    /**
     * Listar todos
     *
     * @param <T>
     * @return
     */
    protected <T extends RealmObject> RealmResults<T> findAll() {
        RealmQuery<T> query = (RealmQuery<T>) realm().where(currentModel);
        RealmResults<T> items = query.findAll();
        return items;
    }


    /**
     * Busca no banco um registro com o Id informado
     *
     * @param id
     * @return
     */
    public T findById(int id) {
        RealmQuery<T> query = realm()
                .where(currentModel)
                .equalTo("id", id);

        List<T> items = query.findAll();
        return items.size() == 0 ? null : items.get(0);
    }


    /**
     * Busca no banco o primeiro resultado com o ID informado ou cria um novo obj
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends RealmObject> T firstOrNew(T model, int id) {

        T record = (T) findById(id);
        if (record == null) {
            record = realm().copyToRealm(model);
        }

        return record;
    }


    /**
     * Helper para debug de Deletar todos os registros
     *
     * @param <T>
     */
    public <T extends RealmObject> void deleteAll() {
        final RealmResults<T> results = findAll();
        realm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }


    /**
     * HTTP Client
     *
     * @param language
     * @return
     */
    private OkHttpClient getClient(final String language) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        /**
         * Interceptar todas as requisições para incluir a api_key
         */
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();


                HttpUrl.Builder urlBuilder = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", App.getTheMovieDbKey());

                if (language != null) {
                    urlBuilder.addQueryParameter("language", language);
                }

                HttpUrl url = urlBuilder.build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };


        httpClient.addInterceptor(interceptor);

        return httpClient.build();
    }


    /**
     * GSON Converter
     *
     * @return
     */
    private GsonConverterFactory getConverter() {
        if (gson == null) {
            gson = new GsonBuilder().setDateFormat(dateFormat).create();
        }

        if (gsonConverterFactory == null) {
            gsonConverterFactory = GsonConverterFactory.create(gson);
        }
        return gsonConverterFactory;
    }


    /**
     * Sobrecarga
     *
     * @return
     */
    protected API api() {
        return api(null);
    }


    /**
     * Request Api
     *
     * @return
     */
    protected API api(String language) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.ENDPOINT)
                .client(getClient(language))
                .addConverterFactory(getConverter())
                .build();

        return retrofit.create(API.class);
    }


    /**
     * Helper para debug
     *
     * @param request
     * @param obj
     */
    protected void requestLogger(Call<?> request, Object obj) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String strDate = simpleDateFormat.format(new Date());

        // Esta inclusão forçada é apenas para debug ¯\_(ツ)_/¯
        String url = request.request().url().toString();
        if (!url.contains("api_key")) {
            url = url + "&api_key=" + App.getTheMovieDbKey();
        }

        Log.e("Request: ", "*******************************************************");
        Log.e("Date Time: ", strDate);
        Log.e("Request: ", url);

        // Se um dia precisar passar POST com parametros...
        if (obj != null) {
            Log.e("Parameters: ", getJson(obj));
        }
        Log.e("Request: ", "*******************************************************");

    }


    /**
     * Json Helper
     *
     * @param object
     * @return
     */
    protected String getJson(Object object) {
        Gson gson = new Gson();

        String out = "";
        try {
            out = gson.toJson(object);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        return out;
    }


}
