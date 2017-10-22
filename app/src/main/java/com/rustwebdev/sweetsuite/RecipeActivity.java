package com.rustwebdev.sweetsuite;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;

/**
 * Created by flanhelsinki on 10/21/17.
 */

public class RecipeActivity extends AppCompatActivity
    implements IngredientsAdapter.IngredientItemListener, ExoPlayer.EventListener {
  public static final String LOG_TAG = RecipeActivity.class.getSimpleName();
  RecyclerView ingredientRv;
  ArrayList<Ingredient> ingredients;
  private SimpleExoPlayer mExoPlayer;
  private SimpleExoPlayerView mPlayerView;
  private static MediaSessionCompat mMediaSession;
  private PlaybackStateCompat.Builder mStateBuilder;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe);
    ingredientRv = findViewById(R.id.ingredient_recyclerView);
    mPlayerView = findViewById(R.id.playerView);

    RecyclerView.LayoutManager mLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    ingredientRv.setLayoutManager(mLayoutManager);
    ingredients = new ArrayList<>();
    ingredients.add(new Ingredient("2", "tsp", "sugar"));
    ingredients.add(new Ingredient("4", "cups", "all-purpose flour"));
    ingredients.add(new Ingredient("1", "tsp", "salt"));
    ingredients.add(new Ingredient("4", "cups", "semisweet chocolate chips"));
    ingredients.add(new Ingredient("3", "tbsp", "vegetable oil"));
    ingredients.add(new Ingredient("1", "lb", "crawfish meat"));
    ingredients.add(new Ingredient("6", "slices", "rye bread"));
    ingredients.add(new Ingredient("3", "oz", "mayonnaise"));
    ingredients.add(new Ingredient("1", "tsp", "extra vigin olive oil"));
    IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(this, ingredients, this);
    ingredientRv.setAdapter(ingredientsAdapter);
    ingredientRv.setNestedScrollingEnabled(false);
    initializeMediaSession();

    initializePlayer(Uri.parse(
        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4"));
  }

  @Override public void onIngredientClick(Ingredient ingredient) {

  }

  private void initializeMediaSession() {

    // Create a MediaSessionCompat.
    mMediaSession = new MediaSessionCompat(this, LOG_TAG);

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

  @Override protected void onDestroy() {
    super.onDestroy();
    releasePlayer();
    mMediaSession.setActive(false);
  }

  private void initializePlayer(Uri mediaUri) {
    if (mExoPlayer == null) {
      // Create an instance of the ExoPlayer.
      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
      mPlayerView.setPlayer(mExoPlayer);

      // Set the ExoPlayer.EventListener to this activity.
      mExoPlayer.addListener(this);

      // Prepare the MediaSource.
      String userAgent = Util.getUserAgent(this, "ClassicalMusicQuiz");
      MediaSource mediaSource =
          new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(this, userAgent),
              new DefaultExtractorsFactory(), null, null);
      mExoPlayer.prepare(mediaSource);
      mExoPlayer.setPlayWhenReady(true);
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

  }

  @Override public void onPlayerError(ExoPlaybackException error) {

  }

  @Override public void onPositionDiscontinuity() {

  }
}

