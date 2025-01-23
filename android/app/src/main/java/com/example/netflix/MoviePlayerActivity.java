package com.example.netflix;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.netflix.viewmodels.MoviePlayerViewModel;

public class MoviePlayerActivity extends AppCompatActivity {
    private VideoView videoView;
    private MoviePlayerViewModel viewModel;
    private boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_player);

        videoView = findViewById(R.id.videoView);
        ImageButton returnButton = findViewById(R.id.returnButton);
        viewModel = new ViewModelProvider(this).get(MoviePlayerViewModel.class);

        viewModel.fetchVideoUrl();

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        viewModel.getVideoUrl().observe(this, videoUrl -> {
            if (videoUrl != null) {
                playVideo(videoUrl);
            }
        });


        returnButton.setOnClickListener(v -> finish()); // Go back to the previous page
    }

    private void playVideo(String url) {
        videoView.setVideoURI(Uri.parse(url));
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true); // Optional: Loop video
            videoView.start();
        });
        videoView.setOnCompletionListener(mp -> finish());
    }

    private void togglePlayback() {
        if (isPlaying) {
            videoView.pause();
        } else {
            videoView.start();
        }
        isPlaying = !isPlaying;
    }

    private void enterFullscreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void exitFullscreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.savePosition(videoView.getCurrentPosition());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getCurrentPosition().observe(this, position -> {
            if (position != null) {
                videoView.seekTo(position);
                videoView.start();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            enterFullscreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            exitFullscreen();
        }
    }
}

