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
import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.R;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.Recipe;
import com.rustwebdev.sweetsuite.di.Injector;
import com.rustwebdev.sweetsuite.idlingResource.RecipeIdlingResource;
import com.rustwebdev.sweetsuite.ui.recipe.RecipeActivity;
import java.util.List;

public class RecipesActivity extends AppCompatActivity implements RecipesViewContract.View {
  private static final String LOG_TAG = RecipesActivity.class.getSimpleName();
  private RecyclerView recyclerView;
  private RecipesPresenter recipesPresenter;
  @Nullable private RecipeIdlingResource mSimpleIdlingResource;
  private RecipesAdapter recipesAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar_main);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("");
    recyclerView = findViewById(R.id.recipe_rv);
    recipesPresenter = new RecipesPresenter(this, Injector.provideRecipeService(),
        Injector.provideMainDatabase(getApplicationContext()));
    mSimpleIdlingResource = getIdlingResource();
    if(savedInstanceState != null){
      recipesPresenter.getRecipesFromDatabase();
    }
    recipesPresenter.getRecipesFromWebservice();
  }

  @Override protected void onStart() {
    super.onStart();
    if (mSimpleIdlingResource != null) {
      mSimpleIdlingResource.setIdleState(false);
    }
  }

  private void configureLayout(List<Recipe> recipeArrayList) {
    recipesAdapter = new RecipesAdapter(recipeArrayList, itemListener);
    RecyclerView.LayoutManager mLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setAdapter(recipesAdapter);
  }

  private final RecipesAdapter.RecipeItemListener itemListener =
      new RecipesAdapter.RecipeItemListener() {
        @Override public void onRecipeClick(Recipe recipe) {
          Intent intent = new Intent(RecipesActivity.this, RecipeActivity.class);
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

  @Override public void showRecipes(List<Recipe> recipes) {
    if (mSimpleIdlingResource != null) {
      mSimpleIdlingResource.setIdleState(true);
    }
    configureLayout(recipes);
  }

  @Override public void updateRecipes() {

  }
}
