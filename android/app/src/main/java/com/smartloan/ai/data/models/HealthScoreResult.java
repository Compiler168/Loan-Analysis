package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Health score result from POST /api/financial/health-score
 */
public class HealthScoreResult {

    @SerializedName("overall_score")
    public int overallScore;

    public String grade;

    @SerializedName("grade_label")
    public String gradeLabel;

    public String summary;

    public List<Breakdown> breakdown;

    public List<Roadmap> roadmap;

    public static class Breakdown {
        public String category;
        public int score;
        public List<String> reasoning;
    }

    public static class Roadmap {
        public String category;
        public String priority;
        public List<String> actions;
    }
}
