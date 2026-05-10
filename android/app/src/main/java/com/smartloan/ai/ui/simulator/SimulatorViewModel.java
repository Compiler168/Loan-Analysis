package com.smartloan.ai.ui.simulator;

import androidx.lifecycle.*;
import com.smartloan.ai.data.api.ApiClient;
import com.smartloan.ai.data.models.*;
import java.util.Map;
import retrofit2.*;

public class SimulatorViewModel extends ViewModel {
    private final MutableLiveData<SimulationResult> result = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<SimulationResult> getResult() { return result; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void simulate(Map<String, Object> data) {
        loading.setValue(true);
        ApiClient.getService().simulate(data).enqueue(new Callback<ApiResponse<SimulationResult>>() {
            @Override public void onResponse(Call<ApiResponse<SimulationResult>> c, Response<ApiResponse<SimulationResult>> r) {
                loading.postValue(false);
                if (r.isSuccessful() && r.body() != null && r.body().isSuccess()) result.postValue(r.body().getData());
                else error.postValue("Simulation failed");
            }
            @Override public void onFailure(Call<ApiResponse<SimulationResult>> c, Throwable t) {
                loading.postValue(false); error.postValue("Network error");
            }
        });
    }
}
