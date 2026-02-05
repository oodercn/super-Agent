package net.ooder.examples.skilla.skill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.skill.Skill;
import net.ooder.sdk.skill.SkillResult;
import net.ooder.sdk.skill.SkillStatus;

import java.util.HashMap;
import java.util.Map;

public class InformationRetrievalSkill implements Skill {
    private static final Logger log = LoggerFactory.getLogger(InformationRetrievalSkill.class);
    private boolean initialized = false;

    @Override
    public String getSkillId() {
        return "information-retrieval-skill";
    }

    @Override
    public String getName() {
        return "InformationRetrievalSkill";
    }

    @Override
    public String getDescription() {
        return "A skill for retrieving information based on user queries";
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query", "The information query string");
        return parameters;
    }

    @Override
    public SkillResult execute(Map<String, Object> params) {
        if (!initialized) {
            log.error("Skill not initialized");
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Skill not initialized", metadata);
        }

        try {
            String query = (String) params.get("query");
            if (query == null || query.isEmpty()) {
                Map<String, Object> metadata = new HashMap<>();
                return SkillResult.failure("Query parameter is required", metadata);
            }

            log.info("Processing information retrieval query: {}", query);

            // 模拟信息检索处理
            String result = "Information retrieved for query: " + query;
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("result", result);
            resultData.put("query", query);
            resultData.put("status", "success");

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("message", "Information retrieval completed successfully");
            return SkillResult.success(resultData, metadata);
        } catch (Exception e) {
            log.error("Error executing skill", e);
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Internal error: " + e.getMessage(), metadata);
        }
    }

    @Override
    public void initialize() {
        log.info("Initializing Information Retrieval Skill");
        // 初始化技能所需的资源
        initialized = true;
        log.info("Information Retrieval Skill initialized successfully");
    }

    @Override
    public void destroy() {
        log.info("Destroying Information Retrieval Skill");
        // 释放技能占用的资源
        initialized = false;
        log.info("Information Retrieval Skill destroyed");
    }

    @Override
    public SkillStatus getStatus() {
        return initialized ? SkillStatus.READY : SkillStatus.UNINITIALIZED;
    }

    public boolean isInitialized() {
        return initialized;
    }
}