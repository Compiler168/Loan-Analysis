package com.smartloan.ai.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

        // Custom Bottom Nav Setup
        setupCustomBottomNav();

        // Drawer listener
        binding.navView.setNavigationItemSelectedListener(this);

        // Update toolbar and bottom nav on destination change
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_dashboard) {
                binding.toolbar.setVisibility(View.GONE);
            } else {
                binding.toolbar.setVisibility(View.VISIBLE);
                binding.toolbar.setTitle(destination.getLabel());
            }
            updateBottomNavUI(destination.getId());
        });
    }

    private void setupCustomBottomNav() {
        binding.customBottomNav.itemDashboard.setOnClickListener(v -> navController.navigate(R.id.nav_dashboard));
        binding.customBottomNav.itemPrediction.setOnClickListener(v -> navController.navigate(R.id.nav_prediction));
        binding.customBottomNav.itemChatbot.setOnClickListener(v -> navController.navigate(R.id.nav_chatbot));
        binding.customBottomNav.itemAnalysis.setOnClickListener(v -> navController.navigate(R.id.nav_analysis));
        binding.customBottomNav.itemSimulator.setOnClickListener(v -> navController.navigate(R.id.nav_simulator));
        binding.customBottomNav.itemProfile.setOnClickListener(v -> navController.navigate(R.id.nav_settings));
    }

    private void updateBottomNavUI(int destinationId) {
        // Reset all
        resetBottomNavItems();

        // Highlight selected
        if (destinationId == R.id.nav_dashboard) {
            setSelected(binding.customBottomNav.itemDashboard, binding.customBottomNav.ivDashboard, binding.customBottomNav.tvDashboard);
        } else if (destinationId == R.id.nav_prediction) {
            setSelected(binding.customBottomNav.itemPrediction, binding.customBottomNav.ivPrediction, binding.customBottomNav.tvPrediction);
        } else if (destinationId == R.id.nav_chatbot) {
            setSelected(binding.customBottomNav.itemChatbot, binding.customBottomNav.ivChatbot, binding.customBottomNav.tvChatbot);
        } else if (destinationId == R.id.nav_analysis) {
            setSelected(binding.customBottomNav.itemAnalysis, binding.customBottomNav.ivAnalysis, binding.customBottomNav.tvAnalysis);
        } else if (destinationId == R.id.nav_simulator) {
            setSelected(binding.customBottomNav.itemSimulator, binding.customBottomNav.ivSimulator, binding.customBottomNav.tvSimulator);
        } else if (destinationId == R.id.nav_settings) {
            setSelected(binding.customBottomNav.itemProfile, binding.customBottomNav.ivProfile, binding.customBottomNav.tvProfile);
        }
    }

    private void resetBottomNavItems() {
        int defaultColor = getResources().getColor(R.color.nav_icon_default, getTheme());
        
        // Reset backgrounds
        binding.customBottomNav.itemDashboard.setBackgroundResource(0);
        binding.customBottomNav.itemPrediction.setBackgroundResource(0);
        binding.customBottomNav.itemChatbot.setBackgroundResource(0);
        binding.customBottomNav.itemAnalysis.setBackgroundResource(0);
        binding.customBottomNav.itemSimulator.setBackgroundResource(0);
        binding.customBottomNav.itemProfile.setBackgroundResource(0);

        // Reset colors
        binding.customBottomNav.ivDashboard.setColorFilter(defaultColor);
        binding.customBottomNav.tvDashboard.setTextColor(defaultColor);
        
        binding.customBottomNav.ivPrediction.setColorFilter(defaultColor);
        binding.customBottomNav.tvPrediction.setTextColor(defaultColor);
        
        binding.customBottomNav.ivChatbot.setColorFilter(defaultColor);
        binding.customBottomNav.tvChatbot.setTextColor(defaultColor);
        
        binding.customBottomNav.ivAnalysis.setColorFilter(defaultColor);
        binding.customBottomNav.tvAnalysis.setTextColor(defaultColor);
        
        binding.customBottomNav.ivSimulator.setColorFilter(defaultColor);
        binding.customBottomNav.tvSimulator.setTextColor(defaultColor);
        
        binding.customBottomNav.ivProfile.setColorFilter(defaultColor);
        binding.customBottomNav.tvProfile.setTextColor(defaultColor);
    }

    private void setSelected(LinearLayout layout, android.widget.ImageView iv, TextView tv) {
        int selectedColor = getResources().getColor(R.color.nav_icon_selected, getTheme());
        layout.setBackgroundResource(R.drawable.bg_nav_item_selected);
        iv.setColorFilter(selectedColor);
        tv.setTextColor(selectedColor);
        // Add a slight scale animation for premium feel
        iv.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).withEndAction(() ->
            iv.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        ).start();
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
