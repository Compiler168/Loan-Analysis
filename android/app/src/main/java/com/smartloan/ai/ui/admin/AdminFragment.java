package com.smartloan.ai.ui.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.card.MaterialCardView;
import com.smartloan.ai.R;
import com.smartloan.ai.databinding.FragmentAdminBinding;
import com.smartloan.ai.data.models.*;
import com.smartloan.ai.utils.ViewUtils;
import java.util.List;

public class AdminFragment extends Fragment {
    private FragmentAdminBinding binding;
    private AdminViewModel viewModel;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AdminViewModel.class);

        binding.swipeRefresh.setOnRefreshListener(() -> viewModel.loadData());

        viewModel.getLoading().observe(getViewLifecycleOwner(), l -> {
            binding.swipeRefresh.setRefreshing(l);
            if(l && binding.statsGrid.getChildCount() == 0) binding.progressBar.setVisibility(View.VISIBLE);
            else binding.progressBar.setVisibility(View.GONE);
        });

        viewModel.getStats().observe(getViewLifecycleOwner(), this::showStats);
        viewModel.getUsers().observe(getViewLifecycleOwner(), this::showUsers);
        viewModel.getError().observe(getViewLifecycleOwner(), e -> {
            if(e != null) ViewUtils.showErrorSnackbar(binding.getRoot(), e);
        });

        viewModel.loadData();
    }

    private void showStats(AdminStats stats) {
        if(stats == null) return;
        binding.statsGrid.removeAllViews();

        String[][] items = {
                {"Total Users", String.valueOf(stats.totalUsers), "#2563EB"},
                {"Active Sessions", String.valueOf(stats.activeSessions), "#22C55E"},
                {"Predictions", String.valueOf(stats.totalPredictions), "#8B5CF6"},
                {"Avg Health", String.valueOf(stats.avgHealthScore), "#06B6D4"}
        };

        for(String[] item : items) {
            MaterialCardView card = new MaterialCardView(requireContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
            params.setMargins(8, 8, 8, 8); card.setLayoutParams(params);
            card.setRadius(getResources().getDimension(R.dimen.card_radius));
            card.setCardElevation(2f);
            card.setCardBackgroundColor(getResources().getColor(R.color.card_background, null));

            LinearLayout inner = new LinearLayout(requireContext());
            inner.setOrientation(LinearLayout.VERTICAL); inner.setPadding(32, 24, 32, 24);

            TextView title = new TextView(requireContext());
            title.setText(item[0]); title.setTextSize(12f);
            title.setTextColor(getResources().getColor(R.color.muted_foreground, null));
            TextView val = new TextView(requireContext());
            val.setText(item[1]); val.setTextSize(20f); val.setTypeface(null, android.graphics.Typeface.BOLD);
            val.setTextColor(Color.parseColor(item[2]));

            inner.addView(title); inner.addView(val);
            card.addView(inner); binding.statsGrid.addView(card);
        }

        binding.tvMlStatus.setText(stats.mlServiceStatus != null ? stats.mlServiceStatus : "Online");
        binding.tvMlStatus.setTextColor(Color.parseColor(stats.mlServiceStatus != null && stats.mlServiceStatus.equals("Online") ? "#22C55E" : "#22C55E"));
        binding.tvUptime.setText(stats.systemUptime != null ? stats.systemUptime : "99.9%");
    }

    private void showUsers(List<AdminUser> users) {
        binding.usersContainer.removeAllViews();
        if(users == null) return;

        for(AdminUser u : users) {
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 16, 0, 16);
            row.setGravity(Gravity.CENTER_VERTICAL);

            View avatar = new View(requireContext());
            avatar.setBackgroundResource(R.drawable.bg_circle_gradient);
            LinearLayout.LayoutParams aLp = new LinearLayout.LayoutParams(40, 40);
            aLp.setMargins(0, 0, 16, 0); avatar.setLayoutParams(aLp);

            LinearLayout textCol = new LinearLayout(requireContext());
            textCol.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams tLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            textCol.setLayoutParams(tLp);

            TextView name = new TextView(requireContext());
            name.setText(u.name); name.setTextSize(14f); name.setTypeface(null, android.graphics.Typeface.BOLD);
            TextView email = new TextView(requireContext());
            email.setText(u.email); email.setTextSize(12f); email.setTextColor(getResources().getColor(R.color.muted_foreground, null));
            textCol.addView(name); textCol.addView(email);

            TextView role = new TextView(requireContext());
            role.setText(u.role.toUpperCase()); role.setTextSize(10f);
            role.setPadding(16, 4, 16, 4); role.setBackgroundResource(R.drawable.bg_muted_rounded);

            row.addView(avatar); row.addView(textCol); row.addView(role);
            binding.usersContainer.addView(row);
        }
    }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}
