package net.ooder.skillcenter.p2p;

/**
 * P2P事件监听器接口
 * 用于监听P2P网络中的各种事件
 */
public interface P2PEventListener {
    /**
     * 当P2P事件发生时调用
     * @param eventType 事件类型
     * @param data 事件数据
     */
    void onEvent(P2PEventType eventType, Object data);
}