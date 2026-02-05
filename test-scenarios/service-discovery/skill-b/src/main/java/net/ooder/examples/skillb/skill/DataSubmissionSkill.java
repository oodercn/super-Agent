package net.ooder.examples.skillb.skill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.skill.Skill;
import net.ooder.sdk.skill.SkillResult;
import net.ooder.sdk.skill.SkillStatus;

import java.util.HashMap;
import java.util.Map;

public class DataSubmissionSkill implements Skill {
    private static final Logger log = LoggerFactory.getLogger(DataSubmissionSkill.class);
    private boolean initialized = false;

    @Override
    public String getSkillId() {
        return "data-submission-skill";
    }

    @Override
    public String getName() {
        return "DataSubmissionSkill";
    }

    @Override
    public String getDescription() {
        return "A skill for submitting data to the system";
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("data", "The data to submit");
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
            String data = (String) params.get("data");
            if (data == null || data.isEmpty()) {
                Map<String, Object> metadata = new HashMap<>();
                return SkillResult.failure("Data parameter is required", metadata);
            }

            log.info("Processing data submission: {}", data);

            // 模拟数据提交处理
            String result = "Data submitted successfully: " + data;
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("result", result);
            resultData.put("data", data);
            resultData.put("status", "success");
            resultData.put("timestamp", System.currentTimeMillis());

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("message", "Data submission completed successfully");
            return SkillResult.success(resultData, metadata);
        } catch (Exception e) {
            log.error("Error executing skill", e);
            Map<String, Object> metadata = new HashMap<>();
            return SkillResult.failure("Internal error: " + e.getMessage(), metadata);
        }
    }

    @Override
    public void initialize() {
        log.info("Initializing Data Submission Skill");
        // 初始化技能所需的资源
        initialized = true;
        log.info("Data Submission Skill initialized successfully");
    }

    @Override
    public void destroy() {
        log.info("Destroying Data Submission Skill");
        // 释放技能占用的资源
        initialized = false;
        log.info("Data Submission Skill destroyed");
    }

    @Override
    public SkillStatus getStatus() {
        return initialized ? SkillStatus.READY : SkillStatus.UNINITIALIZED;
    }

    public boolean isInitialized() {
        return initialized;
    }
}