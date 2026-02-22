package net.ooder.nexus.service.north;

import java.util.concurrent.CompletableFuture;

/**
 * 北向网络服务接口
 *
 * <p>提供 UDP/P2P 通信能力，用于与上层网络交互。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface NorthNetworkService {

    CompletableFuture<UdpResult> sendUdpMessage(String target, byte[] data);

    CompletableFuture<P2pResult> sendP2pMessage(String target, Object message);

    CompletableFuture<BroadcastResult> broadcast(byte[] data);

    void addPeerListener(PeerListener listener);

    void removePeerListener(PeerListener listener);

    class UdpResult {
        private boolean success;
        private String target;
        private int bytesSent;
        private String errorMessage;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getTarget() { return target; }
        public void setTarget(String target) { this.target = target; }
        public int getBytesSent() { return bytesSent; }
        public void setBytesSent(int bytesSent) { this.bytesSent = bytesSent; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    class P2pResult {
        private boolean success;
        private String targetId;
        private String messageId;
        private long latency;
        private String errorMessage;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public String getMessageId() { return messageId; }
        public void setMessageId(String messageId) { this.messageId = messageId; }
        public long getLatency() { return latency; }
        public void setLatency(long latency) { this.latency = latency; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    class BroadcastResult {
        private boolean success;
        private int recipientCount;
        private String errorMessage;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public int getRecipientCount() { return recipientCount; }
        public void setRecipientCount(int recipientCount) { this.recipientCount = recipientCount; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    interface PeerListener {
        void onPeerConnected(String peerId, String address);
        void onPeerDisconnected(String peerId);
        void onMessageReceived(String peerId, Object message);
    }
}
