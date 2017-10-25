package com.rustwebdev.sweetsuite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.rustwebdev.sweetsuite.data.Recipe;
import com.rustwebdev.sweetsuite.data.RecipeService;
import com.rustwebdev.sweetsuite.di.Injector;
import com.rustwebdev.sweetsuite.recipe.RecipeActivity;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
  private static final String LOG_TAG = MainActivity.class.getSimpleName();
  private ArrayList<Recipe> recipeArrayList;
  private RecyclerView recyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar_main);
    setSupportActionBar(toolbar);
    //noinspection ConstantConditions
    getSupportActionBar().setTitle("");
    recyclerView = findViewById(R.id.recipe_rv);

    RecipeService recipeService = Injector.provideMovieService();
    recipeService.getRecipes().enqueue(new Callback<ArrayList<Recipe>>() {

      @Override
      public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
        recipeArrayList = response.body();
        configureLayout();
      }

      @Override public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
        Log.d(LOG_TAG, "Retrofit has failed. " + t);
      }
    });
  }

  private void configureLayout() {
    RecipesAdapter recipesAdapter = new RecipesAdapter(recipeArrayList, itemListener);
    RecyclerView.LayoutManager mLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setAdapter(recipesAdapter);
  }

  private final RecipesAdapter.RecipeItemListener itemListener = new RecipesAdapter.RecipeItemListener() {
    @Override public void onRecipeClick(Recipe recipe) {
      Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
      intent.putExtra(Constants.RECIPE_PARCELABLE, recipe);
      startActivity(intent);
    }
  };
}
