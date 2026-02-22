package net.ooder.nexus.common.constant;

/**
 * 协议常量定义
 */
public final class ProtocolConstants {

    private ProtocolConstants() {}

    // 协议类型
    public static final String PROTOCOL_TYPE_MCP = "MCP";
    public static final String PROTOCOL_TYPE_ROUTE = "ROUTE";
    public static final String PROTOCOL_TYPE_END = "END";

    // MCP命令类型
    public static final String MCP_REGISTER = "MCP_REGISTER";
    public static final String MCP_DEREGISTER = "MCP_DEREGISTER";
    public static final String MCP_HEARTBEAT = "MCP_HEARTBEAT";
    public static final String MCP_STATUS = "MCP_STATUS";
    public static final String MCP_DISCOVER = "MCP_DISCOVER";
    public static final String MCP_CONFIG = "MCP_CONFIG";

    // Route命令类型
    public static final String ROUTE_REGISTER = "ROUTE_REGISTER";
    public static final String ROUTE_DEREGISTER = "ROUTE_DEREGISTER";
    public static final String ROUTE_UPDATE = "ROUTE_UPDATE";
    public static final String ROUTE_QUERY = "ROUTE_QUERY";
    public static final String ROUTE_STATUS = "ROUTE_STATUS";
    public static final String ROUTE_HEARTBEAT = "ROUTE_HEARTBEAT";

    // End命令类型
    public static final String END_REGISTER = "END_REGISTER";
    public static final String END_DEREGISTER = "END_DEREGISTER";
    public static final String END_CAPABILITY = "END_CAPABILITY";
    public static final String END_STATUS = "END_STATUS";
    public static final String END_COMMAND = "END_COMMAND";
    public static final String END_RESULT = "END_RESULT";
    public static final String END_HEARTBEAT = "END_HEARTBEAT";

    // 协议版本
    public static final String PROTOCOL_VERSION = "2.0";

    // 默认参数
    public static final int DEFAULT_HEARTBEAT_INTERVAL = 30000;
    public static final int DEFAULT_TIMEOUT = 5000;
    public static final int DEFAULT_RETRY_COUNT = 3;
}
