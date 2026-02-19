package net.ooder.sdk.api.skill;

public interface SkillCallback {
    
    void onSuccess(SkillResponse response);
    
    void onError(SkillResponse response);
    
    void onTimeout(SkillRequest request);
    
    default void onRetry(SkillRequest request, int retryCount, Throwable lastError) {
    }
}
