package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * ROUTE_CONFIGURE
 * 
 */
public class RouteConfigureCommand extends Command {
    /**
     * 
     */
    @JSONField(name = "configName")
    private String configName;

    /**
     * 
     */
    @JSONField(name = "configValue")
    private Map<String, Object> configValue;

    /**
     * 
     */
    @JSONField(name = "persistent")
    private boolean persistent;

    /**
     * 
     */
    public RouteConfigureCommand() {
        super(CommandType.ROUTE_CONFIGURE);
    }

    /**
     * 
     * @param configName 
     * @param configValue 
     * @param persistent 
     */
    public RouteConfigureCommand(String configName, Map<String, Object> configValue, boolean persistent) {
        super(CommandType.ROUTE_CONFIGURE);
        this.configName = configName;
        this.configValue = configValue;
        this.persistent = persistent;
    }

    // GetterSetter
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Map<String, Object> getConfigValue() {
        return configValue;
    }

    public void setConfigValue(Map<String, Object> configValue) {
        this.configValue = configValue;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    @Override
    public String toString() {
        return "RouteConfigureCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", configName='" + configName + "'" +
                ", configValue=" + configValue +
                ", persistent=" + persistent +
                '}';
    }
}













