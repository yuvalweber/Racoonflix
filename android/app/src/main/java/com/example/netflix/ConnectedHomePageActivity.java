// ConnectedHomePageActivity is the main activity for the connected home page of the Netflix app.
public class ConnectedHomePageActivity extends AppCompatActivity {

	private static final String TAG = "ConnectedHomePage";
	private RecyclerView recyclerView;
	private EditText searchBar;
	private DrawerLayout drawerLayout;
	private MovieViewModel movieViewModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connected_home_page);

		// Initialize DrawerLayout for navigation drawer
		drawerLayout = findViewById(R.id.drawer_layout);

		// Get instance of the app database and token DAO
		AppDatabase database = AppDatabase.getInstance(this);
		TokenDao tokenDao = database.tokenDao();

		// Setup Hamburger Icon to open navigation drawer
		ImageView hamburgerIcon = findViewById(R.id.hamburger_icon);
		hamburgerIcon.setOnClickListener(v -> {
			if (drawerLayout != null) {
				drawerLayout.openDrawer(GravityCompat.START);
			}
		});

		// Initialize Search Bar for movie search
		searchBar = findViewById(R.id.search_bar);

		// Add TextWatcher to Search Bar for real-time search functionality
		searchBar.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// No action needed before text changes
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// Perform search when text length is more than 2 characters
				String query = s.toString().trim();
				if (s.length() > 2) {
					Log.d(TAG, "Real-time search query: " + query);
					fetchSearchedMovies(query);
				} else if (query.isEmpty()) {
					Log.d(TAG, "Search bar cleared. Returning to default movies.");
					observeMoviesByCategory();
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				// No action needed after text changes
			}
		});

		// Initialize RecyclerView for displaying movie categories
		recyclerView = findViewById(R.id.recycler_view_categories);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		// Initialize NavigationView for navigation drawer items
		NavigationView navigationView = findViewById(R.id.navigation_view);

		// Initialize ViewModel for movie data
		movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

		// Setup Navigation Item Selection Listener
		navigationView.setNavigationItemSelectedListener(item -> {
			if (item.getItemId() == R.id.nav_logout) {
				// Handle logout action
				tokenDao.clearTokens();
				Intent intent = new Intent(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				finish();
			} else if(item.getItemId() == R.id.nav_categories) {
				// Fetch all movies and categories
				Log.d(TAG, "Fetching all movies and categories.");
				getAllMoviesAndCategories();
			}
			return true;
		});

		// Observe and display default movies by category
		observeMoviesByCategory();
	}

	//Observes movies by category and updates the RecyclerView with the fetched data.
	private void observeMoviesByCategory() {
		movieViewModel.getMoviesByCategory().observe(this, categorizedMovies -> {
			if (categorizedMovies != null && !categorizedMovies.isEmpty()) {
				Log.d(TAG, "Movies fetched successfully: " + categorizedMovies);
				CategoryAdapter categoryAdapter = new CategoryAdapter(this, categorizedMovies);
				recyclerView.setAdapter(categoryAdapter);
			} else {
				Log.d(TAG, "No movies available to display.");
			}
		});
	}

	//Fetches movies based on the search query and updates the RecyclerView with the search results.
	private void fetchSearchedMovies(String query) {
		movieViewModel.searchMovies(query).observe(this, categorizedMovies -> {
			if (categorizedMovies != null && !categorizedMovies.isEmpty()) {
				Log.d(TAG, "Search results fetched successfully: " + categorizedMovies);
				CategoryAdapter categoryAdapter = new CategoryAdapter(this, categorizedMovies);
				recyclerView.setAdapter(categoryAdapter);
			} else {
				Log.d(TAG, "No search results found.");
			}
		});
	}

	/**
	 * Fetches all movies and categories and updates the RecyclerView with the fetched data.
	 * Closes the navigation drawer if it is open.
	 */
	private void getAllMoviesAndCategories() {
		movieViewModel.getAllMovies().observe(this, categorizedMovies -> {
			if (categorizedMovies != null && !categorizedMovies.isEmpty()) {
				Log.d(TAG, "Movies fetched successfully: " + categorizedMovies);
				CategoryAdapter categoryAdapter = new CategoryAdapter(this, categorizedMovies);
				recyclerView.setAdapter(categoryAdapter);
			} else {
				Log.d(TAG, "No movies available to display.");
			}

			// Close the navigation drawer if open
			if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
				drawerLayout.closeDrawer(GravityCompat.START);
			}
		});
	}
}
