package com.rustwebdev.sweetsuite.recipe;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.rustwebdev.sweetsuite.BaseRecipeFragment;
import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.IngredientsAdapter;
import com.rustwebdev.sweetsuite.R;
import com.rustwebdev.sweetsuite.RecipePagerAdapter;
import com.rustwebdev.sweetsuite.data.Ingredient;
import com.rustwebdev.sweetsuite.data.Recipe;

public class RecipeActivity extends AppCompatActivity
    implements IngredientsAdapter.IngredientItemListener  //ExoPlayer.EventListener {
{
  public static final String LOG_TAG = RecipeActivity.class.getSimpleName();

  private Recipe recipe;
  @BindView(R.id.nav_recipe_step) TextView navRecipeStep;
  @BindView(R.id.nav_recipe_title) TextView navRecipeName;
  @BindView(R.id.pager) ViewPager mViewPager;
  @BindView(R.id.back_nav) FrameLayout backNavBtn;
  @BindView(R.id.forward_nav) FrameLayout forwardNavBtn;
  RecipePagerAdapter mRecipePagerAdapter;
  private int currentPosition = 0;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);
    ButterKnife.bind(this);
    recipe = getIntent().getParcelableExtra(Constants.RECIPE_PARCELABLE);
    if (savedInstanceState != null) {
      if (savedInstanceState.containsKey(Constants.VIEW_PAGER_POSITION)) {
        currentPosition = savedInstanceState.getInt(Constants.VIEW_PAGER_POSITION);
      }
    }
    Log.d(LOG_TAG, "CurrentPosition is:" + currentPosition);
    navRecipeName.setText(recipe.getName());
    navRecipeStep.setText((currentPosition + 1) + ". " + recipe.getSteps()
        .get(currentPosition)
        .getShortDescription());
    mRecipePagerAdapter = new RecipePagerAdapter(getSupportFragmentManager(), recipe);
    mViewPager.setAdapter(mRecipePagerAdapter);
    if (mViewPager.getCurrentItem() == 0) {
      backNavBtn.setVisibility(View.INVISIBLE);
    } else if (mViewPager.getCurrentItem() == recipe.getSteps().size()) {
      forwardNavBtn.setVisibility(View.INVISIBLE);
      backNavBtn.setVisibility(View.VISIBLE);
    } else {
      backNavBtn.setVisibility(View.VISIBLE);
      forwardNavBtn.setVisibility(View.VISIBLE);
    }
    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

      @Override public void onPageSelected(int position) {
        super.onPageSelected(position);
        Log.d(LOG_TAG, "CurrentPositionOnSelect: " + currentPosition);
        BaseRecipeFragment oldFrag =
            (BaseRecipeFragment) mRecipePagerAdapter.getRegisteredFragment(currentPosition);
        if (oldFrag == null) {
          return;
        } else {
          oldFrag.onPauseFragment();
        }
        changeMenuPosition(position);

        BaseRecipeFragment newFrag =
            (BaseRecipeFragment) mRecipePagerAdapter.getRegisteredFragment(position);
        newFrag.onResumeFragment();
        currentPosition = position;

        if (position == 0) {
          backNavBtn.setVisibility(View.INVISIBLE);
        } else if (position == recipe.getSteps().size() - 1) {
          forwardNavBtn.setVisibility(View.INVISIBLE);
          backNavBtn.setVisibility(View.VISIBLE);
        } else {
          backNavBtn.setVisibility(View.VISIBLE);
          forwardNavBtn.setVisibility(View.VISIBLE);
        }
        navRecipeStep.setText(
            (position + 1) + ". " + recipe.getSteps().get(position).getShortDescription());
      }
    });
    Toolbar toolbar = findViewById(R.id.toolbar_main);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("");
  }

  private void changeMenuPosition(int position) {
    Log.d(LOG_TAG, "changeMenuPositionwasCalled");
    navRecipeStep.setText(
        (position) + ". " + recipe.getSteps().get(position).getShortDescription());
  }

  @Override public void onIngredientClick(Ingredient ingredient) {

  }

  @OnClick(R.id.forward_nav) public void forwardFragment() {
    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
  }

  @OnClick(R.id.back_nav) public void backFragment() {
    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
  }

  public interface OnFragmentChangeState {
    public void onResumeFragment();

    public void onPauseFragment();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(LOG_TAG, "SaveInstanceStateCalled. CurrentPosition:" + currentPosition);
    outState.putInt(Constants.VIEW_PAGER_POSITION, currentPosition);

  }


}

