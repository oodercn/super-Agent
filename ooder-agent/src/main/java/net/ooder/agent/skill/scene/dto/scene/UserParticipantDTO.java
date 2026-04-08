package net.ooder.mvp.skill.scene.dto.scene;

import java.util.List;

public class UserParticipantDTO {
    private String userId;
    private String userName;
    private String organization;
    private List<String> permissions;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}
