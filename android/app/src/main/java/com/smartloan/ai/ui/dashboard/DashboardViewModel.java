package com.smartloan.ai.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartloan.ai.data.api.ApiClient;
import com.smartloan.ai.data.models.ApiResponse;
import com.smartloan.ai.data.models.DashboardData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<DashboardData> dashboardData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<DashboardData> getDashboardData() { return dashboardData; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void loadDashboard() {
        loading.setValue(true);
        ApiClient.getService().getDashboard().enqueue(new Callback<ApiResponse<DashboardData>>() {
            @Override
            public void onResponse(Call<ApiResponse<DashboardData>> call, Response<ApiResponse<DashboardData>> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    dashboardData.postValue(response.body().getData());
                } else {
                    error.postValue("Failed to load dashboard");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DashboardData>> call, Throwable t) {
                loading.postValue(false);
                error.postValue("Network error: " + t.getMessage());
            }
        });
    }
}
