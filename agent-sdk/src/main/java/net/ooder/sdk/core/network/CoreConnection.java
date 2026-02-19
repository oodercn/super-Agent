package net.ooder.sdk.core.network;

import java.util.concurrent.CompletableFuture;

public interface CoreConnection {
    
    String getConnectionId();
    
    ConnectionType getType();
    
    ConnectionState getState();
    
    CompletableFuture<Void> connect();
    
    CompletableFuture<Void> disconnect();
    
    CompletableFuture<byte[]> send(byte[] data);
    
    CompletableFuture<byte[]> receive();
    
    void setConnectionListener(ConnectionListener listener);
    
    void setTimeout(long timeoutMs);
    
    long getTimeout();
}
