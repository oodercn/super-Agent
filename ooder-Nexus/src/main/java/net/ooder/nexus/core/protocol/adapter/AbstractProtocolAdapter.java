package net.ooder.nexus.core.protocol.adapter;

import net.ooder.nexus.core.protocol.model.CommandPacket;
import net.ooder.nexus.core.protocol.model.CommandResult;
import net.ooder.nexus.core.protocol.model.ProtocolStatus;
import net.ooder.nexus.core.protocol.ProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * 协议适配器抽象基类
 */
public abstract class AbstractProtocolAdapter implements ProtocolHandler {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected final String protocolType;
    protected final ProtocolStatus status;
    protected final Set<String> supportedCommands = new HashSet<>();
    protected String description;

    public AbstractProtocolAdapter(String protocolType) {
        this.protocolType = protocolType;
        this.status = new ProtocolStatus(protocolType);
        this.description = protocolType + " Protocol Adapter";
        initializeSupportedCommands();
    }

    protected abstract void initializeSupportedCommands();

    @Override
    public String getProtocolType() {
        return protocolType;
    }

    @Override
    public ProtocolStatus getStatus() {
        return status;
    }

    @Override
    public boolean supportsCommand(String commandType) {
        return supportedCommands.contains(commandType);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void initialize() {
        logger.info("Initializing {} adapter...", protocolType);
        try {
            doInitialize();
            status.markRunning();
            logger.info("{} adapter initialized successfully", protocolType);
        } catch (Exception e) {
            status.markError(e.getMessage());
            logger.error("Failed to initialize {} adapter", protocolType, e);
            throw new RuntimeException("Failed to initialize adapter: " + protocolType, e);
        }
    }

    protected abstract void doInitialize();

    @Override
    public void destroy() {
        logger.info("Destroying {} adapter...", protocolType);
        try {
            doDestroy();
            status.markStopped();
            logger.info("{} adapter destroyed", protocolType);
        } catch (Exception e) {
            logger.error("Error destroying {} adapter", protocolType, e);
        }
    }

    protected abstract void doDestroy();

    @Override
    public boolean validateCommand(CommandPacket packet) {
        if (packet == null || packet.getHeader() == null) {
            logger.warn("Invalid command packet: null or missing header");
            return false;
        }

        String cmdProtocolType = packet.getProtocolType();
        if (!protocolType.equals(cmdProtocolType)) {
            logger.warn("Protocol type mismatch: expected {}, got {}", protocolType, cmdProtocolType);
            return false;
        }

        String commandType = packet.getCommandType();
        if (commandType == null || commandType.isEmpty()) {
            logger.warn("Command type is empty");
            return false;
        }

        if (!supportsCommand(commandType)) {
            logger.warn("Unsupported command type: {}", commandType);
            return false;
        }

        return doValidateCommand(packet);
    }

    protected abstract boolean doValidateCommand(CommandPacket packet);

    @Override
    public CommandResult handleCommand(CommandPacket packet) {
        String commandType = packet.getCommandType();
        String commandId = packet.getHeader().getCommandId();

        logger.debug("Handling {} command: {}", protocolType, commandId);

        try {
            CommandResult result = doHandleCommand(packet);
            logger.debug("Command {} processed: {}", commandId, result.isSuccess());
            return result;
        } catch (Exception e) {
            logger.error("Error handling command: {}", commandId, e);
            return CommandResult.error(commandId, 500, "Handler error: " + e.getMessage());
        }
    }

    protected abstract CommandResult doHandleCommand(CommandPacket packet);

    protected Set<String> getSupportedCommands() {
        return supportedCommands;
    }

    protected void addSupportedCommand(String commandType) {
        supportedCommands.add(commandType);
    }

    protected void setDescription(String description) {
        this.description = description;
    }
}
