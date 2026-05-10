package com.smartloan.ai.ui.analysis;

import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.card.MaterialCardView;
import com.smartloan.ai.R;
import com.smartloan.ai.databinding.FragmentAnalysisBinding;
import com.smartloan.ai.data.models.*;
import com.smartloan.ai.utils.ViewUtils;
import java.util.*;

public class AnalysisFragment extends Fragment {
    private FragmentAnalysisBinding binding;
    private AnalysisViewModel viewModel;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AnalysisViewModel.class);

        binding.btnHealth.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.runHealthScore(buildFormData());
        });
        binding.btnRisk.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.runRiskAnalysis(buildFormData());
        });

        viewModel.getLoading().observe(getViewLifecycleOwner(), l ->
                binding.progressBar.setVisibility(l ? View.VISIBLE : View.GONE));
        viewModel.getHealthResult().observe(getViewLifecycleOwner(), this::showHealthResult);
        viewModel.getRiskResult().observe(getViewLifecycleOwner(), this::showRiskResult);
        viewModel.getError().observe(getViewLifecycleOwner(), e -> {
            if (e != null) ViewUtils.showErrorSnackbar(binding.getRoot(), e);
        });
    }

    private Map<String, Object> buildFormData() {
        Map<String, Object> m = new HashMap<>();
        m.put("monthly_income", getD(binding.etIncome)); m.put("monthly_expenses", getD(binding.etExpenses));
        m.put("savings_balance", getD(binding.etSavings)); m.put("credit_score", getI(binding.etCreditScore));
        m.put("existing_emi", getD(binding.etEmi)); m.put("existing_loans", 1);
        m.put("employment_years", 5); m.put("missed_payments_last_year", 0);
        m.put("bankruptcies", 0); m.put("age", 35); m.put("dependents", 1);
        m.put("property_value", 150000); m.put("loan_amount", 50000);
        m.put("loan_term_months", 36); m.put("interest_rate", 10);
        return m;
    }

    private void showHealthResult(HealthScoreResult data) {
        if (data == null) return;
        binding.resultsContainer.removeAllViews();

        // Score card
        MaterialCardView scoreCard = createCard();
        LinearLayout scoreInner = new LinearLayout(requireContext());
        scoreInner.setOrientation(LinearLayout.VERTICAL);
        scoreInner.setPadding(32, 32, 32, 32);

        TextView scoreVal = new TextView(requireContext());
        scoreVal.setText(data.overallScore + "/100 — " + data.grade + " " + data.gradeLabel);
        scoreVal.setTextSize(22f); scoreVal.setTypeface(null, android.graphics.Typeface.BOLD);
        scoreVal.setTextColor(data.overallScore >= 75 ? Color.parseColor("#22C55E") :
                data.overallScore >= 50 ? Color.parseColor("#EAB308") : Color.parseColor("#EF4444"));

        TextView summary = new TextView(requireContext());
        summary.setText(data.summary); summary.setTextSize(13f);
        summary.setTextColor(getResources().getColor(R.color.muted_foreground, null));
        summary.setPadding(0, 8, 0, 0);

        scoreInner.addView(scoreVal); scoreInner.addView(summary);
        scoreCard.addView(scoreInner);
        binding.resultsContainer.addView(scoreCard);

        // Breakdown
        if (data.breakdown != null) {
            MaterialCardView bCard = createCard();
            LinearLayout bInner = new LinearLayout(requireContext());
            bInner.setOrientation(LinearLayout.VERTICAL); bInner.setPadding(32, 32, 32, 32);
            TextView bTitle = new TextView(requireContext());
            bTitle.setText("Category Breakdown"); bTitle.setTextSize(16f);
            bTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            bInner.addView(bTitle);

            for (HealthScoreResult.Breakdown b : data.breakdown) {
                TextView cat = new TextView(requireContext());
                cat.setText(b.category + ": " + b.score + "/100");
                cat.setTextSize(13f); cat.setPadding(0, 16, 0, 4);
                cat.setTypeface(null, android.graphics.Typeface.BOLD);

                ProgressBar pb = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
                pb.setMax(100); pb.setProgress(b.score);
                pb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 12));

                bInner.addView(cat); bInner.addView(pb);
            }
            bCard.addView(bInner);
            binding.resultsContainer.addView(bCard);
        }
    }

    private void showRiskResult(RiskAnalysisResult data) {
        if (data == null) return;
        binding.resultsContainer.removeAllViews();

        MaterialCardView rCard = createCard();
        LinearLayout rInner = new LinearLayout(requireContext());
        rInner.setOrientation(LinearLayout.VERTICAL); rInner.setPadding(32, 32, 32, 32);

        TextView rLevel = new TextView(requireContext());
        rLevel.setText(data.riskLevel.toUpperCase() + " RISK — Score: " + data.overallRisk);
        rLevel.setTextSize(20f); rLevel.setTypeface(null, android.graphics.Typeface.BOLD);
        try { rLevel.setTextColor(Color.parseColor(data.riskColor)); } catch (Exception e) { }

        TextView rSummary = new TextView(requireContext());
        rSummary.setText(data.summary); rSummary.setTextSize(13f);
        rSummary.setTextColor(getResources().getColor(R.color.muted_foreground, null));

        rInner.addView(rLevel); rInner.addView(rSummary);
        rCard.addView(rInner);
        binding.resultsContainer.addView(rCard);

        if (data.dimensions != null) {
            for (RiskAnalysisResult.Dimension d : data.dimensions) {
                MaterialCardView dCard = createCard();
                LinearLayout dInner = new LinearLayout(requireContext());
                dInner.setOrientation(LinearLayout.VERTICAL); dInner.setPadding(24, 20, 24, 20);

                TextView dim = new TextView(requireContext());
                dim.setText(d.dimension + " — " + d.value);
                dim.setTextSize(14f); dim.setTypeface(null, android.graphics.Typeface.BOLD);

                TextView msg = new TextView(requireContext());
                msg.setText(d.message); msg.setTextSize(12f);
                msg.setTextColor(getResources().getColor(R.color.muted_foreground, null));

                ProgressBar pb = new ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal);
                pb.setMax(100); pb.setProgress(d.score);
                pb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 8));

                dInner.addView(dim); dInner.addView(pb); dInner.addView(msg);
                dCard.addView(dInner);
                binding.resultsContainer.addView(dCard);
            }
        }
    }

    private MaterialCardView createCard() {
        MaterialCardView c = new MaterialCardView(requireContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 12, 0, 0); c.setLayoutParams(lp);
        c.setRadius(getResources().getDimension(R.dimen.card_radius));
        c.setCardElevation(4f);
        c.setCardBackgroundColor(getResources().getColor(R.color.card_background, null));
        c.setStrokeWidth(1);
        c.setStrokeColor(getResources().getColor(R.color.card_stroke, null));
        return c;
    }

    private int getI(com.google.android.material.textfield.TextInputEditText e) {
        try { return Integer.parseInt(e.getText().toString().trim()); } catch (Exception ex) { return 0; }
    }
    private double getD(com.google.android.material.textfield.TextInputEditText e) {
        try { return Double.parseDouble(e.getText().toString().trim()); } catch (Exception ex) { return 0; }
    }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}
