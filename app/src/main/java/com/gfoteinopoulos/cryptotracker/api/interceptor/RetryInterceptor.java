package com.gfoteinopoulos.cryptotracker.api.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class RetryInterceptor implements Interceptor{

    private static final String TAG = "CryptoTracker_Retry";
    private final int maxRetries;

    public RetryInterceptor(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public  Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException lastException = null;

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                if (response != null) {
                    response.close();
                }
                response = chain.proceed(request);
                if (response.isSuccessful()) {
                    return response;
                }
                if (response.code() == 429) {
                    long waitTime = getWaitTime(attempt);
                    Log.w(TAG, "Rate limited. Waiting " + waitTime + "ms before retry " + (attempt + 1));
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry interrupted", e);
                    }
                } else if (response.code() >= 500) {
                    long waitTime = getWaitTime(attempt);
                    Log.w(TAG, "Server error " + response.code() + ". Retrying in " + waitTime + "ms");
                    try {
                        Thread.sleep(waitTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry interrupted", e);
                    }
                } else {
                    return response;
                }
            } catch (IOException e) {
                lastException = e;
                long waitTime = getWaitTime(attempt);
                Log.e(TAG, "Network error on attempt " + (attempt + 1) + ": " + e.getMessage());
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Retry interrupted", ie);
                }
            }
        }
        if (response != null) {
            return response;
        }

        throw lastException != null ? lastException : new IOException("Request failed after " + maxRetries + " attempts");
    }
    private long getWaitTime(int attempt) {
        return (long) Math.pow(2, attempt) * 1000;

    }
}
