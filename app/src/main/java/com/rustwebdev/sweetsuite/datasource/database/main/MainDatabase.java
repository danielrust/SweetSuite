package com.rustwebdev.sweetsuite.datasource.database.main;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.Ingredient;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.IngredientDao;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.Recipe;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.RecipeDao;
import com.rustwebdev.sweetsuite.datasource.database.main.step.Step;
import com.rustwebdev.sweetsuite.datasource.database.main.step.StepDao;

@Database(entities = { Step.class, Recipe.class, Ingredient.class }, version = 1)
public abstract class MainDatabase extends RoomDatabase {
  public static final String DATABASE_NAME = "main";

  public abstract RecipeDao recipeDao();

  public abstract StepDao stepDao();

  public abstract IngredientDao ingredientDao();
}




