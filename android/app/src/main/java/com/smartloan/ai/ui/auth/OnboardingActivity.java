package com.smartloan.ai.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.smartloan.ai.R;
import com.smartloan.ai.databinding.ActivityOnboardingBinding;
import com.smartloan.ai.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ActivityOnboardingBinding binding;
    private TokenManager tokenManager;
    private List<OnboardingItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tokenManager = TokenManager.getInstance(this);

        setupViewPager();
        setupListeners();
    }

    private void setupViewPager() {
        items = new ArrayList<>();
        items.add(new OnboardingItem(
                getString(R.string.onboarding_title_1),
                getString(R.string.onboarding_desc_1),
                R.drawable.onboarding_1
        ));
        items.add(new OnboardingItem(
                getString(R.string.onboarding_title_2),
                getString(R.string.onboarding_desc_2),
                R.drawable.onboarding_2
        ));
        items.add(new OnboardingItem(
                getString(R.string.onboarding_title_3),
                getString(R.string.onboarding_desc_3),
                R.drawable.onboarding_3
        ));

        OnboardingAdapter adapter = new OnboardingAdapter(items);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(3);

        // Premium Page Transformer (Parallax + Fade + Scale)
        binding.viewPager.setPageTransformer((page, position) -> {
            float absPos = Math.abs(position);
            page.setAlpha(1.0f - absPos * 0.3f);
            
            View title = page.findViewById(R.id.tvTitle);
            View desc = page.findViewById(R.id.tvDescription);
            View card = page.findViewById(R.id.cvImage);
            
            if (position < -1) {
                page.setAlpha(0f);
            } else if (position <= 1) {
                if (title != null) title.setTranslationX(position * 600);
                if (desc != null) desc.setTranslationX(position * 400);
                if (card != null) {
                    card.setScaleX(1.0f - absPos * 0.2f);
                    card.setScaleY(1.0f - absPos * 0.2f);
                }
            } else {
                page.setAlpha(0f);
            }
        });

        setupIndicators();
        setCurrentIndicator(0);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                setCurrentIndicator(position);
                updateButtons(position);
            }
        });
    }

    private void setupIndicators() {
        binding.indicatorContainer.removeAllViews();
        ImageView[] indicators = new ImageView[items.size()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(12, 0, 12, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    this, R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            binding.indicatorContainer.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = binding.indicatorContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.indicatorContainer.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this, R.drawable.bg_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this, R.drawable.bg_indicator_inactive));
            }
        }
    }

    private void updateButtons(int position) {
        if (position == items.size() - 1) {
            binding.btnNext.setVisibility(View.GONE);
            binding.btnGetStarted.setVisibility(View.VISIBLE);
            binding.btnSkip.setVisibility(View.INVISIBLE);
        } else {
            binding.btnNext.setVisibility(View.VISIBLE);
            binding.btnGetStarted.setVisibility(View.GONE);
            binding.btnSkip.setVisibility(View.VISIBLE);
        }
    }

    private void setupListeners() {
        binding.btnNext.setOnClickListener(v -> {
            binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
        });

        binding.btnSkip.setOnClickListener(v -> finishOnboarding());
        binding.btnGetStarted.setOnClickListener(v -> finishOnboarding());
    }

    private void finishOnboarding() {
        tokenManager.setOnboardingCompleted(true);
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
