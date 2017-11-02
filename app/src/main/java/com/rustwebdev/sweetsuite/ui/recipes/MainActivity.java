package com.rustwebdev.sweetsuite.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import com.rustwebdev.sweetsuite.App;
import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.R;
import com.rustwebdev.sweetsuite.datasource.database.main.MainDatabase;
import com.rustwebdev.sweetsuite.datasource.webservice.recipes.RecipeService;
import com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto.DtoRecipe;
import com.rustwebdev.sweetsuite.idlingResource.RecipeIdlingResource;
import com.rustwebdev.sweetsuite.recipe.RecipeActivity;
import java.util.ArrayList;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
  private static final String LOG_TAG = MainActivity.class.getSimpleName();
  private ArrayList<DtoRecipe> recipeArrayList;
  private RecyclerView recyclerView;
  @Inject RecipeService recipeService;
  @Inject MainDatabase database;
  @Inject RecipesPresenter recipesPresenter;
  @Nullable private RecipeIdlingResource mSimpleIdlingResource;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar_main);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");
    recyclerView = findViewById(R.id.recipe_rv);
    mSimpleIdlingResource = getIdlingResource();
    ((App) getApplication()).getComponent().inject(this);
  }

  @Override protected void onStart() {
    super.onStart();
    recipesPresenter.getRecipesFromWebservice();

  }

  private void getRecipesFromJson() {
    if (mSimpleIdlingResource != null) {
      mSimpleIdlingResource.setIdleState(false);
    }
    recipeService.getRecipes().enqueue(new Callback<ArrayList<DtoRecipe>>() {

      @Override public void onResponse(@NonNull Call<ArrayList<DtoRecipe>> call,
          @NonNull Response<ArrayList<DtoRecipe>> response) {
        recipeArrayList = response.body();

        if (mSimpleIdlingResource != null) {
          mSimpleIdlingResource.setIdleState(true);
          configureLayout();
        }
      }

      @Override
      public void onFailure(@NonNull Call<ArrayList<DtoRecipe>> call, @NonNull Throwable t) {
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

  private final RecipesAdapter.RecipeItemListener itemListener =
      new RecipesAdapter.RecipeItemListener() {
        @Override public void onRecipeClick(DtoRecipe recipe) {
          Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
          intent.putExtra(Constants.RECIPE_PARCELABLE, recipe);
          startActivity(intent);
        }
      };

  @VisibleForTesting @NonNull public RecipeIdlingResource getIdlingResource() {
    if (mSimpleIdlingResource != null) {
      return mSimpleIdlingResource;
    } else {
      mSimpleIdlingResource = new RecipeIdlingResource();
      return mSimpleIdlingResource;
    }
  }
}
