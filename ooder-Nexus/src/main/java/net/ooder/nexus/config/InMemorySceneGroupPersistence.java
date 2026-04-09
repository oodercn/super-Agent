package net.ooder.nexus.config;

import net.ooder.scene.group.SceneGroup;
import net.ooder.scene.group.persistence.SceneGroupPersistence;
import net.ooder.scene.participant.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySceneGroupPersistence implements SceneGroupPersistence {

    private static final Logger log = LoggerFactory.getLogger(InMemorySceneGroupPersistence.class);

    private final Map<String, SceneGroup> groups = new ConcurrentHashMap<>();
    private final Map<String, List<Participant>> participants = new ConcurrentHashMap<>();

    @Override
    public void save(SceneGroup group) throws IOException {
        log.info("[InMemorySceneGroupPersistence] Saving scene group: {}", group.getSceneGroupId());
        groups.put(group.getSceneGroupId(), group);
    }

    @Override
    public Optional<SceneGroup> load(String sceneGroupId) throws IOException {
        log.debug("[InMemorySceneGroupPersistence] Loading scene group: {}", sceneGroupId);
        return Optional.ofNullable(groups.get(sceneGroupId));
    }

    @Override
    public void delete(String sceneGroupId) throws IOException {
        log.info("[InMemorySceneGroupPersistence] Deleting scene group: {}", sceneGroupId);
        groups.remove(sceneGroupId);
        participants.remove(sceneGroupId);
    }

    @Override
    public List<String> listAllSceneGroupIds() throws IOException {
        log.debug("[InMemorySceneGroupPersistence] Listing all scene group ids");
        return new ArrayList<>(groups.keySet());
    }

    @Override
    public void saveParticipants(String sceneGroupId, List<Participant> participantList) throws IOException {
        log.info("[InMemorySceneGroupPersistence] Saving participants for scene group: {}", sceneGroupId);
        participants.put(sceneGroupId, new ArrayList<>(participantList));
    }

    @Override
    public List<Participant> loadParticipants(String sceneGroupId) throws IOException {
        log.debug("[InMemorySceneGroupPersistence] Loading participants for scene group: {}", sceneGroupId);
        return participants.getOrDefault(sceneGroupId, Collections.emptyList());
    }
}
