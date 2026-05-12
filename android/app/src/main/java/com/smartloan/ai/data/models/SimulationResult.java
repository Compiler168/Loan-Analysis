package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Simulation result from POST /api/financial/simulate.
 * Mirrors the JSON shape returned by the ML simulation engine.
 */
public class SimulationResult {

    /** Plain-text summary string returned by the ML service. */
    public String summary;

    public Trajectory baseline;
    public Trajectory projected;
    public Comparison comparison;

    @SerializedName("chart_data")
    public List<ChartPoint> chartData;

    public List<Recommendation> recommendations;

    public static class Trajectory {
        public List<TrajectoryPoint> trajectory;

        @SerializedName("final_savings")
        public double finalSavings;

        @SerializedName("monthly_net")
        public double monthlyNet;

        @SerializedName("current_emi")
        public double currentEmi;

        @SerializedName("new_emi")
        public double newEmi;
    }

    public static class TrajectoryPoint {
        public int month;
        public double savings;

        @SerializedName("net_income")
        public double netIncome;

        @SerializedName("cumulative_interest")
        public double cumulativeInterest;
    }

    public static class Comparison {
        @SerializedName("savings_difference")
        public double savingsDifference;

        @SerializedName("monthly_difference")
        public double monthlyDifference;

        @SerializedName("emi_difference")
        public double emiDifference;

        @SerializedName("projection_months")
        public int projectionMonths;
    }

    public static class ChartPoint {
        public String month;
        public double baseline;
        public double projected;
    }

    public static class Recommendation {
        public String type;
        public String message;
    }
}
