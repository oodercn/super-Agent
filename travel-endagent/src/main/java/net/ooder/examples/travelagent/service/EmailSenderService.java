package net.ooder.examples.travelagent.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EmailSenderService {

    /**
     * Send meeting notification emails
     * @param meetingTime Meeting time
     * @param meetingPlace Meeting place
     * @param contactPerson Contact person
     * @param tourName Tour name
     * @param emails List of email addresses
     * @return Send result
     */
    public SendResult sendMeetingNotifications(String meetingTime, String meetingPlace, 
                                             String contactPerson, String tourName, 
                                             List<String> emails) {
        SendResult result = new SendResult();
        result.setTotalEmails(emails.size());
        result.setSuccessCount(0);
        result.setFailedCount(0);
        result.setMeetingTime(meetingTime);
        result.setMeetingPlace(meetingPlace);
        result.setContactPerson(contactPerson);
        result.setTourName(tourName);
        result.setSentEmails(new ArrayList<>());
        result.setFailedEmails(new ArrayList<>());

        System.out.println("===== Travel Agency Email Sender ======");
        System.out.println("Meeting Time: " + meetingTime);
        System.out.println("Meeting Place: " + meetingPlace);
        System.out.println("Contact Person: " + contactPerson);
        System.out.println("Tour Name: " + tourName);
        System.out.println("=====================================");

        System.out.println("Found " + emails.size() + " email addresses");
        System.out.println("Email list:");
        for (int i = 0; i < emails.size(); i++) {
            System.out.println((i + 1) + ". " + emails.get(i));
        }
        System.out.println("=====================================");

        // Prepare email content
        String subject = tourName + " - Meeting Notification";
        String content = prepareEmailContent(meetingTime, meetingPlace, contactPerson, tourName);

        System.out.println("Email content prepared");
        System.out.println("Subject: " + subject);
        System.out.println("Content preview:");
        System.out.println(content.substring(0, Math.min(150, content.length())) + "...");
        System.out.println("=====================================");

        // Send emails
        System.out.println("Sending emails...");
        for (String email : emails) {
            System.out.println("\nSending to: " + email);
            try {
                // Simulate network delay
                TimeUnit.MILLISECONDS.sleep(300);
                System.out.println("SUCCESS: Email sent!");
                result.getSentEmails().add(email);
                result.setSuccessCount(result.getSuccessCount() + 1);
            } catch (Exception e) {
                System.out.println("FAILED: " + e.getMessage());
                result.getFailedEmails().add(email);
                result.setFailedCount(result.getFailedCount() + 1);
            }
        }

        System.out.println("\n===== Email Sending Summary =====");
        System.out.println("Total emails: " + result.getTotalEmails());
        System.out.println("Success: " + result.getSuccessCount());
        System.out.println("Failed: " + result.getFailedCount());
        System.out.println("Success rate: " + (result.getSuccessCount() * 100.0 / result.getTotalEmails()) + "%");
        System.out.println("=====================================");

        return result;
    }

    /**
     * Prepare email content
     */
    private String prepareEmailContent(String meetingTime, String meetingPlace, 
                                      String contactPerson, String tourName) {
        return "Dear Tourist,\n\n" +
               "Welcome to our '" + tourName + "' tour.\n\n" +
               "Important Meeting Information:\n" +
               "Time: " + meetingTime + "\n" +
               "Place: " + meetingPlace + "\n" +
               "Contact Person: " + contactPerson + "\n" +
               "Phone: 138-0013-8000\n\n" +
               "Please arrive at the meeting place on time.\n" +
               "Please bring your ID and necessary items.\n\n" +
               "If you have any questions, please contact our guide.\n\n" +
               "Have a nice trip!\n\n" +
               "Best regards,\n" +
               "Sunshine Travel Agency";
    }

    /**
     * Send result class
     */
    public static class SendResult {
        private int totalEmails;
        private int successCount;
        private int failedCount;
        private String meetingTime;
        private String meetingPlace;
        private String contactPerson;
        private String tourName;
        private List<String> sentEmails;
        private List<String> failedEmails;

        // Getters and setters
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
        public List<String> getSentEmails() { return sentEmails; }
        public void setSentEmails(List<String> sentEmails) { this.sentEmails = sentEmails; }
        public List<String> getFailedEmails() { return failedEmails; }
        public void setFailedEmails(List<String> failedEmails) { this.failedEmails = failedEmails; }
    }
}
