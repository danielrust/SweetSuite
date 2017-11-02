package com.rustwebdev.sweetsuite.datasource.webservice;

import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.datasource.webservice.recipes.RecipeService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module public class ServiceModule {
  @Provides @Singleton public static Retrofit provideRetrofit() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();

    return new Retrofit.Builder().baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();
  }

  @Provides @Singleton public static RecipeService provideRecipeService() {
    return provideRetrofit().create(RecipeService.class);
  }
}
