package net.ooder.sdk.nexus.offline.model;

public interface NetworkStateListener {
    
    void onNetworkAvailable();
    
    void onNetworkLost();
    
    void onNetworkStateChanged(NetworkState state);
}
