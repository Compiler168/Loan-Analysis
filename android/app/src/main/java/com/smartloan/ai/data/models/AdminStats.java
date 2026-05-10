package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AdminStats {
    @SerializedName("total_users")
    public int totalUsers;

    @SerializedName("active_sessions")
    public int activeSessions;

    @SerializedName("predictions_today")
    public int predictionsToday;

    @SerializedName("total_predictions")
    public int totalPredictions;

    @SerializedName("avg_health_score")
    public int avgHealthScore;

    @SerializedName("chatbot_sessions")
    public int chatbotSessions;

    @SerializedName("system_uptime")
    public String systemUptime;

    @SerializedName("ml_service_status")
    public String mlServiceStatus;

    @SerializedName("monthly_stats")
    public List<MonthlyStat> monthlyStats;

    public static class MonthlyStat {
        public String month;
        public int users;
        public int predictions;
    }
}
