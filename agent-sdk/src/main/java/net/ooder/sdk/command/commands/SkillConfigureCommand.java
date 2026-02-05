package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * SKILL_CONFIGURE
 * 
 */
public class SkillConfigureCommand extends Command {
    /**
     * ID
     */
    @JSONField(name = "skillId")
    private String skillId;

    /**
     * 
     */
    @JSONField(name = "configKey")
    private String configKey;

    /**
     * 
     */
    @JSONField(name = "configValue")
    private String configValue;

    /**
     * 
     */
    @JSONField(name = "configMap")
    private Map<String, String> configMap;

    /**
     * 
     */
    public SkillConfigureCommand() {
        super(CommandType.SKILL_CONFIGURE);
    }

    /**
     * 
     * @param skillId ID
     * @param configKey 
     * @param configValue 
     */
    public SkillConfigureCommand(String skillId, String configKey, String configValue) {
        super(CommandType.SKILL_CONFIGURE);
        this.skillId = skillId;
        this.configKey = configKey;
        this.configValue = configValue;
    }

    /**
     * 
     * @param skillId ID
     * @param configMap 
     */
    public SkillConfigureCommand(String skillId, Map<String, String> configMap) {
        super(CommandType.SKILL_CONFIGURE);
        this.skillId = skillId;
        this.configMap = configMap;
    }

    // GetterSetter
    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Map<String, String> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    @Override
    public String toString() {
        return "SkillConfigureCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", skillId='" + skillId + '\'' +
                ", configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                ", configMap=" + configMap +
                '}';
    }
}














