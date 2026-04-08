package net.ooder.agent;

import net.ooder.scene.autoconfigure.SceneEngineAutoConfiguration;
import net.ooder.scene.skill.config.KnowledgePersistenceAutoConfiguration;
import net.ooder.skill.hotplug.autoconfigure.HotPlugAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
    SceneEngineAutoConfiguration.class,
    KnowledgePersistenceAutoConfiguration.class,
    HotPlugAutoConfiguration.class
})
@ComponentScan(
    basePackages = {
        "net.ooder.agent",
        "net.ooder.skill.common",
        "net.ooder.skill.workflow",
        "net.ooder.skill.discovery",
        "net.ooder.skill.menu"
    }
)
public class AgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentApplication.class, args);
    }
}
