package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * ROUTE_ADD命令的实现类
 * 使用DTO模式构建需要为所有字段提供getter/setter方法
 */
public class RouteAddCommand extends Command {
    /**
     * 路由ID
     */
    @JSONField(name = "routeId")
    private String routeId;

    /**
     * 路由源
     */
    @JSONField(name = "source")
    private String source;

    /**
     * 路由目标
     */
    @JSONField(name = "destination")
    private String destination;

    /**
     * 路由信息
     */
    @JSONField(name = "routeInfo")
    private Map<String, Object> routeInfo;

    /**
     * 默认构造方法
     */
    public RouteAddCommand() {
        super(CommandType.ROUTE_ADD);
    }

    /**
     * 带参数的构造方法
     * @param routeId 路由ID
     * @param source 路由源
     * @param destination 路由目标
     * @param routeInfo 路由信息
     */
    public RouteAddCommand(String routeId, String source, String destination, Map<String, Object> routeInfo) {
        super(CommandType.ROUTE_ADD);
        this.routeId = routeId;
        this.source = source;
        this.destination = destination;
        this.routeInfo = routeInfo;
    }

    // Getter和Setter方法
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Map<String, Object> getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(Map<String, Object> routeInfo) {
        this.routeInfo = routeInfo;
    }

    @Override
    public String toString() {
        return "RouteAddCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", routeId='" + routeId + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", routeInfo=" + routeInfo +
                '}';
    }
}













