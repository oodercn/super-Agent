package net.ooder.skill.travel;

import net.ooder.skill.travel.HttpApiClientService.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skill/travel")
public class TravelNotificationController {

    @Autowired
    private ExcelReaderService excelReaderService;

    @Autowired
    private HttpApiClientService httpApiClientService;

    /**
     * Process travel notification request
     * @param request Skill request
     * @return Skill response
     */
    @PostMapping("/notify")
    public SkillResponse processNotificationRequest(@RequestBody SkillRequest request) {
        System.out.println("===== Travel Notification Skill =====");
        System.out.println("User Input: " + request.getUserInput());
        System.out.println("Excel File: " + request.getExcelFile());
        System.out.println("Meeting Time: " + request.getMeetingTime());
        System.out.println("Meeting Place: " + request.getMeetingPlace());
        System.out.println("=====================================");

        try {
            // Read emails from Excel
            List<String> emails = excelReaderService.readEmailsFromExcel(request.getExcelFile());
            
            // Call notification API
            ApiResponse apiResponse = httpApiClientService.callTravelNotificationApi(
                    request.getMeetingTime(),
                    request.getMeetingPlace(),
                    "李导游", // Default contact person
                    "故宫一日游", // Default tour name
                    emails
            );

            // Create skill response
            SkillResponse response = new SkillResponse();
            response.setSuccess(apiResponse.isSuccess());
            response.setMessage(apiResponse.isSuccess() ? 
                    "邮件通知发送成功！" : 
                    "邮件通知发送失败：" + apiResponse.getMessage());
            response.setTotalEmails(apiResponse.getTotalEmails());
            response.setSuccessCount(apiResponse.getSuccessCount());
            response.setFailedCount(apiResponse.getFailedCount());
            response.setMeetingTime(apiResponse.getMeetingTime());
            response.setMeetingPlace(apiResponse.getMeetingPlace());

            System.out.println("===== Skill Response =====");
            System.out.println("Success: " + response.isSuccess());
            System.out.println("Message: " + response.getMessage());
            System.out.println("Total Emails: " + response.getTotalEmails());
            System.out.println("Success Count: " + response.getSuccessCount());
            System.out.println("Failed Count: " + response.getFailedCount());
            System.out.println("=====================================");

            return response;

        } catch (Exception e) {
            System.out.println("Error processing notification request: " + e.getMessage());
            e.printStackTrace();

            SkillResponse errorResponse = new SkillResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("处理请求时发生错误：" + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Process guest feedback request
     * @param request Feedback request
     * @return Feedback response
     */
    @PostMapping("/feedback")
    public FeedbackResponse processFeedbackRequest(@RequestBody FeedbackRequest request) {
        System.out.println("===== Guest Feedback Form =====");
        System.out.println("Guest Name: " + request.getGuestName());
        System.out.println("Contact Phone: " + request.getContactPhone());
        System.out.println("Need Ticket: " + request.isNeedTicket());
        System.out.println("Need Language Service: " + request.isNeedLanguageService());
        System.out.println("Language Type: " + request.getLanguageType());
        System.out.println("Need Wheelchair: " + request.isNeedWheelchair());
        System.out.println("Special Requirements: " + request.getSpecialRequirements());
        System.out.println("=====================================");

        try {
            // Process feedback (save to file, database, etc.)
            // For now, just log and return success
            
            FeedbackResponse response = new FeedbackResponse();
            response.setSuccess(true);
            response.setMessage("反馈提交成功！");
            response.setGuestName(request.getGuestName());
            response.setNeedTicket(request.isNeedTicket());
            response.setNeedLanguageService(request.isNeedLanguageService());
            response.setNeedWheelchair(request.isNeedWheelchair());

            System.out.println("===== Feedback Response =====");
            System.out.println("Success: " + response.isSuccess());
            System.out.println("Message: " + response.getMessage());
            System.out.println("Guest Name: " + response.getGuestName());
            System.out.println("=====================================");

            return response;

        } catch (Exception e) {
            System.out.println("Error processing feedback request: " + e.getMessage());
            e.printStackTrace();

            FeedbackResponse errorResponse = new FeedbackResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("处理反馈时发生错误：" + e.getMessage());
            return errorResponse;
        }
    }

    /**
     * Skill request class
     */
    public static class SkillRequest {
        private String userInput;
        private String excelFile;
        private String meetingTime;
        private String meetingPlace;

        // Getters and setters
        public String getUserInput() { return userInput; }
        public void setUserInput(String userInput) { this.userInput = userInput; }
        public String getExcelFile() { return excelFile; }
        public void setExcelFile(String excelFile) { this.excelFile = excelFile; }
        public String getMeetingTime() { return meetingTime; }
        public void setMeetingTime(String meetingTime) { this.meetingTime = meetingTime; }
        public String getMeetingPlace() { return meetingPlace; }
        public void setMeetingPlace(String meetingPlace) { this.meetingPlace = meetingPlace; }
    }

    /**
     * Skill response class
     */
    public static class SkillResponse {
        private boolean success;
        private String message;
        private int totalEmails;
        private int successCount;
        private int failedCount;
        private String meetingTime;
        private String meetingPlace;

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
    }

    /**
     * Feedback request class
     */
    public static class FeedbackRequest {
        private String guestName;
        private String contactPhone;
        private boolean needTicket;
        private boolean needLanguageService;
        private String languageType;
        private boolean needWheelchair;
        private String specialRequirements;

        // Getters and setters
        public String getGuestName() { return guestName; }
        public void setGuestName(String guestName) { this.guestName = guestName; }
        public String getContactPhone() { return contactPhone; }
        public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
        public boolean isNeedTicket() { return needTicket; }
        public void setNeedTicket(boolean needTicket) { this.needTicket = needTicket; }
        public boolean isNeedLanguageService() { return needLanguageService; }
        public void setNeedLanguageService(boolean needLanguageService) { this.needLanguageService = needLanguageService; }
        public String getLanguageType() { return languageType; }
        public void setLanguageType(String languageType) { this.languageType = languageType; }
        public boolean isNeedWheelchair() { return needWheelchair; }
        public void setNeedWheelchair(boolean needWheelchair) { this.needWheelchair = needWheelchair; }
        public String getSpecialRequirements() { return specialRequirements; }
        public void setSpecialRequirements(String specialRequirements) { this.specialRequirements = specialRequirements; }
    }

    /**
     * Feedback response class
     */
    public static class FeedbackResponse {
        private boolean success;
        private String message;
        private String guestName;
        private boolean needTicket;
        private boolean needLanguageService;
        private boolean needWheelchair;

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getGuestName() { return guestName; }
        public void setGuestName(String guestName) { this.guestName = guestName; }
        public boolean isNeedTicket() { return needTicket; }
        public void setNeedTicket(boolean needTicket) { this.needTicket = needTicket; }
        public boolean isNeedLanguageService() { return needLanguageService; }
        public void setNeedLanguageService(boolean needLanguageService) { this.needLanguageService = needLanguageService; }
        public boolean isNeedWheelchair() { return needWheelchair; }
        public void setNeedWheelchair(boolean needWheelchair) { this.needWheelchair = needWheelchair; }
    }
}
