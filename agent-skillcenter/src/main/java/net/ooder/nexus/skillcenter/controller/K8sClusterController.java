package net.ooder.nexus.skillcenter.controller;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.client.KubernetesClient;
import net.ooder.nexus.skillcenter.dto.kubernetes.*;
import net.ooder.nexus.skillcenter.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/k8s")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class K8sClusterController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(K8sClusterController.class);

    @Autowired(required = false)
    private KubernetesClient kubernetesClient;

    @PostMapping("/clusters/list")
    public ResultModel<List<ClusterInfoDTO>> listClusters() {
        long startTime = System.currentTimeMillis();
        logRequestStart("listClusters", null);

        try {
            List<ClusterInfoDTO> clusters = new ArrayList<>();
            
            ClusterInfoDTO cluster = new ClusterInfoDTO();
            cluster.setName("default");
            cluster.setEndpoint(kubernetesClient != null ? 
                kubernetesClient.getMasterUrl().toString() : "mock://kubernetes.local");
            cluster.setVersion("v1.28.0");
            cluster.setStatus(kubernetesClient != null ? "healthy" : "mock");
            cluster.setCreatedAt(System.currentTimeMillis());
            clusters.add(cluster);

            logRequestEnd("listClusters", clusters.size() + " clusters", System.currentTimeMillis() - startTime);
            return ResultModel.success(clusters);
        } catch (Exception e) {
            logRequestError("listClusters", e);
            return ResultModel.error(500, "获取集群列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/clusters/info")
    public ResultModel<ClusterInfoDTO> getClusterInfo(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        String cluster = request.getOrDefault("cluster", "default");
        logRequestStart("getClusterInfo", cluster);

        try {
            ClusterInfoDTO info = new ClusterInfoDTO();
            info.setName(cluster);
            info.setEndpoint(kubernetesClient != null ? 
                kubernetesClient.getMasterUrl().toString() : "mock://kubernetes.local");
            info.setVersion("v1.28.0");
            info.setStatus(kubernetesClient != null ? "healthy" : "mock");
            info.setCreatedAt(System.currentTimeMillis());

            if (kubernetesClient != null) {
                NodeList nodes = kubernetesClient.nodes().list();
                info.setNodeCount(nodes.getItems().size());
                
                PodList pods = kubernetesClient.pods().inAnyNamespace().list();
                info.setPodCount(pods.getItems().size());
                
                ServiceList services = kubernetesClient.services().inAnyNamespace().list();
                info.setServiceCount(services.getItems().size());
                
                NamespaceList namespaces = kubernetesClient.namespaces().list();
                List<String> nsNames = new ArrayList<>();
                for (Namespace ns : namespaces.getItems()) {
                    nsNames.add(ns.getMetadata().getName());
                }
                info.setNamespaces(nsNames);
            } else {
                info.setNodeCount(3);
                info.setPodCount(15);
                info.setServiceCount(5);
                info.setNamespaces(Arrays.asList("default", "kube-system", "production"));
            }

            logRequestEnd("getClusterInfo", info.getStatus(), System.currentTimeMillis() - startTime);
            return ResultModel.success(info);
        } catch (Exception e) {
            logRequestError("getClusterInfo", e);
            return ResultModel.error(500, "获取集群信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/clusters/nodes")
    public ResultModel<List<NodeInfoDTO>> listNodes(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        String cluster = request.getOrDefault("cluster", "default");
        logRequestStart("listNodes", cluster);

        try {
            List<NodeInfoDTO> nodes = new ArrayList<>();

            if (kubernetesClient != null) {
                for (Node node : kubernetesClient.nodes().list().getItems()) {
                    NodeInfoDTO dto = convertNode(node);
                    nodes.add(dto);
                }
            } else {
                NodeInfoDTO node1 = new NodeInfoDTO();
                node1.setName("master-node");
                node1.setStatus("Ready");
                node1.setRole("master");
                node1.setVersion("v1.28.0");
                node1.setCpuCapacity(4);
                node1.setCpuUsage(1.2);
                node1.setMemoryCapacity(16384L * 1024 * 1024);
                node1.setMemoryUsage(8192L * 1024 * 1024);
                node1.setPodCapacity(110);
                node1.setPodCount(15);
                nodes.add(node1);

                NodeInfoDTO node2 = new NodeInfoDTO();
                node2.setName("worker-node-1");
                node2.setStatus("Ready");
                node2.setRole("worker");
                node2.setVersion("v1.28.0");
                node2.setCpuCapacity(8);
                node2.setCpuUsage(4.5);
                node2.setMemoryCapacity(32768L * 1024 * 1024);
                node2.setMemoryUsage(20480L * 1024 * 1024);
                node2.setPodCapacity(110);
                node2.setPodCount(25);
                nodes.add(node2);
            }

            logRequestEnd("listNodes", nodes.size() + " nodes", System.currentTimeMillis() - startTime);
            return ResultModel.success(nodes);
        } catch (Exception e) {
            logRequestError("listNodes", e);
            return ResultModel.error(500, "获取节点列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/clusters/namespaces")
    public ResultModel<List<String>> listNamespaces(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        String cluster = request.getOrDefault("cluster", "default");
        logRequestStart("listNamespaces", cluster);

        try {
            List<String> namespaces = new ArrayList<>();

            if (kubernetesClient != null) {
                for (Namespace ns : kubernetesClient.namespaces().list().getItems()) {
                    namespaces.add(ns.getMetadata().getName());
                }
            } else {
                namespaces = Arrays.asList("default", "kube-system", "kube-public", "production", "staging");
            }

            logRequestEnd("listNamespaces", namespaces.size() + " namespaces", System.currentTimeMillis() - startTime);
            return ResultModel.success(namespaces);
        } catch (Exception e) {
            logRequestError("listNamespaces", e);
            return ResultModel.error(500, "获取命名空间列表失败: " + e.getMessage());
        }
    }

    private NodeInfoDTO convertNode(Node node) {
        NodeInfoDTO dto = new NodeInfoDTO();
        dto.setName(node.getMetadata().getName());
        
        NodeStatus status = node.getStatus();
        for (NodeCondition condition : status.getConditions()) {
            if ("Ready".equals(condition.getType())) {
                dto.setStatus("True".equals(condition.getStatus()) ? "Ready" : "NotReady");
                break;
            }
        }
        
        if (node.getMetadata().getLabels() != null) {
            String role = node.getMetadata().getLabels().get("node-role.kubernetes.io/master");
            if (role != null) {
                dto.setRole("master");
            } else {
                role = node.getMetadata().getLabels().get("node-role.kubernetes.io/worker");
                dto.setRole(role != null ? "worker" : "worker");
            }
        }
        
        dto.setVersion(status.getNodeInfo().getKubeletVersion());
        dto.setKernelVersion(status.getNodeInfo().getKernelVersion());
        dto.setOs(status.getNodeInfo().getOperatingSystem());
        dto.setArchitecture(status.getNodeInfo().getArchitecture());
        
        Map<String, Quantity> allocatable = status.getAllocatable();
        if (allocatable != null) {
            Quantity cpu = allocatable.get("cpu");
            if (cpu != null) {
                dto.setCpuCapacity(parseCpu(cpu.getAmount()));
            }
            Quantity memory = allocatable.get("memory");
            if (memory != null) {
                dto.setMemoryCapacity(parseMemory(memory.getAmount()));
            }
            Quantity pods = allocatable.get("pods");
            if (pods != null) {
                dto.setPodCapacity(Integer.parseInt(pods.getAmount()));
            }
        }
        
        dto.setCreatedAt(System.currentTimeMillis());
        
        return dto;
    }

    private double parseCpu(String cpu) {
        if (cpu == null) return 0;
        if (cpu.endsWith("m")) {
            return Double.parseDouble(cpu.substring(0, cpu.length() - 1)) / 1000.0;
        }
        return Double.parseDouble(cpu);
    }

    private long parseMemory(String memory) {
        if (memory == null) return 0;
        if (memory.endsWith("Ki")) {
            return Long.parseLong(memory.substring(0, memory.length() - 2)) * 1024;
        } else if (memory.endsWith("Mi")) {
            return Long.parseLong(memory.substring(0, memory.length() - 2)) * 1024 * 1024;
        } else if (memory.endsWith("Gi")) {
            return Long.parseLong(memory.substring(0, memory.length() - 2)) * 1024 * 1024 * 1024;
        }
        return Long.parseLong(memory);
    }
}
