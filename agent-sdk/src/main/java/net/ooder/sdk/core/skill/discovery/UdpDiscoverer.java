
package net.ooder.sdk.core.skill.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.skill.SkillDiscoverer;
import net.ooder.sdk.api.skill.SkillManifest;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.common.enums.DiscoveryMethod;

public class UdpDiscoverer implements SkillDiscoverer {
    
    private static final Logger log = LoggerFactory.getLogger(UdpDiscoverer.class);
    
    private static final String DISCOVERY_MULTICAST_GROUP = "239.255.255.250";
    private static final int DISCOVERY_PORT = 1900;
    private static final String DISCOVERY_MESSAGE = "OODER_SKILL_DISCOVER";
    private static final String RESPONSE_PREFIX = "OODER_SKILL_RESPONSE:";
    private static final int BUFFER_SIZE = 4096;
    
    private long timeout = 5000;
    private DiscoveryFilter filter;
    private String multicastGroup = DISCOVERY_MULTICAST_GROUP;
    private int port = DISCOVERY_PORT;
    private int listenPort = DISCOVERY_PORT + 1;
    private final AtomicBoolean listening = new AtomicBoolean(false);
    private final Map<String, SkillPackage> discoveredSkills = new ConcurrentHashMap<>();
    private ExecutorService executor;
    private MulticastSocket multicastSocket;
    private DatagramSocket responseSocket;
    
    public UdpDiscoverer() {
    }
    
    public UdpDiscoverer(String multicastGroup, int port) {
        this.multicastGroup = multicastGroup;
        this.port = port;
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discover() {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> result = new ArrayList<>();
            
            try {
                ensureSocketOpen();
                
                String listenAddr = InetAddress.getLocalHost().getHostAddress();
                int localPort = responseSocket.getLocalPort();
                
                String discoveryMsg = DISCOVERY_MESSAGE + ":" + listenAddr + ":" + localPort;
                byte[] sendData = discoveryMsg.getBytes(StandardCharsets.UTF_8);
                
                InetAddress group = InetAddress.getByName(multicastGroup);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, group, port);
                multicastSocket.send(sendPacket);
                
                log.debug("Sent discovery broadcast to {}:{}", multicastGroup, port);
                
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < timeout) {
                    try {
                        byte[] receiveData = new byte[BUFFER_SIZE];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        responseSocket.setSoTimeout((int) Math.min(1000, timeout - (System.currentTimeMillis() - startTime)));
                        responseSocket.receive(receivePacket);
                        
                        String response = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
                        SkillPackage pkg = parseResponse(response, receivePacket.getAddress().getHostAddress());
                        
                        if (pkg != null && passesFilter(pkg)) {
                            if (!discoveredSkills.containsKey(pkg.getSkillId())) {
                                discoveredSkills.put(pkg.getSkillId(), pkg);
                                result.add(pkg);
                            }
                        }
                    } catch (SocketTimeoutException e) {
                        // Continue waiting
                    }
                }
                
                log.info("Discovered {} skill packages via UDP broadcast", result.size());
            } catch (Exception e) {
                log.error("Failed to discover skills via UDP: {}", e.getMessage());
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<SkillPackage> discover(String skillId) {
        return CompletableFuture.supplyAsync(() -> {
            SkillPackage cached = discoveredSkills.get(skillId);
            if (cached != null) {
                return cached;
            }
            
            List<SkillPackage> all = discover().join();
            for (SkillPackage pkg : all) {
                if (skillId.equals(pkg.getSkillId())) {
                    return pkg;
                }
            }
            
            return null;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discoverByScene(String sceneId) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> result = new ArrayList<>();
            List<SkillPackage> all = discover().join();
            
            for (SkillPackage pkg : all) {
                if (sceneId.equals(pkg.getSceneId()) && passesFilter(pkg)) {
                    result.add(pkg);
                }
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> search(String query) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> result = new ArrayList<>();
            if (query == null || query.isEmpty()) {
                return result;
            }
            
            String lowerQuery = query.toLowerCase();
            List<SkillPackage> all = discover().join();
            
            for (SkillPackage pkg : all) {
                if (matchesQuery(pkg, lowerQuery) && passesFilter(pkg)) {
                    result.add(pkg);
                }
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> searchByCapability(String capabilityId) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> result = new ArrayList<>();
            if (capabilityId == null || capabilityId.isEmpty()) {
                return result;
            }
            
            List<SkillPackage> all = discover().join();
            
            for (SkillPackage pkg : all) {
                if (hasCapability(pkg, capabilityId) && passesFilter(pkg)) {
                    result.add(pkg);
                }
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discoverByCategory(String category) {
        return discoverByCategory(category, null);
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> discoverByCategory(String category, String subCategory) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> result = new ArrayList<>();
            if (category == null || category.isEmpty()) {
                return result;
            }
            
            List<SkillPackage> all = discover().join();
            
            for (SkillPackage pkg : all) {
                if (category.equals(pkg.getCategory()) && passesFilter(pkg)) {
                    if (subCategory == null || subCategory.isEmpty() || subCategory.equals(pkg.getSubCategory())) {
                        result.add(pkg);
                    }
                }
            }
            
            return result;
        });
    }
    
    @Override
    public CompletableFuture<List<SkillPackage>> searchByTags(List<String> tags) {
        return CompletableFuture.supplyAsync(() -> {
            List<SkillPackage> result = new ArrayList<>();
            if (tags == null || tags.isEmpty()) {
                return result;
            }
            
            List<SkillPackage> all = discover().join();
            
            for (SkillPackage pkg : all) {
                if (hasAllTags(pkg, tags) && passesFilter(pkg)) {
                    result.add(pkg);
                }
            }
            
            return result;
        });
    }
    
    @Override
    public DiscoveryMethod getMethod() {
        return DiscoveryMethod.UDP_BROADCAST;
    }
    
    @Override
    public boolean isAvailable() {
        return true;
    }
    
    @Override
    public void setTimeout(long timeoutMs) {
        this.timeout = timeoutMs;
    }
    
    @Override
    public long getTimeout() {
        return timeout;
    }
    
    @Override
    public void setFilter(DiscoveryFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public DiscoveryFilter getFilter() {
        return filter;
    }
    
    public void setMulticastGroup(String multicastGroup) {
        this.multicastGroup = multicastGroup;
    }
    
    public String getMulticastGroup() {
        return multicastGroup;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public int getPort() {
        return port;
    }
    
    public void startListening() {
        if (listening.compareAndSet(false, true)) {
            executor = Executors.newSingleThreadExecutor();
            executor.submit(this::listenForDiscoveries);
        }
    }
    
    public void stopListening() {
        if (listening.compareAndSet(true, false)) {
            if (executor != null) {
                executor.shutdown();
                executor = null;
            }
            closeSockets();
        }
    }
    
    private void ensureSocketOpen() throws IOException {
        if (multicastSocket == null || multicastSocket.isClosed()) {
            multicastSocket = new MulticastSocket(port);
            multicastSocket.setTimeToLive(1);
        }
        
        if (responseSocket == null || responseSocket.isClosed()) {
            responseSocket = new DatagramSocket(0);
        }
    }
    
    private void closeSockets() {
        if (multicastSocket != null && !multicastSocket.isClosed()) {
            multicastSocket.close();
        }
        if (responseSocket != null && !responseSocket.isClosed()) {
            responseSocket.close();
        }
    }
    
    private void listenForDiscoveries() {
        try {
            InetAddress group = InetAddress.getByName(multicastGroup);
            MulticastSocket socket = new MulticastSocket(port);
            socket.joinGroup(group);
            
            byte[] buffer = new byte[BUFFER_SIZE];
            
            while (listening.get()) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    
                    String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                    
                    if (message.startsWith(DISCOVERY_MESSAGE + ":")) {
                        String[] parts = message.substring(DISCOVERY_MESSAGE.length() + 1).split(":");
                        if (parts.length >= 2) {
                            String responderAddr = parts[0];
                            int responderPort = Integer.parseInt(parts[1]);
                            respondToDiscovery(responderAddr, responderPort);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    // Continue
                } catch (Exception e) {
                    if (listening.get()) {
                        log.debug("Error processing discovery packet: {}", e.getMessage());
                    }
                }
            }
            
            socket.leaveGroup(group);
            socket.close();
        } catch (Exception e) {
            log.error("Error in discovery listener: {}", e.getMessage());
        }
    }
    
    private void respondToDiscovery(String addr, int port) {
        try {
            DatagramSocket socket = new DatagramSocket();
            
            StringBuilder response = new StringBuilder(RESPONSE_PREFIX);
            response.append("{");
            response.append("\"skillId\":\"").append(getLocalSkillId()).append("\",");
            response.append("\"name\":\"").append(getLocalSkillName()).append("\",");
            response.append("\"version\":\"").append(getLocalSkillVersion()).append("\",");
            response.append("\"sceneId\":\"").append(getLocalSceneId()).append("\",");
            response.append("\"capabilities\":[]");
            response.append("}");
            
            byte[] data = response.toString().getBytes(StandardCharsets.UTF_8);
            InetAddress address = InetAddress.getByName(addr);
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            socket.close();
            
            log.debug("Responded to discovery request from {}:{}", addr, port);
        } catch (Exception e) {
            log.warn("Failed to respond to discovery: {}", e.getMessage());
        }
    }
    
    private SkillPackage parseResponse(String response, String sourceAddr) {
        if (response == null || !response.startsWith(RESPONSE_PREFIX)) {
            return null;
        }
        
        String json = response.substring(RESPONSE_PREFIX.length());
        return parseSkillPackage(json, sourceAddr);
    }
    
    private SkillPackage parseSkillPackage(String json, String sourceAddr) {
        if (json == null || json.isEmpty() || !json.trim().startsWith("{")) {
            return null;
        }
        
        Map<String, Object> data = parseJson(json);
        if (data.isEmpty()) {
            return null;
        }
        
        SkillPackage pkg = new SkillPackage();
        SkillManifest manifest = new SkillManifest();
        
        pkg.setSkillId(getString(data, "skillId"));
        pkg.setName(getString(data, "name"));
        pkg.setDescription(getString(data, "description"));
        pkg.setVersion(getString(data, "version"));
        pkg.setSceneId(getString(data, "sceneId"));
        pkg.setSource("udp:" + sourceAddr);
        
        manifest.setSkillId(pkg.getSkillId());
        manifest.setName(pkg.getName());
        manifest.setDescription(pkg.getDescription());
        manifest.setVersion(pkg.getVersion());
        manifest.setSceneId(pkg.getSceneId());
        
        pkg.setManifest(manifest);
        
        return pkg;
    }
    
    private Map<String, Object> parseJson(String json) {
        Map<String, Object> result = new HashMap<>();
        if (json == null || json.isEmpty()) {
            return result;
        }
        
        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            return result;
        }
        
        json = json.substring(1, json.length() - 1).trim();
        
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean inKey = true;
        boolean inString = false;
        boolean inValue = false;
        int depth = 0;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
                if (inValue) value.append(c);
                continue;
            }
            
            if (!inString) {
                if (c == '{' || c == '[') depth++;
                if (c == '}' || c == ']') depth--;
                
                if (c == ':' && inKey && depth == 0) {
                    inKey = false;
                    inValue = true;
                    continue;
                }
                if (c == ',' && !inKey && depth == 0) {
                    String k = key.toString().trim();
                    String v = value.toString().trim();
                    if (!k.isEmpty()) {
                        result.put(k, parseValue(v));
                    }
                    key = new StringBuilder();
                    value = new StringBuilder();
                    inKey = true;
                    inValue = false;
                    continue;
                }
            }
            
            if (inKey) {
                key.append(c);
            } else if (inValue) {
                value.append(c);
            }
        }
        
        String k = key.toString().trim();
        String v = value.toString().trim();
        if (!k.isEmpty()) {
            result.put(k, parseValue(v));
        }
        
        return result;
    }
    
    private Object parseValue(String v) {
        if (v == null || v.isEmpty()) return null;
        v = v.trim();
        if ("null".equals(v)) return null;
        if ("true".equals(v)) return true;
        if ("false".equals(v)) return false;
        if (v.startsWith("\"") && v.endsWith("\"")) {
            return v.substring(1, v.length() - 1);
        }
        try {
            if (v.contains(".")) {
                return Double.parseDouble(v);
            }
            return Long.parseLong(v);
        } catch (NumberFormatException e) {
            return v;
        }
    }
    
    private String getString(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }
    
    private boolean matchesQuery(SkillPackage pkg, String query) {
        if (pkg.getName() != null && pkg.getName().toLowerCase().contains(query)) {
            return true;
        }
        if (pkg.getDescription() != null && pkg.getDescription().toLowerCase().contains(query)) {
            return true;
        }
        if (pkg.getSkillId() != null && pkg.getSkillId().toLowerCase().contains(query)) {
            return true;
        }
        return false;
    }
    
    private boolean hasCapability(SkillPackage pkg, String capabilityId) {
        if (pkg.getCapabilities() == null) {
            return false;
        }
        for (net.ooder.sdk.api.skill.Capability cap : pkg.getCapabilities()) {
            if (capabilityId.equals(cap.getCapId())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasAllTags(SkillPackage pkg, List<String> requiredTags) {
        if (pkg.getTags() == null) {
            return false;
        }
        for (String tag : requiredTags) {
            if (!pkg.getTags().contains(tag)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean passesFilter(SkillPackage pkg) {
        if (filter == null) {
            return true;
        }
        if (filter.getSceneId() != null && !filter.getSceneId().equals(pkg.getSceneId())) {
            return false;
        }
        if (filter.getVersion() != null && !filter.getVersion().equals(pkg.getVersion())) {
            return false;
        }
        return true;
    }
    
    protected String getLocalSkillId() {
        return System.getProperty("ooder.skill.id", "unknown");
    }
    
    protected String getLocalSkillName() {
        return System.getProperty("ooder.skill.name", "Unknown Skill");
    }
    
    protected String getLocalSkillVersion() {
        return System.getProperty("ooder.skill.version", "1.0.0");
    }
    
    protected String getLocalSceneId() {
        return System.getProperty("ooder.scene.id", "");
    }
}
