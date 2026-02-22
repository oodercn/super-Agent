package net.ooder.nexus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 服务配置类
 * 管理 Mock 模式和真实模式的切换
 */
@Configuration
public class NexusServiceConfig {

    @Value("${mock.enabled:false}")
    private boolean mockEnabled;

    @Value("${mcpagent.service.type:REAL}")
    private String serviceType;

    @Value("${mcpagent.service.switch.enabled:true}")
    private boolean switchEnabled;

    @Value("${mcpagent.service.switch.require-auth:false}")
    private boolean requireAuth;

    /**
     * 初始化配置
     * 根据 mock.enabled 设置自动调整 serviceType
     */
    @PostConstruct
    public void init() {
        // 如果 mock.enabled=true，强制使用 MOCK 模式
        // 如果 mock.enabled=false，使用配置文件中指定的模式（默认 REAL）
        if (mockEnabled) {
            this.serviceType = "MOCK";
        }
    }

    /**
     * 检查是否启用 Mock 模式
     * @return true 表示启用 Mock 模式，false 表示使用真实模式
     */
    public boolean isMockEnabled() {
        return mockEnabled || "MOCK".equalsIgnoreCase(serviceType);
    }

    public boolean isMockEnabledFromConfig() {
        return mockEnabled;
    }

    public void setMockEnabled(boolean mockEnabled) {
        this.mockEnabled = mockEnabled;
        if (mockEnabled) {
            this.serviceType = "MOCK";
        }
    }

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
            return mockEnabled ? "MOCK" : "REAL";
        }
        return type.toUpperCase();
    }

    public boolean isValidServiceType(String type) {
        return "MOCK".equals(type) || "REAL".equals(type);
    }
}
