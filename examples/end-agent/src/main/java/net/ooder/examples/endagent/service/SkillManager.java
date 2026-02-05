package net.ooder.examples.endagent.service;

import net.ooder.examples.endagent.model.Capability;
import net.ooder.examples.endagent.model.Skill;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SkillManager {
    private final Map<String, Skill> skills = new ConcurrentHashMap<>();

    public SkillManager() {
        // 初始化一些示例技能
        initializeExampleSkills();
    }

    // 初始化示例技能
    private void initializeExampleSkills() {
        // 创建文件转换技能
        Skill fileConverterSkill = new Skill();
        fileConverterSkill.setSkillId("file-converter-001");
        fileConverterSkill.setName("File Converter");
        fileConverterSkill.setDescription("Converts files between different formats");
        fileConverterSkill.setVersion("1.0.0");

        // 创建文件转换能力
        Capability convertCapability = new Capability();
        convertCapability.setId("convert-001");
        convertCapability.setName("convert");
        convertCapability.setDescription("Converts a file from one format to another");
        convertCapability.setReturnType("string");

        // 为了简化，这里不设置具体的参数
        convertCapability.setParameters(new ArrayList<>());

        List<Capability> convertCapabilities = new ArrayList<>();
        convertCapabilities.add(convertCapability);
        fileConverterSkill.setCapabilities(convertCapabilities);

        // 创建文本处理技能
        Skill textProcessorSkill = new Skill();
        textProcessorSkill.setSkillId("text-processor-001");
        textProcessorSkill.setName("Text Processor");
        textProcessorSkill.setDescription("Processes and manipulates text content");
        textProcessorSkill.setVersion("1.0.0");

        // 创建文本分析能力
        Capability analyzeCapability = new Capability();
        analyzeCapability.setId("analyze-001");
        analyzeCapability.setName("analyze");
        analyzeCapability.setDescription("Analyzes text content");
        analyzeCapability.setReturnType("object");
        analyzeCapability.setParameters(new ArrayList<>());

        // 创建文本转换能力
        Capability transformCapability = new Capability();
        transformCapability.setId("transform-001");
        transformCapability.setName("transform");
        transformCapability.setDescription("Transforms text content");
        transformCapability.setReturnType("string");
        transformCapability.setParameters(new ArrayList<>());

        List<Capability> textProcessCapabilities = new ArrayList<>();
        textProcessCapabilities.add(analyzeCapability);
        textProcessCapabilities.add(transformCapability);
        textProcessorSkill.setCapabilities(textProcessCapabilities);

        // 添加到技能映射
        skills.put(fileConverterSkill.getSkillId(), fileConverterSkill);
        skills.put(textProcessorSkill.getSkillId(), textProcessorSkill);

        System.out.println("Initialized example skills: " + skills.size());
    }

    // 获取所有技能
    public List<Skill> getAllSkills() {
        return new ArrayList<>(skills.values());
    }

    // 根据ID获取技能
    public Skill getSkillById(String skillId) {
        return skills.get(skillId);
    }

    // 注册技能
    public void registerSkill(Skill skill) {
        skills.put(skill.getSkillId(), skill);
    }

    // 取消注册技能
    public void unregisterSkill(String skillId) {
        skills.remove(skillId);
    }

    // 调用技能能力
    public Object callCapability(String skillId, String capabilityId, Map<String, Object> params) throws Exception {
        Skill skill = skills.get(skillId);
        if (skill == null) {
            throw new Exception("Skill not found: " + skillId);
        }

        Capability capability = skill.getCapabilities().stream()
                .filter(cap -> cap.getId().equals(capabilityId))
                .findFirst()
                .orElseThrow(() -> new Exception("Capability not found: " + capabilityId));

        // 根据不同的能力ID执行不同的逻辑
        switch (capabilityId) {
            case "convert-001":
                return handleConvertCapability(params);
            case "analyze-001":
                return handleAnalyzeCapability(params);
            case "transform-001":
                return handleTransformCapability(params);
            default:
                throw new Exception("Unsupported capability: " + capabilityId);
        }
    }

    // 处理文件转换能力
    private Object handleConvertCapability(Map<String, Object> params) {
        // 简化实现，实际应该包含真实的文件转换逻辑
        String sourceFormat = (String) params.getOrDefault("source_format", "txt");
        String targetFormat = (String) params.getOrDefault("target_format", "pdf");
        String content = (String) params.getOrDefault("content", "Sample content");

        return "Converted from " + sourceFormat + " to " + targetFormat + ": " + content;
    }

    // 处理文本分析能力
    private Object handleAnalyzeCapability(Map<String, Object> params) {
        // 简化实现，实际应该包含真实的文本分析逻辑
        String text = (String) params.getOrDefault("text", "Sample text");

        Map<String, Object> result = new HashMap<>();
        result.put("wordCount", text.split("\\s+").length);
        result.put("characterCount", text.length());
        result.put("sentenceCount", text.split("[.!?]+").length);

        return result;
    }

    // 处理文本转换能力
    private Object handleTransformCapability(Map<String, Object> params) {
        // 简化实现，实际应该包含真实的文本转换逻辑
        String text = (String) params.getOrDefault("text", "Sample text");
        String operation = (String) params.getOrDefault("operation", "uppercase");

        switch (operation) {
            case "uppercase":
                return text.toUpperCase();
            case "lowercase":
                return text.toLowerCase();
            case "reverse":
                return new StringBuilder(text).reverse().toString();
            default:
                return text;
        }
    }
}
