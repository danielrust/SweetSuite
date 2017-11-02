package com.rustwebdev.sweetsuite.datasource.database.main.ingredient;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "ingredient") public class Ingredient {
  public Ingredient(long recipe_id, String quantity, String measure, String ingredient) {
    this.quantity = quantity;
    this.measure = measure;
    this.ingredient = ingredient;
    this.recipe_id = recipe_id;
  }

  @PrimaryKey(autoGenerate = true)  int id;
  long recipe_id;
  String quantity;
  String measure;
  String ingredient;
}
