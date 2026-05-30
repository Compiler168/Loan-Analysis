package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;

/**
 * Prediction result from POST /api/loans/predict
 */
public class PredictionResult {

    @SerializedName("ensemble")
    public Ensemble ensemble;

    @SerializedName("models")
    public Map<String, ModelResult> models;

    @SerializedName("risk_reasons")
    public List<RiskReason> riskReasons;

    @SerializedName("top_factors")
    public Map<String, Double> topFactors;

    @SerializedName("derived_metrics")
    public DerivedMetrics derivedMetrics;

    public static class Ensemble {
        public double probability;
        public boolean approved;
        public String confidence;
        @SerializedName("confidence_score")
        public double confidenceScore;
    }

    public static class ModelResult {
        public double probability;
        public boolean approved;
    }

    public static class RiskReason {
        public String factor;
        public String severity;
        public String message;
        public String suggestion;
    }

    public static class DerivedMetrics {
        @SerializedName("requested_emi")
        public double requestedEmi;

        @SerializedName("dti_ratio")
        public double dtiRatio;

        @SerializedName("savings_ratio")
        public double savingsRatio;
    }
}
