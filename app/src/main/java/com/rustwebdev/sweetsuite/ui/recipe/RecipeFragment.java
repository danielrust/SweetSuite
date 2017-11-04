package com.rustwebdev.sweetsuite.ui.recipe;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.rustwebdev.sweetsuite.datasource.database.main.step.Step;

@SuppressWarnings("WeakerAccess") public class RecipeFragment extends BaseRecipeFragment
    implements RecipeActivity.OnFragmentChangeState, ExoPlayer.EventListener {
  public static final String LOG_TAG = RecipeFragment.class.getSimpleName();
  private SimpleExoPlayer mExoPlayer;
  private SimpleExoPlayerView mPlayerView;
  @BindView(R.id.step_name) TextView stepName;
  @BindView(R.id.placeholder_logo) ImageView placeholderImg;
  private Unbinder unbinder;

  public static RecipeFragment newInstance(Step dtoStep, int position) {
    Bundle args = new Bundle();
    RecipeFragment fragment = new RecipeFragment();
    args.putParcelable(Constants.RECIPE_FRAGMENT_PARCELABLE, dtoStep);
    args.putInt(Constants.FRAGMENT_TAG, position);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.recipe_fragment, container, false);
    unbinder = ButterKnife.bind(this, view);
    Step step = getArguments().getParcelable(Constants.STEP_PARCELABLE);
    mPlayerView = view.findViewById(R.id.playerView);
    assert step != null;
    stepName.setText(step.description);

    if (!step.videoURL.isEmpty()) {
      initializePlayer(Uri.parse(step.videoURL));
    } else {
      mPlayerView.setVisibility(View.INVISIBLE);
      placeholderImg.setVisibility(View.VISIBLE);
    }
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
