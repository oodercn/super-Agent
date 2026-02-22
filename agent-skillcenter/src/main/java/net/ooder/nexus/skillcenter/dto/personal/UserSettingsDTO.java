package net.ooder.nexus.skillcenter.dto.personal;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class UserSettingsDTO extends BaseDTO {

    private NotificationSettingsDTO notifications;
    private PrivacySettingsDTO privacy;
    private InterfaceSettingsDTO interfaceSettings;

    public UserSettingsDTO() {}

    public NotificationSettingsDTO getNotifications() {
        return notifications;
    }

    public void setNotifications(NotificationSettingsDTO notifications) {
        this.notifications = notifications;
    }

    public PrivacySettingsDTO getPrivacy() {
        return privacy;
    }

    public void setPrivacy(PrivacySettingsDTO privacy) {
        this.privacy = privacy;
    }

    public InterfaceSettingsDTO getInterfaceSettings() {
        return interfaceSettings;
    }

    public void setInterfaceSettings(InterfaceSettingsDTO interfaceSettings) {
        this.interfaceSettings = interfaceSettings;
    }

    public static class NotificationSettingsDTO {
        private boolean email;
        private boolean push;
        private boolean sms;

        public boolean isEmail() {
            return email;
        }

        public void setEmail(boolean email) {
            this.email = email;
        }

        public boolean isPush() {
            return push;
        }

        public void setPush(boolean push) {
            this.push = push;
        }

        public boolean isSms() {
            return sms;
        }

        public void setSms(boolean sms) {
            this.sms = sms;
        }
    }

    public static class PrivacySettingsDTO {
        private boolean publicProfile;
        private boolean showSkills;
        private boolean showActivity;

        public boolean isPublicProfile() {
            return publicProfile;
        }

        public void setPublicProfile(boolean publicProfile) {
            this.publicProfile = publicProfile;
        }

        public boolean isShowSkills() {
            return showSkills;
        }

        public void setShowSkills(boolean showSkills) {
            this.showSkills = showSkills;
        }

        public boolean isShowActivity() {
            return showActivity;
        }

        public void setShowActivity(boolean showActivity) {
            this.showActivity = showActivity;
        }
    }

    public static class InterfaceSettingsDTO {
        private String theme;
        private String language;
        private boolean compactMode;

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public boolean isCompactMode() {
            return compactMode;
        }

        public void setCompactMode(boolean compactMode) {
            this.compactMode = compactMode;
        }
    }
}
