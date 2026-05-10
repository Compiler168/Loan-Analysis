package com.smartloan.ai.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.smartloan.ai.R;
import com.smartloan.ai.databinding.ActivityMainBinding;
import com.smartloan.ai.ui.auth.LoginActivity;
import com.smartloan.ai.utils.Constants;
import com.smartloan.ai.utils.TokenManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    private NavController navController;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply theme before super
        tokenManager = TokenManager.getInstance(this);
        applyTheme();

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        setupNavigation();
        setupDrawerHeader();
    }

    private void applyTheme() {
        String theme = tokenManager.getTheme();
        switch (theme) {
            case Constants.THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case Constants.THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    private void setupNavigation() {
        // Setup NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // Setup Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.toolbar,
                R.string.nav_dashboard, R.string.nav_settings);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Bottom Nav → NavController
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        // Drawer listener
        binding.navView.setNavigationItemSelectedListener(this);

        // Update toolbar title on destination change
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            binding.toolbar.setTitle(destination.getLabel());
        });
    }

    private void setupDrawerHeader() {
        View headerView = binding.navView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.tvNavName);
        TextView tvEmail = headerView.findViewById(R.id.tvNavEmail);
        TextView tvRole = headerView.findViewById(R.id.tvNavRole);

        tvName.setText(tokenManager.getUserName());
        tvEmail.setText(tokenManager.getUserEmail());
        tvRole.setText(tokenManager.getUserRole().toUpperCase());

        // Hide admin nav item if not admin
        if (!"admin".equals(tokenManager.getUserRole())) {
            binding.navView.getMenu().findItem(R.id.nav_admin).setVisible(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        binding.drawerLayout.closeDrawer(GravityCompat.START);

        if (id == R.id.nav_logout) {
            logout();
            return true;
        }

        // Navigate to destination
        try {
            navController.navigate(id);
            // Sync bottom nav if applicable
            if (id == R.id.nav_dashboard || id == R.id.nav_prediction ||
                    id == R.id.nav_chatbot || id == R.id.nav_analysis || id == R.id.nav_simulator) {
                binding.bottomNav.setSelectedItemId(id);
            }
        } catch (Exception e) {
            // Destination not found in nav graph, ignore
        }

        return true;
    }

    private void logout() {
        tokenManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
