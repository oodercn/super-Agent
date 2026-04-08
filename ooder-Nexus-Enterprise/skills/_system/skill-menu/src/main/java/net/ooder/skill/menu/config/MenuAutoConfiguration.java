package net.ooder.skill.menu.config;

import net.ooder.skill.menu.entity.Menu;
import net.ooder.skill.menu.service.MenuRoleConfigService;
import net.ooder.skill.menu.service.impl.MenuRoleConfigServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnProperty(name = "skill.menu.enabled", havingValue = "true", matchIfMissing = true)
@EnableJpaRepositories(basePackages = "net.ooder.skill.menu.repository")
@EntityScan(basePackages = "net.ooder.skill.menu.entity")
public class MenuAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MenuRoleConfigService.class)
    public MenuRoleConfigService menuRoleConfigService() {
        return new MenuRoleConfigServiceImpl();
    }
}
