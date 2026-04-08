package net.ooder.mvp.skill.scene.capability.service;

import net.ooder.mvp.skill.scene.capability.model.BusinessCategory;
import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityCategory;
import net.ooder.mvp.skill.scene.capability.model.SkillForm;
import net.ooder.mvp.skill.scene.capability.model.Visibility;
import net.ooder.mvp.skill.scene.discovery.MvpSkillIndexLoader;
import net.ooder.mvp.skill.scene.dto.discovery.CapabilityDTO;
import net.ooder.mvp.skill.scene.dto.discovery.SkillStatisticsDTO;
import net.ooder.mvp.skill.scene.dto.discovery.SkillStatisticsDTO.CategoryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkillStatisticsService {
    
    @Autowired
    private MvpSkillIndexLoader mvpSkillIndexLoader;
    
    private static final Map<String, String> CAPABILITY_TO_BUSINESS_MAP = new HashMap<>();
    static {
        CAPABILITY_TO_BUSINESS_MAP.put("llm", "AI_ASSISTANT");
        CAPABILITY_TO_BUSINESS_MAP.put("know", "AI_ASSISTANT");
        CAPABILITY_TO_BUSINESS_MAP.put("comm", "OFFICE_COLLABORATION");
        CAPABILITY_TO_BUSINESS_MAP.put("media", "MARKETING_OPERATIONS");
        CAPABILITY_TO_BUSINESS_MAP.put("mon", "SYSTEM_MONITOR");
        CAPABILITY_TO_BUSINESS_MAP.put("sec", "SECURITY_AUDIT");
        CAPABILITY_TO_BUSINESS_MAP.put("iot", "INFRASTRUCTURE");
        CAPABILITY_TO_BUSINESS_MAP.put("org", "INFRASTRUCTURE");
        CAPABILITY_TO_BUSINESS_MAP.put("sys", "INFRASTRUCTURE");
        CAPABILITY_TO_BUSINESS_MAP.put("auth", "SECURITY_AUDIT");
        CAPABILITY_TO_BUSINESS_MAP.put("net", "INFRASTRUCTURE");
        CAPABILITY_TO_BUSINESS_MAP.put("vfs", "SYSTEM_TOOLS");
        CAPABILITY_TO_BUSINESS_MAP.put("db", "SYSTEM_TOOLS");
        CAPABILITY_TO_BUSINESS_MAP.put("payment", "SYSTEM_TOOLS");
        CAPABILITY_TO_BUSINESS_MAP.put("search", "DATA_PROCESSING");
        CAPABILITY_TO_BUSINESS_MAP.put("sched", "INFRASTRUCTURE");
        CAPABILITY_TO_BUSINESS_MAP.put("util", "SYSTEM_TOOLS");
    }
    
    public SkillStatisticsDTO getStatistics() {
        List<CapabilityDTO> capabilities = mvpSkillIndexLoader.getSkillsFromEntryFiles("LOCAL");
        
        SkillStatisticsDTO dto = new SkillStatisticsDTO();
        dto.setTotal(capabilities.size());
        
        dto.setByBusinessCategory(calculateBusinessCategoryStats(capabilities));
        dto.setBySkillForm(calculateSkillFormStats(capabilities));
        dto.setByVisibility(calculateVisibilityStats(capabilities));
        dto.setByCategory(calculateCategoryStats(capabilities));
        
        return dto;
    }
    
    private String inferBusinessCategory(CapabilityDTO cap) {
        if (cap.getBusinessCategory() != null && !cap.getBusinessCategory().isEmpty()) {
            return cap.getBusinessCategory();
        }
        
        String capCat = cap.getCapabilityCategory();
        if (capCat != null) {
            String inferred = CAPABILITY_TO_BUSINESS_MAP.get(capCat.toLowerCase());
            if (inferred != null) {
                return inferred;
            }
        }
        
        return "SYSTEM_TOOLS";
    }
    
    private List<CategoryStatistics> calculateBusinessCategoryStats(List<CapabilityDTO> capabilities) {
        Map<String, Integer> counts = new HashMap<>();
        
        for (CapabilityDTO cap : capabilities) {
            String bc = inferBusinessCategory(cap);
            counts.merge(bc, 1, Integer::sum);
        }
        
        List<CategoryStatistics> stats = new ArrayList<>();
        for (BusinessCategory bc : BusinessCategory.values()) {
            int count = counts.getOrDefault(bc.getCode(), 0);
            if (count > 0) {
                stats.add(new CategoryStatistics(bc.getCode(), bc.getName(), count, capabilities.size()));
            }
        }
        
        return stats;
    }
    
    private List<CategoryStatistics> calculateSkillFormStats(List<CapabilityDTO> capabilities) {
        Map<String, Integer> counts = new HashMap<>();
        
        for (CapabilityDTO cap : capabilities) {
            String form = cap.getSkillForm();
            if (form == null || form.isEmpty()) {
                form = "PROVIDER";
            }
            counts.merge(form, 1, Integer::sum);
        }
        
        List<CategoryStatistics> stats = new ArrayList<>();
        for (SkillForm form : SkillForm.values()) {
            int count = counts.getOrDefault(form.getCode(), 0);
            stats.add(new CategoryStatistics(form.getCode(), form.getName(), count, capabilities.size()));
        }
        
        return stats;
    }
    
    private List<CategoryStatistics> calculateVisibilityStats(List<CapabilityDTO> capabilities) {
        Map<String, Integer> counts = new HashMap<>();
        
        for (CapabilityDTO cap : capabilities) {
            String vis = cap.getVisibility();
            if (vis == null || vis.isEmpty()) {
                vis = "public";
            }
            counts.merge(vis, 1, Integer::sum);
        }
        
        List<CategoryStatistics> stats = new ArrayList<>();
        for (Visibility vis : Visibility.values()) {
            int count = counts.getOrDefault(vis.getCode(), 0);
            stats.add(new CategoryStatistics(vis.getCode(), vis.getName(), count, capabilities.size()));
        }
        
        return stats;
    }
    
    private List<CategoryStatistics> calculateCategoryStats(List<CapabilityDTO> capabilities) {
        Map<String, Integer> counts = new HashMap<>();
        
        for (CapabilityDTO cap : capabilities) {
            String cat = cap.getCapabilityCategory();
            if (cat == null || cat.isEmpty()) {
                cat = "util";
            }
            counts.merge(cat.toLowerCase(), 1, Integer::sum);
        }
        
        List<CategoryStatistics> stats = new ArrayList<>();
        for (CapabilityCategory cat : CapabilityCategory.values()) {
            int count = counts.getOrDefault(cat.getCode(), 0);
            if (count > 0) {
                stats.add(new CategoryStatistics(cat.getCode(), cat.getName(), count, capabilities.size()));
            }
        }
        
        return stats;
    }
}
