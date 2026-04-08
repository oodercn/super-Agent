package net.ooder.agent.spi;

import net.ooder.skill.common.spi.storage.PageResult;
import net.ooder.skill.common.spi.storage.SceneGroupData;
import net.ooder.skill.common.spi.storage.SceneGroupStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Profile("test")
public class InMemorySceneGroupStorage implements SceneGroupStorage {

    private static final Logger log = LoggerFactory.getLogger(InMemorySceneGroupStorage.class);

    private final Map<String, SceneGroupData> store = new ConcurrentHashMap<>();

    @Override
    public SceneGroupData save(SceneGroupData data) {
        log.debug("[InMemorySceneGroupStorage] Saving scene group: {}", data.getId());
        if (data.getId() == null || data.getId().isEmpty()) {
            data.setId(UUID.randomUUID().toString());
        }
        if (data.getCreatedAt() == null) {
            data.setCreatedAt(System.currentTimeMillis());
        }
        data.setUpdatedAt(System.currentTimeMillis());
        store.put(data.getId(), data);
        return data;
    }

    @Override
    public Optional<SceneGroupData> findById(String id) {
        log.debug("[InMemorySceneGroupStorage] Finding by id: {}", id);
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public PageResult<SceneGroupData> findByOwnerId(String ownerId, int pageNum, int pageSize) {
        log.debug("[InMemorySceneGroupStorage] Finding by ownerId: {}, page: {}, size: {}", ownerId, pageNum, pageSize);
        
        List<SceneGroupData> filtered = store.values().stream()
                .filter(d -> ownerId.equals(d.getOwnerId()))
                .sorted((a, b) -> Long.compare(b.getCreatedAt(), a.getCreatedAt()))
                .collect(Collectors.toList());

        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, filtered.size());

        PageResult<SceneGroupData> result = new PageResult<>();
        result.setList(start < filtered.size() ? 
                filtered.subList(start, end) : new ArrayList<>());
        result.setTotal(filtered.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public List<SceneGroupData> findByStatus(String status) {
        log.debug("[InMemorySceneGroupStorage] Finding by status: {}", status);
        return store.values().stream()
                .filter(d -> status.equals(d.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SceneGroupData> findAll() {
        log.debug("[InMemorySceneGroupStorage] Finding all");
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(String id) {
        log.debug("[InMemorySceneGroupStorage] Deleting by id: {}", id);
        store.remove(id);
    }

    @Override
    public long count() {
        log.debug("[InMemorySceneGroupStorage] Counting all");
        return store.size();
    }

    @Override
    public boolean existsById(String id) {
        log.debug("[InMemorySceneGroupStorage] Checking exists by id: {}", id);
        return store.containsKey(id);
    }
}
