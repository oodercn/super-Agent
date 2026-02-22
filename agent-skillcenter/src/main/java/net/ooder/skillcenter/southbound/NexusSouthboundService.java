package net.ooder.skillcenter.southbound;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 南向独立服务接口
 * 
 * 提供Nexus在无北向支持时可独立使用的核心能力
 */
public interface NexusSouthboundService {

    CompletableFuture<StartupResult> startIndependent(SouthboundConfig config);

    boolean isIndependentMode();

    SouthboundStatus getStatus();

    CompletableFuture<List<PeerNode>> discoverLocalPeers();

    List<PeerNode> getKnownPeers();

    boolean isPeerOnline(String peerId);

    CompletableFuture<InstallResult> installSkill(String source);

    CompletableFuture<Boolean> uninstallSkill(String skillId);

    List<InstalledSkill> getInstalledSkills();

    CompletableFuture<ExecutionResult> executeSkill(String skillId, Map<String, Object> params);

    CompletableFuture<Boolean> shareSkillToPeer(String skillId, String peerId);

    CompletableFuture<Boolean> shareSkillToGroup(String skillId, String groupId);

    List<SharedSkill> getReceivedShares();

    CompletableFuture<Boolean> acceptShare(String shareId);

    CompletableFuture<Boolean> declineShare(String shareId);

    CompletableFuture<SceneGroup> createSceneGroup(SceneGroupConfig config);

    CompletableFuture<Boolean> joinSceneGroup(String groupId, String inviteCode);

    CompletableFuture<Boolean> leaveSceneGroup(String groupId);

    List<SceneGroup> getJoinedSceneGroups();

    List<GroupMember> getSceneGroupMembers(String groupId);

    KeyPair generateLocalKeyPair();

    String signData(String data, String privateKey);

    boolean verifySignature(String data, String signature, String publicKey);

    String encryptForPeer(String peerId, String data);

    String decryptFromPeer(String peerId, String encryptedData);
}
