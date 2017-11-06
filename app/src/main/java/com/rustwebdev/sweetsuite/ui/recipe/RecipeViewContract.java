package com.rustwebdev.sweetsuite.ui.recipe;

import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.Ingredient;
import com.rustwebdev.sweetsuite.datasource.database.main.step.Step;
import java.util.List;

/**
 * Created by flanhelsinki on 11/3/17.
 */

interface RecipeViewContract {
  interface View {
    void returnSteps(List<Step> steps);

    void returnIngredients(List<Ingredient> ingredients);
  }
}
