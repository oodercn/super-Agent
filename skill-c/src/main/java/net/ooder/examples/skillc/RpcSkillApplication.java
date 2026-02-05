package net.ooder.examples.skillc;

import net.ooder.examples.skillc.service.SceneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "net.ooder.examples.skillc")
public class RpcSkillApplication implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(RpcSkillApplication.class);

    @Autowired
    private SceneService sceneService;

    @Value("${skill.scene.id:RBC_SCENE_001}")
    private String sceneId;

    public static void main(String[] args) {
        SpringApplication.run(RpcSkillApplication.class, args);
        logger.info("Skill C - Route Agent Application Started");
    }

    @Override
    public void run(ApplicationArguments args) {
        // Initialize default scene
        sceneService.createScene("RBC代填代报场景", "代填代报数据流转场景", sceneId);
        logger.info("Default scene initialized: {}", sceneId);
        logger.info("Skill C - Route Agent is ready for coordination");
    }
}