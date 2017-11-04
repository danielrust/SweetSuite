package com.rustwebdev.sweetsuite.datasource.database.main.ingredient;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao public interface IngredientDao {
  @Insert void insertIngredient(Ingredient ingredient);

  @Query("SELECT * FROM ingredient WHERE recipe_id = :id") List<Ingredient> getIngredients(
      long id);
}
