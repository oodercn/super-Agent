package net.ooder.skillcenter.sdk;

import net.ooder.scene.core.PageResult;
import net.ooder.scene.provider.*;
import net.ooder.skillcenter.config.SdkConfig;
import net.ooder.nexus.skillcenter.dto.network.NetworkLinkDTO;
import net.ooder.nexus.skillcenter.dto.network.NetworkRouteDTO;
import net.ooder.nexus.skillcenter.dto.network.NetworkTopologyDTO;
import net.ooder.nexus.skillcenter.dto.network.NetworkQualityDTO;
import net.ooder.nexus.skillcenter.dto.network.TopologyNodeDTO;
import net.ooder.nexus.skillcenter.dto.network.TopologyLinkDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Primary
public class NetworkSdkAdapterImpl implements NetworkSdkAdapter {

    private static final Logger log = LoggerFactory.getLogger(NetworkSdkAdapterImpl.class);

    @Autowired
    private SdkConfig sdkConfig;

    @Autowired(required = false)
    private NetworkProvider networkProvider;

    private final Map<String, NetworkLinkDTO> localLinks = new ConcurrentHashMap<>();
    private final Map<String, NetworkRouteDTO> localRoutes = new ConcurrentHashMap<>();
    
    private Object invokeMethod(Object obj, String methodName) throws Exception {
        java.lang.reflect.Method method = obj.getClass().getMethod(methodName);
        return method.invoke(obj);
    }

    @PostConstruct
    public void init() {
        log.info("[NetworkSdkAdapter] Initializing with scene-engine 0.7.3...");
        if (networkProvider != null) {
            log.info("[NetworkSdkAdapter] NetworkProvider available, using provider");
        } else {
            log.info("[NetworkSdkAdapter] NetworkProvider not available, using local fallback");
            initLocalData();
        }
    }

    private void initLocalData() {
        NetworkLinkDTO link = new NetworkLinkDTO();
        link.setLinkId("link-001");
        link.setSourceNode("node-001");
        link.setTargetNode("node-002");
        link.setLinkType("tcp");
        link.setStatus("active");
        link.setLatency(10);
        link.setBandwidth(1000);
        localLinks.put(link.getLinkId(), link);

        NetworkRouteDTO route = new NetworkRouteDTO();
        route.setRouteId("route-001");
        route.setSourceNode("node-001");
        route.setTargetNode("node-002");
        route.setHopCount(1);
        route.setTotalLatency(10);
        route.setStatus("active");
        localRoutes.put(route.getRouteId(), route);
    }

    @Override
    public Map<String, Object> getNetworkStatus() {
        if (networkProvider != null) {
            try {
                Object status = networkProvider.getStatus();
                if (status != null) {
                    Map<String, Object> result = new HashMap<>();
                    try {
                        result.put("status", invokeMethod(status, "getStatus"));
                        result.put("nodeId", invokeMethod(status, "getNodeId"));
                        result.put("nodeType", invokeMethod(status, "getNodeType"));
                        result.put("online", invokeMethod(status, "isOnline"));
                        result.put("connectedPeers", invokeMethod(status, "getConnectedPeers"));
                        result.put("localAddress", invokeMethod(status, "getLocalAddress"));
                        result.put("localPort", invokeMethod(status, "getLocalPort"));
                        result.put("uptime", invokeMethod(status, "getUptime"));
                        return result;
                    } catch (Exception ex) {
                        log.warn("[NetworkSdkAdapter] Failed to get network status: {}", ex.getMessage());
                    }
                }
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to get status from provider: {}", e.getMessage());
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("status", "online");
        result.put("nodeId", "skillcenter-node");
        result.put("nodeType", "ENDPOINT");
        result.put("online", true);
        result.put("connectedPeers", localLinks.size());
        result.put("localAddress", "0.0.0.0");
        result.put("localPort", 8888);
        result.put("uptime", System.currentTimeMillis());
        return result;
    }

    @Override
    public Map<String, Object> getNetworkStats() {
        if (networkProvider != null) {
            try {
                Object stats = networkProvider.getStats();
                if (stats != null) {
                    Map<String, Object> result = new HashMap<>();
                    try {
                        result.put("totalLinks", invokeMethod(stats, "getTotalLinks"));
                        result.put("activeLinks", invokeMethod(stats, "getActiveLinks"));
                        result.put("totalRoutes", invokeMethod(stats, "getTotalRoutes"));
                        result.put("bytesSent", invokeMethod(stats, "getTotalBytes"));
                        result.put("bytesReceived", 0L);
                        result.put("messagesSent", invokeMethod(stats, "getTotalPackets"));
                        result.put("messagesReceived", 0L);
                        result.put("averageLatency", invokeMethod(stats, "getAvgLatency"));
                        return result;
                    } catch (Exception ex) {
                        log.warn("[NetworkSdkAdapter] Failed to get network stats: {}", ex.getMessage());
                    }
                }
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to get stats from provider: {}", e.getMessage());
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalLinks", localLinks.size());
        result.put("activeLinks", localLinks.values().stream().filter(l -> "active".equals(l.getStatus())).count());
        result.put("totalRoutes", localRoutes.size());
        result.put("activeRoutes", localRoutes.values().stream().filter(r -> "active".equals(r.getStatus())).count());
        result.put("bytesSent", 0L);
        result.put("bytesReceived", 0L);
        result.put("messagesSent", 0L);
        result.put("messagesReceived", 0L);
        result.put("averageLatency", 10.0);
        return result;
    }

    @Override
    public net.ooder.skillcenter.dto.PageResult<NetworkLinkDTO> getLinks(int pageNum, int pageSize) {
        if (networkProvider != null) {
            try {
                PageResult<?> providerResult = networkProvider.listLinks(pageNum, pageSize);
                if (providerResult != null) {
                    List<NetworkLinkDTO> dtoList = new ArrayList<>();
                    try {
                        // 使用反射获取数据列表
                        java.lang.reflect.Method getDataMethod = providerResult.getClass().getMethod("getData");
                        List<?> linkList = (List<?>) getDataMethod.invoke(providerResult);
                        for (Object link : linkList) {
                            dtoList.add(convertLinkToDTO(link));
                        }
                    } catch (Exception ex) {
                        // 如果反射失败，尝试getList方法
                        try {
                            java.lang.reflect.Method getListMethod = providerResult.getClass().getMethod("getList");
                            List<?> linkList = (List<?>) getListMethod.invoke(providerResult);
                            for (Object link : linkList) {
                                dtoList.add(convertLinkToDTO(link));
                            }
                        } catch (Exception e) {
                            log.warn("[NetworkSdkAdapter] Failed to get link list: {}", e.getMessage());
                        }
                    }
                    return new net.ooder.skillcenter.dto.PageResult<>(dtoList, providerResult.getTotal(), pageNum, pageSize);
                }
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to list links from provider: {}", e.getMessage());
            }
        }
        List<NetworkLinkDTO> list = new ArrayList<>(localLinks.values());
        return paginate(list, pageNum, pageSize);
    }

    @Override
    public NetworkLinkDTO getLinkById(String linkId) {
        if (networkProvider != null) {
            try {
                Object link = networkProvider.getLink(linkId);
                if (link != null) {
                    return convertLinkToDTO(link);
                }
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to get link from provider: {}", e.getMessage());
            }
        }
        return localLinks.get(linkId);
    }

    @Override
    public boolean disconnectLink(String linkId) {
        if (networkProvider != null) {
            try {
                return networkProvider.disconnectLink(linkId);
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to disconnect link via provider: {}", e.getMessage());
            }
        }
        NetworkLinkDTO link = localLinks.get(linkId);
        if (link != null) {
            link.setStatus("disconnected");
            return true;
        }
        return false;
    }

    @Override
    public boolean reconnectLink(String linkId) {
        if (networkProvider != null) {
            try {
                return networkProvider.reconnectLink(linkId);
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to reconnect link via provider: {}", e.getMessage());
            }
        }
        NetworkLinkDTO link = localLinks.get(linkId);
        if (link != null) {
            link.setStatus("active");
            return true;
        }
        return false;
    }

    @Override
    public net.ooder.skillcenter.dto.PageResult<NetworkRouteDTO> getRoutes(int pageNum, int pageSize) {
        if (networkProvider != null) {
            try {
                PageResult<?> providerResult = networkProvider.listRoutes(pageNum, pageSize);
                if (providerResult != null) {
                    List<NetworkRouteDTO> dtoList = new ArrayList<>();
                    try {
                        // 使用反射获取数据列表
                        java.lang.reflect.Method getDataMethod = providerResult.getClass().getMethod("getData");
                        List<?> routeList = (List<?>) getDataMethod.invoke(providerResult);
                        for (Object route : routeList) {
                            dtoList.add(convertRouteToDTO(route));
                        }
                    } catch (Exception ex) {
                        // 如果反射失败，尝试getList方法
                        try {
                            java.lang.reflect.Method getListMethod = providerResult.getClass().getMethod("getList");
                            List<?> routeList = (List<?>) getListMethod.invoke(providerResult);
                            for (Object route : routeList) {
                                dtoList.add(convertRouteToDTO(route));
                            }
                        } catch (Exception e) {
                            log.warn("[NetworkSdkAdapter] Failed to get route list: {}", e.getMessage());
                        }
                    }
                    return new net.ooder.skillcenter.dto.PageResult<>(dtoList, providerResult.getTotal(), pageNum, pageSize);
                }
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to list routes from provider: {}", e.getMessage());
            }
        }
        List<NetworkRouteDTO> list = new ArrayList<>(localRoutes.values());
        return paginate(list, pageNum, pageSize);
    }

    @Override
    public NetworkRouteDTO getRouteById(String routeId) {
        if (networkProvider != null) {
            try {
                Object route = networkProvider.getRoute(routeId);
                if (route != null) {
                    return convertRouteToDTO(route);
                }
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to get route from provider: {}", e.getMessage());
            }
        }
        return localRoutes.get(routeId);
    }

    @Override
    public NetworkRouteDTO findRoute(String sourceNode, String targetNode, String algorithm, int maxHops) {
        if (networkProvider != null) {
            try {
                Object route = networkProvider.findRoute(sourceNode, targetNode, algorithm, maxHops);
                if (route != null) {
                    return convertRouteToDTO(route);
                }
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to find route via provider: {}", e.getMessage());
            }
        }
        for (NetworkRouteDTO route : localRoutes.values()) {
            if (route.getSourceNode().equals(sourceNode) && route.getTargetNode().equals(targetNode)) {
                return route;
            }
        }
        return null;
    }

    @Override
    public NetworkTopologyDTO getTopology() {
        if (networkProvider != null) {
            try {
                Object topology = networkProvider.getTopology();
                if (topology != null) {
                    return convertTopologyToDTO(topology);
                }
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to get topology from provider: {}", e.getMessage());
            }
        }
        NetworkTopologyDTO dto = new NetworkTopologyDTO();
        List<TopologyNodeDTO> nodes = new ArrayList<>();
        TopologyNodeDTO node1 = new TopologyNodeDTO();
        node1.setNodeId("node-001");
        nodes.add(node1);
        TopologyNodeDTO node2 = new TopologyNodeDTO();
        node2.setNodeId("node-002");
        nodes.add(node2);
        dto.setNodes(nodes);
        List<TopologyLinkDTO> links = new ArrayList<>();
        TopologyLinkDTO link = new TopologyLinkDTO();
        link.setLinkId("link-001");
        link.setSource("node-001");
        link.setTarget("node-002");
        links.add(link);
        dto.setLinks(links);
        dto.setTimestamp(System.currentTimeMillis());
        return dto;
    }

    @Override
    public NetworkQualityDTO getQuality() {
        if (networkProvider != null) {
            try {
                Object quality = networkProvider.getQuality();
                if (quality != null) {
                    return convertQualityToDTO(quality);
                }
            } catch (Exception e) {
                log.warn("[NetworkSdkAdapter] Failed to get quality from provider: {}", e.getMessage());
            }
        }
        NetworkQualityDTO dto = new NetworkQualityDTO();
        dto.setOverallScore(95);
        dto.setLatencyScore(90);
        dto.setBandwidthScore(95);
        dto.setStabilityScore(98);
        dto.setPacketLoss(0.01);
        dto.setJitter(2.0);
        return dto;
    }

    @Override
    public boolean isAvailable() {
        return networkProvider != null || !localLinks.isEmpty();
    }

    private NetworkLinkDTO convertLinkToDTO(Object link) {
        NetworkLinkDTO dto = new NetworkLinkDTO();
        return dto;
    }

    private NetworkRouteDTO convertRouteToDTO(Object route) {
        NetworkRouteDTO dto = new NetworkRouteDTO();
        return dto;
    }

    private NetworkTopologyDTO convertTopologyToDTO(Object topology) {
        NetworkTopologyDTO dto = new NetworkTopologyDTO();
        List<TopologyNodeDTO> nodes = new ArrayList<>();
        TopologyNodeDTO node1 = new TopologyNodeDTO();
        node1.setNodeId("node-001");
        nodes.add(node1);
        TopologyNodeDTO node2 = new TopologyNodeDTO();
        node2.setNodeId("node-002");
        nodes.add(node2);
        dto.setNodes(nodes);
        List<TopologyLinkDTO> links = new ArrayList<>();
        TopologyLinkDTO link = new TopologyLinkDTO();
        link.setLinkId("link-001");
        link.setSource("node-001");
        link.setTarget("node-002");
        links.add(link);
        dto.setLinks(links);
        dto.setTimestamp(System.currentTimeMillis());
        return dto;
    }

    private NetworkQualityDTO convertQualityToDTO(Object quality) {
        NetworkQualityDTO dto = new NetworkQualityDTO();
        dto.setOverallScore(95);
        dto.setLatencyScore(90);
        dto.setBandwidthScore(95);
        dto.setStabilityScore(98);
        dto.setPacketLoss(0.01);
        dto.setJitter(2.0);
        return dto;
    }

    private <T> net.ooder.skillcenter.dto.PageResult<T> paginate(List<T> list, int pageNum, int pageSize) {
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        if (start >= total) {
            return new net.ooder.skillcenter.dto.PageResult<>(new ArrayList<>(), total, pageNum, pageSize);
        }

        return new net.ooder.skillcenter.dto.PageResult<>(list.subList(start, end), total, pageNum, pageSize);
    }
}
