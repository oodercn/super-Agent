package net.ooder.sdk.core.protocol;

public interface CoreProtocol {
    
    String getProtocolType();
    
    String getProtocolVersion();
    
    byte[] encode(Object message) throws ProtocolException;
    
    <T> T decode(byte[] data, Class<T> type) throws ProtocolException;
    
    boolean validate(byte[] data);
    
    ProtocolMetadata getMetadata();
}
