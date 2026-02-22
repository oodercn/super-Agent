package net.ooder.skillcenter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * SDK配置类
 * 用于配置Mock模式和真实SDK模式的切换
 */
@Configuration
@ConfigurationProperties(prefix = "skillcenter.sdk")
public class SdkConfig {

    /**
     * SDK模式: mock - 使用Mock数据, real - 使用真实SDK
     */
    private String mode = "mock";

    /**
     * 是否启用请求日志
     */
    private boolean enableLogging = true;

    /**
     * Mock数据延迟时间(毫秒),模拟网络延迟
     */
    private long mockDelay = 300;

    /**
     * 真实SDK的基础URL
     */
    private String baseUrl = "http://localhost:8080";

    /**
     * 连接超时时间(毫秒)
     */
    private int connectTimeout = 5000;

    /**
     * 读取超时时间(毫秒)
     */
    private int readTimeout = 10000;

    // ==================== Agent SDK 配置 ====================

    /**
     * Agent ID
     */
    private String agentId = "skillcenter-agent";

    /**
     * Agent 名称
     */
    private String agentName = "SkillCenter Agent";

    /**
     * Agent 类型 (END, MCP, ROUTE)
     */
    private String agentType = "END";

    /**
     * Agent 端点地址
     */
    private String endpoint = "localhost";

    /**
     * UDP端口
     */
    private int udpPort = 8888;

    /**
     * UDP缓冲区大小
     */
    private int udpBufferSize = 65535;

    /**
     * UDP超时时间(毫秒)
     */
    private int udpTimeout = 5000;

    /**
     * UDP最大包大小
     */
    private int udpMaxPacketSize = 8192;

    /**
     * 心跳间隔(毫秒)
     */
    private int heartbeatInterval = 30000;

    /**
     * 心跳超时时间(毫秒)
     */
    private int heartbeatTimeout = 10000;

    /**
     * 心跳丢失阈值
     */
    private int heartbeatLossThreshold = 3;

    /**
     * SDK 0.7.0 - 技能存储基础路径
     */
    private String basePath = "./skillcenter";

    /**
     * SDK 0.7.0 - SkillCenter服务URL
     */
    private String skillCenterUrl = "https://skillcenter.ooder.net";

    // ==================== Getters and Setters ====================

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isEnableLogging() {
        return enableLogging;
    }

    public void setEnableLogging(boolean enableLogging) {
        this.enableLogging = enableLogging;
    }

    public long getMockDelay() {
        return mockDelay;
    }

    public void setMockDelay(long mockDelay) {
        this.mockDelay = mockDelay;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getUdpBufferSize() {
        return udpBufferSize;
    }

    public void setUdpBufferSize(int udpBufferSize) {
        this.udpBufferSize = udpBufferSize;
    }

    public int getUdpTimeout() {
        return udpTimeout;
    }

    public void setUdpTimeout(int udpTimeout) {
        this.udpTimeout = udpTimeout;
    }

    public int getUdpMaxPacketSize() {
        return udpMaxPacketSize;
    }

    public void setUdpMaxPacketSize(int udpMaxPacketSize) {
        this.udpMaxPacketSize = udpMaxPacketSize;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    public int getHeartbeatTimeout() {
        return heartbeatTimeout;
    }

    public void setHeartbeatTimeout(int heartbeatTimeout) {
        this.heartbeatTimeout = heartbeatTimeout;
    }

    public int getHeartbeatLossThreshold() {
        return heartbeatLossThreshold;
    }

    public void setHeartbeatLossThreshold(int heartbeatLossThreshold) {
        this.heartbeatLossThreshold = heartbeatLossThreshold;
    }

    /**
     * 检查是否为Mock模式
     */
    public boolean isMockMode() {
        return "mock".equalsIgnoreCase(mode);
    }

    /**
     * 检查是否为真实SDK模式
     */
    public boolean isRealMode() {
        return "real".equalsIgnoreCase(mode);
    }

    /**
     * 检查是否为SDK 0.7.0模式
     */
    public boolean isSdkMode() {
        return "sdk".equalsIgnoreCase(mode);
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getSkillCenterUrl() {
        return skillCenterUrl;
    }

    public void setSkillCenterUrl(String skillCenterUrl) {
        this.skillCenterUrl = skillCenterUrl;
    }
}
