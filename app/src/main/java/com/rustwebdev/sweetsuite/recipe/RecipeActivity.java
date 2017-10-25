package com.rustwebdev.sweetsuite.recipe;

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
import com.rustwebdev.sweetsuite.BaseRecipeFragment;
import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.R;
import com.rustwebdev.sweetsuite.RecipePagerAdapter;
import com.rustwebdev.sweetsuite.data.Recipe;

@SuppressWarnings("WeakerAccess") public class RecipeActivity extends AppCompatActivity {

  public static final String LOG_TAG = RecipeActivity.class.getSimpleName();
  private Recipe recipe;
  @BindView(R.id.nav_recipe_step) TextView navRecipeStep;
  @BindView(R.id.nav_recipe_title) TextView navRecipeName;
  @BindView(R.id.pager) ViewPager mViewPager;
  @BindView(R.id.back_nav) FrameLayout backNavBtn;
  @BindView(R.id.forward_nav) FrameLayout forwardNavBtn;
  private RecipePagerAdapter mRecipePagerAdapter;
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
    navRecipeName.setText(recipe.getName());
    navRecipeStep.setText(this.getString(R.string.nav_recipe_tv_text, currentPosition,
        recipe.getSteps().get(currentPosition).getShortDescription()));
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
        } else if (position == recipe.getSteps().size() - 1) {
          forwardNavBtn.setVisibility(View.INVISIBLE);
          backNavBtn.setVisibility(View.VISIBLE);
        } else {
          backNavBtn.setVisibility(View.VISIBLE);
          forwardNavBtn.setVisibility(View.VISIBLE);
        }
        navRecipeStep.setText(getString(R.string.nav_recipe_tv_text, currentPosition,
            recipe.getSteps().get(position).getShortDescription()));
      }
    });
    Toolbar toolbar = findViewById(R.id.toolbar_main);
    setSupportActionBar(toolbar);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    getSupportActionBar().setTitle("");
  }

  private void changeMenuPosition(int position) {
    navRecipeStep.setText(getString(R.string.nav_recipe_tv_text, currentPosition,
        recipe.getSteps().get(position).getShortDescription()));
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
  }

  public interface OnFragmentChangeState {
    void onPauseFragment();
  }
}

