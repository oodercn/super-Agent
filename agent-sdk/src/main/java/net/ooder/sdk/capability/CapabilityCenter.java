package net.ooder.sdk.capability;

public interface CapabilityCenter {
    
    CapabilitySpecService getSpecService();
    
    CapabilityDistService getDistService();
    
    CapabilityMgtService getMgtService();
    
    CapabilityMonService getMonService();
    
    CapabilityCoopService getCoopService();
    
    void initialize();
    
    void shutdown();
    
    boolean isInitialized();
}
