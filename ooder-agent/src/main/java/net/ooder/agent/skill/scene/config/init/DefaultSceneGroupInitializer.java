package net.ooder.mvp.skill.scene.config.init;

import net.ooder.scene.event.SceneEventPublisher;
import net.ooder.scene.group.SceneGroup;
import net.ooder.scene.group.SceneGroupManager;
import net.ooder.scene.participant.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(2)
public class DefaultSceneGroupInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DefaultSceneGroupInitializer.class);

    private final SceneGroupManager sceneGroupManager;

    @Value("${ooder.scene.auto-init:true}")
    private boolean autoInit;

    @Value("${ooder.scene.default-group-id:sg-default}")
    private String defaultGroupId;

    @Autowired(required = false)
    public DefaultSceneGroupInitializer(SceneGroupManager sceneGroupManager) {
        this.sceneGroupManager = sceneGroupManager;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (sceneGroupManager == null) {
            log.warn("[DefaultSceneGroup] SceneGroupManager not available, skipping initialization");
            return;
        }

        if (!autoInit) {
            log.info("[DefaultSceneGroup] Auto-init disabled, skipping");
            return;
        }

        List<SceneGroup> existingGroups = sceneGroupManager.getAllSceneGroups();
        if (!existingGroups.isEmpty()) {
            log.info("[DefaultSceneGroup] Found {} existing scene groups, skipping initialization", existingGroups.size());
            return;
        }

        log.info("[DefaultSceneGroup] No scene groups found, creating default scene group...");
        createDefaultSceneGroup();
    }

    private void createDefaultSceneGroup() {
        try {
            SceneGroup defaultGroup = sceneGroupManager.createSceneGroup(
                defaultGroupId,
                "template-default",
                "system",
                SceneGroup.CreatorType.SYSTEM
            );

            defaultGroup.setName("默认场景组");
            defaultGroup.setDescription("系统自动创建的默认场景组");

            addDefaultParticipants(defaultGroup);
            
            defaultGroup.activate();

            log.info("[DefaultSceneGroup] Default scene group created and activated: {}", defaultGroupId);
            
        } catch (Exception e) {
            log.error("[DefaultSceneGroup] Failed to create default scene group: {}", e.getMessage(), e);
        }
    }

    private void addDefaultParticipants(SceneGroup sceneGroup) {
        Participant llmAgent = new Participant(
            "agent-llm-001",
            "agent-llm-001",
            "LLM智能助手",
            Participant.Type.AGENT
        );
        llmAgent.setRole(Participant.Role.LLM_ASSISTANT);
        llmAgent.join();
        llmAgent.activate();
        sceneGroup.addParticipant(llmAgent);
        log.info("[DefaultSceneGroup] Added LLM Agent participant");

        Participant coordinatorAgent = new Participant(
            "agent-coordinator-001",
            "agent-coordinator-001",
            "协调Agent",
            Participant.Type.AGENT
        );
        coordinatorAgent.setRole(Participant.Role.COORDINATOR);
        coordinatorAgent.join();
        coordinatorAgent.activate();
        sceneGroup.addParticipant(coordinatorAgent);
        log.info("[DefaultSceneGroup] Added Coordinator Agent participant");

        Participant superAgent = new Participant(
            "super-agent-001",
            "super-agent-001",
            "超级Agent",
            Participant.Type.SUPER_AGENT
        );
        superAgent.setRole(Participant.Role.MANAGER);
        superAgent.join();
        superAgent.activate();
        sceneGroup.addParticipant(superAgent);
        log.info("[DefaultSceneGroup] Added Super Agent participant");

        Participant systemUser = new Participant(
            "user-admin-001",
            "user-admin-001",
            "系统管理员",
            Participant.Type.USER
        );
        systemUser.setRole(Participant.Role.OWNER);
        systemUser.join();
        systemUser.activate();
        sceneGroup.addParticipant(systemUser);
        log.info("[DefaultSceneGroup] Added System User participant");
    }
}
