package com.rustwebdev.sweetsuite;

import android.support.v4.app.Fragment;
import com.rustwebdev.sweetsuite.recipe.RecipeActivity;

public class BaseRecipeFragment extends Fragment implements RecipeActivity.OnFragmentChangeState {

  @Override public void onPauseFragment() {
  }
}
