package com.rustwebdev.sweetsuite.ui.recipes;

import com.rustwebdev.sweetsuite.datasource.database.main.recipe.Recipe;
import java.util.List;

interface RecipesViewContract {
  interface View {
    void showRecipes(List<Recipe> movies);
  }
}
