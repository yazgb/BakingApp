package com.android.yaz.bakingtime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.yaz.bakingtime.model.Recipe;
import com.android.yaz.bakingtime.model.RecipeStep;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = RecipeStepDetailFragment.class.getSimpleName();

    public static final String STEPS_ARRAY = "steps_array";
    public static final String STEP_INDEX = "step_index";
    public static final String VIDEO_URL = "video_url";
    public static final String STEP_DESCRIPTION = "step_description";
    public static final String PLAYBACK_POSITION = "playback_position";

    @BindView(R.id.playerView) PlayerView mPlayerView;
    @BindView(R.id.recipe_step_description_tv) TextView mRecipeDescriptionTextView;
    @BindView(R.id.video_placeholder_iv) ImageView mVideoPlaceholderImageView;
    @BindView(R.id.horizontal_divider) Guideline mHorizontalDivider;
    @BindView(R.id.back_button) Button mBackButton;
    @BindView(R.id.next_button) Button mNextButton;

    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private RecipeStep[] mStepsArray;
    private int mRecipeStepIndex;
    private String mVideoURL;
    private String mStepDescription;
    private long mPlaybackPosition = -1;

    private Context mContext;
    private boolean mTwoPane;

    public RecipeStepDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "ON_CREATE_VIEW");
        mContext = getContext();

        if(savedInstanceState != null) {
            Log.d(TAG, "savedInstanceState NO ES NULL");
            mVideoURL = savedInstanceState.getString(VIDEO_URL);
            mStepDescription = savedInstanceState.getString(STEP_DESCRIPTION);
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION);
            mRecipeStepIndex = savedInstanceState.getInt(STEP_INDEX);
            mStepsArray = (RecipeStep[]) savedInstanceState.getParcelableArray(STEPS_ARRAY);
        } else {
            Log.d(TAG, "savedInstanceState NULL, SET POSITION -1");
            mPlaybackPosition = -1;
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        if(getArguments() != null) {
            Bundle b = getArguments();
            mTwoPane = b.getBoolean(DetailActivity.TWO_PANE);
        } else
            mTwoPane = false;

        setupUI();

        return rootView;
    }

    private void setupUI() {

        if(mVideoURL!=null && !mVideoURL.isEmpty()) {

            mVideoPlaceholderImageView.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
            initializedMediaSession();
            initializePlayer(mVideoURL);
        }
        else {

            mPlayerView.setVisibility(View.GONE);
            mVideoPlaceholderImageView.setVisibility(View.VISIBLE);
            mVideoPlaceholderImageView.setImageResource(R.drawable.ic_orange_cake);
        }

        if(mStepDescription!=null && !mStepDescription.isEmpty())
            mRecipeDescriptionTextView.setText(mStepDescription);
        else
            mRecipeDescriptionTextView.setText(R.string.no_step_description);

        if(mTwoPane) {
            hideNextBackButtons();
        } else {
            mBackButton.setOnClickListener(this);
            mNextButton.setOnClickListener(this);

            if(mRecipeStepIndex == 0)
                mBackButton.setVisibility(View.GONE);

            if(mRecipeStepIndex == mStepsArray.length -1 )
                mNextButton.setVisibility(View.GONE);

            if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setupFullScreen();
            }
        }
    }

    private void setVideoDefaultPosition() {
        Log.d(TAG,"setVideoDefaultPosition()");
        if(mExoPlayer != null)
            mExoPlayer.seekToDefaultPosition();
    }

    private void hideNextBackButtons() {
        mBackButton.setVisibility(View.GONE);
        mNextButton.setVisibility(View.GONE);
    }

    private void setupFullScreen() {

        Log.d(TAG, "LANDSCAPE");

        if(mVideoURL!=null && !mVideoURL.isEmpty()) {
            mPlayerView.setVisibility(View.VISIBLE);
            mVideoPlaceholderImageView.setVisibility(View.GONE);
            mHorizontalDivider.setVisibility(View.GONE);
            mRecipeDescriptionTextView.setVisibility(View.GONE);
            hideNextBackButtons();

            mPlayerView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
            ((ViewGroup) mPlayerView.getParent()).setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else
        {
            hideNextBackButtons();
        }
    }

    public void setStepsArray(RecipeStep[] stepsArray) {
        mStepsArray = stepsArray;
    }

    public void setRecipeStepIndex(int recipeStepIndex) {
        mRecipeStepIndex = recipeStepIndex;
    }

    public void setVideoURL(String videoURL) {
        mVideoURL = videoURL;
    }

    public void setStepDescription(String stepDescription) {
        mStepDescription = stepDescription;
    }

    public void setPlaybackPosition(long mPlaybackPosition) {
        this.mPlaybackPosition = mPlaybackPosition;
    }

    private void initializedMediaSession() {
        Log.d(TAG,"initializedMediaSession");
        mMediaSession = new MediaSessionCompat(mContext, TAG);
        mMediaSession.setFlags( MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_REWIND |
                        PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_FAST_FORWARD);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MyMediaSessionCallback());
        mMediaSession.setActive(true);
    }

    private void initializePlayer(String videoURL) {

        if(mExoPlayer == null) {
            Log.d(TAG,"InitializePlayer");
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mPlayerView.setPlayer(mExoPlayer);

            DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                    Util.getUserAgent(getContext(), mContext.getPackageName()), defaultBandwidthMeter);

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoURL));

            Log.d(TAG,"Playback Position: " + mPlaybackPosition);
            if(mPlaybackPosition > -1) {
                Log.d(TAG,"Resume playbackPosition " + mPlaybackPosition);
                mExoPlayer.seekTo(mPlaybackPosition);
                mExoPlayer.prepare(mediaSource,false,true);
            } else {
                Log.d(TAG,"Do Not resume playbackPosition ");
                mExoPlayer.prepare(mediaSource);
            }
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.addListener(eventListener);
        }

    }

    private void releasePlayer() {
        if(mExoPlayer != null) {
            Log.d(TAG, "Release ExoPLAYER");
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            Log.d(TAG, "Current position: " + mPlaybackPosition);
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
            mMediaSession.setActive(false);
        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "ON_PAUSE");
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "ON_SAVE_INSTANCE_STATE RecipeStepDetailFragment");
        outState.putString(VIDEO_URL, mVideoURL);
        outState.putString(STEP_DESCRIPTION, mStepDescription);
        outState.putLong(PLAYBACK_POSITION, mPlaybackPosition);
        outState.putInt(STEP_INDEX, mRecipeStepIndex);
        outState.putParcelableArray(STEPS_ARRAY, mStepsArray);
    }

    Player.EventListener eventListener = new Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if((playbackState == Player.STATE_READY) && playWhenReady) {
                Log.d(TAG, "onPlayerStateChanged: PLAYING");
                mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
            } else if(playbackState == Player.STATE_READY) {
                Log.d(TAG, "onPlayerStateChanged: PAUSED");
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 0f);
            }
            mMediaSession.setPlaybackState(mStateBuilder.build());
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    };

    @Override
    public void onClick(View view) {
        Button buttonPressed = (Button) view;

        Log.d(TAG, "OnClick Index actual: " + mRecipeStepIndex);

        if(buttonPressed.getId() == R.id.next_button) {
            if(mRecipeStepIndex + 1 < mStepsArray.length) {
                Log.d(TAG, "CLICK NEXT BUTTON");
                mRecipeStepIndex++;
                setVideoDefaultPosition();
                ((RecipeStepDetailActivity) getActivity()).replaceRecipeStep(mStepsArray, mRecipeStepIndex);
            }

        } else if(buttonPressed.getId() == R.id.back_button) {
            if(mRecipeStepIndex - 1 >= 0 ) {
                Log.d(TAG, "CLICK BACK BUTTON");
                mRecipeStepIndex--;
                setVideoDefaultPosition();
                ((RecipeStepDetailActivity) getActivity()).replaceRecipeStep(mStepsArray, mRecipeStepIndex);
            }
        }
    }

    private class MyMediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}
