package net.ooder.sdk.api.event;

public class PrimaryChangedEvent extends Event {
    
    private String groupId;
    private String oldPrimaryId;
    private String newPrimaryId;
    
    public PrimaryChangedEvent() {
        super();
    }
    
    public PrimaryChangedEvent(String groupId, String oldPrimaryId, String newPrimaryId) {
        super("SceneGroupManager");
        this.groupId = groupId;
        this.oldPrimaryId = oldPrimaryId;
        this.newPrimaryId = newPrimaryId;
    }
    
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }
    
    public String getOldPrimaryId() { return oldPrimaryId; }
    public void setOldPrimaryId(String oldPrimaryId) { this.oldPrimaryId = oldPrimaryId; }
    
    public String getNewPrimaryId() { return newPrimaryId; }
    public void setNewPrimaryId(String newPrimaryId) { this.newPrimaryId = newPrimaryId; }
}
