package net.ooder.examples.travelagent;

import net.ooder.examples.travelagent.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/travel")
public class TravelAgentController {

    @Autowired
    private EmailSenderService emailSenderService;

    /**
     * Send meeting notification emails
     * @param request Send request
     * @return Send result
     */
    @PostMapping("/send-notification")
    public EmailSenderService.SendResult sendMeetingNotification(
            @RequestBody SendNotificationRequest request) {
        return emailSenderService.sendMeetingNotifications(
                request.getMeetingTime(),
                request.getMeetingPlace(),
                request.getContactPerson(),
                request.getTourName(),
                request.getEmails()
        );
    }

    /**
     * Send notification request class
     */
    public static class SendNotificationRequest {
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
}
