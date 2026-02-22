package net.ooder.skillcenter.sdk.protocol;

import net.ooder.nexus.skillcenter.dto.protocol.DiscoveryDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DiscoveryDTO.DiscoveryRequestDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DiscoveryDTO.DiscoveryResultDTO;
import net.ooder.nexus.skillcenter.dto.protocol.DiscoveryDTO.PeerDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DiscoveryProtocolAdapter {

    CompletableFuture<DiscoveryResultDTO> discoverPeers(DiscoveryRequestDTO request);

    CompletableFuture<List<PeerDTO>> listDiscoveredPeers();

    CompletableFuture<PeerDTO> discoverMcp();

    void addDiscoveryListener(DiscoveryEventListener listener);

    void removeDiscoveryListener(DiscoveryEventListener listener);

    void startBroadcast();

    void stopBroadcast();

    boolean isBroadcasting();

    boolean isAvailable();

    interface DiscoveryEventListener {
        void onPeerDiscovered(PeerDTO peer);
        void onPeerLost(String peerId);
        void onMcpDiscovered(PeerDTO mcp);
        void onDiscoveryComplete(DiscoveryResultDTO result);
    }
}
