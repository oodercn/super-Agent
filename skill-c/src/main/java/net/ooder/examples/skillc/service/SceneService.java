/**
 * 作者：ooderAI agent team
 * 版本：V0.6.0
 * 日期：2026-01-18
 */
package net.ooder.examples.skillc.service;

import net.ooder.examples.skillc.model.Group;
import net.ooder.examples.skillc.model.Scene;
import net.ooder.examples.skillc.model.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

@Service
public class SceneService {
    private static final Logger logger = LoggerFactory.getLogger(SceneService.class);
    private final Map<String, Scene> scenes = new ConcurrentHashMap<>();
    private final Map<String, Group> groups = new ConcurrentHashMap<>();
    
    // 错误码定义
    public static final int SCENE_NOT_FOUND = 1001;
    public static final int SCENE_ALREADY_EXISTS = 1002;
    public static final int GROUP_NOT_FOUND = 1003;
    public static final int GROUP_ALREADY_EXISTS = 1004;
    public static final int AGENT_NOT_FOUND = 1005;
    public static final int AGENT_ALREADY_IN_SCENE = 1006;
    public static final int AGENT_ALREADY_IN_GROUP = 1007;

    public Scene createScene(String name, String description, String sceneId) {
        if (scenes.containsKey(sceneId)) {
            logger.warn("Scene {} already exists", sceneId);
            return scenes.get(sceneId);
        }
        
        Scene scene = new Scene(name, description, sceneId);
        scenes.put(sceneId, scene);
        logger.info("Created new scene: {} (ID: {})", name, sceneId);
        return scene;
    }

    public Scene getScene(String sceneId) {
        return scenes.get(sceneId);
    }

    public boolean joinScene(String sceneId, Agent agent) {
        Scene scene = scenes.get(sceneId);
        if (scene == null) {
            logger.error("Scene {} not found for agent {}", sceneId, agent.getAgentId());
            return false;
        }
        
        if (scene.containsParticipant(agent.getAgentId())) {
            logger.warn("Agent {} already in scene {}, updating information", agent.getAgentId(), sceneId);
            scene.updateParticipant(agent);
            logger.info("Agent {} information updated in scene {}", agent.getAgentId(), sceneId);
            return true;
        }
        
        scene.addParticipant(agent);
        logger.info("Agent {} joined scene {} as {}", agent.getAgentId(), sceneId, agent.getType());
        return true;
    }

    public boolean leaveScene(String sceneId, String agentId) {
        Scene scene = scenes.get(sceneId);
        if (scene == null) {
            logger.error("Scene {} not found", sceneId);
            return false;
        }
        
        if (!scene.containsParticipant(agentId)) {
            logger.warn("Agent {} not in scene {}", agentId, sceneId);
            return false;
        }
        
        scene.removeParticipant(agentId);
        logger.info("Agent {} left scene {}", agentId, sceneId);
        return true;
    }

    public List<Agent> getSceneParticipants(String sceneId) {
        Scene scene = scenes.get(sceneId);
        if (scene == null) {
            logger.error("Scene {} not found", sceneId);
            return new ArrayList<>();
        }
        
        return scene.getParticipants();
    }

    public List<Scene> getAllScenes() {
        return new ArrayList<>(scenes.values());
    }

    public boolean isSceneActive(String sceneId) {
        return scenes.containsKey(sceneId);
    }
    
    // Group management methods
    public Group createGroup(String name, String description, String groupId, String sceneId) {
        if (!scenes.containsKey(sceneId)) {
            logger.error("Scene {} not found for creating group", sceneId);
            return null;
        }
        
        if (groups.containsKey(groupId)) {
            logger.warn("Group {} already exists", groupId);
            return groups.get(groupId);
        }
        
        Scene scene = scenes.get(sceneId);
        Group group = new Group(name, description, groupId, sceneId);
        groups.put(groupId, group);
        scene.addGroup(group);
        
        logger.info("Created new group: {} (ID: {}) in scene {}", name, groupId, sceneId);
        return group;
    }
    
    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }
    
    public boolean addAgentToGroup(String groupId, Agent agent) {
        Group group = groups.get(groupId);
        if (group == null) {
            logger.error("Group {} not found for adding agent", groupId);
            return false;
        }
        
        if (group.containsMember(agent.getAgentId())) {
            logger.warn("Agent {} already in group {}", agent.getAgentId(), groupId);
            return false;
        }
        
        // 确保agent也在场景中
        Scene scene = scenes.get(group.getSceneId());
        if (scene != null && !scene.containsParticipant(agent.getAgentId())) {
            scene.addParticipant(agent);
            logger.info("Agent {} added to scene {} before adding to group {}", 
                agent.getAgentId(), group.getSceneId(), groupId);
        }
        
        group.addMember(agent);
        logger.info("Agent {} added to group {}", agent.getAgentId(), groupId);
        return true;
    }
    
    public boolean removeAgentFromGroup(String groupId, String agentId) {
        Group group = groups.get(groupId);
        if (group == null) {
            logger.error("Group {} not found for removing agent", groupId);
            return false;
        }
        
        if (!group.containsMember(agentId)) {
            logger.warn("Agent {} not in group {}", agentId, groupId);
            return false;
        }
        
        group.removeMember(agentId);
        logger.info("Agent {} removed from group {}", agentId, groupId);
        return true;
    }
    
    public boolean deleteGroup(String groupId) {
        Group group = groups.get(groupId);
        if (group == null) {
            logger.error("Group {} not found for deletion", groupId);
            return false;
        }
        
        Scene scene = scenes.get(group.getSceneId());
        if (scene != null) {
            scene.removeGroup(groupId);
        }
        
        groups.remove(groupId);
        logger.info("Deleted group: {}", groupId);
        return true;
    }
    
    public List<Group> getSceneGroups(String sceneId) {
        Scene scene = scenes.get(sceneId);
        if (scene == null) {
            logger.error("Scene {} not found for getting groups", sceneId);
            return new ArrayList<>();
        }
        
        return scene.getGroups();
    }
    
    public List<Agent> getGroupMembers(String groupId) {
        Group group = groups.get(groupId);
        if (group == null) {
            logger.error("Group {} not found for getting members", groupId);
            return new ArrayList<>();
        }
        
        return group.getMembers();
    }
}