package net.ooder.sdk.network.packet.params;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;

/**
 * SKILL_INVOKE命令参数类
 */
public class SkillInvokeParams {
    @JSONField(name = "skillId")
    private String skillId;

    @JSONField(name = "params")
    private Map<String, Object> params;

    // 默认构造方法
    public SkillInvokeParams() {
    }

    // 带参数的构造方法
    public SkillInvokeParams(String skillId, Map<String, Object> params) {
        this.skillId = skillId;
        this.params = params;
    }

    // Getter和Setter方法
    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "SkillInvokeParams{" +
                "skillId='" + skillId + '\'' +
                ", params=" + params +
                '}';
    }
}