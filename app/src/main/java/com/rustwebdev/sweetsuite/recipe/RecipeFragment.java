package com.rustwebdev.sweetsuite.recipe;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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
import com.rustwebdev.sweetsuite.BaseRecipeFragment;
import com.rustwebdev.sweetsuite.Constants;
import com.rustwebdev.sweetsuite.R;
import com.rustwebdev.sweetsuite.data.Step;

public class RecipeFragment extends BaseRecipeFragment
    implements RecipeActivity.OnFragmentChangeState, ExoPlayer.EventListener {
  public static final String LOG_TAG = RecipeFragment.class.getSimpleName();
  int fragNumber;
  Step step;
  private SimpleExoPlayer mExoPlayer;
  private SimpleExoPlayerView mPlayerView;
  private static MediaSessionCompat mMediaSession;
  private PlaybackStateCompat.Builder mStateBuilder;
  @BindView(R.id.step_name) TextView stepName;
  private Unbinder unbinder;

  public static RecipeFragment newInstance(Step step, int position) {
    Bundle args = new Bundle();
    RecipeFragment fragment = new RecipeFragment();
    args.putParcelable(Constants.RECIPE_FRAGMENT_PARCELABLE, step);
    args.putInt(Constants.FRAGMENT_TAG, position);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.recipe_fragment, container, false);
    unbinder = ButterKnife.bind(this, view);
    fragNumber = getArguments().getInt(Constants.FRAGMENT_TAG);
    step = getArguments().getParcelable(Constants.RECIPE_FRAGMENT_PARCELABLE);
    mPlayerView = view.findViewById(R.id.playerView);
    stepName.setText(step.getDescription());

    if (!step.getVideoURL().isEmpty()) {
      initializeMediaSession();
      initializePlayer(Uri.parse(step.getVideoURL()));
    } else {
      mPlayerView.setVisibility(View.INVISIBLE);
    }
    return view;
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
    mMediaSession.setCallback(new RecipeFragment.MySessionCallback());

    // Start the Media Session since the activity is active.
    mMediaSession.setActive(true);
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

  @Override public void onResumeFragment() {
    if (mExoPlayer != null) {
      mExoPlayer.setPlayWhenReady(true);
    }
  }

  @Override public void onPauseFragment() {
    if (mExoPlayer != null) {
      mExoPlayer.setPlayWhenReady(false);
    }
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    if (mExoPlayer != null) {
      releasePlayer();
    }
    unbinder.unbind();
    Log.d(LOG_TAG, "ViewDestroyed");
  }

  private void releasePlayer() {
    //mNotificationManager.cancelAll();
    mExoPlayer.stop();
    mExoPlayer.release();
    mExoPlayer = null;
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
}
