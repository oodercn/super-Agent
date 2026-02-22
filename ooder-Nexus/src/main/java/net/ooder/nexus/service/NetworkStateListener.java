package net.ooder.nexus.service;

/**
 * 网络状态监听器
 */
public interface NetworkStateListener {
    
    /**
     * 网络连接事件
     */
    void onNetworkConnected();
    
    /**
     * 网络断开事件
     */
    void onNetworkDisconnected();
    
    /**
     * 网络质量变更事件
     */
    void onNetworkQualityChanged(int quality);
}
