package net.ooder.mvp.skill.scene.capability.connector;

import net.ooder.mvp.skill.scene.capability.model.CapabilityBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class InternalConnector implements Connector {

    private static final Logger log = LoggerFactory.getLogger(InternalConnector.class);

    @Override
    public String getType() {
        return "INTERNAL";
    }

    @Override
    public Object invoke(CapabilityBinding binding, Map<String, Object> params) throws Exception {
        String capId = binding.getCapId();
        String capabilityId = binding.getCapabilityId();
        
        log.debug("[InternalConnector] Invoking internal capability: {} ({})", capId, capabilityId);
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", true);
        result.put("capId", capId);
        result.put("capabilityId", capabilityId);
        result.put("params", params);
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }

    @Override
    public boolean isAvailable(CapabilityBinding binding) {
        return binding.getCapId() != null && !binding.getCapId().isEmpty();
    }

    @Override
    public void close() {
    }
}
