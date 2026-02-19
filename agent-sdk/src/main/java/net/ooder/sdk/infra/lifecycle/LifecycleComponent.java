
package net.ooder.sdk.infra.lifecycle;

public interface LifecycleComponent {
    
    void initialize() throws Exception;
    
    void start() throws Exception;
    
    void stop();
    
    void destroy();
    
    String getName();
    
    LifecycleManager.State getState();
}
