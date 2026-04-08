package net.ooder.skill.config.service;

import net.ooder.skill.config.dto.CliCallLogDTO;
import net.ooder.skill.config.dto.CliConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class CliConfigService {

    private static final Logger log = LoggerFactory.getLogger(CliConfigService.class);

    private final Map<String, CliConfigDTO> cliConfigs = new ConcurrentHashMap<>();
    private final List<CliCallLogDTO> callLogs = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_LOGS = 1000;

    public CliConfigService() {
        initDefaultConfigs();
    }

    private void initDefaultConfigs() {
        CliConfigDTO dingding = new CliConfigDTO();
        dingding.setCliId("dingding");
        dingding.setName("钉钉");
        dingding.setType("im");
        dingding.setEnabled(false);
        dingding.setIcon("ri-message-2-line");
        dingding.setDescription("钉钉企业通讯工具集成");
        Map<String, Object> ddSettings = new HashMap<>();
        ddSettings.put("appKey", "");
        ddSettings.put("appSecret", "");
        ddSettings.put("corpId", "");
        ddSettings.put("agentId", "");
        dingding.setSettings(ddSettings);
        cliConfigs.put("dingding", dingding);

        CliConfigDTO wecom = new CliConfigDTO();
        wecom.setCliId("wecom");
        wecom.setName("企业微信");
        wecom.setType("im");
        wecom.setEnabled(false);
        wecom.setIcon("ri-wechat-line");
        wecom.setDescription("企业微信通讯工具集成");
        Map<String, Object> wcSettings = new HashMap<>();
        wcSettings.put("corpId", "");
        wcSettings.put("agentId", "");
        wcSettings.put("secret", "");
        wecom.setSettings(wcSettings);
        cliConfigs.put("wecom", wecom);

        CliConfigDTO feishu = new CliConfigDTO();
        feishu.setCliId("feishu");
        feishu.setName("飞书");
        feishu.setType("im");
        feishu.setEnabled(false);
        feishu.setIcon("ri-message-3-line");
        feishu.setDescription("飞书企业通讯工具集成");
        Map<String, Object> fsSettings = new HashMap<>();
        fsSettings.put("appId", "");
        fsSettings.put("appSecret", "");
        feishu.setSettings(fsSettings);
        cliConfigs.put("feishu", feishu);
    }

    public List<CliConfigDTO> getAllCliConfigs() {
        return new ArrayList<>(cliConfigs.values());
    }

    public CliConfigDTO getCliConfig(String cliId) {
        return cliConfigs.get(cliId);
    }

    public CliConfigDTO updateCliConfig(String cliId, CliConfigDTO config) {
        config.setCliId(cliId);
        config.setLastSyncTime(System.currentTimeMillis());
        cliConfigs.put(cliId, config);
        log.info("[CliConfigService] Updated CLI config: {}", cliId);
        return config;
    }

    public void addCallLog(CliCallLogDTO logEntry) {
        if (callLogs.size() >= MAX_LOGS) {
            callLogs.remove(0);
        }
        callLogs.add(logEntry);
    }

    public List<CliCallLogDTO> getCliLogs(String cliId, String action, int limit) {
        return callLogs.stream()
                .filter(log -> cliId == null || cliId.equals(log.getCliId()))
                .filter(log -> action == null || action.isEmpty() || action.equals(log.getAction()))
                .sorted((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public CliCallLogDTO getCliLogDetail(String logId) {
        return callLogs.stream()
                .filter(log -> logId.equals(log.getLogId()))
                .findFirst()
                .orElse(null);
    }

    public void enableCli(String cliId) {
        CliConfigDTO config = cliConfigs.get(cliId);
        if (config != null) {
            config.setEnabled(true);
            config.setStatus("active");
            log.info("[CliConfigService] Enabled CLI: {}", cliId);
        }
    }

    public void disableCli(String cliId) {
        CliConfigDTO config = cliConfigs.get(cliId);
        if (config != null) {
            config.setEnabled(false);
            config.setStatus("inactive");
            log.info("[CliConfigService] Disabled CLI: {}", cliId);
        }
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalClis", cliConfigs.size());
        stats.put("enabledClis", cliConfigs.values().stream().filter(CliConfigDTO::isEnabled).count());
        stats.put("totalCalls", callLogs.size());
        stats.put("successCalls", callLogs.stream().filter(CliCallLogDTO::isSuccess).count());
        stats.put("failedCalls", callLogs.stream().filter(log -> !log.isSuccess()).count());
        return stats;
    }
}
