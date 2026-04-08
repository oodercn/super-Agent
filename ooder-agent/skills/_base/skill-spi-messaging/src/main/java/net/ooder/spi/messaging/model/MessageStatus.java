package net.ooder.spi.messaging.model;

import net.ooder.scene.message.queue.DeliveryStatus;

public enum MessageStatus {
    SENDING,
    SENT,
    DELIVERED,
    READ,
    FAILED,
    EXPIRED;
    
    public static MessageStatus fromDeliveryStatus(DeliveryStatus status) {
        if (status == null) return SENDING;
        switch (status) {
            case CREATED:
            case SENDING:
            case PENDING:
                return SENDING;
            case DELIVERED:
                return DELIVERED;
            case READ:
                return READ;
            case FAILED:
                return FAILED;
            case EXPIRED:
                return EXPIRED;
            case ACKNOWLEDGED:
                return DELIVERED;
            case RETRYING:
                return SENDING;
            default:
                return SENDING;
        }
    }
    
    public DeliveryStatus toDeliveryStatus() {
        switch (this) {
            case SENDING:
                return DeliveryStatus.SENDING;
            case SENT:
                return DeliveryStatus.DELIVERED;
            case DELIVERED:
                return DeliveryStatus.DELIVERED;
            case READ:
                return DeliveryStatus.READ;
            case FAILED:
                return DeliveryStatus.FAILED;
            case EXPIRED:
                return DeliveryStatus.EXPIRED;
            default:
                return DeliveryStatus.PENDING;
        }
    }
}
