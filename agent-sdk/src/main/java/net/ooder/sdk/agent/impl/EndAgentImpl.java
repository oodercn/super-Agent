package net.ooder.sdk.agent.impl;

import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.network.packet.ResponsePacket;
import net.ooder.sdk.network.udp.UDPSDK;
import java.util.Map;

public class EndAgentImpl extends AbstractEndAgent {
    
    public EndAgentImpl(UDPSDK udpSDK, String agentId, String agentName, Map<String, Object> capabilities) {
        super(udpSDK, agentId, agentName, "EndAgent", capabilities);
    }
    
    @Override
    protected ResponsePacket createResponse(TaskPacket taskPacket, boolean success, String message) {
        // 实现响应创建逻辑
        return ResponsePacket.<String>builder()
                .commandId(taskPacket.getTaskId())
                .status(success ? "success" : "error")
                .data(message)
                .build();
    }
}
