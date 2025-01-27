package com.example.netflix;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.netflix.fragments.CategoryManagementFragment;
import com.example.netflix.fragments.MovieManagementFragment;
import com.example.netflix.dao.TokenDao;
import com.example.netflix.database.AppDatabase;
import com.google.android.material.navigation.NavigationView;

public class ManagementActivity extends AppCompatActivity {

    private static final String TAG = "ManagementActivity";
    private DrawerLayout drawerLayout;
    private String selectedSection = null;
    private String selectedAction = null;

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

        // Get AppDatabase instance
        AppDatabase database = AppDatabase.getInstance(this);
        TokenDao tokenDao = database.tokenDao();

        // Setup Hamburger Icon
        ImageView hamburgerIcon = findViewById(R.id.hamburger_icon);
        hamburgerIcon.setOnClickListener(v -> {
            if (drawerLayout != null) {
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
}
