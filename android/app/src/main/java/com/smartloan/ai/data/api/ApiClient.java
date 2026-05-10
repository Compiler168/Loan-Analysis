package com.smartloan.ai.data.api;

import android.content.Context;

import com.smartloan.ai.utils.Constants;
import com.smartloan.ai.utils.TokenManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton Retrofit API client for SmartLoan AI+.
 */
public class ApiClient {

    private static ApiService apiService;
    private static Retrofit retrofit;

    private ApiClient() {}

    public static void init(Context context) {
        if (apiService == null) {
            TokenManager tokenManager = TokenManager.getInstance(context);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenManager))
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
    }

    public static ApiService getService() {
        if (apiService == null) {
            throw new IllegalStateException("ApiClient not initialized. Call init(context) first.");
        }
        return apiService;
    }
}
