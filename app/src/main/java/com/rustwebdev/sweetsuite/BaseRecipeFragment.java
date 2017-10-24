package com.rustwebdev.sweetsuite;

import android.support.v4.app.Fragment;
import com.rustwebdev.sweetsuite.recipe.RecipeActivity;

/**
 * Created by flanhelsinki on 10/23/17.
 */

public class BaseRecipeFragment extends Fragment implements RecipeActivity.OnFragmentChangeState {

  @Override public void onResumeFragment() {

  }

  @Override public void onPauseFragment() {
  }
}
