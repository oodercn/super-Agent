package net.ooder.nexus.common.constant;

/**
 * 状态常量定义
 */
public final class StatusConstants {

    private StatusConstants() {}

    // 通用状态
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_ERROR = "ERROR";
    public static final String STATUS_UNKNOWN = "UNKNOWN";

    // 节点状态
    public static final String NODE_STATUS_ONLINE = "ONLINE";
    public static final String NODE_STATUS_OFFLINE = "OFFLINE";
    public static final String NODE_STATUS_BUSY = "BUSY";
    public static final String NODE_STATUS_MAINTENANCE = "MAINTENANCE";

    // 执行状态
    public static final String EXECUTION_STATUS_RUNNING = "RUNNING";
    public static final String EXECUTION_STATUS_COMPLETED = "COMPLETED";
    public static final String EXECUTION_STATUS_FAILED = "FAILED";
    public static final String EXECUTION_STATUS_CANCELLED = "CANCELLED";
    public static final String EXECUTION_STATUS_PAUSED = "PAUSED";

    // 模拟器状态
    public static final String SIMULATOR_STATUS_STOPPED = "STOPPED";
    public static final String SIMULATOR_STATUS_STARTING = "STARTING";
    public static final String SIMULATOR_STATUS_RUNNING = "RUNNING";
    public static final String SIMULATOR_STATUS_ERROR = "ERROR";
}
