package net.ooder.skill.discovery.service;

import net.ooder.skill.discovery.dto.discovery.CapabilityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

@Service
public class MdnsDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(MdnsDiscoveryService.class);

    private static final String MDNS_GROUP = "224.0.0.251";
    private static final int MDNS_PORT = 5353;
    private static final int BUFFER_SIZE = 4096;

    @Value("${ooder.discovery.mdns.enabled:true}")
    private boolean enabled;

    @Value("${ooder.discovery.mdns.service-type:_ooder-skill._tcp}")
    private String defaultServiceType;

    @Value("${ooder.discovery.mdns.timeout:15}")
    private int defaultTimeoutSeconds;

    @Value("${ooder.discovery.mdns.domain:local}")
    private String domain;

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private volatile boolean running = false;

    public List<CapabilityDTO> discover(String serviceType, int timeoutSeconds) {
        if (!enabled) {
            log.info("[discover] mDNS discovery is disabled");
            return Collections.emptyList();
        }

        String actualServiceType = serviceType != null && !serviceType.isEmpty() ? serviceType : defaultServiceType;
        int actualTimeout = timeoutSeconds > 0 ? timeoutSeconds : defaultTimeoutSeconds;

        log.info("[discover] Starting mDNS discovery for service type: {}, timeout: {}s", actualServiceType, actualTimeout);

        List<CapabilityDTO> capabilities = new ArrayList<>();
        Map<String, CapabilityDTO> discoveredMap = new ConcurrentHashMap<>();

        try {
            InetAddress group = InetAddress.getByName(MDNS_GROUP);
            MulticastSocket socket = new MulticastSocket(MDNS_PORT);
            socket.setReuseAddress(true);
            socket.setSoTimeout(1000);

            NetworkInterface networkInterface = getSuitableNetworkInterface();
            if (networkInterface != null) {
                socket.joinGroup(new InetSocketAddress(group, MDNS_PORT), networkInterface);
            } else {
                socket.joinGroup(group);
            }

            long startTime = System.currentTimeMillis();
            long timeoutMs = actualTimeout * 1000L;

            while (System.currentTimeMillis() - startTime < timeoutMs) {
                try {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    CapabilityDTO cap = createCapabilityFromResponse(packet);
                    if (cap != null && cap.getId() != null) {
                        discoveredMap.putIfAbsent(cap.getId(), cap);
                    }
                } catch (SocketTimeoutException e) {
                    log.debug("[discover] Socket timeout, continuing to listen...");
                }
            }

            if (networkInterface != null) {
                socket.leaveGroup(new InetSocketAddress(group, MDNS_PORT), networkInterface);
            } else {
                socket.leaveGroup(group);
            }
            socket.close();

            capabilities.addAll(discoveredMap.values());
            log.info("[discover] Discovered {} capabilities via mDNS", capabilities.size());

        } catch (Exception e) {
            log.error("[discover] mDNS discovery failed: {}", e.getMessage(), e);
        }

        return capabilities;
    }

    private NetworkInterface getSuitableNetworkInterface() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces != null && interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback() || !networkInterface.isUp() || !networkInterface.supportsMulticast()) {
                continue;
            }
            return networkInterface;
        }
        return null;
    }

    private CapabilityDTO createCapabilityFromResponse(DatagramPacket packet) {
        try {
            CapabilityDTO cap = new CapabilityDTO();
            cap.setId("mdns-" + UUID.randomUUID().toString().substring(0, 8));
            cap.setSource("mdns");
            cap.setStatus("available");
            cap.setName("Remote Skill @ " + packet.getAddress().getHostAddress());
            cap.setDescription("Discovered via mDNS from " + packet.getAddress().getHostAddress());
            return cap;
        } catch (Exception e) {
            log.debug("[createCapabilityFromResponse] Failed to create capability: {}", e.getMessage());
            return null;
        }
    }

    public void shutdown() {
        running = false;
        executor.shutdownNow();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("[shutdown] mDNS discovery service did not shut down gracefully");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
