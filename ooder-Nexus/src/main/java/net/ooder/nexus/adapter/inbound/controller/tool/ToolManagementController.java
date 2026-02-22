package net.ooder.nexus.adapter.inbound.controller.tool;

import net.ooder.nexus.common.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 工具管理控制器
 * 处理工具列表、详情、执行、历史等操作
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/tools")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class ToolManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ToolManagementController.class);

    private final Map<String, Tool> tools = new ConcurrentHashMap<>();
    private final List<ToolExecutionRecord> executionHistory = new ArrayList<>();
    private int toolIdGenerator = 1;
    private int executionIdGenerator = 1;

    public ToolManagementController() {
        initializeDefaultTools();
    }

    private void initializeDefaultTools() {
        List<ToolParameter> pingParams = new ArrayList<>();
        pingParams.add(new ToolParameter("target", "string", true, "目标IP地址或域名"));
        pingParams.add(new ToolParameter("count", "integer", false, "Ping次数", "4"));
        pingParams.add(new ToolParameter("timeout", "integer", false, "超时时间(毫秒)", "1000"));
        addTool(new Tool("network-ping", "网络Ping工具", "network", "用于测试网络连接的Ping工具", "v1.0", true, pingParams));

        List<ToolParameter> traceParams = new ArrayList<>();
        traceParams.add(new ToolParameter("target", "string", true, "目标IP地址或域名"));
        traceParams.add(new ToolParameter("maxHops", "integer", false, "最大跳数", "30"));
        traceParams.add(new ToolParameter("timeout", "integer", false, "超时时间(毫秒)", "1000"));
        addTool(new Tool("network-trace", "网络跟踪工具", "network", "用于跟踪网络路由的Traceroute工具", "v1.0", true, traceParams));

        List<ToolParameter> scanParams = new ArrayList<>();
        scanParams.add(new ToolParameter("target", "string", true, "目标IP地址"));
        scanParams.add(new ToolParameter("startPort", "integer", false, "起始端口", "1"));
        scanParams.add(new ToolParameter("endPort", "integer", false, "结束端口", "1000"));
        scanParams.add(new ToolParameter("timeout", "integer", false, "超时时间(毫秒)", "500"));
        addTool(new Tool("network-port-scan", "端口扫描工具", "network", "用于扫描目标主机开放端口的工具", "v1.0", true, scanParams));

        addTool(new Tool("system-info", "系统信息工具", "system", "用于获取系统信息的工具", "v1.0", true, Collections.emptyList()));
        addTool(new Tool("system-resources", "系统资源工具", "system", "用于获取系统资源使用情况的工具", "v1.0", true, Collections.emptyList()));

        List<ToolParameter> processParams = new ArrayList<>();
        processParams.add(new ToolParameter("limit", "integer", false, "返回进程数量限制", "50"));
        addTool(new Tool("system-processes", "系统进程工具", "system", "用于获取系统进程信息的工具", "v1.0", true, processParams));

        List<ToolParameter> networkDiagParams = new ArrayList<>();
        networkDiagParams.add(new ToolParameter("target", "string", false, "目标IP地址或域名"));
        addTool(new Tool("diagnostic-network", "网络诊断工具", "diagnostic", "用于诊断网络问题的工具", "v1.0", true, networkDiagParams));

        addTool(new Tool("diagnostic-system", "系统诊断工具", "diagnostic", "用于诊断系统问题的工具", "v1.0", true, Collections.emptyList()));

        List<ToolParameter> serviceDiagParams = new ArrayList<>();
        serviceDiagParams.add(new ToolParameter("serviceName", "string", true, "服务名称"));
        addTool(new Tool("diagnostic-service", "服务诊断工具", "diagnostic", "用于诊断服务状态的工具", "v1.0", true, serviceDiagParams));
    }

    private void addTool(Tool tool) {
        tool.setId("tool-" + toolIdGenerator++);
        tools.put(tool.getCode(), tool);
    }

    @PostMapping("/list")
    @ResponseBody
    public ResultModel<Map<String, Object>> getToolList(@RequestBody Map<String, Object> request) {
        try {
            String category = (String) request.get("category");
            logger.info("获取工具列表请求，类别: {}", category);

            List<Tool> toolList = new ArrayList<>(tools.values());
            if (category != null) {
                toolList = toolList.stream().filter(tool -> category.equals(tool.getCategory())).collect(Collectors.toList());
            }

            Map<String, Object> result = new HashMap<>();
            result.put("tools", toolList);
            result.put("count", toolList.size());

            return ResultModel.success(result);
        } catch (Exception e) {
            logger.error("获取工具列表失败", e);
            return ResultModel.error("获取工具列表失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/detail")
    @ResponseBody
    public ResultModel<Tool> getToolDetail(@RequestBody Map<String, String> request) {
        try {
            String toolCode = request.get("toolCode");
            Tool tool = tools.get(toolCode);
            if (tool == null) {
                return ResultModel.error("工具不存在", 404);
            }
            return ResultModel.success(tool);
        } catch (Exception e) {
            return ResultModel.error("获取工具详情失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/execute")
    @ResponseBody
    public ResultModel<ToolExecutionResult> executeTool(@RequestBody Map<String, Object> request) {
        try {
            String toolCode = (String) request.get("toolCode");
            Map<String, Object> parameters = (Map<String, Object>) request.getOrDefault("parameters", new HashMap<>());

            Tool tool = tools.get(toolCode);
            if (tool == null) {
                return ResultModel.error("工具不存在", 404);
            }

            if (!tool.isEnabled()) {
                return ResultModel.error("工具未启用", 400);
            }

            List<String> missingParams = new ArrayList<>();
            for (ToolParameter param : tool.getParameters()) {
                if (param.isRequired() && !parameters.containsKey(param.getName())) {
                    missingParams.add(param.getName());
                }
            }

            if (!missingParams.isEmpty()) {
                return ResultModel.error("缺少必要参数: " + String.join(", ", missingParams), 400);
            }

            ToolExecutionResult result = executeToolInternal(tool, parameters);

            ToolExecutionRecord record = new ToolExecutionRecord(
                    "exec-" + executionIdGenerator++, tool.getCode(), tool.getName(), parameters,
                    result.getStatus(), result.getOutput(), result.getError(), result.getExecutionTime(), new Date());
            executionHistory.add(record);

            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("执行工具失败: " + e.getMessage(), 500);
        }
    }

    private ToolExecutionResult executeToolInternal(Tool tool, Map<String, Object> parameters) {
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(500);
            String output = "工具执行成功\n参数: " + parameters.toString();
            long executionTime = System.currentTimeMillis() - startTime;
            return new ToolExecutionResult("success", output, null, executionTime, new Date());
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            return new ToolExecutionResult("error", null, e.getMessage(), executionTime, new Date());
        }
    }

    @PostMapping("/execution/history")
    @ResponseBody
    public ResultModel<Map<String, Object>> getExecutionHistory(@RequestBody Map<String, Object> request) {
        try {
            int limit = request.get("limit") != null ? (Integer) request.get("limit") : 50;
            List<ToolExecutionRecord> history = new ArrayList<>(executionHistory);
            history.sort((r1, r2) -> r2.getTimestamp().compareTo(r1.getTimestamp()));
            history = history.stream().limit(limit).collect(Collectors.toList());

            Map<String, Object> result = new HashMap<>();
            result.put("history", history);
            result.put("count", history.size());

            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("获取工具执行历史失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/execution/detail")
    @ResponseBody
    public ResultModel<ToolExecutionRecord> getExecutionDetail(@RequestBody Map<String, String> request) {
        try {
            String executionId = request.get("executionId");
            ToolExecutionRecord record = executionHistory.stream()
                    .filter(r -> executionId.equals(r.getId()))
                    .findFirst()
                    .orElse(null);

            if (record == null) {
                return ResultModel.error("执行记录不存在", 404);
            }

            return ResultModel.success(record);
        } catch (Exception e) {
            return ResultModel.error("获取工具执行详情失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/categories")
    @ResponseBody
    public ResultModel<Map<String, Object>> getToolCategories() {
        try {
            List<String> categories = tools.values().stream().map(Tool::getCategory).distinct().collect(Collectors.toList());
            Map<String, Object> result = new HashMap<>();
            result.put("categories", categories);
            result.put("count", categories.size());
            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("获取工具类别失败: " + e.getMessage(), 500);
        }
    }

    public static class Tool {
        private String id;
        private String code;
        private String name;
        private String category;
        private String description;
        private String version;
        private boolean enabled;
        private List<ToolParameter> parameters;

        public Tool(String code, String name, String category, String description, String version, boolean enabled, List<ToolParameter> parameters) {
            this.code = code;
            this.name = name;
            this.category = category;
            this.description = description;
            this.version = version;
            this.enabled = enabled;
            this.parameters = parameters;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getCode() { return code; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public String getDescription() { return description; }
        public String getVersion() { return version; }
        public boolean isEnabled() { return enabled; }
        public List<ToolParameter> getParameters() { return parameters; }
    }

    public static class ToolParameter {
        private String name;
        private String type;
        private boolean required;
        private String description;
        private String defaultValue;

        public ToolParameter(String name, String type, boolean required, String description) {
            this(name, type, required, description, null);
        }

        public ToolParameter(String name, String type, boolean required, String description, String defaultValue) {
            this.name = name;
            this.type = type;
            this.required = required;
            this.description = description;
            this.defaultValue = defaultValue;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public boolean isRequired() { return required; }
        public String getDescription() { return description; }
        public String getDefaultValue() { return defaultValue; }
    }

    public static class ToolExecutionResult {
        private String status;
        private String output;
        private String error;
        private long executionTime;
        private Date timestamp;

        public ToolExecutionResult(String status, String output, String error, long executionTime, Date timestamp) {
            this.status = status;
            this.output = output;
            this.error = error;
            this.executionTime = executionTime;
            this.timestamp = timestamp;
        }

        public String getStatus() { return status; }
        public String getOutput() { return output; }
        public String getError() { return error; }
        public long getExecutionTime() { return executionTime; }
        public Date getTimestamp() { return timestamp; }
    }

    public static class ToolExecutionRecord {
        private String id;
        private String toolCode;
        private String toolName;
        private Map<String, Object> parameters;
        private String status;
        private String output;
        private String error;
        private long executionTime;
        private Date timestamp;

        public ToolExecutionRecord(String id, String toolCode, String toolName, Map<String, Object> parameters, String status, String output, String error, long executionTime, Date timestamp) {
            this.id = id;
            this.toolCode = toolCode;
            this.toolName = toolName;
            this.parameters = parameters;
            this.status = status;
            this.output = output;
            this.error = error;
            this.executionTime = executionTime;
            this.timestamp = timestamp;
        }

        public String getId() { return id; }
        public String getToolCode() { return toolCode; }
        public String getToolName() { return toolName; }
        public Map<String, Object> getParameters() { return parameters; }
        public String getStatus() { return status; }
        public String getOutput() { return output; }
        public String getError() { return error; }
        public long getExecutionTime() { return executionTime; }
        public Date getTimestamp() { return timestamp; }
    }
}
