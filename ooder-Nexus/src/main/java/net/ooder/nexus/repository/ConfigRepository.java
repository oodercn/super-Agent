package net.ooder.nexus.repository;

import net.ooder.nexus.domain.config.model.ConfigHistoryItem;
import net.ooder.nexus.dto.config.ConfigExportDTO;
import net.ooder.nexus.dto.config.ConfigHistoryQueryDTO;
import net.ooder.nexus.dto.config.ConfigImportDTO;
import net.ooder.nexus.dto.config.ConfigSaveDTO;
import net.ooder.nexus.model.ConfigDataResult;
import net.ooder.nexus.model.ConfigExportResult;
import net.ooder.nexus.model.ConfigImportResult;

import java.util.List;
import java.util.Map;

/**
 * Config repository interface
 * Provides data access operations for configurations
 */
public interface ConfigRepository {

    /**
     * Save configuration
     */
    ConfigDataResult save(ConfigSaveDTO saveDTO);

    /**
     * Export configuration
     */
    ConfigExportResult export(ConfigExportDTO exportDTO);

    /**
     * Import configuration
     */
    ConfigImportResult importConfig(ConfigImportDTO importDTO);

    /**
     * Find configuration history
     */
    List<ConfigHistoryItem> findHistory(ConfigHistoryQueryDTO queryDTO);

    /**
     * Get configuration by type
     */
    Map<String, Object> findByType(String configType);

    /**
     * Apply configuration history
     */
    boolean applyHistory(String historyId);

    /**
     * Reset configuration to default
     */
    boolean reset(String configType);

    /**
     * Get all configuration types
     */
    List<String> findAllTypes();

    /**
     * Check if configuration exists
     */
    boolean exists(String configType);
}
