package net.ooder.nexus.repository;

import net.ooder.nexus.domain.mcp.model.LogEntry;
import net.ooder.nexus.dto.log.LogExportDTO;
import net.ooder.nexus.dto.log.LogQueryDTO;

import java.util.List;

/**
 * Log repository interface
 * Provides data access operations for logs
 */
public interface LogRepository {

    /**
     * Find all logs matching the query criteria
     */
    List<LogEntry> findAll(LogQueryDTO query);

    /**
     * Find log by ID
     */
    LogEntry findById(String logId);

    /**
     * Save a log entry
     */
    void save(LogEntry logEntry);

    /**
     * Delete all logs
     */
    void deleteAll();

    /**
     * Count total logs
     */
    long count();

    /**
     * Count logs matching the query criteria
     */
    long count(LogQueryDTO query);

    /**
     * Export logs to file
     */
    String export(LogExportDTO exportDTO);

    /**
     * Get recent logs with limit
     */
    List<LogEntry> findRecent(int limit);
}
