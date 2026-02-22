package net.ooder.nexus.repository;

import net.ooder.nexus.domain.mcp.model.ProtocolHandlerData;
import net.ooder.nexus.dto.protocol.ProtocolCommandDTO;
import net.ooder.nexus.dto.protocol.ProtocolHandlerQueryDTO;
import net.ooder.nexus.dto.protocol.ProtocolHandlerRegisterDTO;

import java.util.List;

/**
 * Protocol repository interface
 * Provides data access operations for protocol handlers
 */
public interface ProtocolRepository {

    /**
     * Find all protocol handlers matching the query
     */
    List<ProtocolHandlerData> findAll(ProtocolHandlerQueryDTO queryDTO);

    /**
     * Register a protocol handler
     */
    ProtocolHandlerData register(ProtocolHandlerRegisterDTO registerDTO);

    /**
     * Handle protocol command
     */
    ProtocolHandlerData handleCommand(ProtocolCommandDTO commandDTO);

    /**
     * Find protocol handler by type
     */
    ProtocolHandlerData findByType(String protocolType);

    /**
     * Remove protocol handler
     */
    boolean remove(String protocolType);

    /**
     * Check if protocol handler exists
     */
    boolean exists(String protocolType);

    /**
     * Get all supported protocol types
     */
    List<String> findAllTypes();

    /**
     * Refresh protocol handlers
     */
    List<ProtocolHandlerData> refresh();

    /**
     * Search protocol handlers by keyword
     */
    List<ProtocolHandlerData> search(String keyword);
}
