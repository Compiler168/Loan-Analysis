package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Simulation result from POST /api/financial/simulate
 */
public class SimulationResult {

    public Summary summary;
    public Comparison comparison;

    @SerializedName("chart_data")
    public List<ChartPoint> chartData;

    public List<Recommendation> recommendations;

    public static class Summary {
        @SerializedName("baseline_monthly_savings")
        public double baselineMonthlySavings;

        @SerializedName("projected_monthly_savings")
        public double projectedMonthlySavings;
    }

    public static class Comparison {
        @SerializedName("savings_difference")
        public double savingsDifference;

        @SerializedName("monthly_difference")
        public double monthlyDifference;

        @SerializedName("emi_difference")
        public double emiDifference;
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
