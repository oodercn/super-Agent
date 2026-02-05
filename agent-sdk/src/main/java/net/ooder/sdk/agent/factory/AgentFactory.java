package net.ooder.sdk.agent.factory;

import net.ooder.sdk.agent.EndAgent;
import net.ooder.sdk.agent.RouteAgent;
import net.ooder.sdk.agent.McpAgent;
import net.ooder.sdk.agent.impl.EndAgentImpl;
import net.ooder.sdk.agent.impl.RouteAgentImpl;
import net.ooder.sdk.agent.impl.McpAgentImpl;
import net.ooder.sdk.config.AgentProperties;
import net.ooder.sdk.config.NetworkProperties;
import net.ooder.sdk.config.RetryProperties;
import net.ooder.sdk.config.PerformanceProperties;
import net.ooder.sdk.config.PortProperties;
import net.ooder.sdk.network.udp.UDPSDK;
import net.ooder.sdk.network.udp.UDPConfig;
import net.ooder.sdk.network.udp.UDPMessageHandler;
import net.ooder.sdk.network.udp.SendResult;
import net.ooder.sdk.network.packet.*;
import net.ooder.sdk.network.factory.NetworkFactory;
import net.ooder.sdk.system.factory.Factory;
import net.ooder.sdk.async.AsyncExecutorService;
import net.ooder.sdk.network.udp.PortManager;
import org.springframework.context.ApplicationContext;
import java.util.Map;

public class AgentFactory {
    
    private static ApplicationContext applicationContext;
    private static AgentProperties agentProperties;
    
    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
        agentProperties = context.getBean(AgentProperties.class);
    }
    
    /**
     * 创建 EndAgent
     */
    public static EndAgent createEndAgent(String agentId, String agentName, Map<String, Object> capabilities) {
        try {
            // 直接创建 EndAgentImpl，不依赖 UDPSDK
            return new EndAgentImpl(null, agentId, agentName, capabilities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create EndAgent: " + e.getMessage(), e);
        }
    }
    
    /**
     * 创建 RouteAgent
     */
    public static RouteAgent createRouteAgent(String agentId, String agentName, Map<String, Object> capabilities) {
        try {
            // 直接创建 RouteAgentImpl，不依赖 UDPSDK
            return new RouteAgentImpl(null, agentId, agentName, capabilities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create RouteAgent: " + e.getMessage(), e);
        }
    }
    
    /**
     * 创建 McpAgent
     */
    public static McpAgent createMcpAgent(String agentId, String agentName, Map<String, Object> capabilities) {
        try {
            // 直接创建 McpAgentImpl，不依赖 UDPSDK
            return new McpAgentImpl(null, agentId, agentName, capabilities);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create McpAgent: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取 NetworkFactory 实例
     */
    private static NetworkFactory getNetworkFactory() {
        if (applicationContext != null) {
            return applicationContext.getBean(NetworkFactory.class);
        } else {
            // 在测试环境中提供默认实现
            return new TestNetworkFactory();
        }
    }
    
    /**
     * 测试环境的 NetworkFactory 实现
     */
    private static class TestNetworkFactory implements NetworkFactory {
        @Override
        public UDPSDK createUDPSDK(UDPConfig config) throws Exception {
            // 直接返回 null，让 EndAgentImpl 等类处理
            return null;
        }
        
        @Override
        public UDPConfig createUDPConfig() {
            return UDPConfig.builder().port(9000).build();
        }
        
        @Override
        public UDPMessageHandler createUDPMessageHandler() {
            return new UDPMessageHandler() {
                @Override
                public void onHeartbeat(HeartbeatPacket packet) {}
                
                @Override
                public void onCommand(CommandPacket packet) {}
                
                @Override
                public void onStatusReport(StatusReportPacket packet) {}
                
                @Override
                public void onTask(TaskPacket packet) {}
                
                @Override
                public void onAuth(AuthPacket packet) {}
                
                @Override
                public void onRoute(RoutePacket packet) {}
                
                @Override
                public void onError(UDPPacket packet, Exception e) {}
            };
        }
    }
    
    /**
     * 根据类型创建 Agent
     */
    public static Object createAgent(String agentType, String agentId, String agentName, Map<String, Object> capabilities) {
        switch (agentType.toLowerCase()) {
            case "endagent":
                return createEndAgent(agentId, agentName, capabilities);
            case "routeagent":
                return createRouteAgent(agentId, agentName, capabilities);
            case "mcpagent":
                return createMcpAgent(agentId, agentName, capabilities);
            default:
                throw new IllegalArgumentException("Invalid agent type: " + agentType);
        }
    }
}
