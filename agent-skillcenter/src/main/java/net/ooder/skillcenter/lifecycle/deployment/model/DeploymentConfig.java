package net.ooder.skillcenter.lifecycle.deployment.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 部署配置 - 符合v0.7.0协议规范
 */
public class DeploymentConfig {
    
    private List<DeploymentMode> modes;
    private boolean singleton;
    private boolean requiresAuth;
    private HealthCheckConfig healthCheck;
    private StartupConfig startup;
    
    public DeploymentConfig() {
        this.modes = new ArrayList<>();
        this.modes.add(DeploymentMode.LOCAL_DEPLOYED);
        this.singleton = false;
        this.requiresAuth = false;
        this.healthCheck = HealthCheckConfig.defaultConfig();
        this.startup = StartupConfig.defaultConfig();
    }
    
    public static DeploymentConfig defaultConfig() {
        return new DeploymentConfig();
    }
    
    public static DeploymentConfig enterprise() {
        DeploymentConfig config = new DeploymentConfig();
        config.getModes().add(DeploymentMode.REMOTE_HOSTED);
        config.setSingleton(true);
        config.setRequiresAuth(true);
        return config;
    }
    
    public static DeploymentConfig tool() {
        DeploymentConfig config = new DeploymentConfig();
        config.setSingleton(false);
        config.setRequiresAuth(false);
        return config;
    }
    
    public static DeploymentConfig infrastructure() {
        DeploymentConfig config = new DeploymentConfig();
        config.setSingleton(true);
        config.setRequiresAuth(true);
        return config;
    }
    
    public void addMode(DeploymentMode mode) {
        if (!modes.contains(mode)) {
            modes.add(mode);
        }
    }
    
    public boolean supportsMode(DeploymentMode mode) {
        return modes.contains(mode);
    }
    
    public List<DeploymentMode> getModes() { return modes; }
    public void setModes(List<DeploymentMode> modes) { this.modes = modes; }
    
    public boolean isSingleton() { return singleton; }
    public void setSingleton(boolean singleton) { this.singleton = singleton; }
    
    public boolean isRequiresAuth() { return requiresAuth; }
    public void setRequiresAuth(boolean requiresAuth) { this.requiresAuth = requiresAuth; }
    
    public HealthCheckConfig getHealthCheck() { return healthCheck; }
    public void setHealthCheck(HealthCheckConfig healthCheck) { this.healthCheck = healthCheck; }
    
    public StartupConfig getStartup() { return startup; }
    public void setStartup(StartupConfig startup) { this.startup = startup; }
}
