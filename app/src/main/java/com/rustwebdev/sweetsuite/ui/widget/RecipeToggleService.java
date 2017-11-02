package com.rustwebdev.sweetsuite.ui.widget;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class RecipeToggleService extends IntentService {
  public static final String ACTION_TOGGLE_RECIPES =
      "com.rustwebdev.sweetsuite.action.toggle_recipes";

  public RecipeToggleService(String name) {
    super("RecipeToggleService");
  }

  @Override protected void onHandleIntent(@Nullable Intent intent) {

  }
}
