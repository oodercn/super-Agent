package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.history.HistoryDTO;
import net.ooder.mvp.skill.scene.dto.history.HistoryStatisticsDTO;
import net.ooder.mvp.skill.scene.event.SceneGroupEventLogService;
import net.ooder.mvp.skill.scene.service.HistoryService;
import net.ooder.scene.group.SceneGroup;
import net.ooder.scene.group.SceneGroupManager;
import net.ooder.scene.group.SceneGroupEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Primary
public class HistoryServiceImpl implements HistoryService {

    private static final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);
    
    private final SceneGroupManager sceneGroupManager;
    private final SceneGroupEventLogService eventLogService;
    private final Map<String, HistoryDTO> historyStore = new ConcurrentHashMap<>();

    @Autowired(required = false)
    public HistoryServiceImpl(SceneGroupManager sceneGroupManager, 
                               SceneGroupEventLogService eventLogService) {
        this.sceneGroupManager = sceneGroupManager;
        this.eventLogService = eventLogService;
        log.info("HistoryServiceImpl initialized with SE SDK SceneGroupManager: {}", sceneGroupManager != null);
    }

    @Override
    public PageResult<HistoryDTO> listMyHistory(String userId, Integer days, String category,
            String status, String keyword, int pageNum, int pageSize) {
        log.info("[listMyHistory] userId={}, days={}, category={}, status={}", userId, days, category, status);
        
        List<HistoryDTO> allHistory = loadHistoryFromSE(userId);
        
        long cutoffTime = days != null ? System.currentTimeMillis() - days * 24L * 3600000 : 0;
        
        List<HistoryDTO> filtered = allHistory.stream()
            .filter(h -> cutoffTime == 0 || h.getStartTime() >= cutoffTime)
            .filter(h -> category == null || category.isEmpty() || category.equals(h.getCategory()))
            .filter(h -> status == null || status.isEmpty() || status.equals(h.getStatus()))
            .filter(h -> keyword == null || keyword.isEmpty() || 
                (h.getSceneGroupName() != null && h.getSceneGroupName().contains(keyword)))
            .sorted((a, b) -> Long.compare(b.getStartTime(), a.getStartTime()))
            .collect(Collectors.toList());

        int total = filtered.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);
        
        List<HistoryDTO> pageData = start < total ? filtered.subList(start, end) : new ArrayList<>();
        
        PageResult<HistoryDTO> result = new PageResult<>();
        result.setList(pageData);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }
    
    private List<HistoryDTO> loadHistoryFromSE(String userId) {
        List<HistoryDTO> historyList = new ArrayList<>();
        
    if (sceneGroupManager != null) {
            try {
                List<SceneGroup> groups = sceneGroupManager.getAllSceneGroups();
                if (groups != null) {
                    for (SceneGroup group : groups) {
                        HistoryDTO history = convertSceneGroupToHistory(group, userId);
                        if (history != null) {
                            historyList.add(history);
                        }
                    }
                }
                log.debug("[loadHistoryFromSE] Loaded {} history records from SE SDK", historyList.size());
            } catch (Exception e) {
                log.warn("[loadHistoryFromSE] Failed to load from SE SDK: {}", e.getMessage());
            }
        }
        
        if (historyList.isEmpty()) {
            historyList.addAll(historyStore.values());
        }
        
        return historyList;
    }
    
    private HistoryDTO convertSceneGroupToHistory(SceneGroup group, String userId) {
        if (group == null) return null;
        
        HistoryDTO history = new HistoryDTO();
        history.setExecutionId("exec-" + group.getSceneGroupId());
        history.setSceneGroupId(group.getSceneGroupId());
        history.setSceneGroupName(group.getName());
        history.setCategory(group.getTemplateId() != null ? "business" : "collaboration");
        history.setStatus(convertStatus(group.getStatus() != null ? group.getStatus().name() : null));
        history.setParticipantCount(group.getAllParticipants() != null ? group.getAllParticipants().size() : 0);
        
        long createTime = group.getCreateTime() != null ? group.getCreateTime().toEpochMilli() : System.currentTimeMillis();
        history.setStartTime(createTime);
        history.setEndTime(createTime);
        history.setDuration(0);
        history.setTriggerType("manual");
        
        return history;
    }
    
    private String convertStatus(String seStatus) {
        if (seStatus == null) return "completed";
        switch (seStatus.toLowerCase()) {
            case "active":
            case "running":
                return "running";
            case "completed":
            case "success":
                return "success";
            case "failed":
            case "error":
                return "failed";
            default:
                return "completed";
        }
    }

    @Override
    public HistoryStatisticsDTO getStatistics(String userId, Integer days) {
        log.info("[getStatistics] userId={}, days={}", userId, days);
        
        List<HistoryDTO> allHistory = loadHistoryFromSE(userId);
        
        long cutoffTime = days != null ? System.currentTimeMillis() - days * 24L * 3600000 : 0;
        
        List<HistoryDTO> filtered = allHistory.stream()
            .filter(h -> cutoffTime == 0 || h.getStartTime() >= cutoffTime)
            .collect(Collectors.toList());
        
        int total = filtered.size();
        long successCount = filtered.stream().filter(h -> "success".equals(h.getStatus()) || "completed".equals(h.getStatus())).count();
        long avgDuration = filtered.isEmpty() ? 0 : 
            (long) filtered.stream().mapToLong(HistoryDTO::getDuration).average().orElse(0);
        
        return new HistoryStatisticsDTO(
 total,
            (int) filtered.stream().mapToInt(HistoryDTO::getParticipantCount).sum(),
            total > 0 ? (int) (successCount * 100 / total) : 0,
            (int) (avgDuration / 1000)
        );
    }
    
    @Override
    public HistoryDTO getExecutionDetail(String executionId, String userId) {
        log.info("[getExecutionDetail] executionId={}, userId={}", executionId, userId);
        
        List<HistoryDTO> allHistory = loadHistoryFromSE(userId);
        
        for (HistoryDTO history : allHistory) {
            if (executionId.equals(history.getExecutionId())) {
                return history;
            }
        }
        
        if (executionId.startsWith("exec-")) {
            String sceneGroupId = executionId.substring(5);
            if (sceneGroupManager != null) {
                try {
                    SceneGroup group = sceneGroupManager.getSceneGroup(sceneGroupId);
                    if (group != null) {
                        return convertSceneGroupToHistory(group, userId);
                    }
                } catch (Exception e) {
                    log.warn("[getExecutionDetail] Failed to get SceneGroup: {}", e.getMessage());
                }
            }
        }
        
        return historyStore.get(executionId);
    }
    
    @Override
    public boolean rerunScene(String userId, String sceneGroupId) {
        log.info("[rerunScene] userId={}, sceneGroupId={}", userId, sceneGroupId);
        
        if (sceneGroupManager != null) {
            try {
                SceneGroup group = sceneGroupManager.getSceneGroup(sceneGroupId);
                if (group != null) {
                    log.info("[rerunScene] Found scene group: {}", group.getName());
                    return true;
                }
            } catch (Exception e) {
                log.error("[rerunScene] Failed: {}", e.getMessage());
            }
        }
        
        return true;
    }

    @Override
    public byte[] exportHistory(String userId, Integer days, String category, String status) {
        log.info("[exportHistory] userId={}, days={}", userId, days);
        
        StringBuilder sb = new StringBuilder();
        sb.append("\uFEFF");
        sb.append("执行ID,场景组ID,场景名称,分类,状态,参与人数,开始时间,结束时间,耗时(ms),触发类型\n");
        
        PageResult<HistoryDTO> result = listMyHistory(userId, days, category, status, null, 1, 10000);
        for (HistoryDTO h : result.getList()) {
            sb.append(h.getExecutionId()).append(",");
            sb.append(h.getSceneGroupId()).append(",");
            sb.append(h.getSceneGroupName()).append(",");
            sb.append(h.getCategory()).append(",");
            sb.append(h.getStatus()).append(",");
            sb.append(h.getParticipantCount()).append(",");
            sb.append(h.getStartTime()).append(",");
            sb.append(h.getEndTime()).append(",");
            sb.append(h.getDuration()).append(",");
            sb.append(h.getTriggerType()).append("\n");
        }
        
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
}
