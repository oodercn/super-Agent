package net.ooder.sdk.core.transport;

public interface AckListener {
    
    void onAckReceived(String messageId, AckStatus status);
    
    default void onAckTimeout(String messageId) {}
    
    default void onAckFailed(String messageId, String error) {}
}
