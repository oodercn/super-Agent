package net.ooder.nexus.service.enterprise;

import net.ooder.nexus.model.Result;

import java.util.List;

/**
 * Enterprise message service interface
 * Provides messaging capabilities through JMQ (JMS-like messaging)
 */
public interface MessageService {

    /**
     * Send message to a topic
     */
    Result<TopicMessage> sendTopicMessage(String topic, String message);

    /**
     * Send message to a specific user
     */
    Result<TopicMessage> sendToPerson(String account, String message);

    /**
     * Broadcast message to all subscribers
     */
    Result<TopicMessage> broadcast(String topic, String message);

    /**
     * Send message to a specific client session
     */
    Result<TopicMessage> sendToClient(String sessionId, String message);

    /**
     * Get user connection info
     */
    Result<JMQUser> getUserInfo();

    /**
     * Execute script on message server
     */
    Result<Boolean> execScript(String script);

    /**
     * Subscribe to a topic
     */
    Result<String> subscribe(String topic);

    /**
     * Unsubscribe from a topic
     */
    Result<Boolean> unsubscribe(String subscriptionId);

    /**
     * Get subscribed topics
     */
    Result<List<String>> getSubscribedTopics();

    /**
     * Topic message model
     */
    class TopicMessage {
        private String id;
        private String topic;
        private String message;
        private String sender;
        private long timestamp;
        private int priority;

        public TopicMessage() {
            this.timestamp = System.currentTimeMillis();
            this.priority = 5;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getTopic() { return topic; }
        public void setTopic(String topic) { this.topic = topic; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
    }

    /**
     * JMQ User model
     */
    class JMQUser {
        private String userId;
        private String userName;
        private String sessionId;
        private String clientType;
        private long connectTime;
        private List<String> subscribedTopics;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String sessionId) { this.sessionId = sessionId; }
        public String getClientType() { return clientType; }
        public void setClientType(String clientType) { this.clientType = clientType; }
        public long getConnectTime() { return connectTime; }
        public void setConnectTime(long connectTime) { this.connectTime = connectTime; }
        public List<String> getSubscribedTopics() { return subscribedTopics; }
        public void setSubscribedTopics(List<String> subscribedTopics) { this.subscribedTopics = subscribedTopics; }
    }
}
