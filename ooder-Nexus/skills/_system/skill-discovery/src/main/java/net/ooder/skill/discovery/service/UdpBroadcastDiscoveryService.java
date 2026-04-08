package net.ooder.skill.discovery.service;

import net.ooder.skill.discovery.dto.discovery.CapabilityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

@Service
public class UdpBroadcastDiscoveryService {

    private static final Logger log = LoggerFactory.getLogger(UdpBroadcastDiscoveryService.class);

    private static final String DISCOVERY_MESSAGE = "OODER_SKILL_DISCOVERY";
    private static final String RESPONSE_PREFIX = "OODER_SKILL_RESPONSE:";
    private static final int BUFFER_SIZE = 4096;

    @Value("${ooder.discovery.udp.port:37021}")
    private int defaultPort;

    @Value("${ooder.discovery.udp.timeout:10}")
    private int defaultTimeoutSeconds;

    @Value("${ooder.discovery.udp.enabled:true}")
    private boolean enabled;

    @Value("${ooder.discovery.udp.broadcast-address:255.255.255.255}")
    private String broadcastAddress;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public List<CapabilityDTO> discover(int port, int timeoutSeconds) {
        return discover(port, timeoutSeconds, null);
    }

    public List<CapabilityDTO> discover(int port, int timeoutSeconds, String filter) {
        if (!enabled) {
            log.info("[discover] UDP discovery is disabled");
            return Collections.emptyList();
        }

        int actualPort = port > 0 ? port : defaultPort;
        int actualTimeout = timeoutSeconds > 0 ? timeoutSeconds : defaultTimeoutSeconds;

        log.info("[discover] Starting UDP broadcast discovery on port {}, timeout {}s", actualPort, actualTimeout);

        List<CapabilityDTO> capabilities = new ArrayList<>();
        Map<String, CapabilityDTO> discoveredMap = new ConcurrentHashMap<>();

        try {
            List<InetAddress> broadcastAddresses = getBroadcastAddresses();
            
            for (InetAddress broadcastAddr : broadcastAddresses) {
                sendDiscoveryRequest(broadcastAddr, actualPort);
            }

            long startTime = System.currentTimeMillis();
            long timeoutMs = actualTimeout * 1000L;

            while (System.currentTimeMillis() - startTime < timeoutMs) {
                List<CapabilityDTO> responses = listenForResponses(actualPort, 1000);
                for (CapabilityDTO cap : responses) {
                    if (filter == null || filter.isEmpty() || 
                        (cap.getName() != null && cap.getName().contains(filter))) {
                        discoveredMap.putIfAbsent(cap.getId(), cap);
                    }
                }
            }

            capabilities.addAll(discoveredMap.values());
            log.info("[discover] Discovered {} capabilities via UDP broadcast", capabilities.size());

        } catch (Exception e) {
            log.error("[discover] UDP broadcast discovery failed: {}", e.getMessage(), e);
        }

        return capabilities;
    }

    private List<InetAddress> getBroadcastAddresses() throws SocketException {
        List<InetAddress> addresses = new ArrayList<>();
        
        try {
            addresses.add(InetAddress.getByName(broadcastAddress));
        } catch (UnknownHostException e) {
            log.warn("[getBroadcastAddresses] Invalid broadcast address: {}", broadcastAddress);
        }

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces != null && interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast != null && !addresses.contains(broadcast)) {
                    addresses.add(broadcast);
                    log.debug("[getBroadcastAddresses] Found broadcast address: {}", broadcast);
                }
            }
        }

        if (addresses.isEmpty()) {
            try {
                addresses.add(InetAddress.getByName("255.255.255.255"));
            } catch (UnknownHostException e) {
                log.error("[getBroadcastAddresses] Failed to get default broadcast address");
            }
        }

        return addresses;
    }

    private void sendDiscoveryRequest(InetAddress broadcastAddress, int port) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            socket.setSoTimeout(1000);

            byte[] sendData = DISCOVERY_MESSAGE.getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastAddress, port);
            socket.send(sendPacket);

            log.debug("[sendDiscoveryRequest] Sent discovery request to {}:{}", broadcastAddress, port);

        } catch (IOException e) {
            log.debug("[sendDiscoveryRequest] Failed to send discovery request to {}:{} - {}", 
                broadcastAddress, port, e.getMessage());
        }
    }

    private List<CapabilityDTO> listenForResponses(int port, int timeoutMs) {
        List<CapabilityDTO> capabilities = new ArrayList<>();

        try (DatagramSocket socket = new DatagramSocket(null)) {
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(port));
            socket.setSoTimeout(timeoutMs);

            byte[] buffer = new byte[BUFFER_SIZE];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

            socket.receive(receivePacket);
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);

            if (response.startsWith(RESPONSE_PREFIX)) {
                CapabilityDTO cap = parseCapability(response.substring(RESPONSE_PREFIX.length()), receivePacket.getAddress().getHostAddress());
                if (cap != null) {
                    capabilities.add(cap);
                }
            }

        } catch (SocketTimeoutException e) {
            log.debug("[listenForResponses] Socket timeout - no more responses");
        } catch (IOException e) {
            log.debug("[listenForResponses] Error listening for responses: {}", e.getMessage());
        }

        return capabilities;
    }

    private CapabilityDTO parseCapability(String jsonData, String sourceIp) {
        try {
            CapabilityDTO cap = new CapabilityDTO();
            cap.setId("udp-" + UUID.randomUUID().toString().substring(0, 8));
            cap.setSource("udp-broadcast");
            cap.setStatus("available");
            cap.setName("Remote Skill @ " + sourceIp);
            cap.setDescription("Discovered via UDP broadcast from " + sourceIp);
            return cap;
        } catch (Exception e) {
            log.debug("[parseCapability] Failed to parse capability: {}", e.getMessage());
            return null;
        }
    }

    public void shutdown() {
        executor.shutdownNow();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("[shutdown] UDP discovery service did not shut down gracefully");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
