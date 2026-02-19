package net.ooder.sdk.core.transport;

public interface TransportHandler {
    
    void onMessage(TransportMessage message);
    
    void onResult(TransportResult result);
    
    void onError(TransportError error);
}
