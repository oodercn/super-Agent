package net.ooder.examples.skillsa2ui;

import net.ooder.examples.skillsa2ui.service.VfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SkillsA2UIApplication {

    @Autowired
    private VfsService vfsService;

    public static void main(String[] args) {
        SpringApplication.run(SkillsA2UIApplication.class, args);
    }

    @Bean
    public CommandLineRunner startupRunner() {
        return args -> {
            // 启动VFS服务
            vfsService.start();
        };
    }

    @Bean
    public CommandLineRunner shutdownRunner(ConfigurableApplicationContext context) {
        return args -> {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                // 停止VFS服务
                vfsService.stop();
                context.close();
            }));
        };
    }
}