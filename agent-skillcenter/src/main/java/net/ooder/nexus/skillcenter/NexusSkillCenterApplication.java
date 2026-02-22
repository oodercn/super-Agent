package net.ooder.nexus.skillcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * SkillCenter Application Main Class - 符合 ooderNexus 规范
 * 应用程序入口点
 */
@SpringBootApplication
@ComponentScan(basePackages = {
        "net.ooder.nexus.skillcenter",
        "net.ooder.skillcenter"
})
public class NexusSkillCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusSkillCenterApplication.class, args);
    }

}