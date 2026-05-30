package com.smartloan.ai;

import android.app.Application;

import com.google.android.material.color.DynamicColors;
import com.smartloan.ai.data.api.ApiClient;
import com.smartloan.ai.utils.Constants;
import com.smartloan.ai.utils.TokenManager;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * SmartLoan AI+ Application class.
 * Initializes API client and applies Material Dynamic Colors.
 */
public class SmartLoanApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Retrofit API client
        ApiClient.init(this);
        // Apply Material Dynamic Colors if available
        DynamicColors.applyToActivitiesIfAvailable(this);

        // Apply saved theme
        applySavedTheme();
    }

    private void applySavedTheme() {
        String theme = TokenManager.getInstance(this).getTheme();
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
}
