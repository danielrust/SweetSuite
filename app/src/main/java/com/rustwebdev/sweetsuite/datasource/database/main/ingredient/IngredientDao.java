package com.rustwebdev.sweetsuite.datasource.database.main.ingredient;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface IngredientDao {
  @Insert void insertIngredient(Ingredient ingredient);

}
