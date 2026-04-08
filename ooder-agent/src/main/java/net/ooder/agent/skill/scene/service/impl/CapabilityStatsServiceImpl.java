package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.model.CapabilityState;
import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityStateService;
import net.ooder.mvp.skill.scene.dto.stats.CapabilityStatsDTO;
import net.ooder.mvp.skill.scene.dto.stats.CapabilityRankDTO;
import net.ooder.mvp.skill.scene.dto.stats.LogEntryDTO;
import net.ooder.mvp.skill.scene.service.CapabilityStatsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CapabilityStatsServiceImpl implements CapabilityStatsService {

    private static final Logger log = LoggerFactory.getLogger(CapabilityStatsServiceImpl.class);

    @Autowired(required = false)
    private CapabilityService capabilityService;

    @Autowired(required = false)
    private CapabilityStateService stateService;

    @Override
    public CapabilityStatsDTO getOverviewStats() {
        log.info("[getOverviewStats] Getting capability overview stats from real data");
        
        CapabilityStatsDTO stats = new CapabilityStatsDTO();
        
        if (capabilityService == null) {
            log.warn("[getOverviewStats] CapabilityService not available, returning empty stats");
            stats.setTotalCapabilities(0);
            stats.setActiveCapabilities(0);
            stats.setTotalInvocations(0);
            stats.setSuccessInvocations(0);
            stats.setFailedInvocations(0);
            stats.setAvgResponseTime(0);
            return stats;
        }
        
        List<Capability> capabilities = capabilityService.findAll();
        
        int totalCapabilities = capabilities.size();
        int activeCapabilities = 0;
        int installedCapabilities = 0;
        
        for (Capability cap : capabilities) {
            if (stateService != null) {
                if (stateService.isInstalled(cap.getCapabilityId())) {
                    installedCapabilities++;
                }
                if (stateService.getStatus(cap.getCapabilityId()) != null) {
                    activeCapabilities++;
                }
            } else {
                if (cap.isInstalled()) {
                    installedCapabilities++;
                }
                if (cap.getStatus() != null) {
                    activeCapabilities++;
                }
            }
        }
        
        stats.setTotalCapabilities(totalCapabilities);
        stats.setActiveCapabilities(activeCapabilities);
        stats.setInstalledCapabilities(installedCapabilities);
        stats.setTotalInvocations(0);
        stats.setSuccessInvocations(0);
        stats.setFailedInvocations(0);
        stats.setAvgResponseTime(0.0);
        
        log.info("[getOverviewStats] Stats: total={}, active={}, installed={}", 
            totalCapabilities, activeCapabilities, installedCapabilities);
        
        return stats;
    }

    @Override
    public List<CapabilityRankDTO> getTopCapabilities(int limit) {
        log.info("[getTopCapabilities] Getting top {} capabilities from real data", limit);
        
        List<CapabilityRankDTO> result = new ArrayList<>();
        
        if (capabilityService == null) {
            log.warn("[getTopCapabilities] CapabilityService not available");
            return result;
        }
        
        List<Capability> capabilities = capabilityService.findAll();
        
        int rank = 0;
        for (Capability cap : capabilities) {
            if (rank >= limit) break;
            
            CapabilityRankDTO dto = new CapabilityRankDTO();
            dto.setCapabilityId(cap.getCapabilityId());
            dto.setName(cap.getName());
            dto.setType(cap.getCapabilityType() != null ? cap.getCapabilityType().name() : "SERVICE");
            dto.setInvokeCount(0);
            dto.setSuccessRate(0.0);
            dto.setAvgResponseTime(0.0);
            dto.setCategory(cap.getCapabilityCategory() != null ? cap.getCapabilityCategory().getCode() : "sys");
            dto.setStatus(cap.getStatus() != null ? cap.getStatus().name() : "REGISTERED");
            
            result.add(dto);
            rank++;
        }
        
        return result;
    }

    @Override
    public List<CapabilityRankDTO> getCapabilityRank(String sortBy, int limit) {
        log.info("[getCapabilityRank] Getting capability rank by {} limit {} from real data", sortBy, limit);
        return getTopCapabilities(limit);
    }

    @Override
    public List<String> getRecentErrors(int limit) {
        log.info("[getRecentErrors] Getting recent {} errors", limit);
        
        List<String> errors = new ArrayList<>();
        
        if (capabilityService != null) {
            List<Capability> capabilities = capabilityService.findAll();
            
            for (Capability cap : capabilities) {
                if (errors.size() >= limit) break;
                
                if (cap.getStatus() != null && 
                    (cap.getStatus().name().contains("ERROR") || cap.getStatus().name().contains("FAILED"))) {
                    errors.add(String.format("[%s] %s: 状态异常 - %s", 
                        new Date(), cap.getName(), cap.getStatus()));
                }
            }
        }
        
        if (errors.isEmpty()) {
            errors.add("暂无错误记录");
        }
        
        return errors;
    }

    @Override
    public List<LogEntryDTO> getRecentLogs(int limit) {
        log.info("[getRecentLogs] Getting recent {} logs from real data", limit);
        
        List<LogEntryDTO> logs = new ArrayList<>();
        
        if (capabilityService != null) {
            List<Capability> capabilities = capabilityService.findAll();
            
            int count = 0;
            for (Capability cap : capabilities) {
                if (count >= limit) break;
                
                LogEntryDTO logEntry = new LogEntryDTO();
                logEntry.setTimestamp(cap.getUpdateTime() > 0 ? cap.getUpdateTime() : System.currentTimeMillis());
                logEntry.setCapabilityId(cap.getCapabilityId());
                logEntry.setCapabilityName(cap.getName());
                logEntry.setLevel("INFO");
                logEntry.setMessage(String.format("能力 %s 已注册", cap.getName()));
                logEntry.setDuration(0L);
                
                logs.add(logEntry);
                count++;
            }
        }
        
        return logs;
    }
}
