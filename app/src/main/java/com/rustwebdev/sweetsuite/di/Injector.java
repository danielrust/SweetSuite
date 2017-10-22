package com.rustwebdev.sweetsuite.di;

import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.data.RecipeService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by flanhelsinki on 10/21/17.
 */

public class Injector {

  public static Retrofit provideRetrofit(String baseUrl) {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

    return new Retrofit.Builder().baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();
  }

  public static RecipeService provideMovieService() {
    return provideRetrofit(Constants.BASE_URL).create(RecipeService.class);
  }
}