package net.ooder.mvp.skill.scene.llm.prompt;

import net.ooder.mvp.skill.scene.config.sdk.ConfigNode;
import net.ooder.mvp.skill.scene.config.service.ConfigLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SkillPromptServiceImpl implements SkillPromptService {

    private static final Logger log = LoggerFactory.getLogger(SkillPromptServiceImpl.class);

    private static final String DEFAULT_PROMPT_FILE = "classpath:prompts/system-prompt.md";
    private static final String SKILL_PROMPT_PATH = "spec.llmConfig.systemPrompt";
    private static final String SKILL_PROMPT_FILE_PATH = "spec.llmConfig.systemPromptFile";

    @Autowired
    private ConfigLoaderService configLoader;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Value("${ooder.llm.prompt.default-file:" + DEFAULT_PROMPT_FILE + "}")
    private String defaultPromptFile;
    
    private final Map<String, String> promptCache = new ConcurrentHashMap<>();
    private String cachedDefaultPrompt;
    
    @PostConstruct
    public void init() {
        cachedDefaultPrompt = loadDefaultPromptFromFile();
        log.info("[SkillPromptService] Initialized, default prompt length: {}", 
            cachedDefaultPrompt != null ? cachedDefaultPrompt.length() : 0);
    }

    @Override
    public String getSystemPrompt(String skillId) {
        return getSystemPrompt(skillId, null);
    }

    @Override
    public String getSystemPrompt(String skillId, String sceneId) {
        String cacheKey = buildCacheKey(skillId, sceneId);
        
        String cached = promptCache.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        String prompt = loadPromptFromConfig(skillId, sceneId);
        
        if (prompt == null || prompt.isEmpty()) {
            prompt = cachedDefaultPrompt;
            log.debug("[SkillPromptService] Using default prompt for skill: {}", skillId);
        }
        
        if (prompt != null) {
            promptCache.put(cacheKey, prompt);
        }
        
        return prompt;
    }

    @Override
    public String getRolePrompt(String skillId, String roleId) {
        ConfigNode skillConfig = configLoader.loadSkillConfig(skillId, true);
        
        if (skillConfig == null) {
            return null;
        }
        
        String rolePromptPath = "spec.llmConfig.rolePrompts." + roleId;
        Object rolePrompt = skillConfig.getNested(rolePromptPath);
        
        if (rolePrompt instanceof String) {
            return (String) rolePrompt;
        }
        
        return null;
    }

    @Override
    public String getDefaultSystemPrompt() {
        if (cachedDefaultPrompt == null) {
            cachedDefaultPrompt = loadDefaultPromptFromFile();
        }
        return cachedDefaultPrompt;
    }

    @Override
    public Map<String, Object> getPromptConfig(String skillId) {
        Map<String, Object> config = new HashMap<>();
        
        ConfigNode skillConfig = configLoader.loadSkillConfig(skillId, true);
        if (skillConfig == null) {
            return config;
        }
        
        Object llmConfig = skillConfig.getNested("spec.llmConfig");
        if (llmConfig instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> llmConfigMap = (Map<String, Object>) llmConfig;
            config.putAll(llmConfigMap);
        }
        
        return config;
    }

    @Override
    public String resolveVariables(String prompt, Map<String, Object> variables) {
        if (prompt == null || variables == null || variables.isEmpty()) {
            return prompt;
        }
        
        String result = prompt;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            String value = entry.getValue() != null ? String.valueOf(entry.getValue()) : "";
            result = result.replace(placeholder, value);
        }
        
        return result;
    }
    
    private String loadPromptFromConfig(String skillId, String sceneId) {
        ConfigNode config = null;
        
        if (sceneId != null) {
            config = configLoader.loadSceneConfig(sceneId, true);
        }
        
        if (config == null) {
            config = configLoader.loadSkillConfig(skillId, true);
        }
        
        if (config == null) {
            return null;
        }
        
        String prompt = config.getNestedString(SKILL_PROMPT_PATH);
        if (prompt != null && !prompt.isEmpty()) {
            log.debug("[SkillPromptService] Loaded prompt from config for skill: {}", skillId);
            return prompt;
        }
        
        String promptFile = config.getNestedString(SKILL_PROMPT_FILE_PATH);
        if (promptFile != null && !promptFile.isEmpty()) {
            String filePrompt = loadPromptFromFile(promptFile, skillId);
            if (filePrompt != null) {
                return filePrompt;
            }
        }
        
        return null;
    }
    
    private String loadPromptFromFile(String filePath, String skillId) {
        try {
            String resourcePath = filePath;
            if (!filePath.startsWith("classpath:") && !filePath.startsWith("file:")) {
                resourcePath = "classpath:skills/" + skillId + "/" + filePath;
            }
            
            Resource resource = resourceLoader.getResource(resourcePath);
            if (!resource.exists()) {
                log.debug("[SkillPromptService] Prompt file not found: {}", resourcePath);
                return null;
            }
            
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            
            log.debug("[SkillPromptService] Loaded prompt from file: {}", resourcePath);
            return content.toString().trim();
            
        } catch (IOException e) {
            log.warn("[SkillPromptService] Failed to load prompt file: {} - {}", filePath, e.getMessage());
            return null;
        }
    }
    
    private String loadDefaultPromptFromFile() {
        try {
            Resource resource = resourceLoader.getResource(defaultPromptFile);
            if (!resource.exists()) {
                log.info("[SkillPromptService] Default prompt file not found: {}, using built-in default", defaultPromptFile);
                return getBuiltInDefaultPrompt();
            }
            
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            
            return content.toString().trim();
            
        } catch (IOException e) {
            log.warn("[SkillPromptService] Failed to load default prompt file: {}", e.getMessage());
            return getBuiltInDefaultPrompt();
        }
    }
    
    private String getBuiltInDefaultPrompt() {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是Ooder场景技能平台的智能助手。\n\n");
        prompt.append("## 平台简介\n");
        prompt.append("Ooder是一个场景驱动的技能管理平台，用户可以通过发现、安装、配置能力来构建自动化场景。\n\n");
        prompt.append("## 核心概念\n");
        prompt.append("- **能力**: 可执行的功能单元，如发送邮件、生成报告等\n");
        prompt.append("- **场景**: 由多个能力组成的自动化流程\n\n");
        prompt.append("## 回复要求\n");
        prompt.append("- 用简洁专业的中文回复\n");
        prompt.append("- 不要提及你是DeepSeek或其他AI模型\n");
        return prompt.toString();
    }
    
    private String buildCacheKey(String skillId, String sceneId) {
        if (sceneId != null) {
            return skillId + ":" + sceneId;
        }
        return skillId;
    }
    
    public void clearCache() {
        promptCache.clear();
        cachedDefaultPrompt = null;
        log.info("[SkillPromptService] Cache cleared");
    }
    
    public void refreshCache(String skillId) {
        promptCache.keySet().removeIf(key -> key.startsWith(skillId));
        log.debug("[SkillPromptService] Cache refreshed for skill: {}", skillId);
    }
}
