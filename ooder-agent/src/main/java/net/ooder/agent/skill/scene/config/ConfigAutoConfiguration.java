package net.ooder.mvp.skill.scene.config;

import net.ooder.mvp.skill.scene.config.sdk.SdkConfigStorage;
import net.ooder.mvp.skill.scene.config.sdk.JsonConfigStorageImpl;
import net.ooder.mvp.skill.scene.config.service.ConfigLoaderService;
import net.ooder.mvp.skill.scene.config.service.impl.ConfigLoaderServiceImpl;
import net.ooder.mvp.skill.scene.config.init.SystemConfigInitializer;
import net.ooder.mvp.skill.scene.config.install.SkillInstallConfigHandler;
import net.ooder.mvp.skill.scene.config.install.SceneInstallConfigHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigAutoConfiguration {

    @Value("${ooder.config.root:./config}")
    private String configRoot;

    @Value("${ooder.config.cache-ttl:300000}")
    private long cacheTtl;

    @Bean
    public SdkConfigStorage sdkConfigStorage() {
        return new JsonConfigStorageImpl(configRoot, cacheTtl);
    }
}
