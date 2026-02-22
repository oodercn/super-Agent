package net.ooder.nexus.adapter.inbound.controller.scene;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.service.scene.SceneGroupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import org.yaml.snakeyaml.Yaml;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景管理控制器
 * 
 * <p>提供场景定义、场景组、能力约束等管理 API。</p>
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@RestController
@RequestMapping(value = "/api/scene", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class SceneManagementController {

    private static final Logger log = LoggerFactory.getLogger(SceneManagementController.class);

    @Autowired(required = false)
    private SceneGroupService sceneGroupService;

    /**
     * 获取场景索引
     */
    @PostMapping("/index")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSceneIndex() {
        log.info("Get scene index request");
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            Map<String, Object> index = loadYamlConfig("scenes/scene-index.yaml");
            result.setData(index);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting scene index", e);
            result.setRequestStatus(500);
            result.setMessage("获取场景索引失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取场景定义列表
     */
    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> listScenes() {
        log.info("List scenes request");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            Map<String, Object> index = loadYamlConfig("scenes/scene-index.yaml");
            List<Map<String, Object>> scenes = new ArrayList<>();
            
            if (index.containsKey("spec")) {
                Map<String, Object> spec = (Map<String, Object>) index.get("spec");
                if (spec.containsKey("scenes")) {
                    scenes = (List<Map<String, Object>>) spec.get("scenes");
                }
            }
            
            for (Map<String, Object> scene : scenes) {
                String sceneId = (String) scene.get("sceneId");
                String configPath = (String) scene.get("configPath");
                if (configPath != null) {
                    try {
                        Map<String, Object> sceneDef = loadYamlConfig(configPath);
                        scene.put("definition", sceneDef);
                    } catch (Exception e) {
                        log.warn("Failed to load scene definition: {}", configPath);
                    }
                }
            }
            
            result.setData(scenes);
            result.setSize(scenes.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error listing scenes", e);
            result.setRequestStatus(500);
            result.setMessage("获取场景列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取场景定义详情
     */
    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getScene(@RequestBody Map<String, String> request) {
        String sceneId = request.get("sceneId");
        log.info("Get scene request: sceneId={}", sceneId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            String configPath = "scenes/" + sceneId + "-scene.yaml";
            Map<String, Object> sceneDef = loadYamlConfig(configPath);
            
            if (sceneDef != null) {
                result.setData(sceneDef);
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("场景不存在");
            }
        } catch (Exception e) {
            log.error("Error getting scene", e);
            result.setRequestStatus(500);
            result.setMessage("获取场景失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取场景能力列表
     */
    @PostMapping("/capabilities")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getCapabilities(@RequestBody Map<String, String> request) {
        String sceneId = request.get("sceneId");
        log.info("Get capabilities request: sceneId={}", sceneId);
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            String configPath = "scenes/" + sceneId + "-scene.yaml";
            Map<String, Object> sceneDef = loadYamlConfig(configPath);
            
            List<Map<String, Object>> capabilities = new ArrayList<>();
            if (sceneDef != null && sceneDef.containsKey("spec")) {
                Map<String, Object> spec = (Map<String, Object>) sceneDef.get("spec");
                if (spec.containsKey("capabilities")) {
                    capabilities = (List<Map<String, Object>>) spec.get("capabilities");
                }
            }
            
            result.setData(capabilities);
            result.setSize(capabilities.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting capabilities", e);
            result.setRequestStatus(500);
            result.setMessage("获取能力列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取场景组列表
     */
    @PostMapping("/groups")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> listSceneGroups() {
        log.info("List scene groups request");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            Map<String, Object> index = loadYamlConfig("scenes/scene-index.yaml");
            List<Map<String, Object>> groups = new ArrayList<>();
            
            if (index.containsKey("spec")) {
                Map<String, Object> spec = (Map<String, Object>) index.get("spec");
                if (spec.containsKey("sceneGroups")) {
                    groups = (List<Map<String, Object>>) spec.get("sceneGroups");
                }
            }
            
            for (Map<String, Object> group : groups) {
                String groupId = (String) group.get("sceneGroupId");
                String configPath = (String) group.get("configPath");
                if (configPath != null) {
                    try {
                        Map<String, Object> groupDef = loadYamlConfig(configPath);
                        group.put("definition", groupDef);
                    } catch (Exception e) {
                        log.warn("Failed to load scene group definition: {}", configPath);
                    }
                }
            }
            
            result.setData(groups);
            result.setSize(groups.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error listing scene groups", e);
            result.setRequestStatus(500);
            result.setMessage("获取场景组列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取场景组详情
     */
    @PostMapping("/groups/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSceneGroup(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        log.info("Get scene group request: groupId={}", groupId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            String configPath = "scenes/" + groupId + "-group.yaml";
            Map<String, Object> groupDef = loadYamlConfig(configPath);
            
            if (groupDef != null) {
                result.setData(groupDef);
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("场景组不存在");
            }
        } catch (Exception e) {
            log.error("Error getting scene group", e);
            result.setRequestStatus(500);
            result.setMessage("获取场景组失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取场景依赖关系
     */
    @PostMapping("/dependencies")
    @ResponseBody
    public ResultModel<Map<String, Object>> getDependencies() {
        log.info("Get dependencies request");
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            Map<String, Object> dependencies = new HashMap<>();
            String[] sceneIds = {"vfs", "org", "auth", "msg", "workflow", "a2ui"};
            
            for (String sceneId : sceneIds) {
                try {
                    String configPath = "scenes/" + sceneId + "-scene.yaml";
                    Map<String, Object> sceneDef = loadYamlConfig(configPath);
                    
                    if (sceneDef != null && sceneDef.containsKey("spec")) {
                        Map<String, Object> spec = (Map<String, Object>) sceneDef.get("spec");
                        Map<String, Object> sceneDeps = new HashMap<>();
                        
                        if (spec.containsKey("dependencies")) {
                            Map<String, Object> deps = (Map<String, Object>) spec.get("dependencies");
                            sceneDeps.put("scenes", deps.get("scenes"));
                            sceneDeps.put("skills", deps.get("skills"));
                        }
                        if (spec.containsKey("collaborativeScenes")) {
                            sceneDeps.put("collaborativeScenes", spec.get("collaborativeScenes"));
                        }
                        
                        dependencies.put(sceneId, sceneDeps);
                    }
                } catch (Exception e) {
                    log.warn("Failed to load dependencies for scene: {}", sceneId);
                }
            }
            
            result.setData(dependencies);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting dependencies", e);
            result.setRequestStatus(500);
            result.setMessage("获取依赖关系失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取场景分类
     */
    @PostMapping("/categories")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getCategories() {
        log.info("Get categories request");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            Map<String, Object> index = loadYamlConfig("scenes/scene-index.yaml");
            List<Map<String, Object>> categories = new ArrayList<>();
            
            if (index.containsKey("spec")) {
                Map<String, Object> spec = (Map<String, Object>) index.get("spec");
                if (spec.containsKey("categories")) {
                    categories = (List<Map<String, Object>>) spec.get("categories");
                }
            }
            
            result.setData(categories);
            result.setSize(categories.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting categories", e);
            result.setRequestStatus(500);
            result.setMessage("获取分类失败: " + e.getMessage());
        }
        return result;
    }

    private Map<String, Object> loadYamlConfig(String path) throws Exception {
        Resource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            return null;
        }
        String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        Yaml yaml = new Yaml();
        return yaml.load(content);
    }
}
