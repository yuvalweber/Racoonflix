package com.example.netflix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.netflix.api.UserApiService;
import com.example.netflix.entities.TokenEntity;
import com.example.netflix.fragments.CategoryManagementFragment;
import com.example.netflix.fragments.MovieManagementFragment;
import com.example.netflix.dao.TokenDao;
import com.example.netflix.database.AppDatabase;
import com.example.netflix.models.User;
import com.example.netflix.network.RetrofitInstance;
import com.example.netflix.repository.UserRepository;
import com.example.netflix.viewmodels.UserViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.atomic.AtomicReference;

public class ManagementActivity extends AppCompatActivity {

    private static final String TAG = "ManagementActivity";
    private DrawerLayout drawerLayout;
    private String selectedSection = null;
    private String selectedAction = null;

    private UserViewModel userViewModel;

    private static final String PREFS_NAME = "theme_prefs";
    private static final String PREF_DARK_MODE = "is_dark_mode";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        // Apply the saved theme preference
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(PREF_DARK_MODE, false);
        AppCompatDelegate.setDefaultNightMode(
                isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        // Setup DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        userViewModel = new UserViewModel(new UserRepository(RetrofitInstance.getInstance().create(UserApiService.class), AppDatabase.getInstance(this).tokenDao()));

        // Get AppDatabase instance
        AppDatabase database = AppDatabase.getInstance(this);
        TokenDao tokenDao = database.tokenDao();

        // Setup Hamburger Icon
        ImageView hamburgerIcon = findViewById(R.id.hamburger_icon);
        hamburgerIcon.setOnClickListener(v -> {
            if (drawerLayout != null) {
                changeUserNameAndPhoto();
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Setup NavigationView
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                tokenDao.clearTokens();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.nav_categories) {
                Log.d(TAG, "Navigating to Categories.");
                Intent intent = new Intent(this, ConnectedHomePageActivity.class);
                intent.putExtra("CATEGORY_NAVIGATION", true);
                startActivity(intent);
                // Implement category navigation logic here
            } else if (item.getItemId() == R.id.nav_home) {
                Log.d(TAG, "Navigating to Home.");
                Intent intent = new Intent(this, ConnectedHomePageActivity.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.nav_management) {
                Log.d(TAG, "Already in Management.");
            } else if (item.getItemId() == R.id.action_toggle_theme) {
                // Toggle the theme
                SharedPreferences prefs2 = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                boolean isDarkMode2 = prefs2.getBoolean(PREF_DARK_MODE, false);

                // Save the new theme preference
                SharedPreferences.Editor editor = prefs2.edit();
                editor.putBoolean(PREF_DARK_MODE, !isDarkMode2);
                editor.apply();

                // Apply the new theme
                AppCompatDelegate.setDefaultNightMode(
                        !isDarkMode2 ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                );
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Section Selector
        RadioGroup sectionSelector = findViewById(R.id.section_selector);
        sectionSelector.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = findViewById(checkedId);
            selectedSection = selected.getText().toString().toLowerCase();
            Log.d(TAG, "Selected Section: " + selectedSection);
            clearForm();
        });

        // Action Selector
        RadioGroup actionSelector = findViewById(R.id.action_selector);
        actionSelector.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selected = findViewById(checkedId);
            selectedAction = selected.getText().toString();
            Log.d(TAG, "Selected Action: " + selectedAction);
            loadForm();
        });
    }

    private void clearForm() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.form_container, new Fragment());
        transaction.commit();
    }

    private void loadForm() {
        if (selectedSection == null || selectedAction == null) {
            return;
        }

        Fragment fragment;
        if ("categories".equals(selectedSection)) {
            fragment = CategoryManagementFragment.newInstance(selectedAction);
        } else {
            fragment = MovieManagementFragment.newInstance(selectedAction);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.form_container, fragment);
        transaction.commit();
    }

    private void changeUserNameAndPhoto() {
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Get the header view of the navigation drawer
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.user_name);
        ImageView profileImageView = headerView.findViewById(R.id.user_image);

        // Fetch the token to get user information
        AtomicReference<TokenEntity> token = new AtomicReference<>();
        Thread thread = new Thread(() -> {
            token.set(userViewModel.getTokenData());
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "Thread interrupted while fetching token data", e);
        }
        if (token.get() != null) {
            String userId = token.get().getUserId();
            String authToken = token.get().getToken();

            userViewModel.getUserById(userId, authToken, new UserRepository.UserCallback() {
                @Override
                public void onSuccess(Object data) {
                    User user = (User) data;

                    // Update the UI with the fetched user data
                    runOnUiThread(() -> {
                        usernameTextView.setText(user.getUserName());
                        Glide.with(ManagementActivity.this)
                                .load(user.getProfilePicture())
                                .placeholder(R.drawable.ic_profile_placeholder) // Fallback icon
                                .circleCrop()
                                .into(profileImageView);
                    });
                }

                @Override
                public void onError(int statusCode, String errorMessage) {
                    Log.e(TAG, "Failed to fetch user info: " + errorMessage);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "Error fetching user info", t);
                }
            });
        } else {
            Log.e(TAG, "No token available to fetch user info.");
        }
    }
}
