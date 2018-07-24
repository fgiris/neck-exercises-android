package com.ceng.muhendis.muneckexercises;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.ceng.muhendis.muneckexercises.helpers.FirebaseDBHelper;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExerciseVideoActivity extends AppCompatActivity {
    SimpleExoPlayerView mExoPlayerView;
    SimpleExoPlayer mExoPlayer;
    FrameLayout mExoFrame;
    boolean isVisible=true;
    ConstraintLayout mConstraintLayout;
    private final String TAG = "ExerciseVideoActivity";
    FirebaseDBHelper mFirebaseDBHelper;
    long startTime,elapsedTime=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_video);

        mFirebaseDBHelper = new FirebaseDBHelper(getApplicationContext());

        mExoFrame = findViewById(R.id.mExoVideoFrame);
        Toolbar toolbar = findViewById(R.id.exDetailsVideoToolbar);
        mConstraintLayout = findViewById(R.id.mExDetailsVideoConstraintLayout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mExoPlayerView = findViewById(R.id.mExoPlayer);

        createExoPlayer();
        mExoPlayerView.setControllerVisibilityListener(new PlaybackControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {
                Log.d(TAG,"Visibility changed");
                toggleToolbar();
            }
        });

    }

    private void toggleToolbar()
    {
        if(isVisible)
        {
            getSupportActionBar().hide();
            isVisible=false;
        }
        else {
            getSupportActionBar().show();
            isVisible=true;
        }
    }

    private void createExoPlayer()
    {
        Context context = getApplicationContext();
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        mExoPlayer =
                ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
        mExoPlayerView.setPlayer(mExoPlayer);

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMtr = new DefaultBandwidthMeter();
        String videoLink = getIntent().getStringExtra(Keys.EX_VIDEO);

        //Uri mp4VideoUri = Uri.parse("https://s3.eu-central-1.amazonaws.com/vexrob/exercises/1008/1008_MIIWRuQtMv97GHYl.mp4");
        //Uri mp4VideoUri = Uri.parse("https://drive.google.com/uc?export=download&id=1q78B2B43uBvv_NiB5qBu6VGVmxwl28lF");
        Uri mp4VideoUri = Uri.parse(videoLink);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, "Marmara Egzersiz"), bandwidthMtr);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mp4VideoUri);
        mExoPlayer.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);
                Log.d(TAG,"PLAYER STATE CHANGED");
                Log.d(TAG,"Playback state: "+playbackState);
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                super.onPlaybackParametersChanged(playbackParameters);
                Log.d(TAG,"PLAYBACK PARAMETERS CHANGED");
            }

        });
        // Prepare the player with the source.
        mExoPlayer.prepare(videoSource);

    }

    private void pausePlayer(){
        mExoPlayer.setPlayWhenReady(false);
        mExoPlayer.getPlaybackState();
    }
    private void startPlayer(){
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.getPlaybackState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPlayer();
        startTime = System.currentTimeMillis();

    }

    @Override
    protected void onStart() {
        super.onStart();
        elapsedTime = 0;
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
        Keys.elapsedTime += System.currentTimeMillis() - startTime;
        //Log.d(TAG,"PAUSED - TIMER VALUE: "+(int)(elapsedTime));
        //mFirebaseDBHelper.insertUsageStatistics(this,(int)(elapsedTime/1000));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
