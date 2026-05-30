package com.smartloan.ai.ui.admin;

import androidx.lifecycle.*;
import com.smartloan.ai.data.api.ApiClient;
import com.smartloan.ai.data.models.*;
import java.util.List;
import retrofit2.*;

public class AdminViewModel extends ViewModel {
    private final MutableLiveData<AdminStats> stats = new MutableLiveData<>();
    private final MutableLiveData<List<AdminUser>> users = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<AdminStats> getStats() { return stats; }
    public LiveData<List<AdminUser>> getUsers() { return users; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void loadData() {
        loading.setValue(true);
        ApiClient.getService().getAdminStats().enqueue(new Callback<ApiResponse<AdminStats>>() {
            @Override public void onResponse(Call<ApiResponse<AdminStats>> c, Response<ApiResponse<AdminStats>> r) {
                if (r.isSuccessful() && r.body() != null && r.body().isSuccess()) stats.postValue(r.body().getData());
                loadUsers();
            }
            @Override public void onFailure(Call<ApiResponse<AdminStats>> c, Throwable t) {
                loading.postValue(false); error.postValue("Failed to load stats");
            }
        });
    }

    private void loadUsers() {
        ApiClient.getService().getAdminUsers().enqueue(new Callback<ApiResponse<List<AdminUser>>>() {
            @Override public void onResponse(Call<ApiResponse<List<AdminUser>>> c, Response<ApiResponse<List<AdminUser>>> r) {
                loading.postValue(false);
                if (r.isSuccessful() && r.body() != null && r.body().isSuccess()) users.postValue(r.body().getData());
            }
            @Override public void onFailure(Call<ApiResponse<List<AdminUser>>> c, Throwable t) {
                loading.postValue(false); error.postValue("Failed to load users");
            }
        });
    }
}
