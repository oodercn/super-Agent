package net.ooder.nexus;

import net.ooder.scene.autoconfigure.SceneEngineAutoConfiguration;
import net.ooder.scene.skill.config.KnowledgePersistenceAutoConfiguration;
import net.ooder.skill.hotplug.autoconfigure.HotPlugAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
    SceneEngineAutoConfiguration.class,
    KnowledgePersistenceAutoConfiguration.class,
    HotPlugAutoConfiguration.class
})
@ComponentScan(
    basePackages = {
        "net.ooder.nexus",
        "net.ooder.skill.common",
        "net.ooder.skill.workflow",
        "net.ooder.skill.discovery",
        "net.ooder.skill.menu"
    },
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = "net\\.ooder\\.skill\\.common\\.controller\\..*"
        )
    }
)
public class NexusApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusApplication.class, args);
    }
}
