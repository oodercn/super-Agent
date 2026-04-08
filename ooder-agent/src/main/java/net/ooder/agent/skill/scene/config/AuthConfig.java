package net.ooder.mvp.skill.scene.config;

import net.ooder.skill.common.service.AuthService;
import net.ooder.mvp.skill.scene.adapter.OrgUserInfoProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class AuthConfig {

    private static final Logger log = LoggerFactory.getLogger(AuthConfig.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private OrgUserInfoProvider userInfoProvider;

    @PostConstruct
    public void configureAuthService() {
        log.info("[AuthConfig] Configuring AuthService with OrgUserInfoProvider");
        authService.setUserInfoProvider(userInfoProvider);
    }
}
