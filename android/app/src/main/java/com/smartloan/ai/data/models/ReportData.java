package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReportData {
    public String id;
    public String type;
    public String title;

    @SerializedName("generated_at")
    public String generatedAt;

    public List<Section> sections;

    public static class Section {
        public String title;
        public String content;
        public List<Item> items;
    }

    public static class Item {
        public String label;
        public String value;
        public String status;
    }
}
