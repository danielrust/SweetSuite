package com.rustwebdev.sweetsuite.ui.recipe;

import android.os.AsyncTask;
import com.rustwebdev.sweetsuite.datasource.database.main.MainDatabase;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.Ingredient;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.IngredientDao;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.RecipeDao;
import com.rustwebdev.sweetsuite.datasource.database.main.step.Step;
import com.rustwebdev.sweetsuite.datasource.database.main.step.StepDao;
import java.util.List;

/**
 * Created by flanhelsinki on 11/3/17.
 */

class RecipePresenter {
  private static final String LOG_TAG = RecipePresenter.class.getSimpleName();

  private final RecipeDao recipeDao;
  private final MainDatabase mainDatabase;
  private static StepDao stepDao;
  private static IngredientDao ingredientDao;
  private static RecipeViewContract.View recipeView;

  public RecipePresenter(RecipeViewContract.View recipeView, MainDatabase mainDatabase) {
    RecipePresenter.recipeView = recipeView;
    stepDao = mainDatabase.stepDao();
    this.recipeDao = mainDatabase.recipeDao();
    ingredientDao = mainDatabase.ingredientDao();
    this.mainDatabase = mainDatabase;
  }

  public void getSteps(long id) {
    new GetStepsFromDatabaseTask().execute(id);
  }

  public void getIngredients(long id) {
    new GetIngredientsFromDatabaseTask().execute(id);
  }

  private static class GetStepsFromDatabaseTask extends AsyncTask<Long, Void, List<Step>> {

    @Override protected List<Step> doInBackground(Long... longs) {
      return stepDao.getSteps(longs[0]);
    }

    @Override protected void onPostExecute(List<Step> steps) {
      recipeView.returnSteps(steps);
    }
  }

  private static class GetIngredientsFromDatabaseTask
      extends AsyncTask<Long, Void, List<Ingredient>> {

    @Override protected List<Ingredient> doInBackground(Long... longs) {
      return ingredientDao.getIngredients(longs[0]);
    }

    @Override protected void onPostExecute(List<Ingredient> ingredients) {
      recipeView.returnIngredients(ingredients);
    }
  }
}
