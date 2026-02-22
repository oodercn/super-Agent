package net.ooder.nexus.skillcenter.dto.personal;

import net.ooder.nexus.skillcenter.dto.BaseDTO;
import java.util.List;

public class HelpContentDTO extends BaseDTO {

    private List<QuickStartDTO> quickStart;
    private List<FaqDTO> faq;
    private SupportDTO support;

    public HelpContentDTO() {}

    public List<QuickStartDTO> getQuickStart() {
        return quickStart;
    }

    public void setQuickStart(List<QuickStartDTO> quickStart) {
        this.quickStart = quickStart;
    }

    public List<FaqDTO> getFaq() {
        return faq;
    }

    public void setFaq(List<FaqDTO> faq) {
        this.faq = faq;
    }

    public SupportDTO getSupport() {
        return support;
    }

    public void setSupport(SupportDTO support) {
        this.support = support;
    }

    public static class QuickStartDTO {
        private String id;
        private String title;
        private String description;
        private String icon;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    public static class FaqDTO {
        private String id;
        private String question;
        private String answer;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }

    public static class SupportDTO {
        private String email;
        private String phone;
        private String hours;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }
    }
}
