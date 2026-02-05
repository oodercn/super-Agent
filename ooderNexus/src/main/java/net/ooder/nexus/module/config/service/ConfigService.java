package net.ooder.nexus.module.config.service;

import java.util.Map;

public interface ConfigService {
    
    Map<String, Object> getBasicConfig();
    
    Map<String, Object> getAdvancedConfig();
    
    Map<String, Object> getSecurityConfig();
    
    Map<String, Object> saveConfig(Map<String, Object> config);
    
    Map<String, Object> resetConfig();
}
