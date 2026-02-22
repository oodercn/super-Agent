package net.ooder.nexus.repository;

import net.ooder.nexus.domain.system.model.HealthCheckResult;
import net.ooder.nexus.dto.health.HealthCheckDTO;
import net.ooder.nexus.dto.health.HealthScheduleDTO;

import java.util.List;

/**
 * Health check repository interface
 * Provides data access operations for health checks
 */
public interface HealthCheckRepository {

    /**
     * Run health check
     */
    HealthCheckResult runCheck(HealthCheckDTO checkDTO);

    /**
     * Save health check schedule
     */
    void saveSchedule(HealthScheduleDTO scheduleDTO);

    /**
     * Find health check history
     */
    List<HealthCheckResult> findHistory(int limit);

    /**
     * Find health check history by type
     */
    List<HealthCheckResult> findHistoryByType(String checkType, int limit);

    /**
     * Get latest health check result
     */
    HealthCheckResult findLatest();

    /**
     * Save health check result
     */
    void saveResult(HealthCheckResult result);

    /**
     * Get all schedules
     */
    List<HealthScheduleDTO> findAllSchedules();

    /**
     * Delete schedule by ID
     */
    void deleteSchedule(String scheduleId);
}
