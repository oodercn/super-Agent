package net.ooder.nexus.skillcenter.controller;

import net.ooder.nexus.skillcenter.dto.PageResult;
import net.ooder.nexus.skillcenter.dto.orchestration.*;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.skillcenter.manager.SkillManager;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/orchestration")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class OrchestrationController extends BaseController {

    private final SkillManager skillManager;
    private final Map<String, OrchestrationTemplateDTO> templateStore;
    private final Map<String, OrchestrationExecutionDTO> executionStore;
    private final Map<String, ExecutionScheduleDTO> scheduleStore;
    private final ExecutorService executorService;

    public OrchestrationController() {
        this.skillManager = SkillManager.getInstance();
        this.templateStore = new ConcurrentHashMap<>();
        this.executionStore = new ConcurrentHashMap<>();
        this.scheduleStore = new ConcurrentHashMap<>();
        this.executorService = Executors.newFixedThreadPool(5);
        initMockData();
    }

    private void initMockData() {
        OrchestrationTemplateDTO template1 = new OrchestrationTemplateDTO();
        template1.setTemplateId("tpl-001");
        template1.setName("数据处理流水线");
        template1.setDescription("数据获取、处理、输出的标准流水线");
        template1.setVersion("1.0.0");
        template1.setStatus("active");
        template1.setCategory("data-processing");
        template1.setCreatedAt(System.currentTimeMillis() - 86400000);
        template1.setAuthor("admin");

        List<OrchestrationTemplateDTO.SkillNode> nodes = new ArrayList<>();
        
        OrchestrationTemplateDTO.SkillNode node1 = new OrchestrationTemplateDTO.SkillNode();
        node1.setNodeId("node-1");
        node1.setSkillId("skill-data-fetch");
        node1.setName("数据获取");
        node1.setExecutionMode("sync");
        nodes.add(node1);

        OrchestrationTemplateDTO.SkillNode node2 = new OrchestrationTemplateDTO.SkillNode();
        node2.setNodeId("node-2");
        node2.setSkillId("skill-data-process");
        node2.setName("数据处理");
        node2.setDependencies(Arrays.asList("node-1"));
        node2.setExecutionMode("sync");
        nodes.add(node2);

        OrchestrationTemplateDTO.SkillNode node3 = new OrchestrationTemplateDTO.SkillNode();
        node3.setNodeId("node-3");
        node3.setSkillId("skill-data-output");
        node3.setName("数据输出");
        node3.setDependencies(Arrays.asList("node-2"));
        node3.setExecutionMode("sync");
        nodes.add(node3);

        template1.setSkills(nodes);

        List<OrchestrationTemplateDTO.DataFlow> flows = new ArrayList<>();
        OrchestrationTemplateDTO.DataFlow flow1 = new OrchestrationTemplateDTO.DataFlow();
        flow1.setFlowId("flow-1");
        flow1.setSourceNode("node-1");
        flow1.setSourceOutput("data");
        flow1.setTargetNode("node-2");
        flow1.setTargetInput("input");
        flows.add(flow1);

        OrchestrationTemplateDTO.DataFlow flow2 = new OrchestrationTemplateDTO.DataFlow();
        flow2.setFlowId("flow-2");
        flow2.setSourceNode("node-2");
        flow2.setSourceOutput("result");
        flow2.setTargetNode("node-3");
        flow2.setTargetInput("data");
        flows.add(flow2);

        template1.setDataFlows(flows);

        OrchestrationTemplateDTO.ExecutionConfig execConfig = new OrchestrationTemplateDTO.ExecutionConfig();
        execConfig.setExecutionMode("sequential");
        execConfig.setMaxConcurrency(3);
        execConfig.setDefaultTimeout(30000);
        execConfig.setContinueOnError(false);
        template1.setExecution(execConfig);

        OrchestrationTemplateDTO.ErrorHandling errorHandling = new OrchestrationTemplateDTO.ErrorHandling();
        errorHandling.setStrategy("stop");
        errorHandling.setMaxRetries(3);
        errorHandling.setRetryDelay(1000);
        template1.setErrorHandling(errorHandling);

        templateStore.put(template1.getTemplateId(), template1);

        ExecutionScheduleDTO schedule = new ExecutionScheduleDTO();
        schedule.setScheduleId("sched-001");
        schedule.setTemplateId("tpl-001");
        schedule.setName("每日数据处理");
        schedule.setCronExpression("0 0 2 * * ?");
        schedule.setTimezone("Asia/Shanghai");
        schedule.setEnabled(true);
        schedule.setCreatedAt(System.currentTimeMillis() - 86400000);
        schedule.setNextExecutionTime(System.currentTimeMillis() + 86400000);
        schedule.setExecutionCount(15);
        schedule.setFailureCount(1);
        schedule.setStatus("active");
        scheduleStore.put(schedule.getScheduleId(), schedule);
    }

    @PostMapping("/templates")
    public ResultModel<PageResult<OrchestrationTemplateDTO>> listTemplates(@RequestBody TemplateQueryRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listTemplates", request);

        try {
            List<OrchestrationTemplateDTO> templates = new ArrayList<>(templateStore.values());

            if (request.getCategory() != null && !request.getCategory().isEmpty()) {
                templates.removeIf(t -> !request.getCategory().equals(t.getCategory()));
            }
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                templates.removeIf(t -> !request.getStatus().equals(t.getStatus()));
            }

            int total = templates.size();
            int pageNum = request.getPageNum() > 0 ? request.getPageNum() : 1;
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 10;
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            List<OrchestrationTemplateDTO> paged = fromIndex < total ? 
                templates.subList(fromIndex, toIndex) : new ArrayList<>();

            PageResult<OrchestrationTemplateDTO> result = new PageResult<>(paged, total, pageNum, pageSize);

            logRequestEnd("listTemplates", total + " templates", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listTemplates", e);
            return ResultModel.error(500, "获取模板列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/templates/get")
    public ResultModel<OrchestrationTemplateDTO> getTemplate(@RequestBody TemplateIdRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getTemplate", request);

        try {
            OrchestrationTemplateDTO template = templateStore.get(request.getTemplateId());
            if (template == null) {
                logRequestEnd("getTemplate", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("模板不存在");
            }

            logRequestEnd("getTemplate", template.getName(), System.currentTimeMillis() - startTime);
            return ResultModel.success(template);
        } catch (Exception e) {
            logRequestError("getTemplate", e);
            return ResultModel.error(500, "获取模板失败: " + e.getMessage());
        }
    }

    @PostMapping("/templates/create")
    public ResultModel<OrchestrationTemplateDTO> createTemplate(@RequestBody OrchestrationTemplateDTO template) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createTemplate", template);

        try {
            String templateId = "tpl-" + UUID.randomUUID().toString().substring(0, 8);
            template.setTemplateId(templateId);
            template.setCreatedAt(System.currentTimeMillis());
            template.setUpdatedAt(System.currentTimeMillis());
            template.setStatus("active");

            templateStore.put(templateId, template);

            logRequestEnd("createTemplate", templateId, System.currentTimeMillis() - startTime);
            return ResultModel.success("创建模板成功", template);
        } catch (Exception e) {
            logRequestError("createTemplate", e);
            return ResultModel.error(500, "创建模板失败: " + e.getMessage());
        }
    }

    @PostMapping("/templates/update")
    public ResultModel<OrchestrationTemplateDTO> updateTemplate(@RequestBody UpdateTemplateRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateTemplate", request);

        try {
            OrchestrationTemplateDTO template = templateStore.get(request.getTemplateId());
            if (template == null) {
                logRequestEnd("updateTemplate", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("模板不存在");
            }

            if (request.getName() != null) template.setName(request.getName());
            if (request.getDescription() != null) template.setDescription(request.getDescription());
            if (request.getSkills() != null) template.setSkills(request.getSkills());
            if (request.getDataFlows() != null) template.setDataFlows(request.getDataFlows());
            template.setUpdatedAt(System.currentTimeMillis());

            logRequestEnd("updateTemplate", template.getName(), System.currentTimeMillis() - startTime);
            return ResultModel.success("更新模板成功", template);
        } catch (Exception e) {
            logRequestError("updateTemplate", e);
            return ResultModel.error(500, "更新模板失败: " + e.getMessage());
        }
    }

    @PostMapping("/templates/delete")
    public ResultModel<Boolean> deleteTemplate(@RequestBody TemplateIdRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteTemplate", request);

        try {
            OrchestrationTemplateDTO removed = templateStore.remove(request.getTemplateId());
            if (removed == null) {
                logRequestEnd("deleteTemplate", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("模板不存在");
            }

            logRequestEnd("deleteTemplate", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("删除模板成功", true);
        } catch (Exception e) {
            logRequestError("deleteTemplate", e);
            return ResultModel.error(500, "删除模板失败: " + e.getMessage());
        }
    }

    @PostMapping("/execute")
    public ResultModel<String> executeTemplate(@RequestBody ExecuteTemplateRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("executeTemplate", request);

        try {
            OrchestrationTemplateDTO template = templateStore.get(request.getTemplateId());
            if (template == null) {
                logRequestEnd("executeTemplate", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("模板不存在");
            }

            String executionId = "exec-" + UUID.randomUUID().toString().substring(0, 8);

            OrchestrationExecutionDTO execution = new OrchestrationExecutionDTO();
            execution.setExecutionId(executionId);
            execution.setTemplateId(request.getTemplateId());
            execution.setName(template.getName());
            execution.setStatus("running");
            execution.setStartTime(System.currentTimeMillis());
            execution.setInput(request.getParameters());

            List<OrchestrationExecutionDTO.NodeExecution> nodeExecutions = new ArrayList<>();
            for (OrchestrationTemplateDTO.SkillNode node : template.getSkills()) {
                OrchestrationExecutionDTO.NodeExecution nodeExec = new OrchestrationExecutionDTO.NodeExecution();
                nodeExec.setNodeId(node.getNodeId());
                nodeExec.setSkillId(node.getSkillId());
                nodeExec.setStatus("pending");
                nodeExecutions.add(nodeExec);
            }
            execution.setNodeExecutions(nodeExecutions);

            OrchestrationExecutionDTO.ExecutionStats stats = new OrchestrationExecutionDTO.ExecutionStats();
            stats.setTotalNodes(nodeExecutions.size());
            stats.setCompletedNodes(0);
            stats.setFailedNodes(0);
            execution.setStats(stats);

            executionStore.put(executionId, execution);

            final String execId = executionId;
            final OrchestrationTemplateDTO templateRef = template;
            executorService.submit(() -> simulateExecution(execId, templateRef));

            logRequestEnd("executeTemplate", executionId, System.currentTimeMillis() - startTime);
            return ResultModel.success(executionId);
        } catch (Exception e) {
            logRequestError("executeTemplate", e);
            return ResultModel.error(500, "执行模板失败: " + e.getMessage());
        }
    }

    private void simulateExecution(String executionId, OrchestrationTemplateDTO template) {
        OrchestrationExecutionDTO execution = executionStore.get(executionId);
        if (execution == null) return;

        try {
            for (OrchestrationExecutionDTO.NodeExecution nodeExec : execution.getNodeExecutions()) {
                nodeExec.setStatus("running");
                nodeExec.setStartTime(System.currentTimeMillis());
                
                Thread.sleep(500 + (long)(Math.random() * 1000));
                
                nodeExec.setStatus("success");
                nodeExec.setEndTime(System.currentTimeMillis());
                nodeExec.setDuration(nodeExec.getEndTime() - nodeExec.getStartTime());
                nodeExec.setOutput(new HashMap<>());
                nodeExec.getOutput().put("result", "processed");
                
                execution.getStats().setCompletedNodes(execution.getStats().getCompletedNodes() + 1);
            }

            execution.setStatus("success");
            execution.setEndTime(System.currentTimeMillis());
            execution.setDuration(execution.getEndTime() - execution.getStartTime());
            execution.setOutput(new HashMap<>());
            execution.getOutput().put("finalResult", "completed");

        } catch (Exception e) {
            execution.setStatus("failed");
            execution.setErrorMessage(e.getMessage());
            execution.setEndTime(System.currentTimeMillis());
        }
    }

    @PostMapping("/status/{executionId}")
    public ResultModel<OrchestrationExecutionDTO> getExecutionStatus(@PathVariable String executionId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getExecutionStatus", executionId);

        try {
            OrchestrationExecutionDTO execution = executionStore.get(executionId);
            if (execution == null) {
                logRequestEnd("getExecutionStatus", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("执行记录不存在");
            }

            logRequestEnd("getExecutionStatus", execution.getStatus(), System.currentTimeMillis() - startTime);
            return ResultModel.success(execution);
        } catch (Exception e) {
            logRequestError("getExecutionStatus", e);
            return ResultModel.error(500, "获取执行状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/executions")
    public ResultModel<PageResult<OrchestrationExecutionDTO>> listExecutions(@RequestBody ExecutionQueryRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listExecutions", request);

        try {
            List<OrchestrationExecutionDTO> executions = new ArrayList<>(executionStore.values());

            if (request.getTemplateId() != null && !request.getTemplateId().isEmpty()) {
                executions.removeIf(e -> !request.getTemplateId().equals(e.getTemplateId()));
            }
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                executions.removeIf(e -> !request.getStatus().equals(e.getStatus()));
            }

            executions.sort((a, b) -> Long.compare(b.getStartTime(), a.getStartTime()));

            int total = executions.size();
            int pageNum = request.getPageNum() > 0 ? request.getPageNum() : 1;
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 10;
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            List<OrchestrationExecutionDTO> paged = fromIndex < total ? 
                executions.subList(fromIndex, toIndex) : new ArrayList<>();

            PageResult<OrchestrationExecutionDTO> result = new PageResult<>(paged, total, pageNum, pageSize);

            logRequestEnd("listExecutions", total + " executions", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listExecutions", e);
            return ResultModel.error(500, "获取执行列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/schedules")
    public ResultModel<PageResult<ExecutionScheduleDTO>> listSchedules(@RequestBody ScheduleQueryRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listSchedules", request);

        try {
            List<ExecutionScheduleDTO> schedules = new ArrayList<>(scheduleStore.values());

            int total = schedules.size();
            int pageNum = request.getPageNum() > 0 ? request.getPageNum() : 1;
            int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 10;
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);

            List<ExecutionScheduleDTO> paged = fromIndex < total ? 
                schedules.subList(fromIndex, toIndex) : new ArrayList<>();

            PageResult<ExecutionScheduleDTO> result = new PageResult<>(paged, total, pageNum, pageSize);

            logRequestEnd("listSchedules", total + " schedules", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listSchedules", e);
            return ResultModel.error(500, "获取计划列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/schedules/create")
    public ResultModel<ExecutionScheduleDTO> createSchedule(@RequestBody CreateScheduleRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("createSchedule", request);

        try {
            String scheduleId = "sched-" + UUID.randomUUID().toString().substring(0, 8);

            ExecutionScheduleDTO schedule = new ExecutionScheduleDTO();
            schedule.setScheduleId(scheduleId);
            schedule.setTemplateId(request.getTemplateId());
            schedule.setName(request.getName());
            schedule.setCronExpression(request.getCronExpression());
            schedule.setTimezone(request.getTimezone() != null ? request.getTimezone() : "Asia/Shanghai");
            schedule.setEnabled(true);
            schedule.setCreatedAt(System.currentTimeMillis());
            schedule.setExecutionCount(0);
            schedule.setFailureCount(0);
            schedule.setStatus("active");

            scheduleStore.put(scheduleId, schedule);

            logRequestEnd("createSchedule", scheduleId, System.currentTimeMillis() - startTime);
            return ResultModel.success("创建计划成功", schedule);
        } catch (Exception e) {
            logRequestError("createSchedule", e);
            return ResultModel.error(500, "创建计划失败: " + e.getMessage());
        }
    }

    @PostMapping("/schedules/toggle")
    public ResultModel<Boolean> toggleSchedule(@RequestBody ScheduleIdRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("toggleSchedule", request);

        try {
            ExecutionScheduleDTO schedule = scheduleStore.get(request.getScheduleId());
            if (schedule == null) {
                logRequestEnd("toggleSchedule", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("计划不存在");
            }

            schedule.setEnabled(!schedule.isEnabled());
            schedule.setStatus(schedule.isEnabled() ? "active" : "paused");

            logRequestEnd("toggleSchedule", schedule.isEnabled(), System.currentTimeMillis() - startTime);
            return ResultModel.success(schedule.isEnabled() ? "计划已启用" : "计划已暂停", schedule.isEnabled());
        } catch (Exception e) {
            logRequestError("toggleSchedule", e);
            return ResultModel.error(500, "切换计划状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/schedules/delete")
    public ResultModel<Boolean> deleteSchedule(@RequestBody ScheduleIdRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteSchedule", request);

        try {
            ExecutionScheduleDTO removed = scheduleStore.remove(request.getScheduleId());
            if (removed == null) {
                logRequestEnd("deleteSchedule", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("计划不存在");
            }

            logRequestEnd("deleteSchedule", true, System.currentTimeMillis() - startTime);
            return ResultModel.success("删除计划成功", true);
        } catch (Exception e) {
            logRequestError("deleteSchedule", e);
            return ResultModel.error(500, "删除计划失败: " + e.getMessage());
        }
    }
}
