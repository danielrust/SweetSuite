package com.rustwebdev.sweetsuite.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.rustwebdev.sweetsuite.R;
import com.rustwebdev.sweetsuite.ui.recipes.MainActivity;

public class RecipeWidgetProvider extends AppWidgetProvider {

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

    //CharSequence widgetText = context.getString(R.string.appwidget_text);
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
    //views.setTextViewText(R.id.appwidget_text, widgetText);
    Intent in = new Intent(context, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
    views.setOnClickPendingIntent(R.id.widget_recipe_title, pendingIntent);
    views.setTextViewText(R.id.widget_recipe_title, "Hello Thersesm!");

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
}

