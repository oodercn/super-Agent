package net.ooder.skillcenter.southbound;

/**
 * 故障转移处理器接口
 */
public interface FailoverHandler {

    void onMemberFailed(String groupId, String memberId);

    void startFailover(String groupId, String failedMemberId);

    void onFailoverComplete(String groupId, String newPrimaryId);
}
