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
public class K8sWorkloadController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(K8sWorkloadController.class);

    @Autowired(required = false)
    private KubernetesClient kubernetesClient;

    @PostMapping("/deployments/list")
    public ResultModel<List<DeploymentDTO>> listDeployments(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        String namespace = request.getOrDefault("namespace", "default");
        logRequestStart("listDeployments", namespace);

        try {
            List<DeploymentDTO> deployments = new ArrayList<>();

            if (kubernetesClient != null) {
                List<Deployment> items;
                if ("all".equals(namespace) || namespace.isEmpty()) {
                    items = kubernetesClient.apps().deployments().inAnyNamespace().list().getItems();
                } else {
                    items = kubernetesClient.apps().deployments().inNamespace(namespace).list().getItems();
                }
                
                for (Deployment deploy : items) {
                    DeploymentDTO dto = convertDeployment(deploy);
                    deployments.add(dto);
                }
            } else {
                DeploymentDTO d1 = new DeploymentDTO();
                d1.setName("skill-web");
                d1.setNamespace("default");
                d1.setImage("ooder/skill-web:v1.2.0");
                d1.setReplicas(3);
                d1.setReadyReplicas(3);
                d1.setAvailableReplicas(3);
                d1.setStatus("running");
                d1.setStrategy("RollingUpdate");
                d1.setCreatedAt(System.currentTimeMillis() - 3600000);
                deployments.add(d1);

                DeploymentDTO d2 = new DeploymentDTO();
                d2.setName("skill-api");
                d2.setNamespace("default");
                d2.setImage("ooder/skill-api:v1.0.0");
                d2.setReplicas(2);
                d2.setReadyReplicas(2);
                d2.setAvailableReplicas(2);
                d2.setStatus("running");
                d2.setStrategy("RollingUpdate");
                d2.setCreatedAt(System.currentTimeMillis() - 7200000);
                deployments.add(d2);

                DeploymentDTO d3 = new DeploymentDTO();
                d3.setName("redis");
                d3.setNamespace("default");
                d3.setImage("redis:7.0");
                d3.setReplicas(1);
                d3.setReadyReplicas(1);
                d3.setAvailableReplicas(1);
                d3.setStatus("running");
                d3.setStrategy("RollingUpdate");
                d3.setCreatedAt(System.currentTimeMillis() - 86400000);
                deployments.add(d3);
            }

            logRequestEnd("listDeployments", deployments.size() + " deployments", System.currentTimeMillis() - startTime);
            return ResultModel.success(deployments);
        } catch (Exception e) {
            logRequestError("listDeployments", e);
            return ResultModel.error(500, "获取Deployment列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/deployments/get")
    public ResultModel<DeploymentDTO> getDeployment(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        String namespace = request.getOrDefault("namespace", "default");
        String name = request.get("name");
        logRequestStart("getDeployment", name);

        try {
            if (kubernetesClient != null) {
                Deployment deploy = kubernetesClient.apps().deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
                
                if (deploy == null) {
                    return ResultModel.notFound("Deployment不存在");
                }
                
                DeploymentDTO dto = convertDeployment(deploy);
                logRequestEnd("getDeployment", name, System.currentTimeMillis() - startTime);
                return ResultModel.success(dto);
            } else {
                DeploymentDTO dto = new DeploymentDTO();
                dto.setName(name);
                dto.setNamespace(namespace);
                dto.setImage("ooder/" + name + ":v1.0.0");
                dto.setReplicas(2);
                dto.setReadyReplicas(2);
                dto.setAvailableReplicas(2);
                dto.setStatus("running");
                dto.setCreatedAt(System.currentTimeMillis() - 3600000);
                return ResultModel.success(dto);
            }
        } catch (Exception e) {
            logRequestError("getDeployment", e);
            return ResultModel.error(500, "获取Deployment失败: " + e.getMessage());
        }
    }

    @PostMapping("/deployments/create")
    public ResultModel<DeploymentDTO> createDeployment(@RequestBody CreateDeploymentRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createDeployment", request.getName());

        try {
            String namespace = request.getNamespace() != null ? request.getNamespace() : "default";
            String name = request.getName();
            String image = request.getImage();
            int replicas = request.getReplicas() > 0 ? request.getReplicas() : 1;

            if (kubernetesClient != null) {
                Map<String, String> labels = new HashMap<>();
                labels.put("app", name);
                labels.put("managed-by", "skillcenter");

                Map<String, Quantity> resources = new HashMap<>();
                if (request.getCpuLimit() > 0) {
                    resources.put("cpu", new Quantity(request.getCpuLimit() + ""));
                }
                if (request.getMemoryLimit() > 0) {
                    resources.put("memory", new Quantity(request.getMemoryLimit() + ""));
                }

                ContainerBuilder containerBuilder = new ContainerBuilder()
                    .withName(name)
                    .withImage(image)
                    .withNewResources()
                        .withLimits(resources)
                        .withRequests(resources)
                    .endResources();

                if (request.getContainerPort() > 0) {
                    containerBuilder.withPorts(new ContainerPortBuilder()
                        .withContainerPort(request.getContainerPort())
                        .build());
                }

                Deployment deployment = new DeploymentBuilder()
                    .withNewMetadata()
                        .withName(name)
                        .withNamespace(namespace)
                        .withLabels(labels)
                    .endMetadata()
                    .withNewSpec()
                        .withReplicas(replicas)
                        .withNewSelector()
                            .withMatchLabels(labels)
                        .endSelector()
                        .withNewTemplate()
                            .withNewMetadata()
                                .withLabels(labels)
                            .endMetadata()
                            .withNewSpec()
                                .withContainers(containerBuilder.build())
                            .endSpec()
                        .endTemplate()
                    .endSpec()
                    .build();

                Deployment created = kubernetesClient.apps().deployments()
                    .inNamespace(namespace)
                    .create(deployment);

                DeploymentDTO dto = convertDeployment(created);
                logRequestEnd("createDeployment", name, System.currentTimeMillis() - startTime);
                return ResultModel.success("创建成功", dto);
            } else {
                DeploymentDTO dto = new DeploymentDTO();
                dto.setName(name);
                dto.setNamespace(namespace);
                dto.setImage(image);
                dto.setReplicas(replicas);
                dto.setReadyReplicas(0);
                dto.setAvailableReplicas(0);
                dto.setStatus("creating");
                dto.setCreatedAt(System.currentTimeMillis());
                return ResultModel.success("创建成功（Mock）", dto);
            }
        } catch (Exception e) {
            logRequestError("createDeployment", e);
            return ResultModel.error(500, "创建Deployment失败: " + e.getMessage());
        }
    }

    @PostMapping("/deployments/scale")
    public ResultModel<Boolean> scaleDeployment(@RequestBody ScaleDeploymentRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("scaleDeployment", request.getName() + " -> " + request.getReplicas());

        try {
            String namespace = request.getNamespace() != null ? request.getNamespace() : "default";
            String name = request.getName();
            int replicas = request.getReplicas();

            if (kubernetesClient != null) {
                kubernetesClient.apps().deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .scale(replicas);
            }

            logRequestEnd("scaleDeployment", "scaled to " + replicas, System.currentTimeMillis() - startTime);
            return ResultModel.success("扩缩容成功", true);
        } catch (Exception e) {
            logRequestError("scaleDeployment", e);
            return ResultModel.error(500, "扩缩容失败: " + e.getMessage());
        }
    }

    @PostMapping("/deployments/delete")
    public ResultModel<Boolean> deleteDeployment(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        String namespace = request.getOrDefault("namespace", "default");
        String name = request.get("name");
        logRequestStart("deleteDeployment", name);

        try {
            if (kubernetesClient != null) {
                Boolean deleted = kubernetesClient.apps().deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .delete();
                
                logRequestEnd("deleteDeployment", name, System.currentTimeMillis() - startTime);
                return ResultModel.success("删除成功", deleted != null && deleted);
            } else {
                return ResultModel.success("删除成功（Mock）", true);
            }
        } catch (Exception e) {
            logRequestError("deleteDeployment", e);
            return ResultModel.error(500, "删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/deployments/restart")
    public ResultModel<Boolean> restartDeployment(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        String namespace = request.getOrDefault("namespace", "default");
        String name = request.get("name");
        logRequestStart("restartDeployment", name);

        try {
            if (kubernetesClient != null) {
                String timestamp = String.valueOf(System.currentTimeMillis());
                kubernetesClient.apps().deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .edit(d -> new DeploymentBuilder(d)
                        .editMetadata()
                            .addToAnnotations("kubectl.kubernetes.io/restartedAt", timestamp)
                        .endMetadata()
                        .build());
            }

            logRequestEnd("restartDeployment", name, System.currentTimeMillis() - startTime);
            return ResultModel.success("重启成功", true);
        } catch (Exception e) {
            logRequestError("restartDeployment", e);
            return ResultModel.error(500, "重启失败: " + e.getMessage());
        }
    }

    @PostMapping("/pods/list")
    public ResultModel<List<PodDTO>> listPods(@RequestBody Map<String, String> request) {
        long startTime = System.currentTimeMillis();
        String namespace = request.getOrDefault("namespace", "default");
        String deployment = request.get("deployment");
        logRequestStart("listPods", namespace + "/" + deployment);

        try {
            List<PodDTO> pods = new ArrayList<>();

            if (kubernetesClient != null) {
                PodList podList;
                if ("all".equals(namespace) || namespace.isEmpty()) {
                    podList = kubernetesClient.pods().inAnyNamespace().list();
                } else if (deployment != null && !deployment.isEmpty()) {
                    podList = kubernetesClient.pods()
                        .inNamespace(namespace)
                        .withLabel("app", deployment)
                        .list();
                } else {
                    podList = kubernetesClient.pods().inNamespace(namespace).list();
                }

                for (Pod pod : podList.getItems()) {
                    PodDTO dto = convertPod(pod);
                    pods.add(dto);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    PodDTO p = new PodDTO();
                    p.setName("skill-web-" + i + "-abc123");
                    p.setNamespace(namespace);
                    p.setDeployment("skill-web");
                    p.setNodeName("worker-node-" + (i % 2 + 1));
                    p.setStatus("Running");
                    p.setPodIP("10.244.1." + (100 + i));
                    p.setCreatedAt(System.currentTimeMillis() - 3600000 * (i + 1));
                    
                    List<PodDTO.ContainerInfo> containers = new ArrayList<>();
                    PodDTO.ContainerInfo c = new PodDTO.ContainerInfo();
                    c.setName("skill-web");
                    c.setImage("ooder/skill-web:v1.2.0");
                    c.setStatus("Running");
                    c.setRestartCount(0);
                    containers.add(c);
                    p.setContainers(containers);
                    
                    pods.add(p);
                }
            }

            logRequestEnd("listPods", pods.size() + " pods", System.currentTimeMillis() - startTime);
            return ResultModel.success(pods);
        } catch (Exception e) {
            logRequestError("listPods", e);
            return ResultModel.error(500, "获取Pod列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/pods/logs")
    public ResultModel<List<String>> getPodLogs(@RequestBody Map<String, Object> request) {
        long startTime = System.currentTimeMillis();
        String namespace = (String) request.getOrDefault("namespace", "default");
        String podName = (String) request.get("podName");
        Integer tailLines = (Integer) request.getOrDefault("tailLines", 100);
        logRequestStart("getPodLogs", podName);

        try {
            List<String> logs = new ArrayList<>();

            if (kubernetesClient != null) {
                String podLogs = kubernetesClient.pods()
                    .inNamespace(namespace)
                    .withName(podName)
                    .tailingLines(tailLines)
                    .getLog();

                if (podLogs != null) {
                    logs = Arrays.asList(podLogs.split("\n"));
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    logs.add("[" + new Date() + "] INFO: Log entry " + i + " from pod " + podName);
                }
            }

            logRequestEnd("getPodLogs", logs.size() + " lines", System.currentTimeMillis() - startTime);
            return ResultModel.success(logs);
        } catch (Exception e) {
            logRequestError("getPodLogs", e);
            return ResultModel.error(500, "获取日志失败: " + e.getMessage());
        }
    }

    private DeploymentDTO convertDeployment(Deployment deploy) {
        DeploymentDTO dto = new DeploymentDTO();
        dto.setName(deploy.getMetadata().getName());
        dto.setNamespace(deploy.getMetadata().getNamespace());
        
        DeploymentStatus status = deploy.getStatus();
        if (status != null) {
            dto.setReplicas(status.getReplicas() != null ? status.getReplicas() : 0);
            dto.setReadyReplicas(status.getReadyReplicas() != null ? status.getReadyReplicas() : 0);
            dto.setAvailableReplicas(status.getAvailableReplicas() != null ? status.getAvailableReplicas() : 0);
            
            int ready = dto.getReadyReplicas();
            int desired = dto.getReplicas();
            if (ready == desired && ready > 0) {
                dto.setStatus("running");
            } else if (ready == 0 && desired == 0) {
                dto.setStatus("stopped");
            } else if (ready < desired) {
                dto.setStatus("updating");
            } else {
                dto.setStatus("unknown");
            }
        } else {
            dto.setStatus("creating");
        }
        
        if (deploy.getSpec() != null && deploy.getSpec().getStrategy() != null) {
            dto.setStrategy(deploy.getSpec().getStrategy().getType());
        }
        
        if (deploy.getSpec() != null && deploy.getSpec().getTemplate() != null &&
            deploy.getSpec().getTemplate().getSpec() != null &&
            deploy.getSpec().getTemplate().getSpec().getContainers() != null &&
            !deploy.getSpec().getTemplate().getSpec().getContainers().isEmpty()) {
            dto.setImage(deploy.getSpec().getTemplate().getSpec().getContainers().get(0).getImage());
        }
        
        dto.setCreatedAt(System.currentTimeMillis());
        
        return dto;
    }

    private PodDTO convertPod(Pod pod) {
        PodDTO dto = new PodDTO();
        dto.setName(pod.getMetadata().getName());
        dto.setNamespace(pod.getMetadata().getNamespace());
        
        if (pod.getMetadata().getLabels() != null) {
            dto.setDeployment(pod.getMetadata().getLabels().get("app"));
        }
        
        PodStatus status = pod.getStatus();
        if (status != null) {
            dto.setStatus(status.getPhase());
            dto.setPodIP(status.getPodIP());
            dto.setNodeName(status.getHostIP());
            
            List<PodDTO.ContainerInfo> containers = new ArrayList<>();
            if (status.getContainerStatuses() != null) {
                for (ContainerStatus cs : status.getContainerStatuses()) {
                    PodDTO.ContainerInfo ci = new PodDTO.ContainerInfo();
                    ci.setName(cs.getName());
                    ci.setImage(cs.getImage());
                    ci.setStatus(cs.getState() != null && cs.getState().getRunning() != null ? "Running" : "NotRunning");
                    ci.setRestartCount(cs.getRestartCount() != null ? cs.getRestartCount() : 0);
                    containers.add(ci);
                }
            }
            dto.setContainers(containers);
        }
        
        dto.setCreatedAt(System.currentTimeMillis());
        
        return dto;
    }
}
