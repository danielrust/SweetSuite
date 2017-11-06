package com.rustwebdev.sweetsuite.ui.recipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.R;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.Ingredient;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.Recipe;
import com.rustwebdev.sweetsuite.datasource.database.main.step.Step;
import com.rustwebdev.sweetsuite.di.Injector;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") public class RecipeActivity extends AppCompatActivity
    implements RecipeViewContract.View {

  public static final String LOG_TAG = RecipeActivity.class.getSimpleName();
  @BindView(R.id.nav_recipe_step) TextView navRecipeStep;
  @BindView(R.id.nav_recipe_title) TextView navRecipeName;
  @BindView(R.id.pager) ViewPager mViewPager;
  @BindView(R.id.back_nav) FrameLayout backNavBtn;
  @BindView(R.id.forward_nav) FrameLayout forwardNavBtn;
  private RecipePagerAdapter mRecipePagerAdapter;
  private int currentPosition = 0;
  RecipePresenter recipePresenter;
  private Recipe recipe;
  private ArrayList<Ingredient> ingredients;
  private ArrayList<Step> steps;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);
    ButterKnife.bind(this);
    recipe = getIntent().getParcelableExtra(Constants.RECIPE_PARCELABLE);
    recipePresenter = new RecipePresenter(this, Injector.provideMainDatabase(this));
    if (savedInstanceState != null) {
      if (savedInstanceState.containsKey(Constants.VIEW_PAGER_POSITION)) {
        currentPosition = savedInstanceState.getInt(Constants.VIEW_PAGER_POSITION);
      }
      if (((savedInstanceState.containsKey(Constants.STEPS_ARRAY))
          && (savedInstanceState.containsKey(Constants.INGREDIENTS_ARRAY)))) {
        ingredients = savedInstanceState.getParcelableArrayList(Constants.INGREDIENTS_ARRAY);
        steps = savedInstanceState.getParcelableArrayList(Constants.STEPS_ARRAY);
      }
      configureLayout();
    } else {
      recipePresenter.getSteps(recipe.id);
    }
    Toolbar toolbar = findViewById(R.id.toolbar_main);
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    getSupportActionBar().setTitle("");
  }

  private void configureLayout() {
    navRecipeName.setText(recipe.name);
    navRecipeStep.setText(this.getString(R.string.nav_recipe_tv_text, currentPosition,
        steps.get(currentPosition).shortDescription));
    mRecipePagerAdapter =
        new RecipePagerAdapter(getSupportFragmentManager(), recipe, steps, ingredients);
    mViewPager.setAdapter(mRecipePagerAdapter);
    if (mViewPager.getCurrentItem() == 0) {
      backNavBtn.setVisibility(View.INVISIBLE);
    } else if (mViewPager.getCurrentItem() == steps.size()) {
      forwardNavBtn.setVisibility(View.INVISIBLE);
      backNavBtn.setVisibility(View.VISIBLE);
    } else {
      backNavBtn.setVisibility(View.VISIBLE);
      forwardNavBtn.setVisibility(View.VISIBLE);
    }
    //noinspection deprecation
    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

      @Override public void onPageSelected(int position) {
        super.onPageSelected(position);
        BaseRecipeFragment oldFrag =
            (BaseRecipeFragment) mRecipePagerAdapter.getRegisteredFragment(currentPosition);
        if (oldFrag == null) {
          return;
        } else {
          oldFrag.onPauseFragment();
        }
        changeMenuPosition(position);

        currentPosition = position;

        if (position == 0) {
          backNavBtn.setVisibility(View.INVISIBLE);
        } else if (position == steps.size() - 1) {
          forwardNavBtn.setVisibility(View.INVISIBLE);
          backNavBtn.setVisibility(View.VISIBLE);
        } else {
          backNavBtn.setVisibility(View.VISIBLE);
          forwardNavBtn.setVisibility(View.VISIBLE);
        }
        navRecipeStep.setText(getString(R.string.nav_recipe_tv_text, currentPosition,
            steps.get(position).shortDescription));
      }
    });
  }

  private void changeMenuPosition(int position) {
    navRecipeStep.setText(getString(R.string.nav_recipe_tv_text, currentPosition,
        steps.get(position).shortDescription));
  }

  @OnClick(R.id.forward_nav) public void forwardFragment() {
    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
  }

  @OnClick(R.id.back_nav) public void backFragment() {
    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(Constants.VIEW_PAGER_POSITION, currentPosition);
    outState.putParcelableArrayList(Constants.INGREDIENTS_ARRAY, ingredients);
    outState.putParcelableArrayList(Constants.STEPS_ARRAY, steps);
  }

  @Override public void returnSteps(List<Step> steps) {
    this.steps = (ArrayList<Step>) steps;
    recipePresenter.getIngredients(recipe.id);
  }

  @Override public void returnIngredients(List<Ingredient> ingredients) {
    this.ingredients = (ArrayList<Ingredient>) ingredients;
    configureLayout();
  }

  public interface OnFragmentChangeState {
    void onPauseFragment();
  }
}

