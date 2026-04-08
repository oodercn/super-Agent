package net.ooder.skill.menu.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ConditionalOnClass(name = "jakarta.persistence.EntityManager")
@EnableJpaRepositories(
    basePackages = "net.ooder.skill.menu.repository",
    entityManagerFactoryRef = "menuEntityManagerFactory",
    transactionManagerRef = "menuTransactionManager"
)
@EnableTransactionManagement
public class MenuJpaConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MenuJpaConfiguration.class);

    public MenuJpaConfiguration() {
        log.info("[MenuJpaConfiguration] Initializing Menu JPA configuration");
    }

    @Bean
    @ConditionalOnMissingBean(name = "menuDataSource")
    public DataSource menuDataSource() {
        log.info("[MenuJpaConfiguration] Creating Menu DataSource");
        return DataSourceBuilder.create()
                .url("jdbc:sqlite:./data/menu.db")
                .driverClassName("org.sqlite.JDBC")
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean menuEntityManagerFactory(DataSource menuDataSource) {
        log.info("[MenuJpaConfiguration] Creating Menu EntityManagerFactory");
        
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(menuDataSource);
        emf.setPackagesToScan("net.ooder.skill.menu.entity");
        emf.setPersistenceUnitName("menu");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(false);
        vendorAdapter.setGenerateDdl(true);
        emf.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        jpaProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        jpaProperties.setProperty("hibernate.format_sql", "false");
        jpaProperties.setProperty("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        emf.setJpaProperties(jpaProperties);

        return emf;
    }

    @Bean
    public PlatformTransactionManager menuTransactionManager(
            LocalContainerEntityManagerFactoryBean menuEntityManagerFactory) {
        log.info("[MenuJpaConfiguration] Creating Menu TransactionManager");
        
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(menuEntityManagerFactory.getObject());
        return transactionManager;
    }
}
