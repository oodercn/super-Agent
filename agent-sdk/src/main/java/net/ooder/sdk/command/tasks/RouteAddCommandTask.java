package net.ooder.sdk.command.tasks;

import net.ooder.sdk.AgentSDK;
import net.ooder.sdk.command.api.CommandAnnotation;
import net.ooder.sdk.command.impl.AbstractCommandTask;
import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.params.RouteAddParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@CommandAnnotation(
        id = "route_add",
        name = "Route Add Command",
        desc = "Add a route to the Route Agent",
        commandType = CommandType.ROUTE_ADD,
        key = "route.add"
)
public class RouteAddCommandTask extends AbstractCommandTask<RouteAddParams> {
    private static final Logger log = LoggerFactory.getLogger(RouteAddCommandTask.class);

    public RouteAddCommandTask() {
        super(CommandType.ROUTE_ADD);
    }

    @Override
    protected CommandResult doExecute(CommandPacket<RouteAddParams> packet) throws Exception {
        log.info("Starting ROUTE_ADD command execution");
        log.info("Packet payload: {}", packet.getPayload());
        log.info("Packet params: {}", packet.getParams());
        
        Object payload = packet.getPayload();
        String routeId = null;
        String source = null;
        String destination = null;
        Map<String, Object> routeInfo = new HashMap<>();
        
        try {
            log.info("Processing payload of type: {}", payload != null ? payload.getClass().getName() : "null");
            
            // 从参数中获取路由信息
            if (payload instanceof RouteAddParams) {
                // 处理特定参数类的情况
                RouteAddParams params = (RouteAddParams) payload;
                routeId = params.getRouteId();
                source = params.getSource();
                destination = params.getDestination();
                routeInfo = params.getRouteInfo() != null ? params.getRouteInfo() : new HashMap<>();
                log.info("Parsed RouteAddParams: routeId={}, source={}, destination={}", routeId, source, destination);
            } else if (payload instanceof Map) {
                // 处理Map参数的情况（向后兼容）
                Map<String, Object> params = (Map<String, Object>) payload;
                routeId = (String) params.get("routeId");
                source = (String) params.get("source");
                destination = (String) params.get("destination");
                routeInfo = (Map<String, Object>) params.getOrDefault("routeInfo", new HashMap<>());
                log.info("Parsed Map params: routeId={}, source={}, destination={}", routeId, source, destination);
            } else {
                String errorMessage = "Invalid payload type: " + (payload != null ? payload.getClass().getName() : "null");
                log.error(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
            
            if (routeId == null) {
                log.error("Route ID is null");
                throw new IllegalArgumentException("Route parameters cannot be null");
            }
            
            // 参数验证
            if (routeId.trim().isEmpty()) {
                log.error("Route ID is empty");
                throw new IllegalArgumentException("Route ID cannot be empty");
            }
            if (source == null || source.trim().isEmpty()) {
                log.error("Source is null or empty");
                throw new IllegalArgumentException("Source cannot be empty");
            }
            if (destination == null || destination.trim().isEmpty()) {
                log.error("Destination is null or empty");
                throw new IllegalArgumentException("Destination cannot be empty");
            }
            
            // 检查路由是否已存在
            log.info("Checking if route exists: {}", routeId);
            AgentSDK.RouteInfo existingRoute = getRoute(routeId);
            log.info("Existing route found: {}", existingRoute != null);
            
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("routeId", routeId);
            resultData.put("source", source);
            resultData.put("destination", destination);
            resultData.put("timestamp", System.currentTimeMillis());
            
            if (existingRoute != null) {
                // 更新现有路由
                log.info("Updating existing route: {}", routeId);
                updateRoute(routeId, routeInfo);
                
                resultData.put("status", "updated");
                resultData.put("message", "Route updated successfully: " + routeId);
                
                log.info("Route updated: {} from {} to {}", routeId, source, destination);
                
                return CommandResult.success(resultData, "Route updated successfully");
                
            } else {
                // 添加新路由
                log.info("Adding new route: {} from {} to {}", routeId, source, destination);
                addRoute(routeId, source, destination, routeInfo);
                
                // 验证路由是否添加成功
                AgentSDK.RouteInfo newRoute = getRoute(routeId);
                log.info("New route added successfully: {}", newRoute != null);
                if (newRoute != null) {
                    log.info("New route details: source={}, destination={}, routeInfo={}", newRoute.getSource(), newRoute.getDestination(), newRoute.getRouteInfo());
                }
                
                resultData.put("routeInfo", routeInfo);
                resultData.put("status", "success");
                resultData.put("message", "Route added successfully: " + routeId);
                
                log.info("Route added: {} from {} to {}", routeId, source, destination);
                
                return CommandResult.success(resultData, "Route added successfully");
            }
            
        } catch (IllegalArgumentException e) {
                log.error("Invalid route parameters: {}", e.getMessage(), e);
                
                // 构建参数错误结果
                Map<String, Object> resultData = new HashMap<>();
                if (payload != null) {
                    if (payload instanceof RouteAddParams) {
                        resultData.put("routeId", ((RouteAddParams) payload).getRouteId());
                    } else if (payload instanceof Map) {
                        resultData.put("routeId", ((Map<String, Object>) payload).get("routeId"));
                    }
                }
                resultData.put("error", e.getMessage());
                resultData.put("timestamp", System.currentTimeMillis());
                
                return CommandResult.parameterError(e.getMessage(), resultData);
            
        } catch (Exception e) {
            log.error("Failed to add route: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> resultData = new HashMap<>();
            if (payload != null) {
                if (payload instanceof RouteAddParams) {
                    resultData.put("routeId", ((RouteAddParams) payload).getRouteId());
                } else if (payload instanceof Map) {
                    resultData.put("routeId", ((Map<String, Object>) payload).get("routeId"));
                }
            }
            resultData.put("error", e.getMessage());
            resultData.put("timestamp", System.currentTimeMillis());
            
            return CommandResult.executionError(e.getMessage(), resultData);
        }
    }
    
    /**
     * 获取路由
     */
    private AgentSDK.RouteInfo getRoute(String routeId) {
        return agentSDK.getRoute(routeId);
    }
    
    /**
     * 更新路由
     */
    private void updateRoute(String routeId, Map<String, Object> routeInfo) {
        agentSDK.updateRoute(routeId, routeInfo);
    }
    
    /**
     * 添加路由
     */
    private void addRoute(String routeId, String source, String destination, Map<String, Object> routeInfo) {
        agentSDK.addRoute(routeId, source, destination, routeInfo);
    }
}