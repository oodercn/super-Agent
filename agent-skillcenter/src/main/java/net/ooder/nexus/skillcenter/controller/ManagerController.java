package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.domain.*;
import net.ooder.skillcenter.dto.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class ManagerController extends BaseController {

    private final Map<String, ManagerActionDTO> actionStore;
    private final Map<String, DomainDTO> domainStore;

    public ManagerController() {
        this.actionStore = new ConcurrentHashMap<>();
        this.domainStore = new ConcurrentHashMap<>();
        initMockData();
    }

    private void initMockData() {
        DomainDTO domain = new DomainDTO();
        domain.setDomainId("domain-001");
        domain.setName("Managed Domain");
        domain.setDescription("Domain managed by manager service");
        domain.setStatus("active");
        domain.setDomainType("managed");
        domain.setOwnerAgentId("manager-001");
        domain.setMemberAgentIds(Arrays.asList("agent-001", "agent-002"));
        domain.setCreatedAt(System.currentTimeMillis() - 86400000L);
        domainStore.put(domain.getDomainId(), domain);
    }

    @PostMapping("/action/execute")
    public ResultModel<ManagerActionDTO> executeAction(@RequestBody ActionExecuteRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("executeAction", request);

        try {
            String actionId = "action-" + UUID.randomUUID().toString().substring(0, 8);
            
            ManagerActionDTO action = new ManagerActionDTO();
            action.setActionId(actionId);
            action.setDomainId(request.getDomainId());
            action.setManagerId(request.getManagerId());
            action.setActionType(request.getActionType());
            action.setTargetId(request.getTargetId());
            action.setTargetType(request.getTargetType());
            action.setParameters(request.getParameters());
            action.setStatus(ManagerActionDTO.ActionStatus.IN_PROGRESS);
            action.setCreatedAt(System.currentTimeMillis());

            ManagerActionDTO.ActionResultDTO result = executeActionInternal(action);
            action.setResult(result);
            action.setStatus(result.isSuccess() ? 
                ManagerActionDTO.ActionStatus.COMPLETED : ManagerActionDTO.ActionStatus.FAILED);
            action.setCompletedAt(System.currentTimeMillis());
            
            actionStore.put(actionId, action);

            logRequestEnd("executeAction", action, System.currentTimeMillis() - startTime);
            return ResultModel.success("操作执行完成", action);
        } catch (Exception e) {
            logRequestError("executeAction", e);
            return ResultModel.error(500, "执行操作失败: " + e.getMessage());
        }
    }

    @PostMapping("/action/get")
    public ResultModel<ManagerActionDTO> getAction(@RequestBody ActionIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getAction", request);

        try {
            ManagerActionDTO action = actionStore.get(request.getActionId());
            if (action == null) {
                return ResultModel.notFound("操作不存在");
            }
            logRequestEnd("getAction", action, System.currentTimeMillis() - startTime);
            return ResultModel.success(action);
        } catch (Exception e) {
            logRequestError("getAction", e);
            return ResultModel.error(500, "获取操作失败: " + e.getMessage());
        }
    }

    @PostMapping("/action/list")
    public ResultModel<PageResult<ManagerActionDTO>> listActions(@RequestBody DomainPageRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listActions", request);

        try {
            List<ManagerActionDTO> filtered = new ArrayList<>();
            for (ManagerActionDTO action : actionStore.values()) {
                if (request.getDomainId() == null || request.getDomainId().equals(action.getDomainId())) {
                    filtered.add(action);
                }
            }
            
            int total = filtered.size();
            int fromIndex = (request.getPageNum() - 1) * request.getPageSize();
            int toIndex = Math.min(fromIndex + request.getPageSize(), total);
            List<ManagerActionDTO> paged = fromIndex < total ? 
                filtered.subList(fromIndex, toIndex) : new ArrayList<>();
            
            PageResult<ManagerActionDTO> result = new PageResult<>(paged, total, request.getPageNum(), request.getPageSize());

            logRequestEnd("listActions", total + " actions", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listActions", e);
            return ResultModel.error(500, "获取操作列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/action/cancel")
    public ResultModel<Boolean> cancelAction(@RequestBody ActionIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("cancelAction", request);

        try {
            ManagerActionDTO action = actionStore.get(request.getActionId());
            if (action == null) {
                return ResultModel.notFound("操作不存在");
            }
            
            if (action.getStatus() == ManagerActionDTO.ActionStatus.COMPLETED ||
                action.getStatus() == ManagerActionDTO.ActionStatus.FAILED) {
                return ResultModel.error(400, "操作已完成，无法取消");
            }
            
            action.setStatus(ManagerActionDTO.ActionStatus.CANCELLED);
            action.setCompletedAt(System.currentTimeMillis());

            logRequestEnd("cancelAction", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("操作已取消", true);
        } catch (Exception e) {
            logRequestError("cancelAction", e);
            return ResultModel.error(500, "取消操作失败: " + e.getMessage());
        }
    }

    @PostMapping("/domain/create")
    public ResultModel<DomainDTO> createDomain(@RequestBody DomainCreateRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createDomain", request);

        try {
            String domainId = "domain-" + UUID.randomUUID().toString().substring(0, 8);
            
            DomainDTO domain = new DomainDTO();
            domain.setDomainId(domainId);
            domain.setName(request.getName());
            domain.setDescription(request.getDescription());
            domain.setDomainType("managed");
            domain.setStatus("active");
            domain.setOwnerAgentId(request.getManagerId());
            domain.setMemberAgentIds(new ArrayList<>());
            domain.setCreatedAt(System.currentTimeMillis());
            domain.setUpdatedAt(System.currentTimeMillis());
            
            domainStore.put(domainId, domain);

            logRequestEnd("createDomain", domain, System.currentTimeMillis() - startTime);
            return ResultModel.success("域创建成功", domain);
        } catch (Exception e) {
            logRequestError("createDomain", e);
            return ResultModel.error(500, "创建域失败: " + e.getMessage());
        }
    }

    @PostMapping("/domain/delete")
    public ResultModel<Boolean> deleteDomain(@RequestBody DomainIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteDomain", request);

        try {
            DomainDTO removed = domainStore.remove(request.getDomainId());
            if (removed == null) {
                return ResultModel.notFound("域不存在");
            }

            logRequestEnd("deleteDomain", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("域删除成功", true);
        } catch (Exception e) {
            logRequestError("deleteDomain", e);
            return ResultModel.error(500, "删除域失败: " + e.getMessage());
        }
    }

    @PostMapping("/domain/update")
    public ResultModel<DomainDTO> updateDomain(@RequestBody DomainUpdateRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateDomain", request);

        try {
            DomainDTO domain = domainStore.get(request.getDomainId());
            if (domain == null) {
                return ResultModel.notFound("域不存在");
            }

            if (request.getName() != null) domain.setName(request.getName());
            if (request.getDescription() != null) domain.setDescription(request.getDescription());
            if (request.getStatus() != null) domain.setStatus(request.getStatus());
            domain.setUpdatedAt(System.currentTimeMillis());

            logRequestEnd("updateDomain", domain, System.currentTimeMillis() - startTime);
            return ResultModel.success("域更新成功", domain);
        } catch (Exception e) {
            logRequestError("updateDomain", e);
            return ResultModel.error(500, "更新域失败: " + e.getMessage());
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

    @PostMapping("/skill/deploy")
    public ResultModel<SkillDeploymentDTO> deploySkill(@RequestBody SkillDeployRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deploySkill", request);

        try {
            String deploymentId = "deploy-" + UUID.randomUUID().toString().substring(0, 8);
            
            SkillDeploymentDTO deployment = new SkillDeploymentDTO();
            deployment.setDeploymentId(deploymentId);
            deployment.setDomainId(request.getDomainId());
            deployment.setSkillId(request.getSkillId());
            deployment.setTargetNodes(request.getTargetNodes());
            deployment.setStatus("deploying");
            deployment.setDeployedBy(request.getManagerId());
            deployment.setCreatedAt(System.currentTimeMillis());
            deployment.setDeployedNodes(0);
            deployment.setTotalNodes(request.getTargetNodes() != null ? request.getTargetNodes().size() : 1);

            logRequestEnd("deploySkill", deployment, System.currentTimeMillis() - startTime);
            return ResultModel.success("技能部署已启动", deployment);
        } catch (Exception e) {
            logRequestError("deploySkill", e);
            return ResultModel.error(500, "部署技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skill/undeploy")
    public ResultModel<Boolean> undeploySkill(@RequestBody SkillUndeployRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("undeploySkill", request);

        try {
            logRequestEnd("undeploySkill", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("技能卸载成功", true);
        } catch (Exception e) {
            logRequestError("undeploySkill", e);
            return ResultModel.error(500, "卸载技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/execution/start")
    public ResultModel<ExecutionControlDTO> startExecution(@RequestBody ExecutionControlRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("startExecution", request);

        try {
            String executionId = "exec-" + UUID.randomUUID().toString().substring(0, 8);
            
            ExecutionControlDTO execution = new ExecutionControlDTO();
            execution.setExecutionId(executionId);
            execution.setDomainId(request.getDomainId());
            execution.setSkillId(request.getSkillId());
            execution.setTargetNodeId(request.getTargetNodeId());
            execution.setParameters(request.getParameters());
            execution.setStatus("running");
            execution.setStartedBy(request.getManagerId());
            execution.setStartedAt(System.currentTimeMillis());

            logRequestEnd("startExecution", execution, System.currentTimeMillis() - startTime);
            return ResultModel.success("执行已启动", execution);
        } catch (Exception e) {
            logRequestError("startExecution", e);
            return ResultModel.error(500, "启动执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/execution/stop")
    public ResultModel<Boolean> stopExecution(@RequestBody ExecutionIdRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("stopExecution", request);

        try {
            logRequestEnd("stopExecution", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("执行已停止", true);
        } catch (Exception e) {
            logRequestError("stopExecution", e);
            return ResultModel.error(500, "停止执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/policy/apply")
    public ResultModel<Boolean> applyPolicy(@RequestBody PolicyApplyRequest request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("applyPolicy", request);

        try {
            logRequestEnd("applyPolicy", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("策略应用成功", true);
        } catch (Exception e) {
            logRequestError("applyPolicy", e);
            return ResultModel.error(500, "应用策略失败: " + e.getMessage());
        }
    }

    private ManagerActionDTO.ActionResultDTO executeActionInternal(ManagerActionDTO action) {
        ManagerActionDTO.ActionResultDTO result = new ManagerActionDTO.ActionResultDTO();
        
        switch (action.getActionType()) {
            case CREATE_DOMAIN:
            case UPDATE_DOMAIN:
            case DELETE_DOMAIN:
            case ADD_MEMBER:
            case REMOVE_MEMBER:
            case DEPLOY_SKILL:
            case UNDEPLOY_SKILL:
            case START_EXECUTION:
            case STOP_EXECUTION:
                result.setSuccess(true);
                result.setMessage("操作执行成功");
                break;
            default:
                result.setSuccess(true);
                result.setMessage("操作已完成");
        }
        
        return result;
    }

    public static class ActionExecuteRequest {
        private String domainId;
        private String managerId;
        private ManagerActionDTO.ActionType actionType;
        private String targetId;
        private String targetType;
        private Map<String, Object> parameters;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getManagerId() { return managerId; }
        public void setManagerId(String managerId) { this.managerId = managerId; }
        public ManagerActionDTO.ActionType getActionType() { return actionType; }
        public void setActionType(ManagerActionDTO.ActionType actionType) { this.actionType = actionType; }
        public String getTargetId() { return targetId; }
        public void setTargetId(String targetId) { this.targetId = targetId; }
        public String getTargetType() { return targetType; }
        public void setTargetType(String targetType) { this.targetType = targetType; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }

    public static class ActionIdRequest {
        private String actionId;

        public String getActionId() { return actionId; }
        public void setActionId(String actionId) { this.actionId = actionId; }
    }

    public static class DomainPageRequest {
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

    public static class DomainCreateRequest {
        private String name;
        private String description;
        private String managerId;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getManagerId() { return managerId; }
        public void setManagerId(String managerId) { this.managerId = managerId; }
    }

    public static class DomainIdRequest {
        private String domainId;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
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

    public static class MemberOperationRequest {
        private String domainId;
        private String agentId;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
    }

    public static class SkillDeployRequest {
        private String domainId;
        private String skillId;
        private List<String> targetNodes;
        private String managerId;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public List<String> getTargetNodes() { return targetNodes; }
        public void setTargetNodes(List<String> targetNodes) { this.targetNodes = targetNodes; }
        public String getManagerId() { return managerId; }
        public void setManagerId(String managerId) { this.managerId = managerId; }
    }

    public static class SkillUndeployRequest {
        private String domainId;
        private String skillId;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
    }

    public static class ExecutionControlRequest {
        private String domainId;
        private String skillId;
        private String targetNodeId;
        private Map<String, Object> parameters;
        private String managerId;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getTargetNodeId() { return targetNodeId; }
        public void setTargetNodeId(String targetNodeId) { this.targetNodeId = targetNodeId; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public String getManagerId() { return managerId; }
        public void setManagerId(String managerId) { this.managerId = managerId; }
    }

    public static class ExecutionIdRequest {
        private String executionId;

        public String getExecutionId() { return executionId; }
        public void setExecutionId(String executionId) { this.executionId = executionId; }
    }

    public static class PolicyApplyRequest {
        private String domainId;
        private DomainPolicyDTO policy;

        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public DomainPolicyDTO getPolicy() { return policy; }
        public void setPolicy(DomainPolicyDTO policy) { this.policy = policy; }
    }

    public static class SkillDeploymentDTO {
        private String deploymentId;
        private String domainId;
        private String skillId;
        private List<String> targetNodes;
        private String status;
        private String deployedBy;
        private long createdAt;
        private int deployedNodes;
        private int totalNodes;

        public String getDeploymentId() { return deploymentId; }
        public void setDeploymentId(String deploymentId) { this.deploymentId = deploymentId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public List<String> getTargetNodes() { return targetNodes; }
        public void setTargetNodes(List<String> targetNodes) { this.targetNodes = targetNodes; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getDeployedBy() { return deployedBy; }
        public void setDeployedBy(String deployedBy) { this.deployedBy = deployedBy; }
        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
        public int getDeployedNodes() { return deployedNodes; }
        public void setDeployedNodes(int deployedNodes) { this.deployedNodes = deployedNodes; }
        public int getTotalNodes() { return totalNodes; }
        public void setTotalNodes(int totalNodes) { this.totalNodes = totalNodes; }
    }

    public static class ExecutionControlDTO {
        private String executionId;
        private String domainId;
        private String skillId;
        private String targetNodeId;
        private Map<String, Object> parameters;
        private String status;
        private String startedBy;
        private long startedAt;

        public String getExecutionId() { return executionId; }
        public void setExecutionId(String executionId) { this.executionId = executionId; }
        public String getDomainId() { return domainId; }
        public void setDomainId(String domainId) { this.domainId = domainId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getTargetNodeId() { return targetNodeId; }
        public void setTargetNodeId(String targetNodeId) { this.targetNodeId = targetNodeId; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getStartedBy() { return startedBy; }
        public void setStartedBy(String startedBy) { this.startedBy = startedBy; }
        public long getStartedAt() { return startedAt; }
        public void setStartedAt(long startedAt) { this.startedAt = startedAt; }
    }
}
