
package net.ooder.sdk.core.skill.lifecycle;

public enum SkillState {
    
    CREATED("created", "Skill has been created"),
    INITIALIZING("initializing", "Skill is initializing"),
    INITIALIZED("initialized", "Skill has been initialized"),
    STARTING("starting", "Skill is starting"),
    RUNNING("running", "Skill is running"),
    PAUSED("paused", "Skill is paused"),
    STOPPING("stopping", "Skill is stopping"),
    STOPPED("stopped", "Skill has been stopped"),
    DESTROYING("destroying", "Skill is being destroyed"),
    DESTROYED("destroyed", "Skill has been destroyed"),
    ERROR("error", "Skill encountered an error"),
    FAILED("failed", "Skill has failed");
    
    private final String code;
    private final String description;
    
    SkillState(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() { return code; }
    public String getDescription() { return description; }
    
    public boolean isRunning() {
        return this == RUNNING;
    }
    
    public boolean isStopped() {
        return this == STOPPED || this == DESTROYED || this == FAILED;
    }
    
    public boolean canStart() {
        return this == INITIALIZED || this == STOPPED || this == PAUSED;
    }
    
    public boolean canStop() {
        return this == RUNNING || this == PAUSED;
    }
    
    public boolean canPause() {
        return this == RUNNING;
    }
    
    public boolean canResume() {
        return this == PAUSED;
    }
    
    public boolean isTerminal() {
        return this == DESTROYED || this == FAILED;
    }
}
