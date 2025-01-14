package io.github.vedtodteckos.simplegithub.rest;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

/**
 * Information about Copilot seat allocation in an organization.
 */
@Value
@Builder
public class CopilotSeatInfo {
    @SerializedName("seats")
    int totalSeats;
    
    @SerializedName("used_seats")
    int usedSeats;
    
    @SerializedName("plan_name")
    String billingPlan;
    
    @SerializedName("public_code_suggestions")
    boolean publicCodeSuggestions;
    
    @SerializedName("is_business_plan")
    boolean businessPlan;
} 