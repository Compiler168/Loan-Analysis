package com.smartloan.ai.data.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName(value = "id", alternate = {"_id"})
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("role")
    private String role;

    @SerializedName("profile")
    private UserProfile profile;

    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public UserProfile getProfile() { return profile; }

    public static class UserProfile {
        @SerializedName("monthly_income")
        public double monthlyIncome;

        @SerializedName("monthly_expenses")
        public double monthlyExpenses;

        @SerializedName("credit_score")
        public int creditScore;

        @SerializedName("employment_status")
        public String employmentStatus;

        @SerializedName("employment_years")
        public int employmentYears;

        @SerializedName("existing_loans")
        public int existingLoans;

        @SerializedName("existing_emi")
        public double existingEmi;

        @SerializedName("savings_balance")
        public double savingsBalance;

        @SerializedName("dependents")
        public int dependents;

        @SerializedName("age")
        public int age;

        @SerializedName("property_value")
        public double propertyValue;
    }
}
