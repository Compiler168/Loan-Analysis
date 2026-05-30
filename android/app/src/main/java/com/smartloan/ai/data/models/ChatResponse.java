package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ChatResponse {
    public String response;
    public String intent;
    public double confidence;
    public List<String> suggestions;
    public String timestamp;
}
