package com.rustwebdev.sweetsuite;

import android.content.Intent;
import android.os.Bundle;
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
  RecipesAdapter.RecipeItemListener mRecipeItemListener;
  ArrayList<Recipe> recipeArrayList;
  RecyclerView recyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");
    recyclerView = findViewById(R.id.recipe_rv);

    RecipeService recipeService = Injector.provideMovieService();
    recipeService.getRecipes().enqueue(new Callback<ArrayList<Recipe>>() {

      @Override
      public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
        recipeArrayList = response.body();
        Log.d(LOG_TAG, recipeArrayList.toString());
        configureLayout();
      }

      @Override public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
        Log.d(LOG_TAG, "Retrofit has failed. " + t);
      }
    });
  }

  private void configureLayout() {
    RecipesAdapter recipesAdapter = new RecipesAdapter(this, recipeArrayList, itemListener);
    RecyclerView.LayoutManager mLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setAdapter(recipesAdapter);
  }

  private RecipesAdapter.RecipeItemListener itemListener = new RecipesAdapter.RecipeItemListener() {
    @Override public void onRecipeClick(Recipe recipe) {
      Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
      intent.putExtra(Constants.RECIPE_PARCELABLE, recipe);
      //ActivityOptions options =
      //    ActivityOptions.makeSceneTransitionAnimation(MoviesActivity.this, imgView,
      //        Constants.MOVIE_IMG_TRANS_SHARED_ELEMENT);
      startActivity(intent);
    }
  };
}
