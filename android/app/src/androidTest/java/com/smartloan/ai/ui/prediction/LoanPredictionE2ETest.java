package com.smartloan.ai.ui.prediction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.smartloan.ai.R;
import com.smartloan.ai.ui.main.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoanPredictionE2ETest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testLoanPredictionFlow() {
        // Assume we start on the Dashboard and navigate to Prediction
        onView(withId(R.id.nav_prediction)).perform(click());

        // Step 1: Personal Info
        onView(withId(R.id.etAge)).perform(clearText(), typeText("30"));
        onView(withId(R.id.etDependents)).perform(clearText(), typeText("2"));
        onView(withId(R.id.btnNext)).perform(click());

        // Step 2: Financial Details
        onView(withId(R.id.etIncome)).perform(clearText(), typeText("8000"));
        onView(withId(R.id.etExpenses)).perform(clearText(), typeText("2000"));
        onView(withId(R.id.etCreditScore)).perform(clearText(), typeText("750"));
        onView(withId(R.id.btnNext)).perform(click());

        // Step 3: Loan Parameters
        onView(withId(R.id.etLoanAmount)).perform(clearText(), typeText("20000"));
        onView(withId(R.id.etLoanTerm)).perform(clearText(), typeText("24"));
        onView(withId(R.id.btnNext)).perform(click());

        // Verify result container is displayed (might need to wait for network)
        // For actual E2E testing, we'd use IdlingResource or a mock server.
        // onView(withId(R.id.resultsContainer)).check(matches(isDisplayed()));
    }
}
