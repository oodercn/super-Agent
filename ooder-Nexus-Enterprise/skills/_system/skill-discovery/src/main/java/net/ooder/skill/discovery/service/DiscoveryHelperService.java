package net.ooder.skill.discovery.service;

import net.ooder.skill.discovery.model.CapabilityCategory;
import net.ooder.skill.discovery.model.SkillDirectory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DiscoveryHelperService {
    
    public List<Map<String, Object>> getDirectoryInfoList() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (SkillDirectory dir : SkillDirectory.getDisplayOrder()) {
            Map<String, Object> info = new HashMap<>();
            info.put("code", dir.getCode());
            info.put("displayName", dir.getDisplayName());
            info.put("icon", dir.getIcon());
            info.put("color", dir.getColor());
            result.add(info);
        }
        return result;
    }
    
    public List<Map<String, Object>> getCategoryInfoList() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (CapabilityCategory cat : CapabilityCategory.values()) {
            Map<String, Object> info = new HashMap<>();
            info.put("code", cat.getCode());
            info.put("displayName", cat.getDisplayName());
            info.put("icon", cat.getIcon());
            info.put("color", getCategoryColor(cat));
            result.add(info);
        }
        return result;
    }
    
    private String getCategoryColor(CapabilityCategory category) {
        switch (category) {
            case SYS: return "#64748b";
            case BIZ: return "#f59e0b";
            case LLM: return "#6366f1";
            case MSG: return "#ef4444";
            case ORG: return "#3b82f6";
            case VFS: return "#8b5cf6";
            case KNOWLEDGE: return "#10b981";
            case PAYMENT: return "#22c55e";
            case MEDIA: return "#ec4899";
            case UTIL: return "#78716c";
            default: return "#8c8c8c";
        }
    }
    
    public Map<String, Object> buildDirectoryStats(List<?> capabilities, SkillDirectoryDetector detector) {
        Map<String, Integer> counts = new HashMap<>();
        for (SkillDirectory dir : SkillDirectory.values()) {
            counts.put(dir.getCode(), 0);
        }
        
        for (Object cap : capabilities) {
            String skillId = extractSkillId(cap);
            if (skillId != null) {
                SkillDirectory dir = detector.detect(skillId);
                counts.merge(dir.getCode(), 1, Integer::sum);
            }
        }
        
        Map<String, Object> stats = new LinkedHashMap<>();
        int total = capabilities.size();
        
        for (SkillDirectory dir : SkillDirectory.getDisplayOrder()) {
            int count = counts.get(dir.getCode());
            double percentage = total > 0 ? (count * 100.0 / total) : 0;
            
            Map<String, Object> dirStat = new LinkedHashMap<>();
            dirStat.put("directory", dir.getCode());
            dirStat.put("displayName", dir.getDisplayName());
            dirStat.put("count", count);
            dirStat.put("percentage", String.format("%.1f", percentage));
            
            stats.put(dir.getCode(), dirStat);
        }
        
        return stats;
    }
    
    private String extractSkillId(Object capability) {
        try {
            if (capability instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) capability;
                Object skillId = map.get("skillId");
                return skillId != null ? skillId.toString() : null;
            }
            
            java.lang.reflect.Method getSkillIdMethod = capability.getClass().getMethod("getSkillId");
            Object skillId = getSkillIdMethod.invoke(capability);
            return skillId != null ? skillId.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
