package com.smartloan.ai;

import android.app.Application;

import com.google.android.material.color.DynamicColors;
import com.smartloan.ai.data.api.ApiClient;

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
    }
}
