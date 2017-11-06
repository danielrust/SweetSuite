package com.rustwebdev.sweetsuite.ui.recipe;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.Ingredient;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.Recipe;
import com.rustwebdev.sweetsuite.datasource.database.main.step.Step;
import java.util.ArrayList;
import java.util.List;

class RecipePagerAdapter extends FragmentStatePagerAdapter {
  private final Recipe recipe;
  private final ArrayList<Step> steps;
  private final ArrayList<Ingredient> ingredients;
  private final SparseArray<Fragment> registeredFragments = new SparseArray<>();

  public RecipePagerAdapter(FragmentManager fm, Recipe recipe, List<Step> steps,
      List<Ingredient> ingredients) {
    super(fm);
    this.recipe = recipe;
    this.ingredients = (ArrayList<Ingredient>) ingredients;
    this.steps = (ArrayList<Step>) steps;
  }

  @Override public Fragment getItem(int position) {
    Fragment fragment;
    if (position == 0) {
      fragment = RecipeBaseFragment.newInstance(steps.get(position),recipe, position, ingredients);
    } else {
      fragment = RecipeFragment.newInstance(steps.get(position), position);
    } return fragment;
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
