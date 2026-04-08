package net.ooder.mvp.skill.scene.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;

@Configuration
public class DataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${app.storage.path:./data}")
    private String storagePath;

    @Value("${app.sqlite.db-name:mvp}")
    private String dbName;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        log.info("[dataSource] Initializing SQLite DataSource");
        
        File dataDir = new File(storagePath);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
            log.info("[dataSource] Created storage directory: {}", storagePath);
        }
        
        String dbPath = storagePath + "/" + dbName + ".db";
        String url = "jdbc:sqlite:" + dbPath;
        
        log.info("[dataSource] SQLite database path: {}", dbPath);
        
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);
        
        log.info("[dataSource] SQLite DataSource initialized successfully");
        return dataSource;
    }
}
