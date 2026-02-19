
package net.ooder.sdk.service.network.udp;

import net.ooder.sdk.service.network.udp.UdpServer.PacketContext;

public interface PacketHandler {
    
    void handle(byte[] data, String senderAddress, PacketContext context);
}
