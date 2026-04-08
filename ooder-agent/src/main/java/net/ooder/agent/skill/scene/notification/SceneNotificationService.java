package net.ooder.mvp.skill.scene.notification;

import net.ooder.scene.skill.notification.NotificationService;
import net.ooder.scene.skill.notification.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SceneNotificationService {

    private static final Logger log = LoggerFactory.getLogger(SceneNotificationService.class);

    @Autowired(required = false)
    private NotificationService sdkNotificationService;

    private Map<String, List<NotificationRecord>> notificationHistory = new ConcurrentHashMap<>();
    private Map<String, UserNotificationPreference> userPreferences = new ConcurrentHashMap<>();

    public static class NotificationRecord {
        private String notificationId;
        private String userId;
        private String title;
        private String content;
        private NotificationService.PushChannel channel;
        private String activationId;
        private long sendTime;
        private boolean delivered;
        private String errorMessage;

        public String getNotificationId() { return notificationId; }
        public void setNotificationId(String notificationId) { this.notificationId = notificationId; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public NotificationService.PushChannel getChannel() { return channel; }
        public void setChannel(NotificationService.PushChannel channel) { this.channel = channel; }
        public String getActivationId() { return activationId; }
        public void setActivationId(String activationId) { this.activationId = activationId; }
        public long getSendTime() { return sendTime; }
        public void setSendTime(long sendTime) { this.sendTime = sendTime; }
        public boolean isDelivered() { return delivered; }
        public void setDelivered(boolean delivered) { this.delivered = delivered; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    public static class UserNotificationPreference {
        private String userId;
        private Map<NotificationService.PushChannel, Boolean> enabledChannels = new EnumMap<>(NotificationService.PushChannel.class);
        private boolean muteAll = false;
        private List<String> mutedScenes = new ArrayList<>();

        public UserNotificationPreference() {
            for (NotificationService.PushChannel channel : NotificationService.PushChannel.values()) {
                enabledChannels.put(channel, true);
            }
        }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public Map<NotificationService.PushChannel, Boolean> getEnabledChannels() { return enabledChannels; }
        public void setEnabledChannels(Map<NotificationService.PushChannel, Boolean> enabledChannels) { this.enabledChannels = enabledChannels; }
        public boolean isMuteAll() { return muteAll; }
        public void setMuteAll(boolean muteAll) { this.muteAll = muteAll; }
        public List<String> getMutedScenes() { return mutedScenes; }
        public void setMutedScenes(List<String> mutedScenes) { this.mutedScenes = mutedScenes; }
    }

    public boolean notifyUser(String userId, String title, String content, 
                              NotificationService.PushChannel channel) {
        log.info("[notifyUser] Sending to user {} via {}: {}", userId, channel, title);
        
        UserNotificationPreference pref = userPreferences.get(userId);
        if (pref != null) {
            if (pref.isMuteAll()) {
                log.info("[notifyUser] User {} has muted all notifications", userId);
                return false;
            }
            Boolean enabled = pref.getEnabledChannels().get(channel);
            if (enabled == null || !enabled) {
                log.info("[notifyUser] Channel {} is disabled for user {}", channel, userId);
                return false;
            }
        }
        
        NotificationRecord record = new NotificationRecord();
        record.setNotificationId(UUID.randomUUID().toString());
        record.setUserId(userId);
        record.setTitle(title);
        record.setContent(content);
        record.setChannel(channel);
        record.setSendTime(System.currentTimeMillis());
        
        try {
            if (sdkNotificationService != null) {
                sdkNotificationService.push(userId, title, content, channel);
                record.setDelivered(true);
                log.info("[notifyUser] Successfully sent notification to user {}", userId);
            } else {
                log.warn("[notifyUser] NotificationService not available, notification not sent");
                record.setDelivered(false);
                record.setErrorMessage("NotificationService not available");
            }
        } catch (Exception e) {
            log.error("[notifyUser] Failed to send notification: {}", e.getMessage());
            record.setDelivered(false);
            record.setErrorMessage(e.getMessage());
        }
        
        notificationHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(record);
        return record.isDelivered();
    }

    public boolean notifyParticipants(String activationId, NotificationMessage message) {
        log.info("[notifyParticipants] Sending to activation {}: {}", activationId, message.getTitle());
        
        try {
            if (sdkNotificationService != null) {
                sdkNotificationService.pushToParticipants(activationId, message);
                log.info("[notifyParticipants] Successfully sent notification to activation {}", activationId);
                return true;
            } else {
                log.warn("[notifyParticipants] NotificationService not available");
                return false;
            }
        } catch (Exception e) {
            log.error("[notifyParticipants] Failed to send notification: {}", e.getMessage());
            return false;
        }
    }

    public boolean notifyInstallComplete(String userId, String capabilityName, boolean success, String message) {
        String title = success ? "安装成功" : "安装失败";
        String content = String.format("能力 %s %s。%s", 
            capabilityName, 
            success ? "安装成功" : "安装失败",
            message != null ? message : "");
        
        return notifyUser(userId, title, content, NotificationService.PushChannel.IN_APP);
    }

    public boolean notifySceneActivated(String userId, String sceneName, String sceneGroupId) {
        String title = "场景已激活";
        String content = String.format("场景 %s 已成功激活，可以开始使用了。", sceneName);
        
        return notifyUser(userId, title, content, NotificationService.PushChannel.IN_APP);
    }

    public boolean notifySceneDelegated(String userId, String sceneName, String fromUser) {
        String title = "场景委托通知";
        String content = String.format("用户 %s 将场景 %s 委托给您处理。", fromUser, sceneName);
        
        return notifyUser(userId, title, content, NotificationService.PushChannel.IN_APP);
    }

    public List<NotificationRecord> getNotificationHistory(String userId) {
        return notificationHistory.getOrDefault(userId, new ArrayList<>());
    }

    public void setUserPreference(String userId, UserNotificationPreference preference) {
        userPreferences.put(userId, preference);
        log.info("[setUserPreference] Set notification preference for user {}", userId);
    }

    public UserNotificationPreference getUserPreference(String userId) {
        return userPreferences.computeIfAbsent(userId, k -> new UserNotificationPreference());
    }

    public void enableChannel(String userId, NotificationService.PushChannel channel, boolean enabled) {
        UserNotificationPreference pref = getUserPreference(userId);
        pref.getEnabledChannels().put(channel, enabled);
        log.info("[enableChannel] {} channel {} for user {}", 
            enabled ? "Enabled" : "Disabled", channel, userId);
    }
}
