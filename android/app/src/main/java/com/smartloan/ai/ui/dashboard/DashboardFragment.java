package com.smartloan.ai.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.card.MaterialCardView;
import com.smartloan.ai.R;
import com.smartloan.ai.databinding.FragmentDashboardBinding;
import com.smartloan.ai.data.models.DashboardData;
import com.smartloan.ai.utils.TokenManager;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        String userName = TokenManager.getInstance(requireContext()).getUserName();
        binding.tvWelcome.setText("Welcome back, " + userName + ". Your intelligent overview.");

        binding.swipeRefresh.setColorSchemeResources(R.color.primary);
        binding.swipeRefresh.setOnRefreshListener(() -> viewModel.loadDashboard());

        observeData();
        viewModel.loadDashboard();
    }

    private void observeData() {
        viewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            binding.loadingLayout.setVisibility(loading ? View.VISIBLE : View.GONE);
            binding.contentLayout.setVisibility(loading ? View.GONE : View.VISIBLE);
            binding.swipeRefresh.setRefreshing(false);
        });

        viewModel.getDashboardData().observe(getViewLifecycleOwner(), data -> {
            if (data != null) populateDashboard(data);
        });
    }

    private void populateDashboard(DashboardData data) {
        buildStatCards(data);
        buildGrowthChart(data);
        buildInsights(data);
        buildIncomeExpensesChart(data);
        buildRadarChart(data);
        buildRecentActivity(data);
    }

    private void buildStatCards(DashboardData data) {
        binding.statsGrid.removeAllViews();
        String[][] stats = {
                {"Loan Approval", data.loanProbability + "%", "#2563EB"},
                {"Health Score", data.healthScore + "/100", "#22C55E"},
                {"Risk Level", capitalize(data.riskLevel), "#EAB308"},
                {"Credit Score", String.valueOf(data.creditScore), "#8B5CF6"},
                {"Monthly Savings", "$" + String.format("%,.0f", data.monthlySavings), "#06B6D4"},
                {"Debt-to-Income", String.format("%.1f%%", data.dtiRatio * 100), "#F43F5E"},
        };

        for (String[] stat : stats) {
            MaterialCardView card = new MaterialCardView(requireContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.setMargins(8, 8, 8, 8);
            card.setLayoutParams(params);
            card.setCardElevation(4f);
            card.setRadius(getResources().getDimension(R.dimen.card_radius));
            card.setCardBackgroundColor(getResources().getColor(R.color.card_background, null));
            card.setStrokeWidth(1);
            card.setStrokeColor(getResources().getColor(R.color.card_stroke, null));

            LinearLayout inner = new LinearLayout(requireContext());
            inner.setOrientation(LinearLayout.VERTICAL);
            inner.setPadding(32, 28, 32, 28);

            TextView title = new TextView(requireContext());
            title.setText(stat[0]);
            title.setTextSize(12f);
            title.setTextColor(getResources().getColor(R.color.muted_foreground, null));

            TextView value = new TextView(requireContext());
            value.setText(stat[1]);
            value.setTextSize(22f);
            value.setTextColor(Color.parseColor(stat[2]));
            value.setTypeface(null, android.graphics.Typeface.BOLD);
            value.setPadding(0, 8, 0, 0);

            inner.addView(title);
            inner.addView(value);
            card.addView(inner);
            binding.statsGrid.addView(card);
        }
    }

    private void buildGrowthChart(DashboardData data) {
        if (data.financialGrowth == null || data.financialGrowth.isEmpty()) return;

        LineChart chart = binding.chartGrowth;
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < data.financialGrowth.size(); i++) {
            DashboardData.FinancialGrowth g = data.financialGrowth.get(i);
            entries.add(new Entry(i, (float) g.netWorth));
            labels.add(g.month);
        }

        LineDataSet dataSet = new LineDataSet(entries, "Net Worth");
        dataSet.setColor(Color.parseColor("#2563EB"));
        dataSet.setLineWidth(3f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#2563EB"));
        dataSet.setFillAlpha(40);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawValues(false);

        chart.setData(new LineData(dataSet));
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.animateX(800);
        chart.invalidate();
    }

    private void buildInsights(DashboardData data) {
        binding.insightsContainer.removeAllViews();
        if (data.insights == null) return;

        for (DashboardData.Insight insight : data.insights) {
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setBackground(getResources().getDrawable(R.drawable.bg_muted_rounded, null));
            row.setPadding(24, 20, 24, 20);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 8, 0, 8);
            row.setLayoutParams(lp);

            TextView icon = new TextView(requireContext());
            icon.setText(insight.icon);
            icon.setTextSize(20f);
            icon.setPadding(0, 0, 16, 0);

            LinearLayout textCol = new LinearLayout(requireContext());
            textCol.setOrientation(LinearLayout.VERTICAL);

            TextView title = new TextView(requireContext());
            title.setText(insight.title);
            title.setTextSize(13f);
            title.setTypeface(null, android.graphics.Typeface.BOLD);
            title.setTextColor(getResources().getColor(R.color.on_surface, null));

            TextView msg = new TextView(requireContext());
            msg.setText(insight.message);
            msg.setTextSize(12f);
            msg.setTextColor(getResources().getColor(R.color.muted_foreground, null));

            textCol.addView(title);
            textCol.addView(msg);
            row.addView(icon);
            row.addView(textCol);
            binding.insightsContainer.addView(row);
        }
    }

    private void buildIncomeExpensesChart(DashboardData data) {
        if (data.incomeVsExpenses == null || data.incomeVsExpenses.isEmpty()) return;

        BarChart chart = binding.chartIncomeExpenses;
        List<BarEntry> incomeEntries = new ArrayList<>();
        List<BarEntry> expenseEntries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < data.incomeVsExpenses.size(); i++) {
            DashboardData.IncomeExpense ie = data.incomeVsExpenses.get(i);
            incomeEntries.add(new BarEntry(i, (float) ie.income));
            expenseEntries.add(new BarEntry(i, (float) ie.expenses));
            labels.add(ie.month);
        }

        BarDataSet incomeSet = new BarDataSet(incomeEntries, "Income");
        incomeSet.setColor(Color.parseColor("#22C55E"));
        incomeSet.setDrawValues(false);

        BarDataSet expenseSet = new BarDataSet(expenseEntries, "Expenses");
        expenseSet.setColor(Color.parseColor("#EF4444"));
        expenseSet.setDrawValues(false);

        BarData barData = new BarData(incomeSet, expenseSet);
        float groupSpace = 0.3f, barSpace = 0.05f, barWidth = 0.3f;
        barData.setBarWidth(barWidth);

        chart.setData(barData);
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setCenterAxisLabels(true);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.groupBars(0f, groupSpace, barSpace);
        chart.setFitBars(true);
        chart.animateY(800);
        chart.invalidate();
    }

    private void buildRadarChart(DashboardData data) {
        if (data.riskRadar == null || data.riskRadar.isEmpty()) return;

        RadarChart chart = binding.chartRadar;
        List<RadarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (DashboardData.RiskRadar r : data.riskRadar) {
            entries.add(new RadarEntry(r.value));
            labels.add(r.category);
        }

        RadarDataSet dataSet = new RadarDataSet(entries, "Score");
        dataSet.setColor(Color.parseColor("#06B6D4"));
        dataSet.setFillColor(Color.parseColor("#06B6D4"));
        dataSet.setDrawFilled(true);
        dataSet.setFillAlpha(80);
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(false);

        chart.setData(new RadarData(dataSet));
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setTextSize(11f);
        chart.getYAxis().setAxisMinimum(0f);
        chart.getYAxis().setAxisMaximum(100f);
        chart.getYAxis().setDrawLabels(false);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setWebLineWidth(1f);
        chart.setWebColor(Color.LTGRAY);
        chart.setWebLineWidthInner(1f);
        chart.setWebColorInner(Color.LTGRAY);
        chart.animateXY(800, 800);
        chart.invalidate();
    }

    private void buildRecentActivity(DashboardData data) {
        binding.activityContainer.removeAllViews();
        if (data.recentActivity == null) return;

        for (DashboardData.RecentActivity activity : data.recentActivity) {
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setBackground(getResources().getDrawable(R.drawable.bg_muted_rounded, null));
            row.setPadding(24, 20, 24, 20);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 6, 0, 6);
            row.setLayoutParams(lp);

            // Dot indicator
            View dot = new View(requireContext());
            int dotSize = 16;
            LinearLayout.LayoutParams dotLp = new LinearLayout.LayoutParams(dotSize, dotSize);
            dotLp.setMargins(0, 0, 16, 0);
            dot.setLayoutParams(dotLp);
            dot.setBackgroundResource(R.drawable.bg_circle_gradient);

            // Message
            TextView msg = new TextView(requireContext());
            msg.setText(activity.message);
            msg.setTextSize(13f);
            msg.setTextColor(getResources().getColor(R.color.on_surface, null));
            LinearLayout.LayoutParams msgLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            msg.setLayoutParams(msgLp);

            // Result
            TextView result = new TextView(requireContext());
            result.setText(activity.result);
            result.setTextSize(12f);
            result.setTypeface(null, android.graphics.Typeface.BOLD);
            result.setTextColor(getResources().getColor(R.color.primary, null));
            result.setPadding(16, 0, 16, 0);

            // Time
            TextView time = new TextView(requireContext());
            time.setText(activity.time);
            time.setTextSize(11f);
            time.setTextColor(getResources().getColor(R.color.muted_foreground, null));

            row.addView(dot);
            row.addView(msg);
            row.addView(result);
            row.addView(time);
            binding.activityContainer.addView(row);
        }
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
