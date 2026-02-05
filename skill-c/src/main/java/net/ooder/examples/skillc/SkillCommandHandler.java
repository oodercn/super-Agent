package net.ooder.examples.skillc;

import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.udp.UDPMessageHandler;
import net.ooder.sdk.system.validation.ParamValidator;
import net.ooder.sdk.system.validation.ValidParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.*;
import org.apache.commons.lang3.exception.ExceptionUtils;

@Component
public class SkillCommandHandler implements UDPMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(SkillCommandHandler.class);

    // 线程池配置
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final long KEEP_ALIVE_TIME = 60L;
    private static final int QUEUE_CAPACITY = 1000;

    private final ExecutorService commandExecutor;
    
    // VFS管理相关
    private final Map<String, VfsInfo> availableVfsServices = new ConcurrentHashMap<>();
    private final List<String> endAgents = new CopyOnWriteArrayList<>();
    
    // VFS相关数据结构
    private static class VfsInfo {
        private final String vfsId;
        private final String vfsUrl;
        private final String groupName;
        private final List<String> capabilities;
        private long lastUpdateTime;
        private String status;
        
        public VfsInfo(String vfsId, String vfsUrl, String groupName, List<String> capabilities) {
            this.vfsId = vfsId;
            this.vfsUrl = vfsUrl;
            this.groupName = groupName;
            this.capabilities = capabilities;
            this.lastUpdateTime = System.currentTimeMillis();
            this.status = "available";
        }
        
        public String getVfsId() {
            return vfsId;
        }
        
        public String getVfsUrl() {
            return vfsUrl;
        }
        
        public String getGroupName() {
            return groupName;
        }
        
        public List<String> getCapabilities() {
            return capabilities;
        }
        
        public void updateStatus(String status) {
            this.status = status;
            this.lastUpdateTime = System.currentTimeMillis();
        }
        
        public Map<String, Object> toMap() {
            Map<String, Object> vfsMap = new HashMap<>();
            vfsMap.put("vfsId", vfsId);
            vfsMap.put("vfsUrl", vfsUrl);
            vfsMap.put("groupName", groupName);
            vfsMap.put("capabilities", capabilities);
            vfsMap.put("status", status);
            vfsMap.put("lastUpdateTime", lastUpdateTime);
            return vfsMap;
        }
    }

    public SkillCommandHandler() {
        // 初始化线程池
        this.commandExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy() // 队列满时的处理策略
        );
    }

    @Override
    public void onCommand(CommandPacket packet) {
        log.info("Received command: {}", packet.getCommand());

        if (packet.getCommand() == null) {
            log.warn("Null command received");
            return;
        }

        // 异步处理命令
        commandExecutor.submit(() -> {
            long startTime = System.currentTimeMillis();
            boolean success = false;
            CommandType commandType = null;
            String commandName = "unknown";
            try {
                String commandStr = packet.getCommand();
                if (commandStr != null) {
                    commandType = CommandType.valueOf(commandStr);
                    commandName = commandType.getValue();
                }
            } catch (IllegalArgumentException e) {
                log.warn("Invalid command type: {}", packet.getCommand(), e);
            }

            try {
                // 1. 命令验证阶段
                if (!validateCommand(packet)) {
                    log.warn("Command validation failed: {}", commandType);
                    return;
                }

                // 2. 命令执行阶段
                executeCommand(packet);
                success = true;
            } catch (Exception e) {
                log.error("Error handling command: {}", commandType, e);
                handleCommandError(packet, e);
            } finally {
                long executionTime = System.currentTimeMillis() - startTime;
                log.info("Command processed: {}, Success: {}, Execution time: {}ms", 
                        commandName, success, executionTime);
            }
        });
    }

    /**
     * 命令验证阶段
     */
    private boolean validateCommand(CommandPacket packet) {
        log.debug("Validating command: {}", packet.getCommand());
        
        // 1. 命令类型验证
        if (packet.getCommand() == null) {
            log.error("Command type is null");
            return false;
        }
        
        // 2. 命令类型有效性验证
        try {
            CommandType.valueOf(packet.getCommand());
        } catch (IllegalArgumentException e) {
            log.error("Invalid command type: {}", packet.getCommand(), e);
            return false;
        }

        // 2. 元数据验证
        if (packet.getMetadata() == null) {
            log.warn("Command metadata is null");
            // 元数据非必填，继续执行
        }

        // 3. 参数基础验证
        if (packet.getParams() == null) {
            // 如果命令需要参数，会在具体的命令处理方法中验证
            packet.setParams(Collections.emptyMap());
        }

        log.debug("Command validation passed: {}", packet.getCommand());
        return true;
    }

    /**
     * 命令执行阶段
     */
    private void executeCommand(CommandPacket packet) {
        log.debug("Executing command: {}", packet.getCommand());

        // 获取命令类型
        CommandType commandType = null;
        try {
            String commandStr = packet.getCommand();
            if (commandStr != null) {
                commandType = CommandType.valueOf(commandStr);
            }
        } catch (IllegalArgumentException e) {
            log.warn("Invalid command type: {}", packet.getCommand(), e);
        }
        if (commandType == null) {
            log.warn("Null command type received");
            return;
        }
        
        // 使用CommandType枚举比较
        if (commandType == CommandType.ROUTE_FORWARD) {
            handleRouteForward(packet);
        } else if (commandType == CommandType.ROUTE_STATUS) {
            handleRouteStatus(packet);
        } else if (commandType == CommandType.ROUTE_ADD) {
            handleRouteAdd(packet);
        } else if (commandType == CommandType.ROUTE_REMOVE) {
            handleRouteRemove(packet);
        } else if (commandType == CommandType.ROUTE_LIST) {
            handleRouteList(packet);
        } else if (commandType == CommandType.ROUTE_CONFIGURE) {
            handleRouteConfigure(packet);
        } else if (commandType == CommandType.VFS_REGISTER) {
            handleVfsRegister(packet);
        } else if (commandType == CommandType.VFS_STATUS) {
            handleVfsStatus(packet);
        } else {
            log.warn("Unknown command: {}", commandType);
        }
    }

    @ValidParam(name = "route", type = String.class, minLength = 1, maxLength = 1000, message = "Route parameter is required and should be a non-empty string")
    private void handleRouteForward(CommandPacket packet) {
        log.info("Handling route.forward command");
        log.info("Params: {}", packet.getParams());

        try {
            // 使用参数验证框架验证参数
            Method method = this.getClass().getDeclaredMethod("handleRouteForward", CommandPacket.class);
            method.setAccessible(true);
            if (!ParamValidator.validate(getParamsAsMap(packet), method)) {
                return;
            }

            String route = (String) getParamsAsMap(packet).get("route");
            String result = forwardRoute(route);
            log.info("Route forward result: {}", result);
        } catch (NoSuchMethodException e) {
            log.error("Method not found for validation", e);
        } catch (Exception e) {
            log.error("Unexpected error handling route.forward command", e);
            throw e; // 重新抛出异常以便统一处理
        }
    }

    @ValidParam(name = "routeId", type = String.class, required = false, pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "RouteId parameter should be a valid UUID")
    private void handleRouteStatus(CommandPacket packet) {
        log.info("Handling route.status command");
        log.info("Params: {}", packet.getParams());

        try {
            // 使用参数验证框架验证参数
            Method method = this.getClass().getDeclaredMethod("handleRouteStatus", CommandPacket.class);
            method.setAccessible(true);
            if (!ParamValidator.validate(getParamsAsMap(packet), method)) {
                return;
            }

            String routeId = (String) getParamsAsMap(packet).get("routeId");

            // 获取路由状态
            Map<String, Object> status = getRouteStatus(routeId);
            log.info("Route status result: {}", status);
        } catch (NoSuchMethodException e) {
            log.error("Method not found for validation", e);
        } catch (Exception e) {
            log.error("Unexpected error handling route.status command", e);
            throw e;
        }
    }

    @ValidParam(name = "route", type = String.class, minLength = 1, maxLength = 1000, message = "Route parameter is required and should be a non-empty string")
    @ValidParam(name = "priority", type = Integer.class, min = 1, max = 100, message = "Priority parameter is required and should be between 1 and 100")
    @ValidParam(name = "description", type = String.class, required = false, maxLength = 500, message = "Description parameter should be a string with maximum length of 500")
    @ValidParam(name = "attributes", type = Map.class, required = false, message = "Attributes parameter should be a map")
    private void handleRouteAdd(CommandPacket packet) {
        log.info("Handling route.add command");
        log.info("Params: {}", packet.getParams());

        try {
            // 使用参数验证框架验证参数
            Method method = this.getClass().getDeclaredMethod("handleRouteAdd", CommandPacket.class);
            method.setAccessible(true);
            if (!ParamValidator.validate(getParamsAsMap(packet), method)) {
                return;
            }

            Map<String, Object> params = getParamsAsMap(packet);
            String route = (String) params.get("route");
            Integer priority = (Integer) params.get("priority");

            // 获取可选参数
            String description = params.containsKey("description") ? (String) params.get("description") : null;
            Map<String, Object> attributes = params.containsKey("attributes") ? (Map<String, Object>) params.get("attributes") : new HashMap<>();

            String routeId = addRoute(route, priority, description, attributes);
            log.info("Route add result: routeId={}", routeId);
        } catch (NoSuchMethodException e) {
            log.error("Method not found for validation", e);
        } catch (Exception e) {
            log.error("Unexpected error handling route.add command", e);
            throw e;
        }
    }

    @ValidParam(name = "routeId", type = String.class, pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "RouteId parameter is required and should be a valid UUID")
    private void handleRouteRemove(CommandPacket packet) {
        log.info("Handling route.remove command");
        log.info("Params: {}", packet.getParams());

        try {
            // 使用参数验证框架验证参数
            Method method = this.getClass().getDeclaredMethod("handleRouteRemove", CommandPacket.class);
            method.setAccessible(true);
            if (!ParamValidator.validate(getParamsAsMap(packet), method)) {
                return;
            }

            String routeId = (String) getParamsAsMap(packet).get("routeId");
            boolean removed = removeRoute(routeId);
            log.info("Route remove result: {}", removed ? "success" : "failure");
        } catch (NoSuchMethodException e) {
            log.error("Method not found for validation", e);
        } catch (Exception e) {
            log.error("Unexpected error handling route.remove command", e);
            throw e;
        }
    }

    @ValidParam(name = "page", type = Integer.class, required = false, min = 1, message = "Page parameter should be at least 1")
    @ValidParam(name = "pageSize", type = Integer.class, required = false, min = 1, max = 100, message = "PageSize parameter should be between 1 and 100")
    private void handleRouteList(CommandPacket packet) {
        log.info("Handling route.list command");
        log.info("Params: {}", packet.getParams());

        try {
            // 使用参数验证框架验证参数
            Method method = this.getClass().getDeclaredMethod("handleRouteList", CommandPacket.class);
            method.setAccessible(true);
            if (!ParamValidator.validate(getParamsAsMap(packet), method)) {
                return;
            }

            // 获取可选参数，设置默认值
            Map<String, Object> params = getParamsAsMap(packet);
            Integer page = params.containsKey("page") ? (Integer) params.get("page") : 1;
            Integer pageSize = params.containsKey("pageSize") ? (Integer) params.get("pageSize") : 20;

            // 获取路由列表
            List<String> routes = listRoutes(page, pageSize);
            log.info("Route list result: total={}, page={}, pageSize={}", routes.size(), page, pageSize);
        } catch (NoSuchMethodException e) {
            log.error("Method not found for validation", e);
        } catch (Exception e) {
            log.error("Unexpected error handling route.list command", e);
            throw e;
        }
    }

    @ValidParam(name = "routeId", type = String.class, pattern = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "RouteId parameter is required and should be a valid UUID")
    @ValidParam(name = "config", type = Map.class, message = "Config parameter is required and should be a map")
    private void handleRouteConfigure(CommandPacket packet) {
        log.info("Handling route.configure command");
        log.info("Params: {}", packet.getParams());

        try {
            // 使用参数验证框架验证参数
            Method method = this.getClass().getDeclaredMethod("handleRouteConfigure", CommandPacket.class);
            method.setAccessible(true);
            if (!ParamValidator.validate(getParamsAsMap(packet), method)) {
                return;
            }

            // 获取配置参数
            Map<String, Object> params = getParamsAsMap(packet);
            String routeId = (String) params.get("routeId");
            Map<String, Object> config = (Map<String, Object>) params.get("config");

            boolean configured = configureRoute(routeId, config);
            log.info("Route configure result: {}", configured ? "success" : "failure");
        } catch (NoSuchMethodException e) {
            log.error("Method not found for validation", e);
        } catch (Exception e) {
            log.error("Unexpected error handling route.configure command", e);
            throw e;
        }
    }

    private String forwardRoute(String route) {
        // 模拟路由转发处理
        try {
            // 模拟处理延迟
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Route forward interrupted");
        }
        return "Forwarded route: " + route;
    }

    /**
     * 添加路由
     */
    private String addRoute(String route, int priority, String description, Map<String, Object> attributes) {
        // 模拟添加路由
        try {
            // 模拟处理延迟
            Thread.sleep(150);
            // 生成唯一路由ID
            return UUID.randomUUID().toString();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Route add interrupted");
            throw new RuntimeException("Route add interrupted", e);
        }
    }

    /**
     * 移除路由
     */
    private boolean removeRoute(String routeId) {
        // 模拟移除路由
        try {
            // 模拟处理延迟
            Thread.sleep(100);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Route remove interrupted");
            throw new RuntimeException("Route remove interrupted", e);
        }
    }

    /**
     * 列出路由
     */
    private List<String> listRoutes(int page, int pageSize) {
        // 模拟获取路由列表
        try {
            // 模拟处理延迟
            Thread.sleep(200);
            // 返回模拟的路由列表
            List<String> routes = new ArrayList<>();
            for (int i = 0; i < pageSize; i++) {
                int index = (page - 1) * pageSize + i + 1;
                routes.add("route-" + index);
            }
            return routes;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Route list interrupted");
            throw new RuntimeException("Route list interrupted", e);
        }
    }

    /**
     * 获取路由状态
     */
    private Map<String, Object> getRouteStatus(String routeId) {
        // 模拟获取路由状态
        try {
            // 模拟处理延迟
            Thread.sleep(100);
            // 返回模拟的路由状态
            Map<String, Object> status = new HashMap<>();
            if (routeId != null) {
                status.put("routeId", routeId);
                status.put("status", "active");
                status.put("lastUpdated", new Date());
                status.put("packetCount", 12345);
                status.put("errorCount", 0);
            } else {
                // 如果没有指定routeId，返回总体路由统计
                status.put("totalRoutes", 100);
                status.put("activeRoutes", 85);
                status.put("inactiveRoutes", 15);
                status.put("totalPacketCount", 1234567);
            }
            return status;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Route status interrupted");
            throw new RuntimeException("Route status interrupted", e);
        }
    }

    /**
     * 配置路由
     */
    private boolean configureRoute(String routeId, Map<String, Object> config) {
        // 模拟配置路由
        try {
            // 模拟处理延迟
            Thread.sleep(200);
            // 验证配置参数
            if (config.containsKey("priority")) {
                Integer priority = (Integer) config.get("priority");
                if (priority < 0 || priority > 100) {
                    log.warn("Invalid priority value: {}. Must be between 0 and 100.", priority);
                    return false;
                }
            }
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Route configure interrupted");
            throw new RuntimeException("Route configure interrupted", e);
        } catch (ClassCastException e) {
            log.error("Invalid configuration parameter type", e);
            return false;
        }
    }
    
    // -------------------- VFS命令处理 --------------------
    
    /**
     * 处理VFS注册命令
     */
    private void handleVfsRegister(CommandPacket packet) {
        log.info("Handling vfs.register command");
        log.info("Params: {}", packet.getParams());
        
        try {
            // 验证VFS注册参数
            Map<String, Object> params = getParamsAsMap(packet);
            String vfsId = (String) params.get("vfs_id");
            String vfsUrl = (String) params.get("vfs_url");
            String groupName = (String) params.get("group_name");
            List<String> capabilities = (List<String>) params.get("capabilities");
            
            if (vfsId == null || vfsUrl == null || groupName == null) {
                log.warn("VFS registration failed: missing required parameters");
                return;
            }
            
            // 创建或更新VFS信息
            VfsInfo vfsInfo = new VfsInfo(vfsId, vfsUrl, groupName, capabilities != null ? capabilities : new ArrayList<>());
            availableVfsServices.put(vfsId, vfsInfo);
            
            log.info("VFS registered successfully: vfsId={}, vfsUrl={}, groupName={}", vfsId, vfsUrl, groupName);
            
            // 当新的VFS服务注册时，向所有endAgent广播VFS可用性
            broadcastVfsAvailability(vfsInfo);
            
        } catch (Exception e) {
            log.error("Error handling vfs.register command", e);
            handleCommandError(packet, e);
        }
    }
    
    /**
     * 处理VFS状态更新命令
     */
    private void handleVfsStatus(CommandPacket packet) {
        log.info("Handling vfs.status command");
        log.info("Params: {}", packet.getParams());
        
        try {
            // 验证VFS状态参数
            Map<String, Object> params = getParamsAsMap(packet);
            String vfsId = (String) params.get("vfs_id");
            String status = (String) params.get("status");
            
            if (vfsId == null || status == null) {
                log.warn("VFS status update failed: missing required parameters");
                return;
            }
            
            // 更新VFS状态
            VfsInfo vfsInfo = availableVfsServices.get(vfsId);
            if (vfsInfo != null) {
                vfsInfo.updateStatus(status);
                log.info("VFS status updated: vfsId={}, status={}", vfsId, status);
                
                // 如果VFS状态变为available，向所有endAgent广播VFS可用性
                if ("available".equals(status)) {
                    broadcastVfsAvailability(vfsInfo);
                }
            } else {
                log.warn("VFS not found: {}", vfsId);
            }
            
        } catch (Exception e) {
            log.error("Error handling vfs.status command", e);
            handleCommandError(packet, e);
        }
    }
    
    /**
     * 向所有endAgent广播VFS可用性
     */
    private void broadcastVfsAvailability(VfsInfo vfsInfo) {
        log.info("Broadcasting VFS availability to all end agents: vfsId={}", vfsInfo.getVfsId());
        
        // 模拟广播消息给所有endAgent
        Map<String, Object> params = new HashMap<>();
        params.put("action", "vfs-sync");
        params.put("vfs_id", vfsInfo.getVfsId());
        params.put("vfs_url", vfsInfo.getVfsUrl());
        params.put("group_name", vfsInfo.getGroupName());
        params.put("sync_type", "both");
        params.put("capabilities", vfsInfo.getCapabilities());
        
        // 向所有已知的endAgent发送消息
        for (String endAgentId : endAgents) {
            try {
                // 模拟发送消息给endAgent
                log.info("Sending VFS sync command to end agent: {}", endAgentId);
                
                // 在实际实现中，这里应该使用agentSDK向指定的endAgent发送命令
                // agentSDK.sendCommandToAgent(endAgentId, CommandType.ROUTE_FORWARD, params);
                
            } catch (Exception e) {
                log.error("Error broadcasting VFS availability to end agent: {}", endAgentId, e);
            }
        }
        
        // 同时，向新注册的endAgent也发送VFS可用性信息
        // 在实际实现中，endAgent列表应该来自于agent发现机制
        log.info("VFS availability broadcast completed");
    }
    
    /**
     * 获取当前可用的VFS服务列表
     */
    private List<Map<String, Object>> getAvailableVfsServices() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (VfsInfo vfsInfo : availableVfsServices.values()) {
            if ("available".equals(vfsInfo.status)) {
                result.add(vfsInfo.toMap());
            }
        }
        return result;
    }

    /**
     * 统一处理命令执行错误
     */
    private void handleCommandError(CommandPacket packet, Exception e) {
        try {
            // 1. 记录详细错误日志
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
            log.error("Command execution error - Command: {}, Error: {}, StackTrace: {}", 
                    packet.getCommand(), errorMessage, ExceptionUtils.getStackTrace(e));

            // 2. 构建错误响应数据包
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("commandId", packet.getMetadata() != null ? packet.getMetadata().getCommandId() : null);
            errorData.put("commandType", packet.getCommand());
            errorData.put("errorCode", getErrorCode(e));
            errorData.put("errorMessage", errorMessage);
            errorData.put("timestamp", System.currentTimeMillis());

            // 3. 发送错误报告
            sendErrorReport(errorData);

            // 4. 根据错误类型决定是否重试
            if (shouldRetry(e)) {
                retryCommand(packet, e);
            }
        } catch (Exception ex) {
            log.error("Error handling command error: {}", ex.getMessage(), ex);
        }
    }

    /**
     * 获取错误代码
     */
    private String getErrorCode(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return "INVALID_PARAMETER";
        } else if (e instanceof InterruptedException) {
            return "OPERATION_INTERRUPTED";
        } else if (e instanceof TimeoutException) {
            return "OPERATION_TIMEOUT";
        } else if (e instanceof ClassCastException) {
            return "TYPE_MISMATCH";
        } else {
            return "INTERNAL_ERROR";
        }
    }

    /**
     * 判断是否应该重试
     */
    private boolean shouldRetry(Exception e) {
        // 只对特定类型的异常进行重试
        return e instanceof IOException || e instanceof SocketException;
    }

    /**
     * 重试命令
     */
    private void retryCommand(CommandPacket packet, Exception e) {
        // 模拟重试逻辑
        log.info("Scheduling retry for command: {}", packet.getCommand());
        // 实际实现中应该使用指数退避等策略
    }

    /**
     * 发送错误报告
     */
    private void sendErrorReport(Map<String, Object> errorData) {
        // 模拟发送错误报告
        log.info("Sending error report: {}", errorData);
        // 实际实现中应该发送到监控系统或管理平台
    }

    @Override
    public void onHeartbeat(net.ooder.sdk.network.packet.HeartbeatPacket packet) {
        log.debug("Received heartbeat from: {}", packet.getAgentId());
    }

    @Override
    public void onStatusReport(net.ooder.sdk.network.packet.StatusReportPacket packet) {
        log.info("Received status report: {}", packet.getReportType());
    }

    @Override
    public void onAuth(net.ooder.sdk.network.packet.AuthPacket packet) {
        log.info("Received auth packet: {}", packet);
    }

    @Override
    public void onTask(net.ooder.sdk.network.packet.TaskPacket packet) {
        log.info("Received task packet: {}", packet);
    }

    @Override
    public void onRoute(net.ooder.sdk.network.packet.RoutePacket packet) {
        log.info("Received route packet: {}", packet);
    }

    @Override
    public void onError(net.ooder.sdk.network.packet.UDPPacket packet, Exception e) {
        log.error("UDP error", e);
    }

    // Helper method to safely get params as Map
    private java.util.Map<String, Object> getParamsAsMap(CommandPacket packet) {
        Object paramsObj = packet.getParams();
        if (paramsObj instanceof java.util.Map) {
            return (java.util.Map<String, Object>) paramsObj;
        }
        return new java.util.HashMap<>();
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        log.info("Shutting down command executor");
        commandExecutor.shutdown();
        try {
            if (!commandExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("Command executor did not terminate gracefully, forcing shutdown");
                commandExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            commandExecutor.shutdownNow();
        }
    }
}
