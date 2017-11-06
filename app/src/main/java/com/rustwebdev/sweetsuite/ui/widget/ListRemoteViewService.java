package com.rustwebdev.sweetsuite.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.R;
import com.rustwebdev.sweetsuite.datasource.database.main.MainDatabase;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.Ingredient;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.IngredientDao;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.Recipe;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.RecipeDao;
import com.rustwebdev.sweetsuite.di.Injector;
import java.util.ArrayList;

public class ListRemoteViewService extends RemoteViewsService {
  private static final String LOG_TAG = ListRemoteViewService.class.getSimpleName();

  @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
    Log.d(LOG_TAG, String.valueOf(intent.getIntExtra(Constants.APP_WIDGET_CURRENT_POSITION, 0)));
    int currentPosition = intent.getIntExtra(Constants.APP_WIDGET_CURRENT_POSITION, 0);
    return new ListRemoteViewsFactory(this.getApplicationContext(), currentPosition);
  }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
  private static final String LOG_TAG = ListRemoteViewService.class.getSimpleName();
  private final int currentRecipe;
  private ArrayList<Recipe> recipes;
  private ArrayList<Ingredient> ingredients;
  private final Context context;

  public ListRemoteViewsFactory(Context applicationContext, int currentRecipe) {
    this.context = applicationContext;
    this.currentRecipe = currentRecipe;
  }

  @Override public void onCreate() {
  }

  @Override public void onDataSetChanged() {
    MainDatabase mainDatabase = Injector.provideSynchronousMainDatabase(context);
    RecipeDao recipeDao = mainDatabase.recipeDao();
    recipes = (ArrayList<Recipe>) recipeDao.getRecipeNames();

    IngredientDao ingredientDao = mainDatabase.ingredientDao();
    ingredients =
        (ArrayList<Ingredient>) ingredientDao.getIngredients(recipes.get(currentRecipe).id);
    Log.d(LOG_TAG, ingredients.toString());
  }

  @Override public void onDestroy() {

  }

  @Override public int getCount() {
    if (ingredients == null) {
      return 0;
    } else {
      return ingredients.size();
    }
  }

  @Override public RemoteViews getViewAt(int position) {
    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_item);
    rv.setTextViewText(R.id.ingredient_tv, ingredients.get(position).quantity
        + " "
        + ingredients.get(position).measure
        + " "
        + ingredients.get(position).ingredient);
    return rv;
  }

  @Override public RemoteViews getLoadingView() {
    return null;
  }

  @Override public int getViewTypeCount() {
    return 1;
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public boolean hasStableIds() {
    return true;
  }
}
