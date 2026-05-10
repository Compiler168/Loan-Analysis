package com.smartloan.ai.ui.simulator;

import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.card.MaterialCardView;
import com.smartloan.ai.R;
import com.smartloan.ai.databinding.FragmentSimulatorBinding;
import com.smartloan.ai.data.models.SimulationResult;
import com.smartloan.ai.utils.ViewUtils;
import java.util.*;

public class SimulatorFragment extends Fragment {
    private FragmentSimulatorBinding binding;
    private SimulatorViewModel viewModel;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSimulatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SimulatorViewModel.class);

        setupSliders();
        binding.btnSimulate.setOnClickListener(v -> runSimulation());

        viewModel.getLoading().observe(getViewLifecycleOwner(), l -> {
            binding.progressBar.setVisibility(l ? View.VISIBLE : View.GONE);
            binding.btnSimulate.setEnabled(!l);
        });
        viewModel.getResult().observe(getViewLifecycleOwner(), this::showResults);
        viewModel.getError().observe(getViewLifecycleOwner(), e -> {
            if (e != null) ViewUtils.showErrorSnackbar(binding.getRoot(), e);
        });
    }

    private void setupSliders() {
        binding.seekIncome.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int p, boolean u) {
                int val = p - 75;
                binding.tvIncomeChange.setText((val >= 0 ? "+" : "") + val + "%");
                binding.tvIncomeChange.setTextColor(val >= 0 ? Color.parseColor("#22C55E") : Color.parseColor("#EF4444"));
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });
        binding.seekExpense.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int p, boolean u) {
                int val = p - 75;
                binding.tvExpenseChange.setText((val >= 0 ? "+" : "") + val + "%");
                binding.tvExpenseChange.setTextColor(val <= 0 ? Color.parseColor("#22C55E") : Color.parseColor("#EF4444"));
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });
    }

    private void runSimulation() {
        Map<String, Object> data = new HashMap<>();
        data.put("monthly_income", getD(binding.etIncome));
        data.put("monthly_expenses", getD(binding.etExpenses));
        data.put("savings_balance", getD(binding.etSavings));
        data.put("existing_emi", 500);
        data.put("loan_amount", 50000); data.put("loan_term_months", 36); data.put("interest_rate", 10);
        data.put("income_change_pct", binding.seekIncome.getProgress() - 75);
        data.put("expense_change_pct", binding.seekExpense.getProgress() - 75);
        data.put("new_loan_amount", getD(binding.etNewLoan));
        data.put("projection_months", getI(binding.etProjection));
        viewModel.simulate(data);
    }

    private void showResults(SimulationResult data) {
        if (data == null) return;
        binding.resultsContainer.setVisibility(View.VISIBLE);

        // Summary cards
        binding.summaryCards.removeAllViews();
        if (data.comparison != null) {
            String[][] summaries = {
                    {"Savings Diff", formatDelta(data.comparison.savingsDifference)},
                    {"Monthly Net Δ", formatDelta(data.comparison.monthlyDifference)},
                    {"EMI Δ", formatDelta(data.comparison.emiDifference)}
            };
            for (String[] s : summaries) {
                MaterialCardView card = new MaterialCardView(requireContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                lp.setMargins(4, 0, 4, 0); card.setLayoutParams(lp);
                card.setRadius(getResources().getDimension(R.dimen.card_radius));
                card.setCardElevation(4f);
                card.setCardBackgroundColor(getResources().getColor(R.color.card_background, null));

                LinearLayout inner = new LinearLayout(requireContext());
                inner.setOrientation(LinearLayout.VERTICAL); inner.setPadding(16, 20, 16, 20);
                inner.setGravity(Gravity.CENTER);

                TextView val = new TextView(requireContext());
                val.setText(s[1]); val.setTextSize(16f); val.setTypeface(null, android.graphics.Typeface.BOLD);
                val.setGravity(Gravity.CENTER);
                TextView label = new TextView(requireContext());
                label.setText(s[0]); label.setTextSize(11f); label.setGravity(Gravity.CENTER);
                label.setTextColor(getResources().getColor(R.color.muted_foreground, null));

                inner.addView(val); inner.addView(label);
                card.addView(inner);
                binding.summaryCards.addView(card);
            }
        }

        // Trajectory chart
        if (data.chartData != null && !data.chartData.isEmpty()) {
            LineChart chart = binding.chartTrajectory;
            List<Entry> baseline = new ArrayList<>(), projected = new ArrayList<>();
            List<String> labels = new ArrayList<>();
            for (int i = 0; i < data.chartData.size(); i++) {
                SimulationResult.ChartPoint p = data.chartData.get(i);
                baseline.add(new Entry(i, (float) p.baseline));
                projected.add(new Entry(i, (float) p.projected));
                labels.add(p.month);
            }
            LineDataSet bDs = new LineDataSet(baseline, "Baseline");
            bDs.setColor(Color.GRAY); bDs.setLineWidth(2f); bDs.setDrawCircles(false); bDs.setDrawValues(false);
            LineDataSet pDs = new LineDataSet(projected, "Projected");
            pDs.setColor(Color.parseColor("#2563EB")); pDs.setLineWidth(3f); pDs.setDrawCircles(false); pDs.setDrawValues(false);

            chart.setData(new LineData(bDs, pDs));
            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM); chart.getXAxis().setDrawGridLines(false);
            chart.getAxisRight().setEnabled(false); chart.getDescription().setEnabled(false);
            chart.animateX(800); chart.invalidate();
        }

        // Recommendations
        binding.recommendationsContainer.removeAllViews();
        if (data.recommendations != null) {
            for (SimulationResult.Recommendation r : data.recommendations) {
                TextView tv = new TextView(requireContext());
                tv.setText(r.message); tv.setTextSize(13f); tv.setPadding(20, 14, 20, 14);
                tv.setBackground(requireContext().getDrawable(R.drawable.bg_muted_rounded));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 4, 0, 4); tv.setLayoutParams(lp);
                binding.recommendationsContainer.addView(tv);
            }
        }
    }

    private String formatDelta(double v) {
        return (v >= 0 ? "+$" : "-$") + String.format("%,.0f", Math.abs(v));
    }
    private int getI(com.google.android.material.textfield.TextInputEditText e) {
        try { return Integer.parseInt(e.getText().toString().trim()); } catch (Exception ex) { return 24; }
    }
    private double getD(com.google.android.material.textfield.TextInputEditText e) {
        try { return Double.parseDouble(e.getText().toString().trim()); } catch (Exception ex) { return 0; }
    }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}
