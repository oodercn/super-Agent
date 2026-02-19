package net.ooder.sdk.api.event;

public class MemberJoinedEvent extends Event {
    
    private String groupId;
    private String memberId;
    
    public MemberJoinedEvent() {
        super();
    }
    
    public MemberJoinedEvent(String groupId, String memberId) {
        super("SceneGroupManager");
        this.groupId = groupId;
        this.memberId = memberId;
    }
    
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
}
