package com.rustwebdev.sweetsuite.ui.recipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto.DtoRecipe;
import com.rustwebdev.sweetsuite.datasource.webservice.recipes.dto.DtoStep;
import com.rustwebdev.sweetsuite.recipe.RecipeFragment;
import java.util.ArrayList;

public class RecipePagerAdapter extends FragmentStatePagerAdapter {
  private final DtoRecipe recipe;
  private final ArrayList<DtoStep> dtoSteps;
  private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

  public RecipePagerAdapter(FragmentManager fm, DtoRecipe recipe) {
    super(fm);
    this.dtoSteps = (ArrayList<DtoStep>) recipe.getDtoSteps();
    this.recipe = recipe;
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment;
    if (position == 0) {
      fragment = RecipeBaseFragment.newInstance(dtoSteps.get(position), recipe, position);
    } else {
      fragment = RecipeFragment.newInstance(dtoSteps.get(position), position);
    }
    return fragment;
  }

  @Override public int getCount() {
    return dtoSteps.size();
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    BaseRecipeFragment fragment = (BaseRecipeFragment) super.instantiateItem(container, position);

    registeredFragments.put(position, fragment);
    return fragment;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    registeredFragments.remove(position);
    super.destroyItem(container, position, object);
  }

  public Fragment getRegisteredFragment(int position) {
    return registeredFragments.get(position);
  }
}
