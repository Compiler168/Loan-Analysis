package com.smartloan.ai.ui.reports;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.card.MaterialCardView;
import com.smartloan.ai.R;
import com.smartloan.ai.databinding.FragmentReportsBinding;
import com.smartloan.ai.data.models.*;
import com.smartloan.ai.utils.ViewUtils;
import java.util.List;

public class ReportsFragment extends Fragment {
    private FragmentReportsBinding binding;
    private ReportsViewModel viewModel;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ReportsViewModel.class);

        binding.btnFinancial.setOnClickListener(v -> viewModel.generateReport("financial_summary"));
        binding.btnLoan.setOnClickListener(v -> viewModel.generateReport("loan_analysis"));
        binding.btnRiskReport.setOnClickListener(v -> viewModel.generateReport("risk_report"));
        binding.btnAiReport.setOnClickListener(v -> viewModel.generateReport("ai_recommendations"));

        viewModel.getLoading().observe(getViewLifecycleOwner(), l ->
                binding.progressBar.setVisibility(l ? View.VISIBLE : View.GONE));
        viewModel.getReport().observe(getViewLifecycleOwner(), this::showReport);
        viewModel.getHistory().observe(getViewLifecycleOwner(), this::showHistory);
        viewModel.getError().observe(getViewLifecycleOwner(), e -> {
            if (e != null) ViewUtils.showErrorSnackbar(binding.getRoot(), e);
        });

        viewModel.loadHistory();
    }

    private void showReport(ReportData data) {
        if (data == null) return;
        binding.reportContent.removeAllViews();
        binding.reportContent.setVisibility(View.VISIBLE);

        // Title
        MaterialCardView titleCard = createCard();
        LinearLayout titleInner = new LinearLayout(requireContext());
        titleInner.setOrientation(LinearLayout.VERTICAL);
        titleInner.setPadding(32, 24, 32, 24);
        TextView title = new TextView(requireContext());
        title.setText(data.title); title.setTextSize(18f);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        TextView date = new TextView(requireContext());
        date.setText("Generated: " + data.generatedAt); date.setTextSize(12f);
        date.setTextColor(getResources().getColor(R.color.muted_foreground, null));
        titleInner.addView(title); titleInner.addView(date);
        titleCard.addView(titleInner);
        binding.reportContent.addView(titleCard);

        // Sections
        if (data.sections != null) {
            for (ReportData.Section section : data.sections) {
                MaterialCardView sCard = createCard();
                LinearLayout sInner = new LinearLayout(requireContext());
                sInner.setOrientation(LinearLayout.VERTICAL);
                sInner.setPadding(32, 24, 32, 24);

                TextView sTitle = new TextView(requireContext());
                sTitle.setText(section.title); sTitle.setTextSize(15f);
                sTitle.setTypeface(null, android.graphics.Typeface.BOLD);
                sInner.addView(sTitle);

                if (section.content != null) {
                    TextView content = new TextView(requireContext());
                    content.setText(section.content); content.setTextSize(13f);
                    content.setPadding(0, 8, 0, 0);
                    sInner.addView(content);
                }

                if (section.items != null) {
                    for (ReportData.Item item : section.items) {
                        LinearLayout row = new LinearLayout(requireContext());
                        row.setOrientation(LinearLayout.HORIZONTAL);
                        row.setPadding(0, 8, 0, 8);

                        TextView label = new TextView(requireContext());
                        label.setText(item.label); label.setTextSize(13f);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                        label.setLayoutParams(lp);

                        TextView val = new TextView(requireContext());
                        val.setText(item.value); val.setTextSize(13f);
                        val.setTypeface(null, android.graphics.Typeface.BOLD);

                        row.addView(label); row.addView(val);
                        sInner.addView(row);
                    }
                }
                sCard.addView(sInner);
                binding.reportContent.addView(sCard);
            }
        }
        viewModel.loadHistory();
    }

    private void showHistory(List<ReportHistory> list) {
        binding.historyContainer.removeAllViews();
        if (list == null || list.isEmpty()) {
            TextView empty = new TextView(requireContext());
            empty.setText("No reports generated yet");
            empty.setTextColor(getResources().getColor(R.color.muted_foreground, null));
            binding.historyContainer.addView(empty);
            return;
        }
        for (ReportHistory h : list) {
            LinearLayout row = new LinearLayout(requireContext());
            row.setPadding(0, 12, 0, 12);
            TextView t = new TextView(requireContext());
            t.setText("📄 " + h.title); t.setTextSize(13f);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            t.setLayoutParams(lp);
            TextView d = new TextView(requireContext());
            d.setText(h.date); d.setTextSize(11f);
            d.setTextColor(getResources().getColor(R.color.muted_foreground, null));
            row.addView(t); row.addView(d);
            binding.historyContainer.addView(row);
        }
    }

    private MaterialCardView createCard() {
        MaterialCardView c = new MaterialCardView(requireContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int margin = (int) (8 * getResources().getDisplayMetrics().density);
        lp.setMargins(0, margin, 0, 0); 
        c.setLayoutParams(lp);
        c.setRadius(getResources().getDimension(R.dimen.card_radius));
        c.setCardElevation(0f);
        c.setCardBackgroundColor(getResources().getColor(R.color.surface, null));
        c.setStrokeWidth(2);
        c.setStrokeColor(getResources().getColor(R.color.card_stroke, null));
        return c;
    }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}
