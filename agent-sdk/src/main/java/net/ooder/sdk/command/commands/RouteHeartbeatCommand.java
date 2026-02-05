package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * ROUTE_HEARTBEAT命令的实现类
 * 用于路由代理心跳
 */
public class RouteHeartbeatCommand extends Command {
    /**
     * 路由代理ID
     */
    @JSONField(name = "routeAgentId")
    private String routeAgentId;

    /**
     * 路由代理状态
     */
    @JSONField(name = "status")
    private String status;

    /**
     * 系统负载信息
     */
    @JSONField(name = "systemLoad")
    private Map<String, Object> systemLoad;

    /**
     * 路由数量
     */
    @JSONField(name = "routeCount")
    private int routeCount;

    /**
     * 最后一次心跳时间
     */
    @JSONField(name = "lastHeartbeatTime")
    private long lastHeartbeatTime;

    /**
     * 默认构造方法
     */
    public RouteHeartbeatCommand() {
        super(CommandType.ROUTE_HEARTBEAT);
    }

    /**
     * 带参数的构造方法
     * @param routeAgentId 路由代理ID
     * @param status 路由代理状态
     * @param systemLoad 系统负载信息
     * @param routeCount 路由数量
     * @param lastHeartbeatTime 最后一次心跳时间
     */
    public RouteHeartbeatCommand(String routeAgentId, String status, Map<String, Object> systemLoad, int routeCount, long lastHeartbeatTime) {
        super(CommandType.ROUTE_HEARTBEAT);
        this.routeAgentId = routeAgentId;
        this.status = status;
        this.systemLoad = systemLoad;
        this.routeCount = routeCount;
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    // Getter和Setter方法
    public String getRouteAgentId() {
        return routeAgentId;
    }

    public void setRouteAgentId(String routeAgentId) {
        this.routeAgentId = routeAgentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getSystemLoad() {
        return systemLoad;
    }

    public void setSystemLoad(Map<String, Object> systemLoad) {
        this.systemLoad = systemLoad;
    }

    public int getRouteCount() {
        return routeCount;
    }

    public void setRouteCount(int routeCount) {
        this.routeCount = routeCount;
    }

    public long getLastHeartbeatTime() {
        return lastHeartbeatTime;
    }

    public void setLastHeartbeatTime(long lastHeartbeatTime) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    @Override
    public String toString() {
        return "RouteHeartbeatCommand{" +
                "commandId='" + getCommandId() + "'" +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + "'" +
                ", receiverId='" + getReceiverId() + "'" +
                ", routeAgentId='" + routeAgentId + "'" +
                ", status='" + status + "'" +
                ", systemLoad=" + systemLoad +
                ", routeCount=" + routeCount +
                ", lastHeartbeatTime=" + lastHeartbeatTime +
                '}';
    }
}











