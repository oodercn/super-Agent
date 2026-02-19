package net.ooder.sdk.core.transport;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CoreTransport {
    
    String getTransportId();
    
    TransportType getTransportType();
    
    CompletableFuture<TransportResult> transmit(TransportMessage message);
    
    void registerHandler(TransportHandler handler);
    
    void unregisterHandler(TransportHandler handler);
    
    void start();
    
    void stop();
    
    boolean isRunning();
    
    default CompletableFuture<BatchResult> transmitBatch(List<TransportMessage> messages) {
        return CompletableFuture.supplyAsync(() -> {
            BatchResult result = new BatchResult();
            result.setTotalCount(messages.size());
            for (TransportMessage message : messages) {
                try {
                    TransportResult r = transmit(message).join();
                    if (r.isSuccess()) {
                        result.incrementSuccess();
                    } else {
                        result.incrementFailed();
                        result.addFailedMessageId(message.getMessageId());
                    }
                } catch (Exception e) {
                    result.incrementFailed();
                    result.addFailedMessageId(message.getMessageId());
                }
            }
            return result;
        });
    }
    
    default void acknowledge(String messageId) {}
    
    default void setAckListener(AckListener listener) {}
    
    default void setQos(int qos) {}
    
    default int getQos() { return 0; }
    
    default void setRetransmitEnabled(boolean enabled) {}
    
    default boolean isRetransmitEnabled() { return false; }
    
    default int getMaxRetransmitCount() { return 3; }
    
    default void setMaxRetransmitCount(int count) {}
}
