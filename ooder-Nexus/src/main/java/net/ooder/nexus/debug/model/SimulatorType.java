package net.ooder.nexus.debug.model;

/**
 * 模拟器类型枚举
 */
public enum SimulatorType {

    MCP_NODE("MCP节点模拟器", "模拟MCP主控节点行为"),
    ROUTE_NODE("路由节点模拟器", "模拟路由节点转发行为"),
    END_DEVICE("终端设备模拟器", "模拟终端设备数据上报"),
    GATEWAY("网关模拟器", "模拟协议网关桥接功能"),
    SENSOR("传感器模拟器", "模拟传感器数据采集"),
    ACTUATOR("执行器模拟器", "模拟执行器控制指令");

    private final String displayName;
    private final String description;

    SimulatorType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
