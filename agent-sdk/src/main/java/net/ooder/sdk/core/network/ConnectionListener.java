package net.ooder.sdk.core.network;

public interface ConnectionListener {
    
    void onConnected(CoreConnection connection);
    
    void onDisconnected(CoreConnection connection);
    
    void onError(CoreConnection connection, Throwable error);
    
    void onStateChanged(CoreConnection connection, ConnectionState oldState, ConnectionState newState);
}
