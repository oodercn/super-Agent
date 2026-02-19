
package net.ooder.sdk.core.skill.lifecycle;

public interface SkillLifecycle {
    
    void initialize();
    
    void start();
    
    void stop();
    
    void pause();
    
    void resume();
    
    void destroy();
    
    SkillState getState();
    
    long getUptime();
    
    long getStartTime();
    
    int getRestartCount();
    
    void addLifecycleListener(LifecycleListener listener);
    
    void removeLifecycleListener(LifecycleListener listener);
    
    interface LifecycleListener {
        void onStateChange(SkillState oldState, SkillState newState);
        void onError(Throwable error);
    }
}
