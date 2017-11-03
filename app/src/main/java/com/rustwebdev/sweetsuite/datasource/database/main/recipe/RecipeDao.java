package com.rustwebdev.sweetsuite.datasource.database.main.recipe;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao public interface RecipeDao {
  @Insert long insertRecipe(Recipe recipe);

  //@Query("SELECT * FROM main") void getRecipes();

  @Insert void insertAll(Recipe... recipes);

  @Query("DELETE FROM recipe") void deleteAll();

  @Query("SELECT * FROM recipe") List<Recipe> getRecipeNames();
}