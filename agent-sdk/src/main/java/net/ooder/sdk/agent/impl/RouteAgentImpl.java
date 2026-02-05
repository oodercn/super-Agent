package net.ooder.sdk.agent.impl;

import net.ooder.sdk.network.udp.UDPSDK;
import java.util.Map;

public class RouteAgentImpl extends AbstractRouteAgent {
    
    public RouteAgentImpl(UDPSDK udpSDK, String agentId, String agentName, Map<String, Object> capabilities) {
        super(udpSDK, agentId, agentName, capabilities);
    }
    
    @Override
    protected net.ooder.sdk.network.packet.ResponsePacket createResponse(net.ooder.sdk.network.packet.TaskPacket taskPacket, boolean success, String message) {
        // 实现响应创建逻辑
        return net.ooder.sdk.network.packet.ResponsePacket.builder()
                .commandId(taskPacket.getTaskId())
                .status(success ? "success" : "error")
                .data(message)
                .build();
    }
}
