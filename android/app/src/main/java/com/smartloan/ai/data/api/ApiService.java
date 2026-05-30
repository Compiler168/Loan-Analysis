package com.smartloan.ai.data.api;

import com.smartloan.ai.data.models.ApiResponse;
import com.smartloan.ai.data.models.AuthData;
import com.smartloan.ai.data.models.LoginRequest;
import com.smartloan.ai.data.models.RegisterRequest;
import com.smartloan.ai.data.models.User;
import com.smartloan.ai.data.models.DashboardData;
import com.smartloan.ai.data.models.PredictionResult;
import com.smartloan.ai.data.models.LoanHistory;
import com.smartloan.ai.data.models.LoanStats;
import com.smartloan.ai.data.models.ChatResponse;
import com.smartloan.ai.data.models.HealthScoreResult;
import com.smartloan.ai.data.models.RiskAnalysisResult;
import com.smartloan.ai.data.models.SimulationResult;
import com.smartloan.ai.data.models.ReportData;
import com.smartloan.ai.data.models.ReportHistory;
import com.smartloan.ai.data.models.AdminStats;
import com.smartloan.ai.data.models.AdminUser;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Retrofit API interface defining all SmartLoan AI+ backend endpoints.
 */
public interface ApiService {

    // ==================== Auth ====================

    @POST("auth/login")
    Call<ApiResponse<AuthData>> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<ApiResponse<AuthData>> register(@Body RegisterRequest request);

    @GET("auth/me")
    Call<ApiResponse<User>> getMe();

    @PUT("auth/profile")
    Call<ApiResponse<User>> updateProfile(@Body Map<String, Object> data);

    // ==================== Dashboard ====================

    @GET("financial/dashboard")
    Call<ApiResponse<DashboardData>> getDashboard();

    // ==================== Loans ====================

    @POST("loans/predict")
    Call<ApiResponse<PredictionResult>> predict(@Body Map<String, Object> request);

    @GET("loans/history")
    Call<ApiResponse<List<LoanHistory>>> getLoanHistory();

    @GET("loans/stats")
    Call<ApiResponse<LoanStats>> getLoanStats();

    // ==================== Chat ====================

    @POST("chat/message")
    Call<ApiResponse<ChatResponse>> sendMessage(@Body Map<String, String> request);

    // ==================== Financial Analysis ====================

    @POST("financial/health-score")
    Call<ApiResponse<HealthScoreResult>> getHealthScore(@Body Map<String, Object> data);

    @POST("financial/risk-analysis")
    Call<ApiResponse<RiskAnalysisResult>> getRiskAnalysis(@Body Map<String, Object> data);

    @POST("financial/simulate")
    Call<ApiResponse<SimulationResult>> simulate(@Body Map<String, Object> request);

    // ==================== Reports ====================

    @POST("reports/generate")
    Call<ApiResponse<ReportData>> generateReport(@Body Map<String, String> data);

    @GET("reports/history")
    Call<ApiResponse<List<ReportHistory>>> getReportHistory();

    // ==================== Admin ====================

    @GET("admin/stats")
    Call<ApiResponse<AdminStats>> getAdminStats();

    @GET("admin/users")
    Call<ApiResponse<List<AdminUser>>> getAdminUsers();
}
