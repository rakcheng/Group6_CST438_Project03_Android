package com.groupsix.project3_cst438;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 *  Main (and only activity in project) Used Android Jetpack Navigation Component
 *  Useful resource : https://developer.android.com/codelabs/android-navigation#0
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigationAndFragments(savedInstanceState);
    }

    private void setupNavigationAndFragments(Bundle savedInstanceState) {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        assert navHostFragment != null;

        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // TODO: Remember to update with all current fragments
        // This updates the title to current fragment's title
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.home_fragment, R.id.createStoryFragment, R.id.userProfileFragment, R.id.viewStoriesFragment, R.id.viewSingleStoryFragment).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }
}