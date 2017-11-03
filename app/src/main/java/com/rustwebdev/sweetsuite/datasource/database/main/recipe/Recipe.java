package com.rustwebdev.sweetsuite.datasource.database.main.recipe;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "recipe") public class Recipe {
  public Recipe(String name, String servings) {
    this.name = name;
    this.servings = servings;
  }

  @PrimaryKey(autoGenerate = true) int id;
  public String name;
  public String servings;
}