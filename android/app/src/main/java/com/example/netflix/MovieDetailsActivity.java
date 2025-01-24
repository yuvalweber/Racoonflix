/**
 * Activity to display the details of a selected movie.
 * This activity shows the movie's image, title, details, and provides options to play the movie trailer or close the activity.
 */
public class MovieDetailsActivity extends AppCompatActivity {

	/**
	 * Called when the activity is first created.
	 * Initializes the UI components and sets up the movie details.
	 *
	 * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_details);

		// Initialize UI components
		ImageView movieImageView = findViewById(R.id.movieImageView);
		TextView movieTitleTextView = findViewById(R.id.movieTitleTextView);
		TextView movieDetailsTextView = findViewById(R.id.movieDetailsTextView);
		ImageButton playMovieButton = findViewById(R.id.playMovieButton);
		ImageButton closeButton = findViewById(R.id.closeButton);

		// Initialize CategoryRepository to fetch category data
		CategoryRepository categoryRepository = new CategoryRepository(this);

		// Get the movie details passed from the previous activity
		Movie movie = (Movie) getIntent().getSerializableExtra("MOVIE_DETAILS");

		if (movie != null) {
			// Set the movie title
			movieTitleTextView.setText(movie.getTitle());

			// Fetch and translate categories
			categoryRepository.fetchCategories().observe(this, categories -> {
				if (categories != null) {
					// Map category IDs to category names
					List<String> categoryNames = movie.getCategory().stream()
							.map(categoryId -> categories.stream()
									.filter(category -> category.getId().equals(categoryId))
									.map(Category::getName)
									.findFirst()
									.orElse("Unknown"))
							.collect(Collectors.toList());

					// Join category names into a single string
					String categoriesString = String.join(", ", categoryNames);

					// Set the movie details text
					movieDetailsTextView.setText(String.format(
							"Year:\t%d\nDirector:\t%s\nDuration:\t%d mins\nCategories:\t%s",
							movie.getYear(),
							movie.getDirector(),
							movie.getDuration(),
							categoriesString
					));
				}
			});

			// Load the movie image using Glide
			Glide.with(this).load(movie.getImage()).into(movieImageView);

			// Set click listener to play the movie trailer
			playMovieButton.setOnClickListener(v -> {
				Intent playIntent = new Intent(MovieDetailsActivity.this, MoviePlayerActivity.class);
				playIntent.putExtra("TRAILER_URL", movie.getTrailer());
				startActivity(playIntent);
			});
		}

		// Set click listener to close the activity
		closeButton.setOnClickListener(v -> finish());
	}
}
