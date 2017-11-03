package com.rustwebdev.sweetsuite.di;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.datasource.database.main.MainDatabase;
import com.rustwebdev.sweetsuite.datasource.webservice.recipes.RecipeService;
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

  public static RecipeService provideRecipeService() {
    return provideRetrofit().create(RecipeService.class);
  }

  public static MainDatabase provideMainDatabase(Context context) {
    return Room.databaseBuilder(context, MainDatabase.class, MainDatabase.DATABASE_NAME).build();
  }
}