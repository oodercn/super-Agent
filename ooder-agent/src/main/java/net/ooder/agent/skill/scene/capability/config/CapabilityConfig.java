package net.ooder.mvp.skill.scene.capability.config;

import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityBindingService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityStateService;
import net.ooder.mvp.skill.scene.capability.service.impl.CapabilityServiceImpl;
import net.ooder.mvp.skill.scene.capability.service.impl.CapabilityBindingServiceImpl;
import net.ooder.mvp.skill.scene.capability.service.impl.CapabilityStateServiceImpl;
import net.ooder.skill.common.storage.JsonStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CapabilityConfig {

    @Autowired
    private JsonStorageService jsonStorageService;

    @Bean
    @Primary
    public CapabilityStateService capabilityStateService() {
        return new CapabilityStateServiceImpl();
    }

    @Bean
    public CapabilityService capabilityService() {
        return new CapabilityServiceImpl(jsonStorageService, capabilityStateService());
    }

    @Bean
    public CapabilityBindingService capabilityBindingService() {
        return new CapabilityBindingServiceImpl(jsonStorageService);
    }
}
