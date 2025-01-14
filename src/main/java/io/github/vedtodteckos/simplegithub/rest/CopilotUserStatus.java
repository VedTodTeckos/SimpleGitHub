package io.github.vedtodteckos.simplegithub.rest;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Status of a user's Copilot access in an organization.
 */
@Value
@Builder
public class CopilotUserStatus {
    @SerializedName("user_login")
    String username;
    
    @SerializedName("seat_assigned")
    boolean seatAssigned;
    
    @SerializedName("last_activity_date")
    LocalDateTime lastActivityDate;
    
    @SerializedName("assignment_date")
    LocalDateTime assignmentDate;
    
    @SerializedName("assigned_by")
    String assignedBy;
    
    @SerializedName("pending_cancellation")
    PendingCancellation pendingCancellation;

    /**
     * Information about pending cancellation of Copilot access.
     */
    @Value
    @Builder
    public static class PendingCancellation {
        @SerializedName("effective_date")
        LocalDateTime effectiveDate;
        
        String reason;
    }
} 