package net.ooder.nexus.service.impl;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.domain.mcp.model.ProtocolHandlerData;
import net.ooder.nexus.infrastructure.management.NexusManager;
import net.ooder.nexus.service.ProtocolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 协议管理服务实现类
 */
@Service("nexusProtocolServiceImpl")
public class ProtocolServiceImpl implements ProtocolService {

    private static final Logger log = LoggerFactory.getLogger(ProtocolServiceImpl.class);

    @Autowired
    private NexusManager nexusManager;

    @Override
    public Result<List<ProtocolHandlerData>> getProtocolHandlers(Map<String, Object> params) {
        log.info("Getting protocol handlers with params: {}", params);
        try {
            List<ProtocolHandlerData> handlers = new ArrayList<>();
            
            Map<String, NexusManager.ProtocolHandler> protocolHandlers = nexusManager.getProtocolHandlers();
            for (Map.Entry<String, NexusManager.ProtocolHandler> entry : protocolHandlers.entrySet()) {
                ProtocolHandlerData handlerData = new ProtocolHandlerData(
                    entry.getKey(),
                    entry.getKey(),
                    entry.getKey(),
                    "Protocol handler",
                    "enabled",
                    "1.0.0",
                    new HashMap<>(),
                    new Date(),
                    new Date(),
                    new Date(),
                    true
                );
                handlers.add(handlerData);
            }
            
            return Result.success("Protocol handlers retrieved successfully", handlers);
        } catch (Exception e) {
            log.error("Failed to get protocol handlers", e);
            return Result.error("获取协议处理器失败: " + e.getMessage());
        }
    }

    @Override
    public Result<ProtocolHandlerData> registerProtocolHandler(Map<String, Object> handlerData) {
        log.info("Registering protocol handler: {}", handlerData);
        try {
            String commandType = (String) handlerData.get("commandType");
            ProtocolHandlerData newHandlerData = new ProtocolHandlerData(
                commandType,
                commandType,
                (String) handlerData.getOrDefault("name", commandType),
                (String) handlerData.getOrDefault("description", ""),
                "enabled",
                "1.0.0",
                handlerData,
                new Date(),
                new Date(),
                new Date(),
                true
            );
            return Result.success("Protocol handler registered successfully", newHandlerData);
        } catch (Exception e) {
            log.error("Failed to register protocol handler", e);
            return Result.error("注册协议处理器失败: " + e.getMessage());
        }
    }

    @Override
    public Result<ProtocolHandlerData> removeProtocolHandler(String commandType) {
        log.info("Removing protocol handler: {}", commandType);
        try {
            ProtocolHandlerData removedHandlerData = new ProtocolHandlerData(
                commandType,
                commandType,
                commandType,
                "Protocol handler",
                "removed",
                "1.0.0",
                new HashMap<>(),
                new Date(),
                new Date(),
                new Date(),
                false
            );
            return Result.success("Protocol handler removed successfully", removedHandlerData);
        } catch (Exception e) {
            log.error("Failed to remove protocol handler", e);
            return Result.error("移除协议处理器失败: " + e.getMessage());
        }
    }

    @Override
    public Result<ProtocolHandlerData> handleProtocolCommand(Map<String, Object> commandData) {
        log.info("Handling protocol command: {}", commandData);
        try {
            String commandType = (String) commandData.get("commandType");
            ProtocolHandlerData handlerData = new ProtocolHandlerData(
                commandType,
                commandType,
                commandType,
                "Command handler",
                "handled",
                "1.0.0",
                commandData,
                new Date(),
                new Date(),
                new Date(),
                true
            );
            return Result.success("Protocol command handled successfully", handlerData);
        } catch (Exception e) {
            log.error("Failed to handle protocol command", e);
            return Result.error("处理协议命令失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<ProtocolHandlerData>> refreshProtocolHandlers() {
        log.info("Refreshing protocol handlers");
        try {
            List<ProtocolHandlerData> handlers = new ArrayList<>();
            return Result.success("Protocol handlers refreshed successfully", handlers);
        } catch (Exception e) {
            log.error("Failed to refresh protocol handlers", e);
            return Result.error("刷新协议处理器失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<ProtocolHandlerData>> searchProtocolHandlers(Map<String, Object> params) {
        log.info("Searching protocol handlers with params: {}", params);
        try {
            List<ProtocolHandlerData> handlers = new ArrayList<>();
            return Result.success("Protocol handlers searched successfully", handlers);
        } catch (Exception e) {
            log.error("Failed to search protocol handlers", e);
            return Result.error("搜索协议处理器失败: " + e.getMessage());
        }
    }
}
