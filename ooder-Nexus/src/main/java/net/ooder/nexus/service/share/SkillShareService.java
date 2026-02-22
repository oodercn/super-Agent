package net.ooder.nexus.service.share;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * P2P 技能分享服务接口
 *
 * <p>提供技能的 P2P 分享能力，支持加密传输。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface SkillShareService {

    /**
     * 准备分享技能
     *
     * @param skillId 技能 ID
     * @param config 分享配置
     * @return 分享准备结果
     */
    CompletableFuture<SharePrepareResultDTO> prepareShare(String skillId, ShareConfigDTO config);

    /**
     * 发送分享邀请
     *
     * @param targetPeerId 目标节点 ID
     * @param shareId 分享 ID
     * @return 邀请发送结果
     */
    CompletableFuture<Void> sendShareInvitation(String targetPeerId, String shareId);

    /**
     * 接受分享邀请
     *
     * @param invitationId 邀请 ID
     * @return 接受结果
     */
    CompletableFuture<Void> acceptShareInvitation(String invitationId);

    /**
     * 拒绝分享邀请
     *
     * @param invitationId 邀请 ID
     * @return 拒绝结果
     */
    CompletableFuture<Void> declineShareInvitation(String invitationId);

    /**
     * 获取待处理分享邀请
     *
     * @return 邀请列表
     */
    CompletableFuture<List<ShareInvitationDTO>> getPendingShareInvitations();

    /**
     * 获取分享进度
     *
     * @param shareId 分享 ID
     * @return 分享进度
     */
    CompletableFuture<ShareProgressDTO> getShareProgress(String shareId);

    /**
     * 取消分享
     *
     * @param shareId 分享 ID
     * @return 取消结果
     */
    CompletableFuture<Void> cancelShare(String shareId);

    /**
     * 获取分享历史
     *
     * @return 分享历史列表
     */
    CompletableFuture<List<ShareRecordDTO>> getShareHistory();

    /**
     * 添加分享事件监听器
     *
     * @param listener 监听器
     */
    void addShareListener(ShareEventListener listener);

    /**
     * 移除分享事件监听器
     *
     * @param listener 监听器
     */
    void removeShareListener(ShareEventListener listener);

    /**
     * 分享配置 DTO
     */
    class ShareConfigDTO {
        private String targetPeerId;
        private boolean encrypt;
        private boolean compress;
        private long expireDuration;
        private String message;

        public String getTargetPeerId() { return targetPeerId; }
        public void setTargetPeerId(String targetPeerId) { this.targetPeerId = targetPeerId; }
        public boolean isEncrypt() { return encrypt; }
        public void setEncrypt(boolean encrypt) { this.encrypt = encrypt; }
        public boolean isCompress() { return compress; }
        public void setCompress(boolean compress) { this.compress = compress; }
        public long getExpireDuration() { return expireDuration; }
        public void setExpireDuration(long expireDuration) { this.expireDuration = expireDuration; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    /**
     * 分享准备结果 DTO
     */
    class SharePrepareResultDTO {
        private String shareId;
        private String skillId;
        private String skillName;
        private String skillVersion;
        private long packageSize;
        private String checksum;
        private boolean ready;

        public String getShareId() { return shareId; }
        public void setShareId(String shareId) { this.shareId = shareId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }
        public String getSkillVersion() { return skillVersion; }
        public void setSkillVersion(String skillVersion) { this.skillVersion = skillVersion; }
        public long getPackageSize() { return packageSize; }
        public void setPackageSize(long packageSize) { this.packageSize = packageSize; }
        public String getChecksum() { return checksum; }
        public void setChecksum(String checksum) { this.checksum = checksum; }
        public boolean isReady() { return ready; }
        public void setReady(boolean ready) { this.ready = ready; }
    }

    /**
     * 分享邀请 DTO
     */
    class ShareInvitationDTO {
        private String invitationId;
        private String shareId;
        private String skillId;
        private String skillName;
        private String skillVersion;
        private String senderId;
        private String senderName;
        private long packageSize;
        private String message;
        private long createdAt;
        private long expiresAt;
        private String status;

        public String getInvitationId() { return invitationId; }
        public void setInvitationId(String invitationId) { this.invitationId = invitationId; }
        public String getShareId() { return shareId; }
        public void setShareId(String shareId) { this.shareId = shareId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }
        public String getSkillVersion() { return skillVersion; }
        public void setSkillVersion(String skillVersion) { this.skillVersion = skillVersion; }
        public String getSenderId() { return senderId; }
        public void setSenderId(String senderId) { this.senderId = senderId; }
        public String getSenderName() { return senderName; }
        public void setSenderName(String senderName) { this.senderName = senderName; }
        public long getPackageSize() { return packageSize; }
        public void setPackageSize(long packageSize) { this.packageSize = packageSize; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public long getExpiresAt() { return expiresAt; }
        public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * 分享进度 DTO
     */
    class ShareProgressDTO {
        private String shareId;
        private String stage;
        private int progress;
        private long bytesTransferred;
        private long totalBytes;
        private long transferSpeed;
        private String status;
        private String errorMessage;

        public String getShareId() { return shareId; }
        public void setShareId(String shareId) { this.shareId = shareId; }
        public String getStage() { return stage; }
        public void setStage(String stage) { this.stage = stage; }
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        public long getBytesTransferred() { return bytesTransferred; }
        public void setBytesTransferred(long bytesTransferred) { this.bytesTransferred = bytesTransferred; }
        public long getTotalBytes() { return totalBytes; }
        public void setTotalBytes(long totalBytes) { this.totalBytes = totalBytes; }
        public long getTransferSpeed() { return transferSpeed; }
        public void setTransferSpeed(long transferSpeed) { this.transferSpeed = transferSpeed; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }

    /**
     * 分享记录 DTO
     */
    class ShareRecordDTO {
        private String shareId;
        private String skillId;
        private String skillName;
        private String direction;
        private String peerId;
        private String peerName;
        private long completedAt;
        private String status;

        public String getShareId() { return shareId; }
        public void setShareId(String shareId) { this.shareId = shareId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getSkillName() { return skillName; }
        public void setSkillName(String skillName) { this.skillName = skillName; }
        public String getDirection() { return direction; }
        public void setDirection(String direction) { this.direction = direction; }
        public String getPeerId() { return peerId; }
        public void setPeerId(String peerId) { this.peerId = peerId; }
        public String getPeerName() { return peerName; }
        public void setPeerName(String peerName) { this.peerName = peerName; }
        public long getCompletedAt() { return completedAt; }
        public void setCompletedAt(long completedAt) { this.completedAt = completedAt; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    /**
     * 分享事件监听器
     */
    interface ShareEventListener {
        void onInvitationReceived(ShareInvitationDTO invitation);
        void onInvitationAccepted(String invitationId);
        void onInvitationDeclined(String invitationId);
        void onShareStarted(String shareId);
        void onShareProgress(String shareId, ShareProgressDTO progress);
        void onShareCompleted(String shareId);
        void onShareFailed(String shareId, String errorMessage);
        void onInstallStarted(String shareId);
        void onInstallCompleted(String shareId, String skillId);
    }
}
