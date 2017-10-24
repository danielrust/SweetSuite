package com.rustwebdev.sweetsuite;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.rustwebdev.sweetsuite.data.Ingredient;
import com.rustwebdev.sweetsuite.data.Recipe;
import com.rustwebdev.sweetsuite.data.Step;
import com.rustwebdev.sweetsuite.recipe.RecipeActivity;
import java.util.ArrayList;

public class RecipeBaseFragment extends BaseRecipeFragment
    implements IngredientsAdapter.IngredientItemListener, ExoPlayer.EventListener,
    RecipeActivity.OnFragmentChangeState {
  public static final String LOG_TAG = RecipeActivity.class.getSimpleName();
  RecyclerView ingredientRv;
  ArrayList<Ingredient> ingredients;
  private SimpleExoPlayer mExoPlayer;
  private SimpleExoPlayerView mPlayerView;
  private static MediaSessionCompat mMediaSession;
  private PlaybackStateCompat.Builder mStateBuilder;
  private Recipe recipe;
  @BindView(R.id.recipe_name) TextView recipeName;
  private Unbinder unbinder;
  public int fragNumber;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_recipe1, container, false);
    unbinder = ButterKnife.bind(this, view);
    recipe = getArguments().getParcelable(Constants.RECIPE_PARCELABLE);
    fragNumber = getArguments().getInt(Constants.FRAGMENT_TAG);
    //recipeName.setText(recipe.getName());
    recipeName.setText(recipe.getName());
    ingredientRv = view.findViewById(R.id.ingredient_recyclerView);
    mPlayerView = view.findViewById(R.id.playerView);

    ((RecipeActivity) getActivity()).getSupportActionBar().setTitle("");
    ((RecipeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    RecyclerView.LayoutManager mLayoutManager =
        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    ingredientRv.setLayoutManager(mLayoutManager);
    ingredients = recipe.getIngredients();

    IngredientsAdapter ingredientsAdapter =
        new IngredientsAdapter(getActivity(), ingredients, this);
    ingredientRv.setAdapter(ingredientsAdapter);
    ingredientRv.setNestedScrollingEnabled(false);
    initializeMediaSession();

    initializePlayer(Uri.parse(recipe.getSteps().get(0).getVideoURL()));
    return view;
  }

  @Override public void onIngredientClick(Ingredient ingredient) {

  }

  private void initializeMediaSession() {

    // Create a MediaSessionCompat.
    mMediaSession = new MediaSessionCompat(getActivity(), LOG_TAG);

    // Enable callbacks from MediaButtons and TransportControls.
    mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
        | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

    // Do not let MediaButtons restart the player when the app is not visible.
    mMediaSession.setMediaButtonReceiver(null);

    // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
    mStateBuilder = new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY
        | PlaybackStateCompat.ACTION_PAUSE
        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        | PlaybackStateCompat.ACTION_PLAY_PAUSE);

    mMediaSession.setPlaybackState(mStateBuilder.build());

    // MySessionCallback has methods that handle callbacks from a media controller.
    mMediaSession.setCallback(new MySessionCallback());

    // Start the Media Session since the activity is active.
    mMediaSession.setActive(true);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
    releasePlayer();
    Log.d(LOG_TAG, "ViewDestroyed");
  }

  private void initializePlayer(Uri mediaUri) {
    if (mExoPlayer == null) {
      // Create an instance of the ExoPlayer.
      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
      mPlayerView.setPlayer(mExoPlayer);

      // Set the ExoPlayer.EventListener to this activity.
      mExoPlayer.addListener(this);

      // Prepare the MediaSource.
      String userAgent = Util.getUserAgent(getActivity(), "ClassicalMusicQuiz");
      MediaSource mediaSource =
          new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(), userAgent),
              new DefaultExtractorsFactory(), null, null);
      mExoPlayer.prepare(mediaSource);
      mExoPlayer.setPlayWhenReady(true);
      mPlayerView.hideController();

    }
  }

  /**
   * Release ExoPlayer.
   */
  private void releasePlayer() {
    //mNotificationManager.cancelAll();
    mExoPlayer.stop();
    mExoPlayer.release();
    mExoPlayer = null;
  }

  public static Fragment newInstance(Step step, Recipe recipe, int position) {
    RecipeBaseFragment recipeBaseFragment = new RecipeBaseFragment();
    Bundle args = new Bundle();
    args.putParcelable(Constants.STEP_PARCELABLE, step);
    args.putParcelable(Constants.RECIPE_PARCELABLE, recipe);
    args.putInt(Constants.FRAGMENT_TAG, position);
    recipeBaseFragment.setArguments(args);
    return recipeBaseFragment;
  }

  @Override public void onResumeFragment() {
    if (mExoPlayer != null) {
      mExoPlayer.setPlayWhenReady(true);
    }
  }

  @Override public void onPauseFragment() {
    mExoPlayer.setPlayWhenReady(false);
  }

  private class MySessionCallback extends MediaSessionCompat.Callback {
    @Override public void onPlay() {
      mExoPlayer.setPlayWhenReady(true);
    }

    @Override public void onPause() {
      mExoPlayer.setPlayWhenReady(false);
    }

    @Override public void onSkipToPrevious() {
      mExoPlayer.seekTo(0);
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
    if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
      mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(),
          1f);
    } else if ((playbackState == ExoPlayer.STATE_READY)) {
      mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
    }
    mMediaSession.setPlaybackState(mStateBuilder.build());
  }

  @Override public void onPlayerError(ExoPlaybackException error) {

  }

  @Override public void onPositionDiscontinuity() {

  }
}

