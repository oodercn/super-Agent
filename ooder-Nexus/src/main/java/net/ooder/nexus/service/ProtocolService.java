package net.ooder.nexus.service;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.domain.mcp.model.ProtocolHandlerData;

import java.util.List;
import java.util.Map;

/**
 * 协议管理服务接口
 * 提供协议处理器的注册、移除、查询和处理等功能
 */
public interface ProtocolService {

    /**
     * 获取协议处理器列表
     */
    Result<List<ProtocolHandlerData>> getProtocolHandlers(Map<String, Object> params);

    /**
     * 注册协议处理器
     */
    Result<ProtocolHandlerData> registerProtocolHandler(Map<String, Object> handlerData);

    /**
     * 移除协议处理器
     */
    Result<ProtocolHandlerData> removeProtocolHandler(String commandType);

    /**
     * 处理协议命令
     */
    Result<ProtocolHandlerData> handleProtocolCommand(Map<String, Object> commandData);

    /**
     * 刷新协议处理器
     */
    Result<List<ProtocolHandlerData>> refreshProtocolHandlers();

    /**
     * 搜索协议处理器
     */
    Result<List<ProtocolHandlerData>> searchProtocolHandlers(Map<String, Object> params);
}
