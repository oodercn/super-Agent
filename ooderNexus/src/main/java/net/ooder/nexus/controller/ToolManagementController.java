package net.ooder.nexus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/tools")
public class ToolManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ToolManagementController.class);

    // 工具存储
    private final Map<String, Tool> tools = new ConcurrentHashMap<>();

    // 工具执行历史
    private final List<ToolExecutionRecord> executionHistory = new ArrayList<>();

    // 工具ID生成器
    private int toolIdGenerator = 1;

    // 执行ID生成器
    private int executionIdGenerator = 1;

    // 初始化默认工具
    public ToolManagementController() {
        initializeDefaultTools();
    }

    /**
     * 初始化默认工具
     */
    private void initializeDefaultTools() {
        // 网络工具
        List<ToolParameter> pingParams = new ArrayList<>();
        pingParams.add(new ToolParameter("target", "string", true, "目标IP地址或域名"));
        pingParams.add(new ToolParameter("count", "integer", false, "Ping次数", "4"));
        pingParams.add(new ToolParameter("timeout", "integer", false, "超时时间(毫秒)", "1000"));
        addTool(new Tool(
                "network-ping",
                "网络Ping工具",
                "network",
                "用于测试网络连接的Ping工具",
                "v1.0",
                true,
                pingParams
        ));

        List<ToolParameter> traceParams = new ArrayList<>();
        traceParams.add(new ToolParameter("target", "string", true, "目标IP地址或域名"));
        traceParams.add(new ToolParameter("maxHops", "integer", false, "最大跳数", "30"));
        traceParams.add(new ToolParameter("timeout", "integer", false, "超时时间(毫秒)", "1000"));
        addTool(new Tool(
                "network-trace",
                "网络跟踪工具",
                "network",
                "用于跟踪网络路由的Traceroute工具",
                "v1.0",
                true,
                traceParams
        ));

        List<ToolParameter> scanParams = new ArrayList<>();
        scanParams.add(new ToolParameter("target", "string", true, "目标IP地址"));
        scanParams.add(new ToolParameter("startPort", "integer", false, "起始端口", "1"));
        scanParams.add(new ToolParameter("endPort", "integer", false, "结束端口", "1000"));
        scanParams.add(new ToolParameter("timeout", "integer", false, "超时时间(毫秒)", "500"));
        addTool(new Tool(
                "network-port-scan",
                "端口扫描工具",
                "network",
                "用于扫描目标主机开放端口的工具",
                "v1.0",
                true,
                scanParams
        ));

        // 系统工具
        addTool(new Tool(
                "system-info",
                "系统信息工具",
                "system",
                "用于获取系统信息的工具",
                "v1.0",
                true,
                Collections.emptyList()
        ));

        addTool(new Tool(
                "system-resources",
                "系统资源工具",
                "system",
                "用于获取系统资源使用情况的工具",
                "v1.0",
                true,
                Collections.emptyList()
        ));

        List<ToolParameter> processParams = new ArrayList<>();
        processParams.add(new ToolParameter("limit", "integer", false, "返回进程数量限制", "50"));
        addTool(new Tool(
                "system-processes",
                "系统进程工具",
                "system",
                "用于获取系统进程信息的工具",
                "v1.0",
                true,
                processParams
        ));

        // 诊断工具
        List<ToolParameter> networkDiagParams = new ArrayList<>();
        networkDiagParams.add(new ToolParameter("target", "string", false, "目标IP地址或域名"));
        addTool(new Tool(
                "diagnostic-network",
                "网络诊断工具",
                "diagnostic",
                "用于诊断网络问题的工具",
                "v1.0",
                true,
                networkDiagParams
        ));

        addTool(new Tool(
                "diagnostic-system",
                "系统诊断工具",
                "diagnostic",
                "用于诊断系统问题的工具",
                "v1.0",
                true,
                Collections.emptyList()
        ));

        List<ToolParameter> serviceDiagParams = new ArrayList<>();
        serviceDiagParams.add(new ToolParameter("serviceName", "string", true, "服务名称"));
        addTool(new Tool(
                "diagnostic-service",
                "服务诊断工具",
                "diagnostic",
                "用于诊断服务状态的工具",
                "v1.0",
                true,
                serviceDiagParams
        ));
    }

    /**
     * 添加工具
     */
    private void addTool(Tool tool) {
        tool.setId("tool-" + toolIdGenerator++);
        tools.put(tool.getCode(), tool);
    }

    /**
     * 获取工具列表
     * @param category 工具类别（可选）
     * @return 工具列表
     */
    @GetMapping("/list")
    public ApiResponse<List<Tool>> getToolList(@RequestParam(required = false) String category) {
        try {
            logger.info("获取工具列表请求，类别: {}", category);
            
            List<Tool> toolList = new ArrayList<>(tools.values());
            
            // 按类别筛选
            if (category != null) {
                toolList = toolList.stream()
                        .filter(tool -> category.equals(tool.getCategory()))
                        .collect(java.util.stream.Collectors.toList());
            }
            
            logger.info("获取工具列表成功，共 {} 个工具", toolList.size());
            return ApiResponse.success(toolList, "获取工具列表成功");
        } catch (Exception e) {
            logger.error("获取工具列表失败", e);
            return ApiResponse.error("获取工具列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取工具详情
     * @param toolCode 工具代码
     * @return 工具详情
     */
    @GetMapping("/detail/{toolCode}")
    public ApiResponse<Tool> getToolDetail(@PathVariable String toolCode) {
        try {
            logger.info("获取工具详情请求，工具代码: {}", toolCode);
            
            Tool tool = tools.get(toolCode);
            if (tool == null) {
                logger.warn("工具不存在，工具代码: {}", toolCode);
                return ApiResponse.error("工具不存在");
            }
            
            logger.info("获取工具详情成功，工具代码: {}", toolCode);
            return ApiResponse.success(tool, "获取工具详情成功");
        } catch (Exception e) {
            logger.error("获取工具详情失败，工具代码: {}", toolCode, e);
            return ApiResponse.error("获取工具详情失败: " + e.getMessage());
        }
    }

    /**
     * 执行工具
     * @param toolCode 工具代码
     * @param parameters 执行参数
     * @return 执行结果
     */
    @PostMapping("/execute/{toolCode}")
    public ApiResponse<ToolExecutionResult> executeTool(@PathVariable String toolCode, @RequestBody Map<String, Object> parameters) {
        try {
            logger.info("执行工具请求，工具代码: {}, 参数: {}", toolCode, parameters);
            
            Tool tool = tools.get(toolCode);
            if (tool == null) {
                logger.warn("工具不存在，工具代码: {}", toolCode);
                return ApiResponse.error("工具不存在");
            }
            
            if (!tool.isEnabled()) {
                logger.warn("工具未启用，工具代码: {}", toolCode);
                return ApiResponse.error("工具未启用");
            }
            
            // 验证参数
            List<String> missingParams = new ArrayList<>();
            for (ToolParameter param : tool.getParameters()) {
                if (param.isRequired() && !parameters.containsKey(param.getName())) {
                    missingParams.add(param.getName());
                }
            }
            
            if (!missingParams.isEmpty()) {
                logger.warn("缺少必要参数，工具代码: {}, 参数: {}", toolCode, missingParams);
                return ApiResponse.error("缺少必要参数: " + String.join(", ", missingParams));
            }
            
            // 模拟工具执行
            ToolExecutionResult result = executeToolInternal(tool, parameters);
            
            // 记录执行历史
            ToolExecutionRecord record = new ToolExecutionRecord(
                    "exec-" + executionIdGenerator++,
                    tool.getCode(),
                    tool.getName(),
                    parameters,
                    result.getStatus(),
                    result.getOutput(),
                    result.getError(),
                    result.getExecutionTime(),
                    new Date()
            );
            executionHistory.add(record);
            
            logger.info("执行工具成功，工具代码: {}, 执行ID: {}", toolCode, record.getId());
            return ApiResponse.success(result, "执行工具成功");
        } catch (Exception e) {
            logger.error("执行工具失败，工具代码: {}", toolCode, e);
            return ApiResponse.error("执行工具失败: " + e.getMessage());
        }
    }

    /**
     * 内部执行工具逻辑
     */
    private ToolExecutionResult executeToolInternal(Tool tool, Map<String, Object> parameters) {
        long startTime = System.currentTimeMillis();
        
        try {
            Thread.sleep(500); // 模拟执行时间
            
            String output = "";
            String error = null;
            String status = "success";
            
            // 根据工具代码模拟执行结果
            switch (tool.getCode()) {
                case "network-ping":
                    String target = (String) parameters.get("target");
                    int count = parameters.containsKey("count") ? Integer.parseInt(parameters.get("count").toString()) : 4;
                    output = "Ping " + target + " 成功\n";
                    for (int i = 0; i < count; i++) {
                        output += "回复来自 " + target + ": 字节=32 时间=" + (10 + i * 5) + "ms TTL=128\n";
                    }
                    break;
                
                case "network-trace":
                    target = (String) parameters.get("target");
                    output = "跟踪路由到 " + target + "\n";
                    for (int i = 1; i <= 5; i++) {
                        output += i + "    192.168.1." + i + "  5ms\n";
                    }
                    output += "目标主机 " + target + " 到达\n";
                    break;
                
                case "network-port-scan":
                    target = (String) parameters.get("target");
                    output = "扫描 " + target + " 的端口\n";
                    output += "开放端口: 22, 80, 443, 3389\n";
                    break;
                
                case "system-info":
                    output = "系统信息:\n";
                    output += "操作系统: Windows 10 Pro\n";
                    output += "版本: 21H2\n";
                    output += "系统类型: 64位操作系统\n";
                    output += "处理器: Intel Core i7-11700K\n";
                    output += "内存: 16.0 GB\n";
                    break;
                
                case "system-resources":
                    output = "系统资源使用情况:\n";
                    output += "CPU使用率: " + (20 + Math.random() * 30) + "%\n";
                    output += "内存使用率: " + (40 + Math.random() * 20) + "%\n";
                    output += "磁盘使用率: " + (30 + Math.random() * 20) + "%\n";
                    break;
                
                case "system-processes":
                    output = "系统进程:\n";
                    output += "进程名称               PID     内存使用\n";
                    output += "System                4       128 MB\n";
                    output += "svchost.exe           1234    256 MB\n";
                    output += "explorer.exe          5678    512 MB\n";
                    break;
                
                case "diagnostic-network":
                    target = parameters.containsKey("target") ? (String) parameters.get("target") : "localhost";
                    output = "网络诊断结果:\n";
                    output += "目标: " + target + "\n";
                    output += "网络连接: 正常\n";
                    output += "DNS解析: 正常\n";
                    output += "延迟: " + (10 + Math.random() * 40) + "ms\n";
                    break;
                
                case "diagnostic-system":
                    output = "系统诊断结果:\n";
                    output += "系统状态: 正常\n";
                    output += "CPU温度: " + (40 + Math.random() * 10) + "°C\n";
                    output += "磁盘健康: 良好\n";
                    output += "内存状态: 正常\n";
                    break;
                
                case "diagnostic-service":
                    String serviceName = (String) parameters.get("serviceName");
                    output = "服务诊断结果:\n";
                    output += "服务名称: " + serviceName + "\n";
                    output += "状态: 运行中\n";
                    output += "启动类型: 自动\n";
                    output += "PID: " + (1000 + Math.random() * 9000) + "\n";
                    break;
                
                default:
                    output = "工具执行成功\n";
                    output += "参数: " + parameters.toString() + "\n";
                    break;
            }
            
            long executionTime = System.currentTimeMillis() - startTime;
            
            return new ToolExecutionResult(
                    status,
                    output,
                    error,
                    executionTime,
                    new Date()
            );
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            return new ToolExecutionResult(
                    "error",
                    null,
                    e.getMessage(),
                    executionTime,
                    new Date()
            );
        }
    }

    /**
     * 获取工具执行历史
     * @param limit 限制数量
     * @return 执行历史
     */
    @GetMapping("/execution/history")
    public ApiResponse<List<ToolExecutionRecord>> getExecutionHistory(@RequestParam(defaultValue = "50") int limit) {
        try {
            logger.info("获取工具执行历史请求，限制: {}", limit);
            
            List<ToolExecutionRecord> history = new ArrayList<>(executionHistory);
            // 按时间倒序排序
            history.sort((r1, r2) -> r2.getTimestamp().compareTo(r1.getTimestamp()));
            // 限制返回数量
            history = history.stream().limit(limit).collect(java.util.stream.Collectors.toList());
            
            logger.info("获取工具执行历史成功，返回 {} 条记录", history.size());
            return ApiResponse.success(history, "获取工具执行历史成功");
        } catch (Exception e) {
            logger.error("获取工具执行历史失败", e);
            return ApiResponse.error("获取工具执行历史失败: " + e.getMessage());
        }
    }

    /**
     * 获取工具执行详情
     * @param executionId 执行ID
     * @return 执行详情
     */
    @GetMapping("/execution/detail/{executionId}")
    public ApiResponse<ToolExecutionRecord> getExecutionDetail(@PathVariable String executionId) {
        try {
            logger.info("获取工具执行详情请求，执行ID: {}", executionId);
            
            ToolExecutionRecord record = executionHistory.stream()
                    .filter(r -> executionId.equals(r.getId()))
                    .findFirst()
                    .orElse(null);
            
            if (record == null) {
                logger.warn("执行记录不存在，执行ID: {}", executionId);
                return ApiResponse.error("执行记录不存在");
            }
            
            logger.info("获取工具执行详情成功，执行ID: {}", executionId);
            return ApiResponse.success(record, "获取工具执行详情成功");
        } catch (Exception e) {
            logger.error("获取工具执行详情失败，执行ID: {}", executionId, e);
            return ApiResponse.error("获取工具执行详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取工具类别
     * @return 工具类别
     */
    @GetMapping("/categories")
    public ApiResponse<List<String>> getToolCategories() {
        try {
            logger.info("获取工具类别请求");
            
            List<String> categories = tools.values().stream()
                    .map(Tool::getCategory)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
            
            logger.info("获取工具类别成功，共 {} 个类别", categories.size());
            return ApiResponse.success(categories, "获取工具类别成功");
        } catch (Exception e) {
            logger.error("获取工具类别失败", e);
            return ApiResponse.error("获取工具类别失败: " + e.getMessage());
        }
    }

    /**
     * 工具模型类
     */
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

        // Getter和Setter方法
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public String getDescription() {
            return description;
        }

        public String getVersion() {
            return version;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public List<ToolParameter> getParameters() {
            return parameters;
        }
    }

    /**
     * 工具参数模型类
     */
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

        // Getter方法
        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public boolean isRequired() {
            return required;
        }

        public String getDescription() {
            return description;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }

    /**
     * 工具执行结果模型类
     */
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

        // Getter方法
        public String getStatus() {
            return status;
        }

        public String getOutput() {
            return output;
        }

        public String getError() {
            return error;
        }

        public long getExecutionTime() {
            return executionTime;
        }

        public Date getTimestamp() {
            return timestamp;
        }
    }

    /**
     * 工具执行记录模型类
     */
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

        // Getter方法
        public String getId() {
            return id;
        }

        public String getToolCode() {
            return toolCode;
        }

        public String getToolName() {
            return toolName;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public String getStatus() {
            return status;
        }

        public String getOutput() {
            return output;
        }

        public String getError() {
            return error;
        }

        public long getExecutionTime() {
            return executionTime;
        }

        public Date getTimestamp() {
            return timestamp;
        }
    }

    /**
     * API响应模型类
     */
    public static class ApiResponse<T> {
        private int code;
        private String message;
        private T data;

        // 构造方法
        private ApiResponse(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        // 成功响应
        public static <T> ApiResponse<T> success(T data, String message) {
            return new ApiResponse<>(200, message, data);
        }

        // 错误响应
        public static <T> ApiResponse<T> error(String message) {
            return new ApiResponse<>(500, message, null);
        }

        // Getter方法
        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public T getData() {
            return data;
        }
    }
}
