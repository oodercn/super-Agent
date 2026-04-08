package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.SkillForm;
import net.ooder.mvp.skill.scene.dto.discovery.CapabilityDetailDTO;
import net.ooder.mvp.skill.scene.dto.discovery.DiscoveryResultDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocalDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(LocalDiscoveryService.class);

    @Autowired
    private CapabilityService capabilityService;

    @Autowired
    private SkillCapabilitySyncService syncService;

    public DiscoveryResultDTO discover() {
        log.info("[discover] Starting local discovery");
        
        List<Capability> allCaps = capabilityService.findAll();
        log.info("[discover] Found {} capabilities from storage", allCaps.size());
        
        List<CapabilityDetailDTO> capabilities = allCaps.stream()
            .filter(cap -> !isInternal(cap))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        Map<String, Integer> stats = calculateStats(allCaps);
        
        DiscoveryResultDTO result = new DiscoveryResultDTO();
        result.setCapabilities(capabilities);
        result.setTotal(capabilities.size());
        result.setStats(stats);
        result.setSource("local");
        result.setTimestamp(System.currentTimeMillis());
        
        log.info("[discover] Discovery completed: {} capabilities (filtered {} internal)", 
                capabilities.size(), allCaps.size() - capabilities.size());
        
        return result;
    }

    public SyncResult sync() {
        log.info("[sync] Starting manual sync from skill.yaml files");
        
        Map<String, Object> syncResult = syncService.syncAllSkills();
        
        SyncResult result = new SyncResult();
        result.setSynced((Integer) syncResult.getOrDefault("synced", 0));
        result.setSkipped((Integer) syncResult.getOrDefault("skipped", 0));
        result.setErrors((Integer) syncResult.getOrDefault("errors", 0));
        result.setTimestamp(System.currentTimeMillis());
        
        log.info("[sync] Sync completed: synced={}, skipped={}, errors={}", 
                result.getSynced(), result.getSkipped(), result.getErrors());
        
        return result;
    }

    public CapabilityDetailDTO getCapabilityDetail(String capabilityId) {
        Capability cap = capabilityService.findById(capabilityId);
        if (cap == null) {
            return null;
        }
        return convertToDTO(cap);
    }

    private boolean isInternal(Capability cap) {
        String visibility = cap.getVisibility();
        return "internal".equals(visibility);
    }

    private CapabilityDetailDTO convertToDTO(Capability cap) {
        CapabilityDetailDTO dto = new CapabilityDetailDTO();
        
        dto.setId(cap.getCapabilityId());
        dto.setName(cap.getName());
        dto.setDescription(cap.getDescription());
        dto.setVersion(cap.getVersion());
        dto.setIcon(cap.getIcon());
        dto.setSkillId(cap.getSkillId());
        dto.setInstalled(cap.isInstalled());
        dto.setVisibility(cap.getVisibility() != null ? cap.getVisibility().toLowerCase() : "public");
        
        SkillForm skillForm = cap.getSkillForm();
        dto.setSkillForm(skillForm != null ? skillForm.getCode() : "PROVIDER");
        
        dto.setSceneType(cap.getSceneType());
        dto.setSceneCapability(cap.isSceneCapability());
        dto.setHasSelfDrive(cap.isHasSelfDrive());
        dto.setBusinessCategory(cap.getBusinessCategory());
        dto.setBusinessSemanticsScore(cap.getBusinessSemanticsScore());
        dto.setMainFirst(cap.isMainFirst());
        dto.setCategory(cap.getCategory());
        
        if (cap.getCapabilityCategory() != null) {
            dto.setCapabilityCategory(cap.getCapabilityCategory().getCode());
        }
        
        dto.setDependencies(cap.getDependencies());
        dto.setTags(cap.getTags());
        
        if (cap.getParticipants() != null) {
            List<String> participantIds = cap.getParticipants().stream()
                .map(p -> p.getUserId())
                .collect(Collectors.toList());
            dto.setParticipants(participantIds);
        }
        
        dto.setType(cap.getCapabilityType() != null ? cap.getCapabilityType().name() : null);
        
        return dto;
    }

    private Map<String, Integer> calculateStats(List<Capability> allCaps) {
        Map<String, Integer> stats = new HashMap<>();
        
        int scene = 0, provider = 0, driver = 0;
        int installed = 0, notInstalled = 0;
        
        for (Capability cap : allCaps) {
            SkillForm form = cap.getSkillForm();
            if (form == SkillForm.SCENE) {
                scene++;
            } else if (form == SkillForm.PROVIDER) {
                provider++;
            } else if (form == SkillForm.DRIVER) {
                driver++;
            }
            
            if (cap.isInstalled()) {
                installed++;
            } else {
                notInstalled++;
            }
        }
        
        stats.put("scene", scene);
        stats.put("provider", provider);
        stats.put("driver", driver);
        stats.put("installed", installed);
        stats.put("notInstalled", notInstalled);
        stats.put("total", allCaps.size());
        
        return stats;
    }

    public static class SyncResult {
        private int synced;
        private int skipped;
        private int errors;
        private long timestamp;

        public int getSynced() { return synced; }
        public void setSynced(int synced) { this.synced = synced; }
        public int getSkipped() { return skipped; }
        public void setSkipped(int skipped) { this.skipped = skipped; }
        public int getErrors() { return errors; }
        public void setErrors(int errors) { this.errors = errors; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}
