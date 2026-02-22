package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.domain.*;
import net.ooder.skillcenter.dto.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/domain")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class DomainController extends BaseController {

    private final Map<String, DomainDTO> domainStore;
    private final Map<String, DomainPolicyDTO> policyStore;
    private final Map<String, DomainTopologyDTO> topologyStore;

    public DomainController() {
        this.domainStore = new ConcurrentHashMap<>();
        this.policyStore = new ConcurrentHashMap<>();
        this.topologyStore = new ConcurrentHashMap<>();
        initMockData();
    }

    private void initMockData() {
        DomainDTO domain = new DomainDTO();
        domain.setDomainId("domain-001");
        domain.setName("Default Domain");
        domain.setDescription("Default management domain");
        domain.setStatus("active");
        domain.setDomainType("managed");
        domain.setOwnerAgentId("agent-001");
        domain.setMemberAgentIds(Arrays.asList("agent-001", "agent-002", "agent-003"));
        domain.setCreatedAt(System.currentTimeMillis() - 86400000L);
        domain.setUpdatedAt(System.currentTimeMillis());
        domainStore.put(domain.getDomainId(), domain);
    }

    @PostMapping("/create")
    public ResultModel<DomainDTO> createDomain(@RequestBody DomainCreateRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createDomain", request);

        try {
            String domainId = "domain-" + UUID.randomUUID().toString().substring(0, 8);
            
            DomainDTO domain = new DomainDTO();
            domain.setDomainId(domainId);
            domain.setName(request.getName());
            domain.setDescription(request.getDescription());
            domain.setDomainType(request.getDomainType() != null ? request.getDomainType() : "managed");
            domain.setStatus("active");
            domain.setOwnerAgentId(request.getOwnerAgentId());
            domain.setMemberAgentIds(request.getMemberAgentIds() != null ? request.getMemberAgentIds() : new ArrayList<>());
            domain.setCreatedAt(System.currentTimeMillis());
            domain.setUpdatedAt(System.currentTimeMillis());
            
            DomainPolicyDTO policy = createDefaultPolicy(domainId);
            domain.setPolicy(policy);
            policyStore.put(domainId, policy);
            
            DomainTopologyDTO topology = createEmptyTopology(domainId);
            domain.setTopology(topology);
            topologyStore.put(domainId, topology);
            
            domainStore.put(domainId, domain);

            logRequestEnd("createDomain", domain, System.currentTimeMillis() - startTime);
            return ResultModel.success("域创建成功", domain);
        } catch (Exception e) {
            logRequestError("createDomain", e);
            return ResultModel.error(500, "创建域失败: " + e.getMessage());
        }
    }

    @PostMapping("/get")
    public ResultModel<DomainDTO> getDomain(@RequestBody DomainIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getDomain", request);

        try {
            DomainDTO domain = domainStore.get(request.getDomainId());
            if (domain == null) {
                logRequestEnd("getDomain", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("域不存在");
            }
            logRequestEnd("getDomain", domain, System.currentTimeMillis() - startTime);
            return ResultModel.success(domain);
        } catch (Exception e) {
            logRequestError("getDomain", e);
            return ResultModel.error(500, "获取域失败: " + e.getMessage());
        }
    }

    @PostMapping("/list")
    public ResultModel<PageResult<DomainDTO>> listDomains(@RequestBody PageRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listDomains", request);

        try {
            List<DomainDTO> allDomains = new ArrayList<>(domainStore.values());
            int total = allDomains.size();
            int fromIndex = (request.getPageNum() - 1) * request.getPageSize();
            int toIndex = Math.min(fromIndex + request.getPageSize(), total);
            
            List<DomainDTO> pagedDomains = fromIndex < total ? 
                allDomains.subList(fromIndex, toIndex) : new ArrayList<>();
            
            PageResult<DomainDTO> result = new PageResult<>(pagedDomains, total, request.getPageNum(), request.getPageSize());
            
            logRequestEnd("listDomains", total + " domains", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listDomains", e);
            return ResultModel.error(500, "获取域列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResultModel<DomainDTO> updateDomain(@RequestBody DomainUpdateRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateDomain", request);

        try {
            DomainDTO domain = domainStore.get(request.getDomainId());
            if (domain == null) {
                logRequestEnd("updateDomain", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("域不存在");
            }

            if (request.getName() != null) domain.setName(request.getName());
            if (request.getDescription() != null) domain.setDescription(request.getDescription());
            if (request.getStatus() != null) domain.setStatus(request.getStatus());
            domain.setUpdatedAt(System.currentTimeMillis());

            logRequestEnd("updateDomain", domain, System.currentTimeMillis() - startTime);
            return ResultModel.success("更新成功", domain);
        } catch (Exception e) {
            logRequestError("updateDomain", e);
            return ResultModel.error(500, "更新域失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResultModel<Boolean> deleteDomain(@RequestBody DomainIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteDomain", request);

        try {
            DomainDTO removed = domainStore.remove(request.getDomainId());
            if (removed == null) {
                logRequestEnd("deleteDomain", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("域不存在");
            }
            policyStore.remove(request.getDomainId());
            topologyStore.remove(request.getDomainId());

            logRequestEnd("deleteDomain", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("删除成功", true);
        } catch (Exception e) {
            logRequestError("deleteDomain", e);
            return ResultModel.error(500, "删除域失败: " + e.getMessage());
        }
    }

    @PostMapping("/member/add")
    public ResultModel<Boolean> addMember(@RequestBody MemberOperationRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("addMember", request);

        try {
            DomainDTO domain = domainStore.get(request.getDomainId());
            if (domain == null) {
                return ResultModel.notFound("域不存在");
            }

            List<String> members = domain.getMemberAgentIds();
            if (members == null) members = new ArrayList<>();
            if (!members.contains(request.getAgentId())) {
                members.add(request.getAgentId());
                domain.setMemberAgentIds(members);
                domain.setUpdatedAt(System.currentTimeMillis());
            }

            logRequestEnd("addMember", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("成员添加成功", true);
        } catch (Exception e) {
            logRequestError("addMember", e);
            return ResultModel.error(500, "添加成员失败: " + e.getMessage());
        }
    }

    @PostMapping("/member/remove")
    public ResultModel<Boolean> removeMember(@RequestBody MemberOperationRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("removeMember", request);

        try {
            DomainDTO domain = domainStore.get(request.getDomainId());
            if (domain == null) {
                return ResultModel.notFound("域不存在");
            }

            List<String> members = domain.getMemberAgentIds();
            if (members != null) {
                members.remove(request.getAgentId());
                domain.setUpdatedAt(System.currentTimeMillis());
            }

            logRequestEnd("removeMember", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("成员移除成功", true);
        } catch (Exception e) {
            logRequestError("removeMember", e);
            return ResultModel.error(500, "移除成员失败: " + e.getMessage());
        }
    }

    @PostMapping("/policy/get")
    public ResultModel<DomainPolicyDTO> getPolicy(@RequestBody DomainIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getPolicy", request);

        try {
            DomainPolicyDTO policy = policyStore.get(request.getDomainId());
            if (policy == null) {
                policy = createDefaultPolicy(request.getDomainId());
                policyStore.put(request.getDomainId(), policy);
            }
            logRequestEnd("getPolicy", policy, System.currentTimeMillis() - startTime);
            return ResultModel.success(policy);
        } catch (Exception e) {
            logRequestError("getPolicy", e);
            return ResultModel.error(500, "获取策略失败: " + e.getMessage());
        }
    }

    @PostMapping("/policy/update")
    public ResultModel<DomainPolicyDTO> updatePolicy(@RequestBody PolicyUpdateRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updatePolicy", request);

        try {
            DomainPolicyDTO policy = policyStore.get(request.getDomainId());
            if (policy == null) {
                policy = createDefaultPolicy(request.getDomainId());
            }

            if (request.getAccessPolicy() != null) policy.setAccessPolicy(request.getAccessPolicy());
            if (request.getExecutionPolicy() != null) policy.setExecutionPolicy(request.getExecutionPolicy());
            if (request.getSharingPolicy() != null) policy.setSharingPolicy(request.getSharingPolicy());
            if (request.getSecurityPolicy() != null) policy.setSecurityPolicy(request.getSecurityPolicy());
            policy.setUpdatedAt(System.currentTimeMillis());
            
            policyStore.put(request.getDomainId(), policy);

            logRequestEnd("updatePolicy", policy, System.currentTimeMillis() - startTime);
            return ResultModel.success("策略更新成功", policy);
        } catch (Exception e) {
            logRequestError("updatePolicy", e);
            return ResultModel.error(500, "更新策略失败: " + e.getMessage());
        }
    }

    @PostMapping("/topology/get")
    public ResultModel<DomainTopologyDTO> getTopology(@RequestBody DomainIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getTopology", request);

        try {
            DomainTopologyDTO topology = topologyStore.get(request.getDomainId());
            if (topology == null) {
                topology = createEmptyTopology(request.getDomainId());
                topologyStore.put(request.getDomainId(), topology);
            }
            logRequestEnd("getTopology", topology, System.currentTimeMillis() - startTime);
            return ResultModel.success(topology);
        } catch (Exception e) {
            logRequestError("getTopology", e);
            return ResultModel.error(500, "获取拓扑失败: " + e.getMessage());
        }
    }

    @PostMapping("/topology/update")
    public ResultModel<DomainTopologyDTO> updateTopology(@RequestBody TopologyUpdateRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateTopology", request);

        try {
            DomainTopologyDTO topology = topologyStore.get(request.getDomainId());
            if (topology == null) {
                topology = createEmptyTopology(request.getDomainId());
            }

            if (request.getNodes() != null) topology.setNodes(request.getNodes());
            if (request.getEdges() != null) topology.setEdges(request.getEdges());
            topology.setLastUpdated(System.currentTimeMillis());
            
            topologyStore.put(request.getDomainId(), topology);

            logRequestEnd("updateTopology", topology, System.currentTimeMillis() - startTime);
            return ResultModel.success("拓扑更新成功", topology);
        } catch (Exception e) {
            logRequestError("updateTopology", e);
            return ResultModel.error(500, "更新拓扑失败: " + e.getMessage());
        }
    }

    @PostMapping("/status")
    public ResultModel<DomainStatusDTO> getDomainStatus(@RequestBody DomainIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getDomainStatus", request);

        try {
            DomainDTO domain = domainStore.get(request.getDomainId());
            if (domain == null) {
                return ResultModel.notFound("域不存在");
            }

            DomainStatusDTO status = new DomainStatusDTO();
            status.setDomainId(domain.getDomainId());
            status.setName(domain.getName());
            status.setStatus(domain.getStatus());
            status.setMemberCount(domain.getMemberAgentIds() != null ? domain.getMemberAgentIds().size() : 0);
            status.setActiveMembers(domain.getMemberAgentIds() != null ? 
                (int) (domain.getMemberAgentIds().size() * 0.8) : 0);
            status.setActiveExecutions(3);
            status.setPendingActions(2);
            status.setHealthScore(85);
            status.setLastActivity(System.currentTimeMillis() - 300000L);

            logRequestEnd("getDomainStatus", status, System.currentTimeMillis() - startTime);
            return ResultModel.success(status);
        } catch (Exception e) {
            logRequestError("getDomainStatus", e);
            return ResultModel.error(500, "获取域状态失败: " + e.getMessage());
        }
    }

    private DomainPolicyDTO createDefaultPolicy(String domainId) {
        DomainPolicyDTO policy = new DomainPolicyDTO();
        policy.setPolicyId("policy-" + UUID.randomUUID().toString().substring(0, 8));
        policy.setDomainId(domainId);
        policy.setCreatedAt(System.currentTimeMillis());
        policy.setUpdatedAt(System.currentTimeMillis());

        DomainPolicyDTO.AccessPolicyDTO accessPolicy = new DomainPolicyDTO.AccessPolicyDTO();
        accessPolicy.setAllowGuestAccess(false);
        accessPolicy.setMaxMembers(100);
        accessPolicy.setAuthenticationMode("certificate");
        policy.setAccessPolicy(accessPolicy);

        DomainPolicyDTO.ExecutionPolicyDTO executionPolicy = new DomainPolicyDTO.ExecutionPolicyDTO();
        executionPolicy.setRequireApproval(false);
        executionPolicy.setMaxConcurrentExecutions(10);
        executionPolicy.setExecutionTimeout(300000L);
        executionPolicy.setAllowRemoteExecution(true);
        policy.setExecutionPolicy(executionPolicy);

        DomainPolicyDTO.SharingPolicyDTO sharingPolicy = new DomainPolicyDTO.SharingPolicyDTO();
        sharingPolicy.setAllowSkillSharing(true);
        sharingPolicy.setAllowDataSharing(false);
        sharingPolicy.setSharingMode("restricted");
        policy.setSharingPolicy(sharingPolicy);

        DomainPolicyDTO.SecurityPolicyDTO securityPolicy = new DomainPolicyDTO.SecurityPolicyDTO();
        securityPolicy.setEncryptionLevel("AES-256");
        securityPolicy.setRequireSignature(true);
        securityPolicy.setSessionTimeout(3600);
        securityPolicy.setAuditLevel("full");
        policy.setSecurityPolicy(securityPolicy);

        return policy;
    }

    private DomainTopologyDTO createEmptyTopology(String domainId) {
        DomainTopologyDTO topology = new DomainTopologyDTO();
        topology.setTopologyId("topo-" + UUID.randomUUID().toString().substring(0, 8));
        topology.setDomainId(domainId);
        topology.setNodes(new ArrayList<>());
        topology.setEdges(new ArrayList<>());
        topology.setLastUpdated(System.currentTimeMillis());
        return topology;
    }

    public static class DomainCreateRequest {
        private String name;
        private String description;
        private String domainType;
        private String ownerAgentId;
        private List<String> memberAgentIds;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDomainType() { return domainType; }
        public void setDomainType(String domainType) { this.domainType = domainType; }
        public String getOwnerAgentId() { return ownerAgentId; }
        public void setOwnerAgentId(String ownerAgentId) { this.ownerAgentId = ownerAgentId; }
        public List<String> getMemberAgentIds() { return memberAgentIds; }
        public void setMemberAgentIds(List<String> memberAgentIds) { this.memberAgentIds = memberAgentIds; }
    }

    public static class DomainUpdateRequest {
        private String domainId;
        private String name;
        private String description;
        private String status;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class DomainIdRequest {
        private String domainId;
        private int pageNum = 1;
        private int pageSize = 10;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public int getPageNum() { return pageNum; }
        public void setPageNum(int pageNum) { this.pageNum = pageNum; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    }

    public static class PageRequest {
        private int pageNum = 1;
        private int pageSize = 10;

        public int getPageNum() { return pageNum; }
        public void setPageNum(int pageNum) { this.pageNum = pageNum; }
        public int getPageSize() { return pageSize; }
        public void setPageSize(int pageSize) { this.pageSize = pageSize; }
    }

    public static class MemberOperationRequest {
        private String domainId;
        private String agentId;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
    }

    public static class PolicyUpdateRequest {
        private String domainId;
        private DomainPolicyDTO.AccessPolicyDTO accessPolicy;
        private DomainPolicyDTO.ExecutionPolicyDTO executionPolicy;
        private DomainPolicyDTO.SharingPolicyDTO sharingPolicy;
        private DomainPolicyDTO.SecurityPolicyDTO securityPolicy;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public DomainPolicyDTO.AccessPolicyDTO getAccessPolicy() { return accessPolicy; }
        public void setAccessPolicy(DomainPolicyDTO.AccessPolicyDTO accessPolicy) { this.accessPolicy = accessPolicy; }
        public DomainPolicyDTO.ExecutionPolicyDTO getExecutionPolicy() { return executionPolicy; }
        public void setExecutionPolicy(DomainPolicyDTO.ExecutionPolicyDTO executionPolicy) { this.executionPolicy = executionPolicy; }
        public DomainPolicyDTO.SharingPolicyDTO getSharingPolicy() { return sharingPolicy; }
        public void setSharingPolicy(DomainPolicyDTO.SharingPolicyDTO sharingPolicy) { this.sharingPolicy = sharingPolicy; }
        public DomainPolicyDTO.SecurityPolicyDTO getSecurityPolicy() { return securityPolicy; }
        public void setSecurityPolicy(DomainPolicyDTO.SecurityPolicyDTO securityPolicy) { this.securityPolicy = securityPolicy; }
    }

    public static class TopologyUpdateRequest {
        private String domainId;
        private List<DomainTopologyDTO.TopologyNodeDTO> nodes;
        private List<DomainTopologyDTO.TopologyEdgeDTO> edges;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public List<DomainTopologyDTO.TopologyNodeDTO> getNodes() { return nodes; }
        public void setNodes(List<DomainTopologyDTO.TopologyNodeDTO> nodes) { this.nodes = nodes; }
        public List<DomainTopologyDTO.TopologyEdgeDTO> getEdges() { return edges; }
        public void setEdges(List<DomainTopologyDTO.TopologyEdgeDTO> edges) { this.edges = edges; }
    }

    public static class DomainStatusDTO {
        private String domainId;
        private String name;
        private String status;
        private int memberCount;
        private int activeMembers;
        private int activeExecutions;
        private int pendingActions;
        private int healthScore;
        private long lastActivity;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public int getMemberCount() { return memberCount; }
        public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
        public int getActiveMembers() { return activeMembers; }
        public void setActiveMembers(int activeMembers) { this.activeMembers = activeMembers; }
        public int getActiveExecutions() { return activeExecutions; }
        public void setActiveExecutions(int activeExecutions) { this.activeExecutions = activeExecutions; }
        public int getPendingActions() { return pendingActions; }
        public void setPendingActions(int pendingActions) { this.pendingActions = pendingActions; }
        public int getHealthScore() { return healthScore; }
        public void setHealthScore(int healthScore) { this.healthScore = healthScore; }
        public long getLastActivity() { return lastActivity; }
        public void setLastActivity(long lastActivity) { this.lastActivity = lastActivity; }
    }
}
