package net.ooder.agent.controller;

import net.ooder.agent.dict.DictService;
import net.ooder.agent.dict.DictDTO;
import net.ooder.agent.dict.DictItemDTO;
import net.ooder.agent.dict.SkillFormEnum;
import net.ooder.agent.dict.CapabilityCategoryEnum;
import net.ooder.agent.dto.selector.SelectorOptionDTO;
import net.ooder.agent.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;
import org.yaml.snakeyaml.Yaml;

@RestController
@RequestMapping("/api/v1/selectors")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class SelectorsController {

    private static final Logger log = LoggerFactory.getLogger(SelectorsController.class);

    @Value("${ooder.dev.path:./.ooder/dev}")
    private String devPath;

    @Autowired(required = false)
    private DictService dictService;

    @GetMapping("/capability-types")
    public ResultModel<List<SelectorOptionDTO>> getCapabilityTypes() {
        log.info("[SelectorsController] Get capability types from real data");
        
        List<SelectorOptionDTO> types = new ArrayList<>();
        
        List<Map<String, Object>> skills = scanDevDirectory();
        Map<String, Integer> formCount = new HashMap<>();
        
        for (Map<String, Object> skill : skills) {
            String form = (String) skill.get("skillForm");
            if (form == null) form = "SERVICE";
            formCount.merge(form, 1, Integer::sum);
        }
        
        Map<String, String> formNames = getDictMapping("skill_form");
        
        for (Map.Entry<String, Integer> entry : formCount.entrySet()) {
            SelectorOptionDTO type = new SelectorOptionDTO(
                entry.getKey(),
                formNames.getOrDefault(entry.getKey(), entry.getKey()),
                entry.getValue()
            );
            types.add(type);
        }
        
        types.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
        
        return ResultModel.success(types);
    }

    @GetMapping("/capability-statuses")
    public ResultModel<List<SelectorOptionDTO>> getCapabilityStatuses() {
        log.info("[SelectorsController] Get capability statuses from real data");
        
        List<SelectorOptionDTO> statuses = new ArrayList<>();
        
        List<Map<String, Object>> skills = scanDevDirectory();
        
        int installedCount = 0;
        int systemCount = 0;
        
        for (Map<String, Object> skill : skills) {
            String dir = (String) skill.get("directory");
            if ("_system".equals(dir)) {
                systemCount++;
            }
            installedCount++;
        }
        
        if (installedCount > 0) {
            statuses.add(new SelectorOptionDTO("INSTALLED", "已安装", installedCount));
        }
        
        if (systemCount > 0) {
            statuses.add(new SelectorOptionDTO("SYSTEM", "系统内置", systemCount));
        }
        
        return ResultModel.success(statuses);
    }

    @GetMapping("/capability-categories")
    public ResultModel<List<SelectorOptionDTO>> getCapabilityCategories() {
        log.info("[SelectorsController] Get capability categories from real data");
        
        List<SelectorOptionDTO> categories = new ArrayList<>();
        
        List<Map<String, Object>> skills = scanDevDirectory();
        Map<String, Integer> categoryCount = new HashMap<>();
        
        for (Map<String, Object> skill : skills) {
            String category = (String) skill.get("category");
            if (category == null) category = "other";
            categoryCount.merge(category, 1, Integer::sum);
        }
        
        Map<String, String> categoryNames = getDictMapping("capability_category");
        
        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            SelectorOptionDTO cat = new SelectorOptionDTO(
                entry.getKey(),
                categoryNames.getOrDefault(entry.getKey().toLowerCase(), entry.getKey()),
                entry.getValue()
            );
            categories.add(cat);
        }
        
        categories.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
        
        return ResultModel.success(categories);
    }
    
    private Map<String, String> getDictMapping(String dictCode) {
        Map<String, String> mapping = new HashMap<>();
        
        if (dictService != null) {
            DictDTO dict = dictService.getDict(dictCode);
            if (dict != null && dict.getItems() != null) {
                for (DictItemDTO item : dict.getItems()) {
                    mapping.put(item.getValue(), item.getName());
                }
                return mapping;
            }
        }
        
        if ("skill_form".equals(dictCode)) {
            for (SkillFormEnum form : SkillFormEnum.values()) {
                mapping.put(form.getCode(), form.getName());
            }
        } else if ("capability_category".equals(dictCode)) {
            for (CapabilityCategoryEnum cat : CapabilityCategoryEnum.values()) {
                mapping.put(cat.getCode(), cat.getName());
            }
        }
        
        return mapping;
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
}
