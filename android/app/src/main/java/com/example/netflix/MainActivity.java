/**
 * MainActivity class that serves as the entry point of the application.
 * It initializes the UI components and sets up the button click listeners.
 */
public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialize buttons
		Button signUpButton = findViewById(R.id.signUpButton);
		Button loginButton = findViewById(R.id.loginButton);

		// Set up button listeners
		signUpButton.setOnClickListener(v -> {
			// Navigate to RegisterActivity when sign-up button is clicked
			Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
			startActivity(intent);
		});

		loginButton.setOnClickListener(v -> {
			// Navigate to LoginActivity when login button is clicked
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
		});
	}
}
