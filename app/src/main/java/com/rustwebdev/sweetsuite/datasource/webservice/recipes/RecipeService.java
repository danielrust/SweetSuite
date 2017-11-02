package com.rustwebdev.sweetsuite.datasource.webservice.recipes;

import com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto.DtoRecipe;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {
  @GET("baking.json") Call<ArrayList<DtoRecipe>> getRecipes();
}
