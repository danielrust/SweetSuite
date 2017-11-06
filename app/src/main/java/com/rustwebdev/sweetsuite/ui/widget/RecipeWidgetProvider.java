package com.rustwebdev.sweetsuite.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.R;
import com.rustwebdev.sweetsuite.datasource.database.main.MainDatabase;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.Recipe;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.RecipeDao;
import com.rustwebdev.sweetsuite.di.Injector;
import com.rustwebdev.sweetsuite.ui.recipe.RecipeActivity;
import java.util.ArrayList;

public class RecipeWidgetProvider extends AppWidgetProvider {
  private static final String LOG_TAG = RecipeWidgetProvider.class.getSimpleName();
  private static final String CHANGE_NAV_BACKWARD = "buttonclickchangenavbackward";
  private static int currentRecipe = 0;
  private static final String CHANGE_NAV_FORWARD = "buttonclickchangenavforward";

  private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId) {
    MainDatabase mainDatabase = Injector.provideSynchronousMainDatabase(context);
    RecipeDao recipeDao = mainDatabase.recipeDao();
    ArrayList<Recipe> recipes = (ArrayList<Recipe>) recipeDao.getRecipeNames();
    Intent intent = new Intent(context, ListRemoteViewService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    intent.putExtra(Constants.APP_WIDGET_CURRENT_POSITION, currentRecipe);
    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

    Intent in = new Intent(context, RecipeActivity.class);
    in.putExtra(Constants.APP_WIDGET_CURRENT_POSITION, currentRecipe);
    in.putExtra(Constants.RECIPE_PARCELABLE,recipes.get(currentRecipe));
    PendingIntent pendingIntent =
        PendingIntent.getActivity(context, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);

    Intent clickForwardIntent = new Intent(context, RecipeWidgetProvider.class);
    clickForwardIntent.putExtra(Constants.POSITION_INCREMENT, currentRecipe + 1);
    clickForwardIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
    clickForwardIntent.setAction(CHANGE_NAV_FORWARD);
    PendingIntent pendingIntentForwardClickEvent =
        PendingIntent.getBroadcast(context, 1, clickForwardIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);
    views.setOnClickPendingIntent(R.id.widget_forward_nav, pendingIntentForwardClickEvent);

    Intent clickBackwardIntent = new Intent(context, RecipeWidgetProvider.class);
    clickBackwardIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
    clickBackwardIntent.setAction(CHANGE_NAV_BACKWARD);
    PendingIntent pendingIntentBackwardClickEvent =
        PendingIntent.getBroadcast(context, 1, clickBackwardIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);
    views.setOnClickPendingIntent(R.id.widget_back_nav, pendingIntentBackwardClickEvent);

    views.setOnClickPendingIntent(R.id.widget_recipe_title, pendingIntent);
    views.setTextViewText(R.id.widget_recipe_title, recipes.get(currentRecipe).name);
    views.setRemoteAdapter(appWidgetId, R.id.widget_list_view, intent);
    views.setEmptyView(R.id.widget_list_view, R.id.empty_view);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }

  @Override public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }

  @Override public void onReceive(Context context, Intent intent) {
    super.onReceive(context, intent);

    if (intent.getAction() == CHANGE_NAV_FORWARD) {
      if (currentRecipe == 3) {
        currentRecipe = 0;
      } else {
        currentRecipe++;
      }
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
      ComponentName thisAppWidget =
          new ComponentName(context.getPackageName(), RecipeWidgetProvider.class.getName());
      int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

      this.onUpdate(context, appWidgetManager, appWidgetIds);
    } else if (intent.getAction() == CHANGE_NAV_BACKWARD) {
      if (currentRecipe == 0) {
        currentRecipe = 3;
      } else {
        currentRecipe--;
      }
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
      ComponentName thisAppWidget =
          new ComponentName(context.getPackageName(), RecipeWidgetProvider.class.getName());
      int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

      this.onUpdate(context, appWidgetManager, appWidgetIds);
    }
  }
}

