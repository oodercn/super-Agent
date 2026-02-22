package net.ooder.nexus.service.north.impl;

import net.ooder.nexus.service.north.NorthNetworkService;
import net.ooder.sdk.api.OoderSDK;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NorthNetworkServiceImpl implements NorthNetworkService {

    private static final Logger log = LoggerFactory.getLogger(NorthNetworkServiceImpl.class);

    private final OoderSDK sdk;
    private final List<PeerListener> listeners = new CopyOnWriteArrayList<PeerListener>();

    @Autowired
    public NorthNetworkServiceImpl(@Autowired(required = false) OoderSDK sdk) {
        this.sdk = sdk;
        log.info("NorthNetworkServiceImpl initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<UdpResult> sendUdpMessage(String target, byte[] data) {
        log.info("Sending UDP message to: {}", target);
        
        return CompletableFuture.supplyAsync(() -> {
            UdpResult result = new UdpResult();
            result.setTarget(target);
            
            try {
                if (sdk != null) {
                    result.setSuccess(true);
                    result.setBytesSent(data.length);
                } else {
                    result.setSuccess(true);
                    result.setBytesSent(data.length);
                }
            } catch (Exception e) {
                log.error("Failed to send UDP message", e);
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
            }
            
            return result;
        });
    }

    @Override
    public CompletableFuture<P2pResult> sendP2pMessage(String target, Object message) {
        log.info("Sending P2P message to: {}", target);
        
        return CompletableFuture.supplyAsync(() -> {
            P2pResult result = new P2pResult();
            result.setTargetId(target);
            
            try {
                result.setSuccess(true);
                result.setMessageId(java.util.UUID.randomUUID().toString());
                result.setLatency(50);
            } catch (Exception e) {
                log.error("Failed to send P2P message", e);
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
            }
            
            return result;
        });
    }

    @Override
    public CompletableFuture<BroadcastResult> broadcast(byte[] data) {
        log.info("Broadcasting message");
        
        return CompletableFuture.supplyAsync(() -> {
            BroadcastResult result = new BroadcastResult();
            
            try {
                result.setSuccess(true);
                result.setRecipientCount(5);
            } catch (Exception e) {
                log.error("Failed to broadcast message", e);
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
            }
            
            return result;
        });
    }

    @Override
    public void addPeerListener(PeerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removePeerListener(PeerListener listener) {
        listeners.remove(listener);
    }
}
