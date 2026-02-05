package net.ooder.sdk.command.commands;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * END_SET_CONFIG
 * End Agent
 */
public class EndSetConfigCommand extends Command {
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
    public EndSetConfigCommand() {
        super(CommandType.END_SET_CONFIG);
    }

    /**
     * 
     * @param configKey 
     * @param configValue 
     */
    public EndSetConfigCommand(String configKey, String configValue) {
        super(CommandType.END_SET_CONFIG);
        this.configKey = configKey;
        this.configValue = configValue;
    }

    /**
     * 
     * @param configMap 
     */
    public EndSetConfigCommand(Map<String, String> configMap) {
        super(CommandType.END_SET_CONFIG);
        this.configMap = configMap;
    }

    // GetterSetter
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
        return "EndSetConfigCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                ", configMap=" + configMap +
                '}';
    }
}














