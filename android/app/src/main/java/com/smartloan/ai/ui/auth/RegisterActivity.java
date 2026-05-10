package com.smartloan.ai.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.smartloan.ai.databinding.ActivityRegisterBinding;
import com.smartloan.ai.ui.main.MainActivity;
import com.smartloan.ai.utils.TokenManager;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel viewModel;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tokenManager = TokenManager.getInstance(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        viewModel.getAuthResult().observe(this, result -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnRegister.setEnabled(true);

            if (result.isSuccess()) {
                tokenManager.saveToken(result.getToken());
                tokenManager.saveUserInfo(
                        result.getUserId(),
                        result.getUserName(),
                        result.getUserEmail(),
                        result.getUserRole()
                );
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                binding.tvError.setVisibility(View.VISIBLE);
                binding.tvError.setText(result.getError());
            }
        });
    }

    private void setupListeners() {
        binding.btnRegister.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String confirm = binding.etConfirmPassword.getText().toString().trim();

            if (name.isEmpty()) { binding.tilName.setError("Name is required"); return; }
            if (email.isEmpty()) { binding.tilEmail.setError("Email is required"); return; }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.setError("Invalid email"); return;
            }
            if (password.length() < 6) { binding.tilPassword.setError("Min 6 characters"); return; }
            if (!password.equals(confirm)) {
                binding.tilConfirmPassword.setError("Passwords don't match"); return;
            }

            binding.tilName.setError(null);
            binding.tilEmail.setError(null);
            binding.tilPassword.setError(null);
            binding.tilConfirmPassword.setError(null);
            binding.tvError.setVisibility(View.GONE);

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnRegister.setEnabled(false);

            viewModel.register(name, email, password);
        });

        binding.tvLogin.setOnClickListener(v -> finish());
    }
}
