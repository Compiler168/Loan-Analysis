package com.smartloan.ai.ui.chatbot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.smartloan.ai.R;
import com.smartloan.ai.databinding.FragmentChatbotBinding;

import java.util.Arrays;
import java.util.List;

public class ChatbotFragment extends Fragment {

    private FragmentChatbotBinding binding;
    private ChatViewModel viewModel;
    private ChatAdapter adapter;

    private final String[] PROMPTS = {
            "Am I eligible for a loan?", "How to improve my credit score?",
            "Budget tips for saving more", "What are my financial risks?",
            "Calculate my EMI", "Explain DTI ratio"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatbotBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        setupRecyclerView();
        setupSuggestionChips();
        setupListeners();
        observeData();

        // Welcome message
        adapter.addMessage(new ChatAdapter.ChatMessage("assistant",
                "Hello! 👋 I'm your SmartLoan AI Financial Advisor. Ask me anything about loans, credit, budgeting, or financial planning!",
                Arrays.asList(PROMPTS[0], PROMPTS[1], PROMPTS[2], PROMPTS[3])));
    }

    private void setupRecyclerView() {
        adapter = new ChatAdapter();
        adapter.setOnSuggestionClickListener(this::sendMessage);
        binding.rvMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMessages.setAdapter(adapter);
    }

    private void setupSuggestionChips() {
        for (String prompt : PROMPTS) {
            TextView chip = new TextView(requireContext());
            chip.setText(prompt);
            chip.setTextSize(13f);
            chip.setPadding(24, 12, 24, 12);
            chip.setBackground(requireContext().getDrawable(R.drawable.bg_muted_rounded));
            chip.setTextColor(getResources().getColor(R.color.on_surface, null));
            android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 12, 0);
            chip.setLayoutParams(lp);
            chip.setOnClickListener(v -> sendMessage(prompt));
            binding.chipsContainer.addView(chip);
        }
    }

    private void setupListeners() {
        binding.btnSend.setOnClickListener(v -> {
            String msg = binding.etMessage.getText().toString().trim();
            if (!msg.isEmpty()) sendMessage(msg);
        });

        binding.etMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String msg = binding.etMessage.getText().toString().trim();
                if (!msg.isEmpty()) sendMessage(msg);
                return true;
            }
            return false;
        });
    }

    private void sendMessage(String text) {
        binding.etMessage.setText("");
        binding.suggestionsScroll.setVisibility(View.GONE);

        adapter.addMessage(new ChatAdapter.ChatMessage("user", text, null));
        scrollToBottom();
        viewModel.sendMessage(text);
    }

    private void observeData() {
        viewModel.getChatResponse().observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                List<String> suggestions = response.suggestions;
                adapter.addMessage(new ChatAdapter.ChatMessage("assistant", response.response, suggestions));
                scrollToBottom();
            }
        });
    }

    private void scrollToBottom() {
        binding.rvMessages.postDelayed(() -> {
            if (adapter.getItemCount() > 0) {
                binding.rvMessages.smoothScrollToPosition(adapter.getLastPosition());
            }
        }, 100);
    }

    @Override
    public void onDestroyView() { super.onDestroyView(); binding = null; }
}
