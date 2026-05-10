package com.smartloan.ai.ui.chatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartloan.ai.R;
import com.smartloan.ai.databinding.ItemChatMessageBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    public static class ChatMessage {
        public String role; // "user" or "assistant"
        public String content;
        public List<String> suggestions;

        public ChatMessage(String role, String content, List<String> suggestions) {
            this.role = role;
            this.content = content;
            this.suggestions = suggestions;
        }
    }

    public interface OnSuggestionClick {
        void onSuggestionClicked(String suggestion);
    }

    private final List<ChatMessage> messages = new ArrayList<>();
    private OnSuggestionClick listener;

    public void setOnSuggestionClickListener(OnSuggestionClick listener) {
        this.listener = listener;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public int getLastPosition() {
        return messages.size() - 1;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatMessageBinding binding = ItemChatMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ChatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() { return messages.size(); }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatMessageBinding binding;

        ChatViewHolder(ItemChatMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ChatMessage msg) {
            if ("user".equals(msg.role)) {
                binding.userLayout.setVisibility(View.VISIBLE);
                binding.botLayout.setVisibility(View.GONE);
                binding.tvUserMessage.setText(msg.content);
            } else {
                binding.botLayout.setVisibility(View.VISIBLE);
                binding.userLayout.setVisibility(View.GONE);
                binding.tvBotMessage.setText(msg.content);

                binding.suggestionsLayout.removeAllViews();
                if (msg.suggestions != null && !msg.suggestions.isEmpty()) {
                    binding.suggestionsLayout.setVisibility(View.VISIBLE);
                    for (String s : msg.suggestions) {
                        android.widget.TextView chip = new android.widget.TextView(itemView.getContext());
                        chip.setText(s);
                        chip.setTextSize(11f);
                        chip.setPadding(20, 8, 20, 8);
                        chip.setBackground(itemView.getContext().getDrawable(R.drawable.bg_muted_rounded));
                        android.widget.LinearLayout.LayoutParams lp =
                                new android.widget.LinearLayout.LayoutParams(
                                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 0, 8, 0);
                        chip.setLayoutParams(lp);
                        chip.setOnClickListener(v -> {
                            if (listener != null) listener.onSuggestionClicked(s);
                        });
                        binding.suggestionsLayout.addView(chip);
                    }
                } else {
                    binding.suggestionsLayout.setVisibility(View.GONE);
                }
            }
        }
    }
}
