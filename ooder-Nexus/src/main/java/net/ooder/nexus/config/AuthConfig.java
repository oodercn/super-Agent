package net.ooder.nexus.config;

import net.ooder.skill.common.api.AuthApi;
import net.ooder.skill.common.api.OrgApi;
import net.ooder.skill.common.model.RoleConfig;
import net.ooder.skill.common.service.AuthService;
import net.ooder.skill.common.service.OrgService;
import net.ooder.skill.common.service.RoleConfigProvider;
import net.ooder.skill.common.storage.JsonStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AuthConfig {

    private static final Logger log = LoggerFactory.getLogger(AuthConfig.class);

    @Value("${app.storage.path:./data}")
    private String storagePath;

    @Bean
    public JsonStorageService jsonStorageService() {
        log.info("[AuthConfig] Creating JsonStorageService with path: {}", storagePath);
        JsonStorageService service = new JsonStorageService(storagePath);
        service.init();
        return service;
    }

    @Bean
    public AuthService authService() {
        log.info("[AuthConfig] Creating AuthService");
        return new AuthService();
    }

    @Bean
    public AuthApi authApi() {
        log.info("[AuthConfig] Creating AuthApi");
        return new AuthApi();
    }

    @Bean
    public OrgService orgService(JsonStorageService storage) {
        log.info("[AuthConfig] Creating OrgService");
        OrgService service = new OrgService(storage);
        service.init();
        return service;
    }

    @Bean
    public OrgApi orgApi() {
        log.info("[AuthConfig] Creating OrgApi");
        return new OrgApi();
    }

    @Bean
    public RoleConfigProvider roleConfigProvider(AuthService authService) {
        log.info("[AuthConfig] Creating RoleConfigProvider");
        RoleConfigProvider provider = new RoleConfigProvider() {
            @Override
            public List<RoleConfig> getRoleConfigs() {
                return Arrays.asList(
                    new RoleConfig("installer", "系统安装者", "安装基础技能包，初始化系统环境", "ri-install-line",
                        "installer", Arrays.asList("skill:install", "skill:view", "system:init")),
                    new RoleConfig("admin", "系统管理员", "发现场景技能，配置分发，推送给参与者", "ri-admin-line",
                        "admin", Arrays.asList("capability:discover", "capability:install", "capability:distribute",
                            "scene:create", "scene:manage", "user:assign", "capability:view", "scene:view")),
                    new RoleConfig("leader", "主导者", "激活场景，获取KEY，执行入网动作", "ri-user-star-line",
                        "leader", Arrays.asList("scene:activate", "scene:manage", "scene:view",
                            "key:generate", "participant:manage", "task:assign")),
                    new RoleConfig("collaborator", "协作者", "参与业务流转，执行分配的任务", "ri-team-line",
                        "collaborator", Arrays.asList("task:view", "task:execute", "task:submit", "scene:view", "todo:view"))
                );
            }

            @Override
            public RoleConfig getRoleConfig(String roleId) {
                return getRoleConfigs().stream()
                    .filter(c -> c.getId().equals(roleId))
                    .findFirst()
                    .orElse(null);
            }
        };
        
        authService.setRoleConfigProvider(provider);
        log.info("[AuthConfig] RoleConfigProvider set to AuthService");
        
        return provider;
    }
}
