package com.gfoteinopoulos.cryptotracker.api;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.gfoteinopoulos.cryptotracker.api.interceptor.CacheInterceptor;
import com.gfoteinopoulos.cryptotracker.api.interceptor.LoggingInterceptor;
import com.gfoteinopoulos.cryptotracker.api.interceptor.RetryInterceptor;

import android.content.Context;

public class ApiClient {

    private static final String BASE_URL = "https://api.coingecko.com/api/v3/";
    private static final int CACHE_SIZE = 10 * 1024 * 1024;
    private static final int MAX_RETRIES = 3;

    private static volatile ApiClient instance;
    private final CoinGeckoApi coinGeckoApi;

    private ApiClient(Context context) {
        Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new CacheInterceptor())
                .addInterceptor(new RetryInterceptor(MAX_RETRIES))
                .addInterceptor(new LoggingInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        coinGeckoApi = retrofit.create(CoinGeckoApi.class);
    }

    public static ApiClient getInstance(Context context) {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public CoinGeckoApi getCoinGeckoApi() {
        return coinGeckoApi;
    }
}
