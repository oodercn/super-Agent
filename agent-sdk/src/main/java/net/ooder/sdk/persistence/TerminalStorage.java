package net.ooder.sdk.persistence;

import net.ooder.sdk.terminal.model.TerminalDevice;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface TerminalStorage {
    // 存储终端设备
    CompletableFuture<Boolean> saveTerminal(TerminalDevice device);
    CompletableFuture<Boolean> saveTerminals(List<TerminalDevice> devices);
    
    // 加载终端设备
    CompletableFuture<TerminalDevice> loadTerminal(String terminalId);
    CompletableFuture<List<TerminalDevice>> loadAllTerminals();
    CompletableFuture<List<TerminalDevice>> loadTerminalsByType(String deviceType);
    CompletableFuture<List<TerminalDevice>> loadTerminalsByStatus(String status);
    
    // 删除终端设备
    CompletableFuture<Boolean> deleteTerminal(String terminalId);
    CompletableFuture<Boolean> deleteTerminals(List<String> terminalIds);
    CompletableFuture<Boolean> deleteAllTerminals();
    
    // 检查终端设备
    CompletableFuture<Boolean> existsTerminal(String terminalId);
    CompletableFuture<Long> countTerminals();
    CompletableFuture<Long> countTerminalsByType(String deviceType);
    CompletableFuture<Long> countTerminalsByStatus(String status);
    
    // 终端设备查询
    CompletableFuture<List<TerminalDevice>> searchTerminals(Map<String, Object> criteria);
    CompletableFuture<List<TerminalDevice>> findTerminalsByAttribute(String attributeName, Object attributeValue);
    
    // 终端设备统计
    CompletableFuture<Map<String, Long>> getTerminalStatusCounts();
    CompletableFuture<Map<String, Long>> getTerminalTypeCounts();
    CompletableFuture<Map<String, Object>> getTerminalStatistics();
    
    // 终端设备更新
    CompletableFuture<Boolean> updateTerminalStatus(String terminalId, String status);
    CompletableFuture<Boolean> updateTerminalMetadata(String terminalId, Map<String, Object> metadata);
    CompletableFuture<Boolean> updateTerminalAttributes(String terminalId, Map<String, Object> attributes);
}
