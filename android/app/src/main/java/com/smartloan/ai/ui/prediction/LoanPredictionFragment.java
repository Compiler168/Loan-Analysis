package com.smartloan.ai.ui.prediction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.smartloan.ai.R;
import com.smartloan.ai.databinding.FragmentLoanPredictionBinding;
import com.smartloan.ai.data.models.PredictionResult;
import com.smartloan.ai.utils.ViewUtils;

import java.util.*;

public class LoanPredictionFragment extends Fragment {

    private FragmentLoanPredictionBinding binding;
    private PredictionViewModel viewModel;
    private int currentStep = 0;
    private final String[] stepTitles = {"Personal Info", "Financial Details", "Loan Parameters"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoanPredictionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PredictionViewModel.class);

        setupEmploymentDropdown();
        updateStep();
        setupListeners();
        observeData();
    }

    private void setupEmploymentDropdown() {
        String[] items = {"salaried", "self_employed", "freelancer", "business_owner", "retired"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, items);
        binding.spinnerEmployment.setAdapter(adapter);
    }

    private void updateStep() {
        binding.step1.setVisibility(currentStep == 0 ? View.VISIBLE : View.GONE);
        binding.step2.setVisibility(currentStep == 1 ? View.VISIBLE : View.GONE);
        binding.step3.setVisibility(currentStep == 2 ? View.VISIBLE : View.GONE);
        binding.tvStepTitle.setText(stepTitles[currentStep]);
        binding.btnBack.setVisibility(currentStep > 0 ? View.VISIBLE : View.GONE);
        binding.btnNext.setText(currentStep < 2 ? "Next" : "🧠 Predict");
        buildStepIndicator();
    }

    private void buildStepIndicator() {
        binding.stepIndicator.removeAllViews();
        for (int i = 0; i < 3; i++) {
            TextView circle = new TextView(requireContext());
            circle.setText(String.valueOf(i + 1));
            circle.setTextSize(12f);
            circle.setGravity(android.view.Gravity.CENTER);
            int size = 72;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
            lp.setMargins(4, 0, 4, 0);
            circle.setLayoutParams(lp);
            circle.setBackgroundResource(i <= currentStep ?
                    R.drawable.bg_circle_gradient : R.drawable.bg_muted_rounded);
            circle.setTextColor(i <= currentStep ? Color.WHITE :
                    getResources().getColor(R.color.muted_foreground, null));
            binding.stepIndicator.addView(circle);

            if (i < 2) {
                View line = new View(requireContext());
                LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(60, 4);
                lineLp.gravity = android.view.Gravity.CENTER_VERTICAL;
                line.setLayoutParams(lineLp);
                line.setBackgroundColor(i < currentStep ?
                        getResources().getColor(R.color.primary, null) :
                        getResources().getColor(R.color.muted, null));
                binding.stepIndicator.addView(line);
            }
        }
    }

    private void setupListeners() {
        binding.btnBack.setOnClickListener(v -> {
            if (currentStep > 0) { currentStep--; updateStep(); }
        });

        binding.btnNext.setOnClickListener(v -> {
            if (currentStep < 2) {
                currentStep++;
                updateStep();
            } else {
                submitPrediction();
            }
        });
    }

    private void submitPrediction() {
        Map<String, Object> form = new HashMap<>();
        form.put("age", getInt(binding.etAge));
        form.put("dependents", getInt(binding.etDependents));
        form.put("employment_status", binding.spinnerEmployment.getText().toString());
        form.put("employment_years", getInt(binding.etEmploymentYears));
        form.put("monthly_income", getDouble(binding.etIncome));
        form.put("monthly_expenses", getDouble(binding.etExpenses));
        form.put("credit_score", getInt(binding.etCreditScore));
        form.put("existing_loans", getInt(binding.etExistingLoans));
        form.put("existing_emi", getDouble(binding.etExistingEmi));
        form.put("savings_balance", getDouble(binding.etSavings));
        form.put("loan_amount", getDouble(binding.etLoanAmount));
        form.put("loan_term_months", getInt(binding.etLoanTerm));
        form.put("interest_rate", getDouble(binding.etInterestRate));
        form.put("property_value", getDouble(binding.etPropertyValue));
        form.put("missed_payments_last_year", getInt(binding.etMissedPayments));
        form.put("bankruptcies", getInt(binding.etBankruptcies));

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnNext.setEnabled(false);
        viewModel.predict(form);
    }

    private void observeData() {
        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
            binding.btnNext.setEnabled(!loading);
        });

        viewModel.getResult().observe(getViewLifecycleOwner(), this::showResults);

        viewModel.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) ViewUtils.showErrorSnackbar(binding.getRoot(), err);
        });
    }

    private void showResults(PredictionResult data) {
        if (data == null) return;
        binding.emptyState.setVisibility(View.GONE);
        binding.resultsContainer.setVisibility(View.VISIBLE);

        boolean approved = data.ensemble.approved;
        double prob = data.ensemble.probability * 100;

        binding.tvApprovalPercent.setText(String.format("%.1f%%", prob));
        binding.tvApprovalPercent.setTextColor(approved ?
                getResources().getColor(R.color.success, null) :
                getResources().getColor(R.color.error, null));
        binding.tvApprovalStatus.setText(approved ? "✅ Likely Approved" : "❌ Likely Rejected");
        binding.tvApprovalStatus.setTextColor(approved ?
                getResources().getColor(R.color.success, null) :
                getResources().getColor(R.color.error, null));
        binding.tvConfidence.setText("Confidence: " + data.ensemble.confidence +
                " (" + String.format("%.0f", data.ensemble.confidenceScore * 100) + "% agreement)");
        binding.cardApproval.setStrokeColor(approved ?
                getResources().getColor(R.color.success, null) :
                getResources().getColor(R.color.error, null));

        // Model breakdown
        binding.modelsContainer.removeAllViews();
        if (data.models != null) {
            for (Map.Entry<String, PredictionResult.ModelResult> entry : data.models.entrySet()) {
                LinearLayout row = new LinearLayout(requireContext());
                row.setOrientation(LinearLayout.VERTICAL);
                row.setPadding(0, 8, 0, 8);

                LinearLayout header = new LinearLayout(requireContext());
                header.setOrientation(LinearLayout.HORIZONTAL);

                TextView name = new TextView(requireContext());
                name.setText(entry.getKey().replace("_", " "));
                name.setTextSize(13f);
                LinearLayout.LayoutParams nlp = new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                name.setLayoutParams(nlp);

                TextView val = new TextView(requireContext());
                val.setText(String.format("%.1f%%", entry.getValue().probability * 100));
                val.setTextSize(13f);
                val.setTypeface(null, android.graphics.Typeface.BOLD);

                header.addView(name);
                header.addView(val);

                ProgressBar pb = new ProgressBar(requireContext(), null,
                        android.R.attr.progressBarStyleHorizontal);
                pb.setMax(100);
                pb.setProgress((int) (entry.getValue().probability * 100));
                pb.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 8));

                row.addView(header);
                row.addView(pb);
                binding.modelsContainer.addView(row);
            }
        }

        // Risk factors
        binding.riskContainer.removeAllViews();
        if (data.riskReasons != null) {
            for (PredictionResult.RiskReason r : data.riskReasons) {
                LinearLayout card = new LinearLayout(requireContext());
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(24, 16, 24, 16);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 4, 0, 4);
                card.setLayoutParams(lp);
                card.setBackground(getResources().getDrawable(R.drawable.bg_muted_rounded, null));

                TextView factor = new TextView(requireContext());
                factor.setText(r.factor + ": " + r.message);
                factor.setTextSize(13f);
                factor.setTypeface(null, android.graphics.Typeface.BOLD);

                TextView suggestion = new TextView(requireContext());
                suggestion.setText(r.suggestion);
                suggestion.setTextSize(12f);
                suggestion.setTextColor(getResources().getColor(R.color.muted_foreground, null));

                card.addView(factor);
                card.addView(suggestion);
                binding.riskContainer.addView(card);
            }
        }

        // Feature importance chart
        if (data.topFactors != null) {
            buildFeatureChart(data.topFactors);
        }
    }

    private void buildFeatureChart(Map<String, Double> factors) {
        HorizontalBarChart chart = binding.chartFeatures;
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Double> e : factors.entrySet()) {
            entries.add(new BarEntry(i, (float) (e.getValue() * 100)));
            labels.add(e.getKey().replace("_", " "));
            i++;
        }
        BarDataSet ds = new BarDataSet(entries, "Importance");
        ds.setColor(Color.parseColor("#2563EB"));
        ds.setDrawValues(false);
        chart.setData(new BarData(ds));
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setFitBars(true);
        chart.animateY(600);
        chart.invalidate();
    }

    private int getInt(com.google.android.material.textfield.TextInputEditText et) {
        try { return Integer.parseInt(et.getText().toString().trim()); } catch (Exception e) { return 0; }
    }

    private double getDouble(com.google.android.material.textfield.TextInputEditText et) {
        try { return Double.parseDouble(et.getText().toString().trim()); } catch (Exception e) { return 0; }
    }

    @Override
    public void onDestroyView() { super.onDestroyView(); binding = null; }
}
