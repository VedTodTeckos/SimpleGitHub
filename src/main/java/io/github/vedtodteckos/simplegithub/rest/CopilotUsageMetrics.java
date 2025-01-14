package io.github.vedtodteckos.simplegithub.rest;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Copilot usage metrics for an organization.
 */
@Value
@Builder
public class CopilotUsageMetrics {
    @SerializedName("daily_metrics")
    List<DailyMetrics> dailyMetrics;
    
    @SerializedName("total_suggestions_accepted")
    int totalSuggestionsAccepted;
    
    @SerializedName("total_lines_accepted")
    int totalLinesAccepted;
    
    @SerializedName("acceptance_rate")
    double acceptanceRate;

    /**
     * Daily usage metrics.
     */
    @Value
    @Builder
    public static class DailyMetrics {
        LocalDateTime date;
        
        @SerializedName("suggestions_accepted")
        int suggestionsAccepted;
        
        @SerializedName("lines_accepted")
        int linesAccepted;
        
        @SerializedName("active_users")
        int activeUsers;
    }
} 