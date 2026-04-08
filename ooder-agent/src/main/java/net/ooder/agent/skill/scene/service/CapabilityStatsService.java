package net.ooder.mvp.skill.scene.service;

import net.ooder.mvp.skill.scene.dto.stats.CapabilityStatsDTO;
import net.ooder.mvp.skill.scene.dto.stats.CapabilityRankDTO;
import net.ooder.mvp.skill.scene.dto.stats.LogEntryDTO;

import java.util.List;

public interface CapabilityStatsService {
    
    CapabilityStatsDTO getOverviewStats();
    
    List<CapabilityRankDTO> getTopCapabilities(int limit);
    
    List<CapabilityRankDTO> getCapabilityRank(String sortBy, int limit);
    
    List<String> getRecentErrors(int limit);
    
    List<LogEntryDTO> getRecentLogs(int limit);
}
