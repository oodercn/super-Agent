package net.ooder.sdk.api.protocol;

import net.ooder.sdk.api.protocol.CommandPacket;
import net.ooder.sdk.api.protocol.CommandResult;

/**
 * Protocol Handler Interface
 *
 * <p>Handles specific protocol commands.</p>
 *
 * @author ooder Team
 * @since 0.7.1
 */
public interface ProtocolHandler {

    /**
     * Get the protocol type this handler supports
     *
     * @return protocol type (e.g., "MCP", "ROUTE", "END")
     */
    String getProtocolType();

    /**
     * Handle a command packet
     *
     * @param packet the command packet
     * @return command result
     */
    CommandResult handleCommand(CommandPacket packet);

    /**
     * Validate a command packet
     *
     * @param packet the command packet
     * @return true if valid
     */
    boolean validateCommand(CommandPacket packet);

    /**
     * Get handler status
     *
     * @return handler status
     */
    ProtocolStatus getStatus();

    /**
     * Initialize the handler
     */
    void initialize();

    /**
     * Shutdown the handler
     */
    void shutdown();

    /**
     * Protocol Handler Status
     */
    enum ProtocolStatus {
        INITIALIZING,
        READY,
        BUSY,
        ERROR,
        SHUTDOWN
    }
}
