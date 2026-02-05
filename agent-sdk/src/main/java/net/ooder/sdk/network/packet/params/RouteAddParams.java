package net.ooder.sdk.network.packet.params;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;

/**
 * ROUTE_ADD命令参数类
 */
public class RouteAddParams {
    @JSONField(name = "routeId")
    private String routeId;

    @JSONField(name = "source")
    private String source;

    @JSONField(name = "destination")
    private String destination;

    @JSONField(name = "routeInfo")
    private Map<String, Object> routeInfo;

    // 默认构造方法
    public RouteAddParams() {
    }

    // 带参数的构造方法
    public RouteAddParams(String routeId, String source, String destination, Map<String, Object> routeInfo) {
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
        return "RouteAddParams{" +
                "routeId='" + routeId + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", routeInfo=" + routeInfo +
                '}';
    }
}