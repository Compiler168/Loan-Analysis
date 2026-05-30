package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;

public class LoanStats {
    @SerializedName("total_predictions")
    public int totalPredictions;

    @SerializedName("approved_count")
    public int approvedCount;

    @SerializedName("rejection_count")
    public int rejectionCount;

    @SerializedName("approval_rate")
    public String approvalRate;

    @SerializedName("latest_probability")
    public Double latestProbability;
}
