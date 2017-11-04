package com.rustwebdev.sweetsuite.ui.recipe;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.R;
import com.rustwebdev.sweetsuite.datasource.database.main.ingredient.Ingredient;
import com.rustwebdev.sweetsuite.datasource.database.main.recipe.Recipe;
import com.rustwebdev.sweetsuite.datasource.database.main.step.Step;
import java.util.ArrayList;

@SuppressWarnings("WeakerAccess") public class RecipeBaseFragment extends BaseRecipeFragment
    implements ExoPlayer.EventListener, RecipeActivity.OnFragmentChangeState {
  public static final String LOG_TAG = RecipeActivity.class.getSimpleName();
  private SimpleExoPlayer mExoPlayer;
  private SimpleExoPlayerView mPlayerView;
  private Step step;
  private ArrayList<Ingredient> ingredients;
  @BindView(R.id.recipe_name) TextView recipeName;
  @BindView(R.id.servings_tv) TextView servingsAmount;
  private Unbinder unbinder;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.recipe_base_fragment, container, false);
    unbinder = ButterKnife.bind(this, view);
    Recipe recipe = getArguments().getParcelable(Constants.RECIPE_PARCELABLE);
    ingredients = getArguments().getParcelableArrayList(Constants.INGREDIENTS_PARCELABLE);
    step = getArguments().getParcelable(Constants.STEP_PARCELABLE);
    assert recipe != null;
    recipeName.setText(recipe.name);
    servingsAmount.setText(getActivity().getString(R.string.servings_amount_tv, recipe.servings));
    RecyclerView ingredientRv = view.findViewById(R.id.ingredient_recyclerView);
    mPlayerView = view.findViewById(R.id.playerView);
    //noinspection ConstantConditions
    ((RecipeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    //noinspection ConstantConditions
    ((RecipeActivity) getActivity()).getSupportActionBar().setTitle("");

    RecyclerView.LayoutManager mLayoutManager =
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    ingredientRv.setLayoutManager(mLayoutManager);

    IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getActivity(), ingredients);
    ingredientRv.setAdapter(ingredientsAdapter);
    ingredientRv.setNestedScrollingEnabled(false);

    initializePlayer(Uri.parse(step.videoURL));
    return view;
  }

  private void initializePlayer(Uri mediaUri) {
    if (mExoPlayer == null) {
      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
      mPlayerView.setPlayer(mExoPlayer);
      mExoPlayer.addListener(this);
      String userAgent = Util.getUserAgent(getActivity(), "SweetSuite");
      MediaSource mediaSource =
          new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(), userAgent),
              new DefaultExtractorsFactory(), null, null);
      mExoPlayer.prepare(mediaSource);
      mPlayerView.hideController();
    }
  }

  private void releasePlayer() {
    mExoPlayer.stop();
    mExoPlayer.release();
    mExoPlayer = null;
  }

  @Override public void onPause() {
    super.onPause();
    if (mExoPlayer != null) {
      mExoPlayer.setPlayWhenReady(false);
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
    releasePlayer();
  }

  public static Fragment newInstance(Step step, Recipe recipe, int position,
      ArrayList<Ingredient> ingredients) {
    RecipeBaseFragment recipeBaseFragment = new RecipeBaseFragment();
    Bundle args = new Bundle();
    args.putParcelable(Constants.STEP_PARCELABLE, step);
    args.putParcelable(Constants.RECIPE_PARCELABLE, recipe);
    args.putParcelableArrayList(Constants.INGREDIENTS_PARCELABLE, ingredients);
    args.putInt(Constants.FRAGMENT_TAG, position);
    recipeBaseFragment.setArguments(args);
    return recipeBaseFragment;
  }

  @Override public void onPauseFragment() {
    if (mExoPlayer != null) {
      mExoPlayer.setPlayWhenReady(false);
    }
  }

  @Override public void onTimelineChanged(Timeline timeline, Object manifest) {

  }

  @Override
  public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

  }

  @Override public void onLoadingChanged(boolean isLoading) {

  }

  @Override public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

  }

  @Override public void onPlayerError(ExoPlaybackException error) {

  }

  @Override public void onPositionDiscontinuity() {

  }
}

