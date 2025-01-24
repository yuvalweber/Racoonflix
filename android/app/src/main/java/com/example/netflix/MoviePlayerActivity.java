/**
 * Activity for playing movie trailers.
 * Handles video playback, fullscreen mode, and saving/restoring playback position.
 */
public class MoviePlayerActivity extends AppCompatActivity {
	private VideoView videoView;
	private MoviePlayerViewModel viewModel;
	private boolean isPlaying = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_player);

		// Initialize VideoView and ViewModel
		videoView = findViewById(R.id.videoView);
		ImageButton returnButton = findViewById(R.id.returnButton);
		viewModel = new ViewModelProvider(this).get(MoviePlayerViewModel.class);

		// Get trailer URL from intent
		String trailerUrl = getIntent().getStringExtra("TRAILER_URL");

		// Fetch video URL using ViewModel
		viewModel.fetchVideoUrl(trailerUrl);

		// Set up media controller for video playback controls
		MediaController mediaController = new MediaController(this);
		videoView.setMediaController(mediaController);
		mediaController.setAnchorView(videoView);

		// Observe video URL and play video when available
		viewModel.getVideoUrl().observe(this, videoUrl -> {
			if (videoUrl != null) {
				playVideo(videoUrl);
			}
		});

		// Set return button to finish activity and go back to the previous page
		returnButton.setOnClickListener(v -> finish());
	}

	//Plays the video from the given URL.
	private void playVideo(String url) {
		videoView.setVideoURI(Uri.parse(url));
		videoView.setOnPreparedListener(mp -> {
			mp.setLooping(true); // Optional: Loop video
			videoView.start();
		});
		videoView.setOnCompletionListener(mp -> finish());
	}

	//Toggles video playback between play and pause.
	private void togglePlayback() {
		if (isPlaying) {
			videoView.pause();
		} else {
			videoView.start();
		}
		isPlaying = !isPlaying;
	}

	//Enters fullscreen mode.
	private void enterFullscreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	//Exits fullscreen mode.
	private void exitFullscreen() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Save current playback position
		viewModel.savePosition(videoView.getCurrentPosition());
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Restore playback position and start video
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

		// Handle orientation changes for fullscreen mode
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			enterFullscreen();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			exitFullscreen();
		}
	}
}

