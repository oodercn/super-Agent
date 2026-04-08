package net.ooder.mvp.skill.scene.capability.registry;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityOwnership;
import net.ooder.mvp.skill.scene.capability.model.CapabilityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CapabilityRegistry {

    private final Map<String, Capability> capabilities = new ConcurrentHashMap<String, Capability>();
    private final Map<String, List<String>> typeIndex = new ConcurrentHashMap<String, List<String>>();
    private final Map<String, List<String>> sceneTypeIndex = new ConcurrentHashMap<String, List<String>>();
    private final Map<String, List<String>> ownershipIndex = new ConcurrentHashMap<String, List<String>>();

    public void register(Capability capability) {
        if (capability == null || capability.getCapabilityId() == null) {
            throw new IllegalArgumentException("Capability and capabilityId must not be null");
        }

        String capId = capability.getCapabilityId();
        capabilities.put(capId, capability);

        if (capability.getCapabilityType() != null) {
            String typeName = capability.getCapabilityType().name();
            if (!typeIndex.containsKey(typeName)) {
                typeIndex.put(typeName, new ArrayList<String>());
            }
            if (!typeIndex.get(typeName).contains(capId)) {
                typeIndex.get(typeName).add(capId);
            }
        }

        if (capability.getSupportedSceneTypes() != null) {
            for (String sceneType : capability.getSupportedSceneTypes()) {
                if (!sceneTypeIndex.containsKey(sceneType)) {
                    sceneTypeIndex.put(sceneType, new ArrayList<String>());
                }
                if (!sceneTypeIndex.get(sceneType).contains(capId)) {
                    sceneTypeIndex.get(sceneType).add(capId);
                }
            }
        }
        
        CapabilityOwnership ownership = capability.getOwnership();
        if (ownership != null) {
            String ownershipName = ownership.name();
            if (!ownershipIndex.containsKey(ownershipName)) {
                ownershipIndex.put(ownershipName, new ArrayList<String>());
            }
            if (!ownershipIndex.get(ownershipName).contains(capId)) {
                ownershipIndex.get(ownershipName).add(capId);
            }
        }
    }

    public void unregister(String capabilityId) {
        Capability capability = capabilities.remove(capabilityId);
        if (capability != null) {
            if (capability.getCapabilityType() != null) {
                String typeName = capability.getCapabilityType().name();
                List<String> typeList = typeIndex.get(typeName);
                if (typeList != null) {
                    typeList.remove(capabilityId);
                }
            }

            if (capability.getSupportedSceneTypes() != null) {
                for (String sceneType : capability.getSupportedSceneTypes()) {
                    List<String> sceneList = sceneTypeIndex.get(sceneType);
                    if (sceneList != null) {
                        sceneList.remove(capabilityId);
                    }
                }
            }
        }
    }

    public Capability findById(String capabilityId) {
        return capabilities.get(capabilityId);
    }

    public List<Capability> findAll() {
        return new ArrayList<Capability>(capabilities.values());
    }

    public List<Capability> findByType(CapabilityType type) {
        List<Capability> result = new ArrayList<Capability>();
        List<String> ids = typeIndex.get(type.name());
        if (ids != null) {
            for (String id : ids) {
                Capability cap = capabilities.get(id);
                if (cap != null) {
                    result.add(cap);
                }
            }
        }
        return result;
    }

    public List<Capability> findBySceneType(String sceneType) {
        List<Capability> result = new ArrayList<Capability>();
        List<String> ids = sceneTypeIndex.get(sceneType);
        if (ids != null) {
            for (String id : ids) {
                Capability cap = capabilities.get(id);
                if (cap != null) {
                    result.add(cap);
                }
            }
        }
        return result;
    }

    public List<Capability> search(String query) {
        List<Capability> result = new ArrayList<Capability>();
        String lowerQuery = query.toLowerCase();

        for (Capability cap : capabilities.values()) {
            if (cap.getName() != null && cap.getName().toLowerCase().contains(lowerQuery)) {
                result.add(cap);
            } else if (cap.getDescription() != null && cap.getDescription().toLowerCase().contains(lowerQuery)) {
                result.add(cap);
            } else if (cap.getCapabilityId() != null && cap.getCapabilityId().toLowerCase().contains(lowerQuery)) {
                result.add(cap);
            }
        }
        return result;
    }

    public int size() {
        return capabilities.size();
    }

    public void clear() {
        capabilities.clear();
        typeIndex.clear();
        sceneTypeIndex.clear();
        ownershipIndex.clear();
    }
    
    public List<Capability> findByOwnership(CapabilityOwnership ownership) {
        List<Capability> result = new ArrayList<Capability>();
        List<String> ids = ownershipIndex.get(ownership.name());
        if (ids != null) {
            for (String id : ids) {
                Capability cap = capabilities.get(id);
                if (cap != null) {
                    result.add(cap);
                }
            }
        }
        return result;
    }
    
    public List<Capability> findByOwnershipAndSceneType(CapabilityOwnership ownership, String sceneType) {
        return findAll().stream()
            .filter(cap -> cap.getOwnership() == ownership)
            .filter(cap -> cap.supportsSceneType(sceneType))
            .collect(Collectors.toList());
    }
    
    public void updateOwnershipIndex(String capabilityId, CapabilityOwnership oldOwnership, CapabilityOwnership newOwnership) {
        if (oldOwnership != null) {
            List<String> oldList = ownershipIndex.get(oldOwnership.name());
            if (oldList != null) {
                oldList.remove(capabilityId);
            }
        }
        
        if (newOwnership != null) {
            String ownershipName = newOwnership.name();
            if (!ownershipIndex.containsKey(ownershipName)) {
                ownershipIndex.put(ownershipName, new ArrayList<String>());
            }
            if (!ownershipIndex.get(ownershipName).contains(capabilityId)) {
                ownershipIndex.get(ownershipName).add(capabilityId);
            }
        }
    }
    
    public void refreshCapability(String capabilityId) {
        Capability capability = capabilities.get(capabilityId);
        if (capability == null) {
            return;
        }
        
        rebuildSceneTypeIndex(capabilityId, capability);
        
        log.debug("Refreshed capability in registry: {}", capabilityId);
    }
    
    private void rebuildSceneTypeIndex(String capabilityId, Capability capability) {
        for (List<String> sceneList : sceneTypeIndex.values()) {
            sceneList.remove(capabilityId);
        }
        
        if (capability.getSupportedSceneTypes() != null) {
            for (String sceneType : capability.getSupportedSceneTypes()) {
                if (!sceneTypeIndex.containsKey(sceneType)) {
                    sceneTypeIndex.put(sceneType, new ArrayList<String>());
                }
                if (!sceneTypeIndex.get(sceneType).contains(capabilityId)) {
                    sceneTypeIndex.get(sceneType).add(capabilityId);
                }
            }
        }
    }
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CapabilityRegistry.class);
}
