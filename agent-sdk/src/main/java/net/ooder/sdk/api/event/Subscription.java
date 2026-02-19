package net.ooder.sdk.api.event;

/**
 * Subscription handle for event bus subscribers
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class Subscription {

    private final String subscriptionId;
    private final String topic;
    private final long createdAt;
    private boolean active;

    public Subscription(String subscriptionId, String topic) {
        this.subscriptionId = subscriptionId;
        this.topic = topic;
        this.createdAt = System.currentTimeMillis();
        this.active = true;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getTopic() {
        return topic;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId='" + subscriptionId + '\'' +
                ", topic='" + topic + '\'' +
                ", active=" + active +
                '}';
    }
}
