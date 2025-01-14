package io.github.vedtodteckos.simplegithub.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubRestClientTest {

    private GitHubRestClient client;
    private static final String TEST_TOKEN = "test-token";
    private static final String TEST_ORG = "test-org";
    private static final String TEST_USER = "test-user";

    @BeforeEach
    void setUp() throws IOException {
        client = new GitHubRestClient(TEST_TOKEN);
    }

    @Test
    void getCopilotSeats_shouldReturnSeatInfo() throws IOException {
        // Given
        String jsonResponse = """
            {
                "seats": 100,
                "used_seats": 50,
                "plan_name": "Business Plus",
                "public_code_suggestions": true,
                "is_business_plan": true
            }
            """;
        mockHttpConnection(jsonResponse);

        // When
        CopilotSeatInfo seatInfo = client.getCopilotSeats(TEST_ORG);

        // Then
        assertNotNull(seatInfo);
        assertEquals(100, seatInfo.getTotalSeats());
        assertEquals(50, seatInfo.getUsedSeats());
        assertEquals("Business Plus", seatInfo.getBillingPlan());
        assertTrue(seatInfo.isPublicCodeSuggestions());
        assertTrue(seatInfo.isBusinessPlan());
    }

    @Test
    void getCopilotUsage_shouldReturnUsageMetrics() throws IOException {
        // Given
        String jsonResponse = """
            {
                "daily_metrics": [{
                    "date": "2024-01-09T00:00:00Z",
                    "suggestions_accepted": 100,
                    "lines_accepted": 500,
                    "active_users": 10
                }],
                "total_suggestions_accepted": 100,
                "total_lines_accepted": 500,
                "acceptance_rate": 0.75
            }
            """;
        mockHttpConnection(jsonResponse);

        // When
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        CopilotUsageMetrics metrics = client.getCopilotUsage(TEST_ORG, startDate, endDate);

        // Then
        assertNotNull(metrics);
        assertEquals(100, metrics.getTotalSuggestionsAccepted());
        assertEquals(500, metrics.getTotalLinesAccepted());
        assertEquals(0.75, metrics.getAcceptanceRate());
        assertFalse(metrics.getDailyMetrics().isEmpty());

        CopilotUsageMetrics.DailyMetrics daily = metrics.getDailyMetrics().get(0);
        assertEquals(100, daily.getSuggestionsAccepted());
        assertEquals(500, daily.getLinesAccepted());
        assertEquals(10, daily.getActiveUsers());
    }

    @Test
    void getCopilotUserStatus_shouldReturnUserStatus() throws IOException {
        // Given
        String jsonResponse = """
            {
                "user_login": "test-user",
                "seat_assigned": true,
                "last_activity_date": "2024-01-09T10:00:00Z",
                "assignment_date": "2024-01-01T00:00:00Z",
                "assigned_by": "admin",
                "pending_cancellation": {
                    "effective_date": "2024-02-01T00:00:00Z",
                    "reason": "user_request"
                }
            }
            """;
        mockHttpConnection(jsonResponse);

        // When
        CopilotUserStatus status = client.getCopilotUserStatus(TEST_ORG, TEST_USER);

        // Then
        assertNotNull(status);
        assertEquals(TEST_USER, status.getUsername());
        assertTrue(status.isSeatAssigned());
        assertNotNull(status.getLastActivityDate());
        assertNotNull(status.getAssignmentDate());
        assertEquals("admin", status.getAssignedBy());
        
        assertNotNull(status.getPendingCancellation());
        assertEquals("user_request", status.getPendingCancellation().getReason());
    }

    @Test
    void assignCopilotSeat_shouldSendPutRequest() throws IOException {
        // Given
        mockHttpConnection("");

        // When/Then
        assertDoesNotThrow(() -> client.assignCopilotSeat(TEST_ORG, TEST_USER));
    }

    @Test
    void removeCopilotSeat_shouldSendDeleteRequest() throws IOException {
        // Given
        mockHttpConnection("");

        // When/Then
        assertDoesNotThrow(() -> client.removeCopilotSeat(TEST_ORG, TEST_USER));
    }

    private void mockHttpConnection(String response) throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(response.getBytes());
        
        when(connection.getInputStream()).thenReturn(inputStream);
        when(connection.getOutputStream()).thenReturn(System.out);
    }
} 