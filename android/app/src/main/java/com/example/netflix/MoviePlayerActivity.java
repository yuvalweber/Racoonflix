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
	// Declare the variables
    private VideoView videoView;
    private MoviePlayerViewModel viewModel;
    private boolean isPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		// Set the layout of the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_player);

        videoView = findViewById(R.id.videoView); // Get the video view
        ImageButton returnButton = findViewById(R.id.returnButton); // Get the return button
        viewModel = new ViewModelProvider(this).get(MoviePlayerViewModel.class); // Get the view model

		// Get the trailer URL from the intent
        String trailerUrl = getIntent().getStringExtra("TRAILER_URL");

        viewModel.fetchVideoUrl(trailerUrl);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

		// Set the video view to be clickable
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
			// Start the video
            mp.setLooping(true);
            videoView.start();
        });
        videoView.setOnCompletionListener(mp -> finish());
    }

    private void togglePlayback() {
		// Pause or play the video
        if (isPlaying) {
            videoView.pause();
        } else {
            videoView.start();
        }
        isPlaying = !isPlaying;
    }

    private void enterFullscreen() {
		// Set the fullscreen flag
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void exitFullscreen() {
		// Clear the fullscreen flag
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
		// Get the current position of the video
        viewModel.getCurrentPosition().observe(this, position -> {
            if (position != null) {
                videoView.seekTo(position);
                videoView.start();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
		// Check the orientation of the screen
        super.onConfigurationChanged(newConfig);
		// If the screen is in landscape mode, enter fullscreen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            enterFullscreen();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            exitFullscreen();
        }
    }
}

