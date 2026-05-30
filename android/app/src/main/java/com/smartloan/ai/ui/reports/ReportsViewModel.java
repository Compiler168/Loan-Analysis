package com.smartloan.ai.ui.reports;

import androidx.lifecycle.*;
import com.smartloan.ai.data.api.ApiClient;
import com.smartloan.ai.data.models.*;
import java.util.*;
import retrofit2.*;

public class ReportsViewModel extends ViewModel {
    private final MutableLiveData<ReportData> report = new MutableLiveData<>();
    private final MutableLiveData<List<ReportHistory>> history = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<ReportData> getReport() { return report; }
    public LiveData<List<ReportHistory>> getHistory() { return history; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void generateReport(String type) {
        loading.setValue(true);
        Map<String, String> body = new HashMap<>();
        body.put("type", type);
        ApiClient.getService().generateReport(body).enqueue(new Callback<ApiResponse<ReportData>>() {
            @Override public void onResponse(Call<ApiResponse<ReportData>> c, Response<ApiResponse<ReportData>> r) {
                loading.postValue(false);
                if (r.isSuccessful() && r.body() != null && r.body().isSuccess()) report.postValue(r.body().getData());
                else error.postValue("Report generation failed");
            }
            @Override public void onFailure(Call<ApiResponse<ReportData>> c, Throwable t) {
                loading.postValue(false); error.postValue("Network error");
            }
        });
    }

    public void loadHistory() {
        ApiClient.getService().getReportHistory().enqueue(new Callback<ApiResponse<List<ReportHistory>>>() {
            @Override public void onResponse(Call<ApiResponse<List<ReportHistory>>> c, Response<ApiResponse<List<ReportHistory>>> r) {
                if (r.isSuccessful() && r.body() != null && r.body().isSuccess()) history.postValue(r.body().getData());
            }
            @Override public void onFailure(Call<ApiResponse<List<ReportHistory>>> c, Throwable t) {}
        });
    }
}
