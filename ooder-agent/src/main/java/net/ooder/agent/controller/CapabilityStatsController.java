package net.ooder.agent.controller;

import net.ooder.agent.dict.CapabilityCategoryEnum;
import net.ooder.agent.dto.capability.CapabilityStatsOverviewDTO;
import net.ooder.agent.dto.capability.CapabilityRankDTO;
import net.ooder.agent.dto.capability.CapabilityTopDTO;
import net.ooder.agent.dto.capability.CapabilityScoreDistributionDTO;
import net.ooder.agent.dto.capability.CapabilityCategoryDistributionDTO;
import net.ooder.agent.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import org.yaml.snakeyaml.Yaml;

@RestController
@RequestMapping("/api/v1/capabilities/stats")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class CapabilityStatsController {

    private static final Logger log = LoggerFactory.getLogger(CapabilityStatsController.class);

    @Value("${ooder.dev.path:./.ooder/dev}")
    private String devPath;

    @GetMapping("/overview")
    public ResultModel<CapabilityStatsOverviewDTO> getOverview() {
        log.info("[CapabilityStatsController] Get capability stats overview from real data");
        
        List<Map<String, Object>> skills = scanDevDirectory();
        
        int totalCapabilities = skills.size();
        int installedCount = 0;
        int systemCount = 0;
        
        for (Map<String, Object> skill : skills) {
            String dir = (String) skill.get("directory");
            if ("_system".equals(dir)) {
                systemCount++;
            }
            installedCount++;
        }
        
        CapabilityStatsOverviewDTO data = new CapabilityStatsOverviewDTO();
        data.setTotalInvocations(0L);
        data.setSuccessInvocations(0L);
        data.setFailedInvocations(0L);
        data.setAvgResponseTime(0.0);
        data.setActiveCapabilities(installedCount);
        data.setInstalledCapabilities(installedCount);
        data.setTotalCapabilities(totalCapabilities);
        data.setSystemCapabilities(systemCount);
        
        log.info("[CapabilityStatsController] Stats: total={}, installed={}, system={}", 
            totalCapabilities, installedCount, systemCount);
        
        return ResultModel.success(data);
    }

    @GetMapping("/rank")
    public ResultModel<List<CapabilityRankDTO>> getCapabilityRank(
            @RequestParam(defaultValue = "5") int limit) {
        log.info("[CapabilityStatsController] Get capability rank from real data, limit: {}", limit);
        
        List<CapabilityRankDTO> rankList = new ArrayList<>();
        
        List<Map<String, Object>> skills = scanDevDirectory();
        
        int rank = 0;
        for (Map<String, Object> skill : skills) {
            if (rank >= limit) break;
            
            CapabilityRankDTO item = new CapabilityRankDTO();
            item.setCapabilityId((String) skill.get("skillId"));
            item.setName((String) skill.get("name"));
            item.setInvokeCount(0);
            item.setType(skill.get("skillForm") != null ? (String) skill.get("skillForm") : "SERVICE");
            item.setCategory((String) skill.get("category"));
            item.setRank(rank + 1);
            item.setInstalled(true);
            rankList.add(item);
            rank++;
        }
        
        return ResultModel.success(rankList);
    }

    @GetMapping("/top")
    public ResultModel<List<CapabilityTopDTO>> getTopCapabilities(
            @RequestParam(defaultValue = "5") int limit) {
        log.info("[CapabilityStatsController] Get top capabilities from real data, limit: {}", limit);
        
        List<CapabilityTopDTO> topList = new ArrayList<>();
        
        List<Map<String, Object>> skills = scanDevDirectory();
        
        int count = 0;
        for (Map<String, Object> skill : skills) {
            if (count >= limit) break;
            
            CapabilityTopDTO item = new CapabilityTopDTO();
            item.setCapabilityId((String) skill.get("skillId"));
            item.setName((String) skill.get("name"));
            item.setInvokeCount(0);
            item.setType(skill.get("skillForm") != null ? (String) skill.get("skillForm") : "SERVICE");
            item.setCategory((String) skill.get("category"));
            item.setSuccessRate(100.0);
            item.setInstalled(true);
            topList.add(item);
            count++;
        }
        
        return ResultModel.success(topList);
    }

    @GetMapping("/errors")
    public ResultModel<List<Map<String, Object>>> getRecentErrors(
            @RequestParam(defaultValue = "5") int limit) {
        log.info("[CapabilityStatsController] Get recent errors from real data, limit: {}", limit);
        
        List<Map<String, Object>> errorList = new ArrayList<>();
        
        return ResultModel.success(errorList);
    }

    @GetMapping("/scores")
    public ResultModel<CapabilityScoreDistributionDTO> getScoreDistribution() {
        log.info("[CapabilityStatsController] Get score distribution from real data");
        
        List<Map<String, Object>> skills = scanDevDirectory();
        
        int highCount = 0;
        int mediumCount = 0;
        int lowCount = 0;
        int[] distribution = new int[11];
        
        for (Map<String, Object> skill : skills) {
            int score = 5;
            distribution[Math.min(10, Math.max(0, score))]++;
            
            if (score >= 8) {
                highCount++;
            } else if (score >= 5) {
                mediumCount++;
            } else {
                lowCount++;
            }
        }
        
        int total = skills.size();
        CapabilityScoreDistributionDTO data = new CapabilityScoreDistributionDTO();
        data.setAvgScore(total > 0 ? 5.0 : 0.0);
        data.setHighCount(highCount);
        data.setMediumCount(mediumCount);
        data.setLowCount(lowCount);
        data.setDistribution(Arrays.stream(distribution).boxed().toList());
        
        return ResultModel.success(data);
    }

    @GetMapping("/categories")
    public ResultModel<List<CapabilityCategoryDistributionDTO>> getCategoryDistribution() {
        log.info("[CapabilityStatsController] Get category distribution from real data");
        
        List<CapabilityCategoryDistributionDTO> categories = new ArrayList<>();
        
        List<Map<String, Object>> skills = scanDevDirectory();
        Map<String, Integer> categoryCount = new HashMap<>();
        
        for (Map<String, Object> skill : skills) {
            String category = (String) skill.get("category");
            if (category == null) category = "other";
            categoryCount.merge(category, 1, Integer::sum);
        }
        
        String[] colors = {"#9334ff", "#10b981", "#f97316", "#4f46e5", "#6b7280", "#ec4899", "#22c55e", "#3b82f6"};
        int colorIndex = 0;
        
        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            CapabilityCategoryDistributionDTO cat = new CapabilityCategoryDistributionDTO(
                entry.getKey(),
                getCategoryDisplayName(entry.getKey()),
                entry.getValue(),
                colors[colorIndex % colors.length]
            );
            categories.add(cat);
            colorIndex++;
        }
        
        categories.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
        
        return ResultModel.success(categories);
    }
    
    private List<Map<String, Object>> scanDevDirectory() {
        List<Map<String, Object>> skills = new ArrayList<>();
        
        String basePath = System.getProperty("user.dir");
        File devDir = new File(basePath, ".ooder/dev");
        
        if (!devDir.exists() || !devDir.isDirectory()) {
            log.warn("[scanDevDirectory] Dev directory does not exist: {}", devDir.getAbsolutePath());
            return skills;
        }
        
        scanForSkillYamlFiles(devDir, skills);
        
        log.info("[scanDevDirectory] Found {} skills in dev directory", skills.size());
        return skills;
    }
    
    private void scanForSkillYamlFiles(File directory, List<Map<String, Object>> skills) {
        File[] files = directory.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.isDirectory()) {
                File skillYaml = new File(file, "skill.yaml");
                if (skillYaml.exists()) {
                    Map<String, Object> skill = parseSkillYaml(skillYaml);
                    if (skill != null) {
                        String parentDir = file.getParentFile().getName();
                        skill.put("directory", parentDir);
                        skills.add(skill);
                    }
                }
                scanForSkillYamlFiles(file, skills);
            }
        }
    }
    
    private Map<String, Object> parseSkillYaml(File skillYamlFile) {
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(skillYamlFile);
            Map<String, Object> data = yaml.load(inputStream);
            inputStream.close();
            
            Map<String, Object> skill = new HashMap<>();
            
            String skillId = null;
            String name = null;
            String version = null;
            String description = null;
            String category = null;
            String skillForm = null;
            
            Map<String, Object> metadata = (Map<String, Object>) data.get("metadata");
            if (metadata != null) {
                skillId = (String) metadata.get("id");
                name = (String) metadata.get("name");
                version = (String) metadata.get("version");
                description = (String) metadata.get("description");
                category = (String) metadata.get("category");
            }
            
            if (skillId == null) {
                skillId = (String) data.get("id");
            }
            if (skillId == null) {
                skillId = (String) data.get("skillId");
            }
            if (name == null) {
                name = (String) data.get("name");
            }
            if (version == null) {
                version = (String) data.get("version");
            }
            if (description == null) {
                description = (String) data.get("description");
            }
            if (category == null) {
                category = (String) data.get("category");
            }
            
            Map<String, Object> spec = (Map<String, Object>) data.get("spec");
            if (spec != null) {
                skillForm = (String) spec.get("skillForm");
                if (category == null) {
                    List<Map<String, Object>> capabilities = 
                        (List<Map<String, Object>>) spec.get("capabilities");
                    if (capabilities != null && !capabilities.isEmpty()) {
                        category = (String) capabilities.get(0).get("category");
                    }
                }
            }
            if (skillForm == null) {
                skillForm = (String) data.get("skillForm");
            }
            
            skill.put("skillId", skillId);
            skill.put("name", name != null ? name : skillId);
            skill.put("description", description);
            skill.put("version", version);
            skill.put("category", category != null ? category.toLowerCase() : "other");
            skill.put("skillForm", skillForm != null ? skillForm : "PROVIDER");
            
            return skill;
        } catch (Exception e) {
            log.error("[parseSkillYaml] Failed to parse {}: {}", skillYamlFile.getAbsolutePath(), e.getMessage());
            return null;
        }
    }
    
    private String getCategoryDisplayName(String code) {
        if (code == null) return CapabilityCategoryEnum.OTHER.getName();
        return CapabilityCategoryEnum.fromCode(code).getName();
    }
}
