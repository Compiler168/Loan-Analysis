package com.smartloan.ai.ui.prediction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartloan.ai.data.api.ApiClient;
import com.smartloan.ai.data.models.ApiResponse;
import com.smartloan.ai.data.models.PredictionResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PredictionViewModel extends ViewModel {

    private final MutableLiveData<PredictionResult> result = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<PredictionResult> getResult() { return result; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void predict(Map<String, Object> formData) {
        loading.setValue(true);
        ApiClient.getService().predict(formData).enqueue(new Callback<ApiResponse<PredictionResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<PredictionResult>> call, Response<ApiResponse<PredictionResult>> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    result.postValue(response.body().getData());
                } else {
                    error.postValue("Prediction failed. Check ML service.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PredictionResult>> call, Throwable t) {
                loading.postValue(false);
                error.postValue("Network error: " + t.getMessage());
            }
        });
    }
}
