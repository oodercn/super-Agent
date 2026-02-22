package net.ooder.nexus.service.collaboration.impl;

import net.ooder.nexus.domain.collaboration.model.CollaborationScene;
import net.ooder.nexus.domain.collaboration.model.CollaborationScene.SceneStatus;
import net.ooder.nexus.domain.collaboration.model.SceneMember;
import net.ooder.nexus.service.collaboration.CollaborationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 协作场景服务实现
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@Service
public class CollaborationServiceImpl implements CollaborationService {

    private static final Logger log = LoggerFactory.getLogger(CollaborationServiceImpl.class);

    private final Map<String, CollaborationScene> sceneStore = new ConcurrentHashMap<String, CollaborationScene>();
    private final Map<String, List<SceneMember>> sceneMembers = new ConcurrentHashMap<String, List<SceneMember>>();

    public CollaborationServiceImpl() {
        initDefaultScenes();
    }

    private void initDefaultScenes() {
        createDefaultScene("scene-001", "项目A协作组", "项目A开发协作", "user-001", "张三", Arrays.asList("skill-001", "skill-002"));
        createDefaultScene("scene-002", "数据分析团队", "数据分析协作", "user-002", "李四", Arrays.asList("skill-005", "skill-006"));
        createDefaultScene("scene-003", "AI研发小组", "AI模型研发协作", "user-003", "王五", Arrays.asList("skill-001", "skill-003", "skill-004"));

        log.info("Initialized {} default scenes", sceneStore.size());
    }

    private void createDefaultScene(String id, String name, String description, String ownerId, String ownerName, List<String> skillIds) {
        CollaborationScene scene = new CollaborationScene();
        scene.setSceneId(id);
        scene.setName(name);
        scene.setDescription(description);
        scene.setOwnerId(ownerId);
        scene.setOwnerName(ownerName);
        scene.setSkillIds(skillIds);
        scene.setStatus(SceneStatus.RUNNING);
        scene.setSceneKey(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        scene.setCreateTime(System.currentTimeMillis() - (long) (Math.random() * 2592000000L));
        scene.setUpdateTime(System.currentTimeMillis());
        scene.setMembers(new ArrayList<SceneMember>());

        sceneStore.put(id, scene);

        List<SceneMember> members = new ArrayList<SceneMember>();
        SceneMember owner = new SceneMember();
        owner.setMemberId(ownerId);
        owner.setMemberName(ownerName);
        owner.setRole("OWNER");
        owner.setJoinedAt(scene.getCreateTime());
        owner.setOnline(true);
        members.add(owner);

        sceneMembers.put(id, members);
    }

    @Override
    public CollaborationScene createScene(String name, String description, String ownerId, String ownerName, List<String> skillIds) {
        log.info("Creating scene: name={}, owner={}", name, ownerId);

        String sceneId = "scene-" + System.currentTimeMillis();

        CollaborationScene scene = new CollaborationScene();
        scene.setSceneId(sceneId);
        scene.setName(name);
        scene.setDescription(description);
        scene.setOwnerId(ownerId);
        scene.setOwnerName(ownerName);
        scene.setSkillIds(skillIds != null ? skillIds : new ArrayList<String>());
        scene.setStatus(SceneStatus.CREATED);
        scene.setSceneKey(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        scene.setCreateTime(System.currentTimeMillis());
        scene.setUpdateTime(System.currentTimeMillis());
        scene.setMembers(new ArrayList<SceneMember>());

        sceneStore.put(sceneId, scene);

        List<SceneMember> members = new ArrayList<SceneMember>();
        SceneMember owner = new SceneMember();
        owner.setMemberId(ownerId);
        owner.setMemberName(ownerName);
        owner.setRole("OWNER");
        owner.setJoinedAt(scene.getCreateTime());
        owner.setOnline(true);
        members.add(owner);

        sceneMembers.put(sceneId, members);

        return scene;
    }

    @Override
    public List<CollaborationScene> getSceneList(String userId, String status, int page, int pageSize) {
        log.info("Getting scene list: userId={}, status={}", userId, status);

        List<CollaborationScene> filtered = new ArrayList<CollaborationScene>();

        for (CollaborationScene scene : sceneStore.values()) {
            boolean isMember = false;
            List<SceneMember> members = sceneMembers.get(scene.getSceneId());
            if (members != null) {
                for (SceneMember m : members) {
                    if (m.getMemberId().equals(userId)) {
                        isMember = true;
                        break;
                    }
                }
            }

            if (!isMember && !scene.getOwnerId().equals(userId)) {
                continue;
            }

            if (status != null && !status.isEmpty()) {
                if (!scene.getStatus().name().equals(status)) {
                    continue;
                }
            }

            filtered.add(scene);
        }

        filtered.sort((a, b) -> Long.compare(b.getUpdateTime(), a.getUpdateTime()));

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, filtered.size());

        if (start >= filtered.size()) {
            return new ArrayList<CollaborationScene>();
        }

        return filtered.subList(start, end);
    }

    @Override
    public CollaborationScene getSceneDetail(String sceneId) {
        log.info("Getting scene detail: {}", sceneId);
        return sceneStore.get(sceneId);
    }

    @Override
    public CollaborationScene updateScene(String sceneId, String name, String description, List<String> skillIds) {
        log.info("Updating scene: {}", sceneId);

        CollaborationScene scene = sceneStore.get(sceneId);
        if (scene == null) {
            return null;
        }

        if (name != null && !name.isEmpty()) {
            scene.setName(name);
        }
        if (description != null) {
            scene.setDescription(description);
        }
        if (skillIds != null) {
            scene.setSkillIds(skillIds);
        }
        scene.setUpdateTime(System.currentTimeMillis());

        return scene;
    }

    @Override
    public boolean deleteScene(String sceneId, String operatorId) {
        log.info("Deleting scene: {}, operator: {}", sceneId, operatorId);

        CollaborationScene scene = sceneStore.get(sceneId);
        if (scene == null) {
            return false;
        }

        if (!scene.getOwnerId().equals(operatorId)) {
            return false;
        }

        sceneStore.remove(sceneId);
        sceneMembers.remove(sceneId);

        return true;
    }

    @Override
    public boolean addMember(String sceneId, String memberId, String memberName, String role, String operatorId) {
        log.info("Adding member to scene: scene={}, member={}", sceneId, memberId);

        CollaborationScene scene = sceneStore.get(sceneId);
        if (scene == null) {
            return false;
        }

        if (!scene.getOwnerId().equals(operatorId)) {
            List<SceneMember> members = sceneMembers.get(sceneId);
            boolean isAdmin = false;
            if (members != null) {
                for (SceneMember m : members) {
                    if (m.getMemberId().equals(operatorId) && "ADMIN".equals(m.getRole())) {
                        isAdmin = true;
                        break;
                    }
                }
            }
            if (!isAdmin) {
                return false;
            }
        }

        List<SceneMember> members = sceneMembers.computeIfAbsent(sceneId, k -> new ArrayList<SceneMember>());

        for (SceneMember m : members) {
            if (m.getMemberId().equals(memberId)) {
                return false;
            }
        }

        SceneMember member = new SceneMember();
        member.setMemberId(memberId);
        member.setMemberName(memberName);
        member.setRole(role != null ? role : "MEMBER");
        member.setJoinedAt(System.currentTimeMillis());
        member.setOnline(false);
        members.add(member);

        scene.setUpdateTime(System.currentTimeMillis());

        return true;
    }

    @Override
    public boolean removeMember(String sceneId, String memberId, String operatorId) {
        log.info("Removing member from scene: scene={}, member={}", sceneId, memberId);

        CollaborationScene scene = sceneStore.get(sceneId);
        if (scene == null) {
            return false;
        }

        if (!scene.getOwnerId().equals(operatorId)) {
            return false;
        }

        List<SceneMember> members = sceneMembers.get(sceneId);
        if (members == null) {
            return false;
        }

        boolean removed = false;
        Iterator<SceneMember> it = members.iterator();
        while (it.hasNext()) {
            SceneMember m = it.next();
            if (m.getMemberId().equals(memberId)) {
                it.remove();
                removed = true;
                break;
            }
        }

        if (removed) {
            scene.setUpdateTime(System.currentTimeMillis());
        }

        return removed;
    }

    @Override
    public List<SceneMember> getMembers(String sceneId) {
        log.info("Getting members for scene: {}", sceneId);
        return sceneMembers.getOrDefault(sceneId, new ArrayList<SceneMember>());
    }

    @Override
    public String generateSceneKey(String sceneId, String operatorId) {
        log.info("Generating scene key: scene={}, operator={}", sceneId, operatorId);

        CollaborationScene scene = sceneStore.get(sceneId);
        if (scene == null) {
            return null;
        }

        if (!scene.getOwnerId().equals(operatorId)) {
            return null;
        }

        String newKey = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        scene.setSceneKey(newKey);
        scene.setUpdateTime(System.currentTimeMillis());

        return newKey;
    }

    @Override
    public boolean updateStatus(String sceneId, String status, String operatorId) {
        log.info("Updating scene status: scene={}, status={}", sceneId, status);

        CollaborationScene scene = sceneStore.get(sceneId);
        if (scene == null) {
            return false;
        }

        if (!scene.getOwnerId().equals(operatorId)) {
            return false;
        }

        try {
            SceneStatus newStatus = SceneStatus.valueOf(status);
            scene.setStatus(newStatus);
            scene.setUpdateTime(System.currentTimeMillis());
            return true;
        } catch (IllegalArgumentException e) {
            log.error("Invalid status: {}", status);
            return false;
        }
    }

    @Override
    public int getSceneCount(String userId) {
        int count = 0;
        for (CollaborationScene scene : sceneStore.values()) {
            if (scene.getOwnerId().equals(userId)) {
                count++;
                continue;
            }
            List<SceneMember> members = sceneMembers.get(scene.getSceneId());
            if (members != null) {
                for (SceneMember m : members) {
                    if (m.getMemberId().equals(userId)) {
                        count++;
                        break;
                    }
                }
            }
        }
        return count;
    }
}
