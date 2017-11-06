package com.rustwebdev.sweetsuite.ui.recipes;

import android.os.AsyncTask;
import android.util.Log;
import com.rustwebdev.sweetsuite.datasource.database.main.MainDatabase;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.Ingredient;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.IngredientDao;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.Recipe;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.RecipeDao;
import com.rustwebdev.sweetsuite.datasource.database.main.step.Step;
import com.rustwebdev.sweetsuite.datasource.database.main.step.StepDao;
import com.rustwebdev.sweetsuite.datasource.webservice.recipes.RecipeService;
import com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto.DtoIngredient;
import com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto.DtoRecipe;
import com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto.DtoStep;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class RecipesPresenter {
  private static final String LOG_TAG = RecipesPresenter.class.getSimpleName();

  private final RecipeService recipeService;
  private static RecipeDao recipeDao;
  private final MainDatabase mainDatabase;
  private final StepDao stepDao;
  private final IngredientDao ingredientDao;
  private static RecipesViewContract.View recipesView;

  public RecipesPresenter(RecipesViewContract.View recipesView, RecipeService recipeService,
      MainDatabase mainDatabase) {
    this.recipeService = recipeService;
    RecipesPresenter.recipesView = recipesView;
    this.stepDao = mainDatabase.stepDao();
    recipeDao = mainDatabase.recipeDao();
    this.ingredientDao = mainDatabase.ingredientDao();
    this.mainDatabase = mainDatabase;
  }

  public void getRecipesFromWebservice() {
    recipeService.getRecipes().enqueue(new Callback<ArrayList<DtoRecipe>>() {
      @Override public void onResponse(Call<ArrayList<DtoRecipe>> call,
          Response<ArrayList<DtoRecipe>> response) {
        processWebServiceResponse(response);
      }

      @Override public void onFailure(Call<ArrayList<DtoRecipe>> call, Throwable t) {
        Log.d(LOG_TAG, "Retrofit failed: " + t);
      }
    });
  }

  private void processWebServiceResponse(final Response<ArrayList<DtoRecipe>> response) {
    if (response.isSuccessful()) {
      Single.fromCallable(new Callable<String>() {
        @Override public String call() throws Exception {
          addRecipesToDatabase(response.body());
          return "true";
        }
      }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    } else {
      Log.d(LOG_TAG, "Failure with webservice");
    }
  }

  private void addRecipesToDatabase(ArrayList<DtoRecipe> dtoRecipes) {
    recipeDao.deleteAll();
    mainDatabase.beginTransaction();
    for (com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto.DtoRecipe dtoResult : dtoRecipes) {
      Recipe recipe = new Recipe(dtoResult.getName(), dtoResult.getServings());
      long recipe_id = recipeDao.insertRecipe(recipe);
      List<DtoStep> dtoSteps = dtoResult.getDtoSteps();
      List<DtoIngredient> dtoIngredients = dtoResult.getIngredients();
      for (DtoStep dtoStep : dtoSteps) {
        Step step = new Step(recipe_id, dtoStep.getShortDescription(), dtoStep.getDescription(),
            dtoStep.getVideoURL(), dtoStep.getThumbnailURL());
        stepDao.insertStep(step);
      }
      for (DtoIngredient dtoIngredient : dtoIngredients) {
        Ingredient ingredient =
            new Ingredient(recipe_id, dtoIngredient.getQuantity(), dtoIngredient.getMeasure(),
                dtoIngredient.getIngredient());
        ingredientDao.insertIngredient(ingredient);
      }
    }
    mainDatabase.setTransactionSuccessful();
    mainDatabase.endTransaction();
    getRecipesFromDatabase();
  }


  public void getRecipesFromDatabase() {
    new GetRecipesFromDatabaseTask().execute();
  }

  private static class GetRecipesFromDatabaseTask extends AsyncTask<Void, Void, List<Recipe>> {

    @Override protected List<Recipe> doInBackground(Void... voids) {
      return recipeDao.getRecipeNames();
    }

    @Override protected void onPostExecute(List<Recipe> recipes) {
      recipesView.showRecipes(recipes);
    }
  }

}

