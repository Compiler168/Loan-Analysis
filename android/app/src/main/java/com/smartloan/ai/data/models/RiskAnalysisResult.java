package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Risk analysis result from POST /api/financial/risk-analysis
 */
public class RiskAnalysisResult {

    @SerializedName("risk_level")
    public String riskLevel;

    @SerializedName("overall_risk")
    public int overallRisk;

    @SerializedName("risk_color")
    public String riskColor;

    public String summary;

    public List<Dimension> dimensions;

    public static class Dimension {
        public String dimension;
        public String severity;
        public String value;
        public int score;
        public String message;
    }
}
