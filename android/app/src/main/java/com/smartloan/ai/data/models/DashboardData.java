package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Dashboard data from GET /api/financial/dashboard
 */
public class DashboardData {

    @SerializedName("loan_probability")
    public double loanProbability;

    @SerializedName("health_score")
    public int healthScore;

    @SerializedName("risk_level")
    public String riskLevel;

    @SerializedName("credit_score")
    public int creditScore;

    @SerializedName("monthly_savings")
    public double monthlySavings;

    @SerializedName("dti_ratio")
    public double dtiRatio;

    @SerializedName("insights")
    public List<Insight> insights;

    @SerializedName("income_vs_expenses")
    public List<IncomeExpense> incomeVsExpenses;

    @SerializedName("financial_growth")
    public List<FinancialGrowth> financialGrowth;

    @SerializedName("risk_radar")
    public List<RiskRadar> riskRadar;

    @SerializedName("emi_forecast")
    public List<EmiForecast> emiForecast;

    @SerializedName("recent_activity")
    public List<RecentActivity> recentActivity;

    public static class Insight {
        public String type, icon, title, message;
    }

    public static class IncomeExpense {
        public String month;
        public double income, expenses;
    }

    public static class FinancialGrowth {
        public String month;
        public double savings, investments;
        @SerializedName("net_worth")
        public double netWorth;
    }

    public static class RiskRadar {
        public String category;
        public float value;
    }

    public static class EmiForecast {
        public String month;
        public double emi, remaining;
    }

    public static class RecentActivity {
        public String type, message, time, result;
    }
}
