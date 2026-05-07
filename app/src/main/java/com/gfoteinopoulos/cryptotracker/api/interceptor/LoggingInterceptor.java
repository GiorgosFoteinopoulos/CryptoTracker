package com.gfoteinopoulos.cryptotracker.api.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoggingInterceptor implements Interceptor{
    private static final String TAG = "CryptoTracker_API";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = System.currentTimeMillis();

        Log.d(TAG, "Sending request: " + request.url());
        Log.d(TAG, "Method: " + request.method());

        Response response;

        try {
            response = chain.proceed(request);
        } catch (IOException e) {
            Log.e(TAG, "Request failed: " + e.getMessage());
            throw e;
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        Log.d(TAG, "Received response: " + response.code() +
                " for " + request.url() +
                " in " + duration + "ms");

        return response;
    }
}
