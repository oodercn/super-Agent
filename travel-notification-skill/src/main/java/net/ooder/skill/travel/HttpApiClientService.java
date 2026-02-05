package net.ooder.skill.travel;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class HttpApiClientService {

    private static final String API_URL = "http://localhost:8083/api/travel/send-notification";

    /**
     * Call travel notification API
     * @param meetingTime Meeting time
     * @param meetingPlace Meeting place
     * @param contactPerson Contact person
     * @param tourName Tour name
     * @param emails List of email addresses
     * @return API response
     */
    public ApiResponse callTravelNotificationApi(String meetingTime, String meetingPlace, 
                                              String contactPerson, String tourName, 
                                              List<String> emails) {
        try {
            System.out.println("Calling travel notification API...");
            System.out.println("API URL: " + API_URL);
            System.out.println("Meeting Time: " + meetingTime);
            System.out.println("Meeting Place: " + meetingPlace);
            System.out.println("Contact Person: " + contactPerson);
            System.out.println("Tour Name: " + tourName);
            System.out.println("Emails: " + emails);
            
            // Create request body
            NotificationRequest request = new NotificationRequest();
            request.setMeetingTime(meetingTime);
            request.setMeetingPlace(meetingPlace);
            request.setContactPerson(contactPerson);
            request.setTourName(tourName);
            request.setEmails(emails);
            
            String requestBody = JSON.toJSONString(request);
            System.out.println("Request Body: " + requestBody);
            
            // Create HTTP connection
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Get response
            int responseCode = conn.getResponseCode();
            System.out.println("API Response Code: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("API Response: " + response.toString());
                    
                    // Parse response
                    ApiResponse apiResponse = JSON.parseObject(response.toString(), ApiResponse.class);
                    apiResponse.setSuccess(true);
                    return apiResponse;
                }
            } else {
                // Read error response
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    System.out.println("API Error Response: " + errorResponse.toString());
                }
                
                ApiResponse errorResponse = new ApiResponse();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("API call failed with response code: " + responseCode);
                return errorResponse;
            }
            
        } catch (Exception e) {
            System.out.println("Error calling travel notification API: " + e.getMessage());
            e.printStackTrace();
            
            ApiResponse errorResponse = new ApiResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Error calling API: " + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Notification request class
     */
    private static class NotificationRequest {
        private String meetingTime;
        private String meetingPlace;
        private String contactPerson;
        private String tourName;
        private List<String> emails;

        // Getters and setters
        public String getMeetingTime() { return meetingTime; }
        public void setMeetingTime(String meetingTime) { this.meetingTime = meetingTime; }
        public String getMeetingPlace() { return meetingPlace; }
        public void setMeetingPlace(String meetingPlace) { this.meetingPlace = meetingPlace; }
        public String getContactPerson() { return contactPerson; }
        public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
        public String getTourName() { return tourName; }
        public void setTourName(String tourName) { this.tourName = tourName; }
        public List<String> getEmails() { return emails; }
        public void setEmails(List<String> emails) { this.emails = emails; }
    }

    /**
     * API response class
     */
    public static class ApiResponse {
        private boolean success;
        private String message;
        private int totalEmails;
        private int successCount;
        private int failedCount;
        private String meetingTime;
        private String meetingPlace;
        private String contactPerson;
        private String tourName;

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public int getTotalEmails() { return totalEmails; }
        public void setTotalEmails(int totalEmails) { this.totalEmails = totalEmails; }
        public int getSuccessCount() { return successCount; }
        public void setSuccessCount(int successCount) { this.successCount = successCount; }
        public int getFailedCount() { return failedCount; }
        public void setFailedCount(int failedCount) { this.failedCount = failedCount; }
        public String getMeetingTime() { return meetingTime; }
        public void setMeetingTime(String meetingTime) { this.meetingTime = meetingTime; }
        public String getMeetingPlace() { return meetingPlace; }
        public void setMeetingPlace(String meetingPlace) { this.meetingPlace = meetingPlace; }
        public String getContactPerson() { return contactPerson; }
        public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
        public String getTourName() { return tourName; }
        public void setTourName(String tourName) { this.tourName = tourName; }
    }
}
