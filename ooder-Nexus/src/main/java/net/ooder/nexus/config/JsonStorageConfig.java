package net.ooder.nexus.config;

import net.ooder.sdk.service.storage.persistence.JsonStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonStorageConfig {

    private static final Logger log = LoggerFactory.getLogger(JsonStorageConfig.class);

    @Value("${storage.json.root-dir:./data/storage}")
    private String rootDir;

    @Bean
    public JsonStorage jsonStorage() {
        log.info("Initializing JsonStorage with root directory: {}", rootDir);
        return new JsonStorage(rootDir);
    }
}
