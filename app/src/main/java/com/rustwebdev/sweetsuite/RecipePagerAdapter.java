package com.rustwebdev.sweetsuite;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import com.rustwebdev.sweetsuite.data.Recipe;
import com.rustwebdev.sweetsuite.data.Step;
import com.rustwebdev.sweetsuite.recipe.RecipeFragment;
import java.util.ArrayList;

public class RecipePagerAdapter extends FragmentStatePagerAdapter {
  private final Recipe recipe;
  private final ArrayList<Step> steps;
  private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

  public RecipePagerAdapter(FragmentManager fm, Recipe recipe) {
    super(fm);
    this.steps = (ArrayList<Step>) recipe.getSteps();
    this.recipe = recipe;
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment;
    if (position == 0) {
      fragment = RecipeBaseFragment.newInstance(steps.get(position), recipe, position);
    } else {
      fragment = RecipeFragment.newInstance(steps.get(position), position);
    }
    return fragment;
  }

  @Override public int getCount() {
    return steps.size();
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
