package net.ooder.nexus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 服务配置类
 */
@Configuration
public class NexusServiceConfig {

    @Value("${mcpagent.service.type:MOCK}")
    private String serviceType;

    @Value("${mcpagent.service.switch.enabled:true}")
    private boolean switchEnabled;

    @Value("${mcpagent.service.switch.require-auth:false}")
    private boolean requireAuth;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public boolean isSwitchEnabled() {
        return switchEnabled;
    }

    public boolean isRequireAuth() {
        return requireAuth;
    }

    public String normalizeServiceType(String type) {
        if (type == null) {
            return "MOCK";
        }
        return type.toUpperCase();
    }

    public boolean isValidServiceType(String type) {
        return "MOCK".equals(type) || "REAL".equals(type);
    }
}
