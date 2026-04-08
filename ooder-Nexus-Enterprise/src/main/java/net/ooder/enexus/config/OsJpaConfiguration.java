package net.ooder.enexus.config;

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

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ConditionalOnClass(name = "jakarta.persistence.EntityManager")
@EnableJpaRepositories(
    basePackages = {
        "net.ooder.enexus.repository",
        "net.ooder.skill.tenant.repository"
    },
    entityManagerFactoryRef = "osEntityManagerFactory",
    transactionManagerRef = "osTransactionManager"
)
public class OsJpaConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OsJpaConfiguration.class);

    public OsJpaConfiguration() {
        log.info("[OsJpaConfiguration] Initializing OS JPA configuration");
    }

    @Bean
    @ConditionalOnMissingBean(name = "osDataSource")
    public DataSource osDataSource() {
        log.info("[OsJpaConfiguration] Creating OS DataSource");
        return DataSourceBuilder.create()
                .url("jdbc:sqlite:./data/os.db")
                .driverClassName("org.sqlite.JDBC")
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean osEntityManagerFactory(DataSource osDataSource) {
        log.info("[OsJpaConfiguration] Creating OS EntityManagerFactory");
        
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(osDataSource);
        emf.setPackagesToScan("net.ooder.enexus.entity", "net.ooder.skill.tenant.entity");
        emf.setPersistenceUnitName("os");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(false);
        vendorAdapter.setGenerateDdl(true);
        emf.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.community.dialect.SQLiteDialect");
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        jpaProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        jpaProperties.setProperty("hibernate.format_sql", "false");
        jpaProperties.setProperty("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        jpaProperties.setProperty("hibernate.implicit_naming_strategy", "org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl");
        emf.setJpaProperties(jpaProperties);

        return emf;
    }

    @Bean
    public PlatformTransactionManager osTransactionManager(
            LocalContainerEntityManagerFactoryBean osEntityManagerFactory) {
        log.info("[OsJpaConfiguration] Creating OS TransactionManager");
        
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(osEntityManagerFactory.getObject());
        return transactionManager;
    }
}
