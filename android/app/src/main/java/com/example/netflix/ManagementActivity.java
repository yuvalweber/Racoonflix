package com.example.netflix;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.netflix.fragments.CategoryManagementFragment;
import com.example.netflix.fragments.MovieManagementFragment;

public class ManagementActivity extends AppCompatActivity {

    private static final String TAG = "ManagementActivity";
    private String selectedSection = null;
    private String selectedAction = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);

        TextView title = findViewById(R.id.management_title);

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
