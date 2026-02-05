package net.ooder.sdk.network.udp;

import net.ooder.sdk.network.packet.AuthPacket;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.HeartbeatPacket;
import net.ooder.sdk.network.packet.RoutePacket;
import net.ooder.sdk.network.packet.StatusReportPacket;
import net.ooder.sdk.network.packet.TaskPacket;
import net.ooder.sdk.network.packet.UDPPacket;

public interface UDPMessageHandler {
    void onHeartbeat(HeartbeatPacket packet);
    void onCommand(CommandPacket packet);
    void onStatusReport(StatusReportPacket packet);
    void onAuth(AuthPacket packet);
    void onTask(TaskPacket packet);
    void onRoute(RoutePacket packet);
    void onError(UDPPacket packet, Exception e);
}