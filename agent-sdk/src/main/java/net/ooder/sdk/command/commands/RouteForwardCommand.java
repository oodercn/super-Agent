package net.ooder.sdk.command.commands;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.model.CommandType;

import java.util.Map;

/**
 * ROUTE_FORWARD命令的实现类
 * 用于路由转发操作
 */
public class RouteForwardCommand extends Command {
    /**
     * 目标路由ID
     */
    @JSONField(name = "routeId")
    private String routeId;

    /**
     * 转发目标地址
     */
    @JSONField(name = "destination")
    private String destination;

    /**
     * 转发内容
     */
    @JSONField(name = "content")
    private Map<String, Object> content;

    /**
     * 是否异步转发
     */
    @JSONField(name = "async")
    private boolean async;

    /**
     * 默认构造方法
     */
    public RouteForwardCommand() {
        super(CommandType.ROUTE_FORWARD);
    }

    /**
     * 带参数的构造方法
     * @param routeId 目标路由ID
     * @param destination 转发目标地址
     * @param content 转发内容
     * @param async 是否异步转发
     */
    public RouteForwardCommand(String routeId, String destination, Map<String, Object> content, boolean async) {
        super(CommandType.ROUTE_FORWARD);
        this.routeId = routeId;
        this.destination = destination;
        this.content = content;
        this.async = async;
    }

    // Getter和Setter方法
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    @Override
    public String toString() {
        return "RouteForwardCommand{" +
                "commandId='" + getCommandId() + '\'' +
                ", commandType=" + getCommandType() +
                ", timestamp=" + getTimestamp() +
                ", senderId='" + getSenderId() + '\'' +
                ", receiverId='" + getReceiverId() + '\'' +
                ", routeId='" + routeId + '\'' +
                ", destination='" + destination + '\'' +
                ", content=" + content +
                ", async=" + async +
                '}';
    }
}










