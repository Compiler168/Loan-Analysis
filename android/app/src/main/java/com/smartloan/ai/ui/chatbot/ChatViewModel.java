package com.smartloan.ai.ui.chatbot;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartloan.ai.data.api.ApiClient;
import com.smartloan.ai.data.models.ApiResponse;
import com.smartloan.ai.data.models.ChatResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends ViewModel {
    private final MutableLiveData<ChatResponse> chatResponse = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);

    public LiveData<ChatResponse> getChatResponse() { return chatResponse; }
    public LiveData<Boolean> getLoading() { return loading; }

    public void sendMessage(String message) {
        loading.setValue(true);
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        body.put("session_id", "main");

        ApiClient.getService().sendMessage(body).enqueue(new Callback<ApiResponse<ChatResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ChatResponse>> call, Response<ApiResponse<ChatResponse>> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    chatResponse.postValue(response.body().getData());
                } else {
                    ChatResponse err = new ChatResponse();
                    err.response = "Sorry, I couldn't process that. Please try again.";
                    chatResponse.postValue(err);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ChatResponse>> call, Throwable t) {
                loading.postValue(false);
                ChatResponse err = new ChatResponse();
                err.response = "Connection error. Please check your network.";
                chatResponse.postValue(err);
            }
        });
    }
}
