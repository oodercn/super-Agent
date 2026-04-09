package net.ooder.nexus.dto.workbench;

import java.util.List;

public class SceneTodoGroupDTO {
    private String sceneGroupId;
    private String sceneName;
    private String sceneStatus;
    private String myRole;
    private int memberCount;
    private int pendingCount;
    private int highPriorityCount;
    private List<TodoItemDTO> todos;

    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getSceneName() { return sceneName; }
    public void setSceneName(String sceneName) { this.sceneName = sceneName; }
    public String getSceneStatus() { return sceneStatus; }
    public void setSceneStatus(String sceneStatus) { this.sceneStatus = sceneStatus; }
    public String getMyRole() { return myRole; }
    public void setMyRole(String myRole) { this.myRole = myRole; }
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    public int getPendingCount() { return pendingCount; }
    public void setPendingCount(int pendingCount) { this.pendingCount = pendingCount; }
    public int getHighPriorityCount() { return highPriorityCount; }
    public void setHighPriorityCount(int highPriorityCount) { this.highPriorityCount = highPriorityCount; }
    public List<TodoItemDTO> getTodos() { return todos; }
    public void setTodos(List<TodoItemDTO> todos) { this.todos = todos; }
}
