package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * SKILL_INVOKE命令的具体实现类
 * 采用贫血模型设计仅包含数据字段和getter/setter方法
 */
public class SkillInvokeCommand extends Command {
    /**
     * 技能ID
     */
    @JSONField(name = "skillId")
    private String skillId;

    /**
     * 技能参数
     */
    @JSONField(name = "params")
    private Map<String, Object> params;

    /**
     * 默认构造方法
     */
    public SkillInvokeCommand() {
        super(CommandType.SKILL_INVOKE);
    }

    /**
     * 带参数的构造方法
     * @param skillId 技能ID
     * @param params 技能参数
     */
    public SkillInvokeCommand(String skillId, Map<String, Object> params) {
        super(CommandType.SKILL_INVOKE);
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
        return "SkillInvokeCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", skillId='" + skillId + '\'' +
                ", params=" + params +
                '}';
    }
}










