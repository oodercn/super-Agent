package net.ooder.sdk.southbound.protocol.model;

import java.util.Map;

public interface CollaborationListener {
    
    void onInvitationReceived(InvitationInfo invitation);
    
    void onGroupJoined(SceneGroupInfo group);
    
    void onGroupLeft(String groupId);
    
    void onTaskAssigned(TaskInfo task);
    
    void onTaskCompleted(String taskId, TaskResult result);
    
    void onMemberJoined(String groupId, MemberInfo member);
    
    void onMemberLeft(String groupId, String memberId);
    
    void onStateChanged(String groupId, Map<String, Object> state);
}
