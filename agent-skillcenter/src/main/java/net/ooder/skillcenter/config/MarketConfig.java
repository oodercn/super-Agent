package net.ooder.skillcenter.config;

import net.ooder.skillcenter.market.SkillMarketManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarketConfig {

    @Bean
    public SkillMarketManager skillMarketManager() {
        return SkillMarketManager.getInstance();
    }
}
