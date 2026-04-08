package net.ooder.mvp.skill.scene.capability.connector;

import net.ooder.mvp.skill.scene.capability.model.CapabilityBinding;
import java.util.Map;

public interface Connector {
    
    String getType();
    
    Object invoke(CapabilityBinding binding, Map<String, Object> params) throws Exception;
    
    boolean isAvailable(CapabilityBinding binding);
    
    void close();
}
