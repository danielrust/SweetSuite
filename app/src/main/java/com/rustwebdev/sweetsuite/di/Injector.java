package com.rustwebdev.sweetsuite.di;

import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.data.RecipeService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Injector {

  private static Retrofit provideRetrofit() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

    return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();
  }

  public static RecipeService provideMovieService() {
    return provideRetrofit().create(RecipeService.class);
  }
}