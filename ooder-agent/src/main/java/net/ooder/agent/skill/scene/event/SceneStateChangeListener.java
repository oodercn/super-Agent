package net.ooder.mvp.skill.scene.event;

public interface SceneStateChangeListener {
    
    void onSceneCreated(SceneStateEvent event);
    
    void onSceneActivated(SceneStateEvent event);
    
    void onSceneDeactivated(SceneStateEvent event);
    
    void onSceneDestroyed(SceneStateEvent event);
    
    void onParticipantJoined(SceneStateEvent event);
    
    void onParticipantLeft(SceneStateEvent event);
    
    void onParticipantRoleChanged(SceneStateEvent event);
    
    void onCapabilityBound(SceneStateEvent event);
    
    void onCapabilityUnbound(SceneStateEvent event);
    
    default int getOrder() {
        return 0;
    }
}
