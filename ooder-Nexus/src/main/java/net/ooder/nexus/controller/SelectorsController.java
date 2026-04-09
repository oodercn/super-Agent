package net.ooder.nexus.controller;

import net.ooder.nexus.dict.DictService;
import net.ooder.nexus.dict.DictDTO;
import net.ooder.nexus.dict.DictItemDTO;
import net.ooder.nexus.dict.SkillFormEnum;
import net.ooder.nexus.dict.CapabilityCategoryEnum;
import net.ooder.nexus.dto.selector.SelectorOptionDTO;
import net.ooder.nexus.model.ResultModel;
import net.ooder.nexus.util.SkillYamlScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 选择器控制器，提供能力类型、状态、分类等筛选选项接口
 */
@RestController
@RequestMapping("/api/v1/selectors")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class SelectorsController {

    private static final Logger log = LoggerFactory.getLogger(SelectorsController.class);

    @Autowired(required = false)
    private DictService dictService;

    @GetMapping("/capability-types")
    public ResultModel<List<SelectorOptionDTO>> getCapabilityTypes() {
        log.info("[SelectorsController] Get capability types from real data");
        
        List<SelectorOptionDTO> types = new ArrayList<>();
        List<Map<String, Object>> skills = SkillYamlScanner.scanDevDirectory();
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
        List<Map<String, Object>> skills = SkillYamlScanner.scanDevDirectory();
        
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
        List<Map<String, Object>> skills = SkillYamlScanner.scanDevDirectory();
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
}
