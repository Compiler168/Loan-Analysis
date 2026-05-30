package com.smartloan.ai.ui.analysis;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.smartloan.ai.data.api.ApiClient;
import com.smartloan.ai.data.models.*;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalysisViewModel extends ViewModel {
    private final MutableLiveData<HealthScoreResult> healthResult = new MutableLiveData<>();
    private final MutableLiveData<RiskAnalysisResult> riskResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<HealthScoreResult> getHealthResult() { return healthResult; }
    public LiveData<RiskAnalysisResult> getRiskResult() { return riskResult; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void runHealthScore(Map<String, Object> data) {
        loading.setValue(true);
        ApiClient.getService().getHealthScore(data).enqueue(new Callback<ApiResponse<HealthScoreResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<HealthScoreResult>> c, Response<ApiResponse<HealthScoreResult>> r) {
                loading.postValue(false);
                if (r.isSuccessful() && r.body() != null && r.body().isSuccess()) healthResult.postValue(r.body().getData());
                else error.postValue("Health score failed");
            }
            @Override
            public void onFailure(Call<ApiResponse<HealthScoreResult>> c, Throwable t) {
                loading.postValue(false); error.postValue("Network error");
            }
        });
    }

    public void runRiskAnalysis(Map<String, Object> data) {
        loading.setValue(true);
        ApiClient.getService().getRiskAnalysis(data).enqueue(new Callback<ApiResponse<RiskAnalysisResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<RiskAnalysisResult>> c, Response<ApiResponse<RiskAnalysisResult>> r) {
                loading.postValue(false);
                if (r.isSuccessful() && r.body() != null && r.body().isSuccess()) riskResult.postValue(r.body().getData());
                else error.postValue("Risk analysis failed");
            }
            @Override
            public void onFailure(Call<ApiResponse<RiskAnalysisResult>> c, Throwable t) {
                loading.postValue(false); error.postValue("Network error");
            }
        });
    }
}
