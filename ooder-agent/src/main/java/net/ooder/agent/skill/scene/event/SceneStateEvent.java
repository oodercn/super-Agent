package net.ooder.mvp.skill.scene.event;

import java.util.Map;

public class SceneStateEvent {
    private String sceneGroupId;
    private String sceneId;
    private String eventType;
    private String source;
    private String participantId;
    private String participantName;
    private String oldState;
    private String newState;
    private long timestamp;
    private Map<String, Object> data;

    public static final String EVENT_SCENE_CREATED = "SCENE_CREATED";
    public static final String EVENT_SCENE_ACTIVATED = "SCENE_ACTIVATED";
    public static final String EVENT_SCENE_DEACTIVATED = "SCENE_DEACTIVATED";
    public static final String EVENT_SCENE_DESTROYED = "SCENE_DESTROYED";
    public static final String EVENT_PARTICIPANT_JOINED = "PARTICIPANT_JOINED";
    public static final String EVENT_PARTICIPANT_LEFT = "PARTICIPANT_LEFT";
    public static final String EVENT_PARTICIPANT_ROLE_CHANGED = "PARTICIPANT_ROLE_CHANGED";
    public static final String EVENT_CAPABILITY_BOUND = "CAPABILITY_BOUND";
    public static final String EVENT_CAPABILITY_UNBOUND = "CAPABILITY_UNBOUND";

    public String getSceneGroupId() { return sceneGroupId; }
    public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
    public String getSceneId() { return sceneId; }
    public void setSceneId(String sceneId) { this.sceneId = sceneId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getParticipantId() { return participantId; }
    public void setParticipantId(String participantId) { this.participantId = participantId; }
    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }
    public String getOldState() { return oldState; }
    public void setOldState(String oldState) { this.oldState = oldState; }
    public String getNewState() { return newState; }
    public void setNewState(String newState) { this.newState = newState; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }

    public static SceneStateEvent create(String sceneGroupId, String eventType) {
        SceneStateEvent event = new SceneStateEvent();
        event.setSceneGroupId(sceneGroupId);
        event.setEventType(eventType);
        event.setTimestamp(System.currentTimeMillis());
        return event;
    }

    public static SceneStateEvent create(String sceneGroupId, String eventType, String participantId) {
        SceneStateEvent event = create(sceneGroupId, eventType);
        event.setParticipantId(participantId);
        return event;
    }

    public static SceneStateEvent create(String sceneGroupId, String eventType, 
                                          String oldState, String newState) {
        SceneStateEvent event = create(sceneGroupId, eventType);
        event.setOldState(oldState);
        event.setNewState(newState);
        return event;
    }
}
