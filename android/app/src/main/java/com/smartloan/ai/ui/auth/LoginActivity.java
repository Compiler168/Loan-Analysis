package com.smartloan.ai.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.smartloan.ai.databinding.ActivityLoginBinding;
import com.smartloan.ai.ui.main.MainActivity;
import com.smartloan.ai.utils.TokenManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenManager = TokenManager.getInstance(this);

        // Auto-redirect if already logged in
        if (tokenManager.isLoggedIn()) {
            navigateToMain();
            return;
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        viewModel.getAuthResult().observe(this, result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnLogin.setEnabled(true);

            if (result.isSuccess()) {
                tokenManager.saveToken(result.getToken());
                tokenManager.saveUserInfo(
                        result.getUserId(),
                        result.getUserName(),
                        result.getUserEmail(),
                        result.getUserRole()
                );
                navigateToMain();
            } else {
                binding.tvError.setVisibility(View.VISIBLE);
                binding.tvError.setText(result.getError());
            }
        });
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            // Validation
            if (email.isEmpty()) {
                binding.tilEmail.setError("Email is required");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.setError("Invalid email format");
                return;
            }
            if (password.isEmpty()) {
                binding.tilPassword.setError("Password is required");
                return;
            }
            if (password.length() < 6) {
                binding.tilPassword.setError("Minimum 6 characters");
                return;
            }

            // Clear errors
            binding.tilEmail.setError(null);
            binding.tilPassword.setError(null);
            binding.tvError.setVisibility(View.GONE);

            // Show loading
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnLogin.setEnabled(false);

            viewModel.login(email, password);
        });

        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
