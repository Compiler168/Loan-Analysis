package com.smartloan.ai.ui.settings;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.smartloan.ai.R;
import com.smartloan.ai.databinding.FragmentSettingsBinding;
import com.smartloan.ai.utils.Constants;
import com.smartloan.ai.utils.TokenManager;
import com.smartloan.ai.utils.ViewUtils;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private TokenManager tokenManager;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tokenManager = TokenManager.getInstance(requireContext());

        // Load current settings
        binding.etName.setText(tokenManager.getUserName());
        binding.etEmail.setText(tokenManager.getUserEmail());

        String theme = tokenManager.getTheme();
        if(Constants.THEME_LIGHT.equals(theme)) binding.rbLight.setChecked(true);
        else if(Constants.THEME_DARK.equals(theme)) binding.rbDark.setChecked(true);
        else binding.rbSystem.setChecked(true);

        binding.btnSave.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {
        // Save Name
        String newName = binding.etName.getText().toString().trim();
        tokenManager.saveUserInfo(
                tokenManager.getUserId(),
                newName,
                tokenManager.getUserEmail(),
                tokenManager.getUserRole()
        );

        // Save Theme
        String newTheme = Constants.THEME_SYSTEM;
        if(binding.rbLight.isChecked()) newTheme = Constants.THEME_LIGHT;
        else if(binding.rbDark.isChecked()) newTheme = Constants.THEME_DARK;
        
        String oldTheme = tokenManager.getTheme();
        tokenManager.saveTheme(newTheme);

        ViewUtils.showSuccessSnackbar(binding.getRoot(), getString(R.string.saved));

        // Apply theme if changed
        if(!newTheme.equals(oldTheme)) {
            switch (newTheme) {
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

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}
