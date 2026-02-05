package net.ooder.examples.skilla;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = "net.ooder.examples.skilla",
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class RpcSkillApplication {
    private static final Logger logger = LoggerFactory.getLogger(RpcSkillApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RpcSkillApplication.class, args);
        logger.info("Skill A - Information Retrieval Application Started");
        logger.info("Skill A is ready to retrieve information from Network A");
    }
}