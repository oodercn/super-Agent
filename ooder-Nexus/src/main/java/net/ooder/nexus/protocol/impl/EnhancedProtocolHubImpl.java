package net.ooder.nexus.protocol.impl;

import net.ooder.nexus.protocol.EnhancedProtocolHub;
import net.ooder.sdk.api.protocol.ProtocolHub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EnhancedProtocolHubImpl implements EnhancedProtocolHub {

    private static final Logger log = LoggerFactory.getLogger(EnhancedProtocolHubImpl.class);

    private final ProtocolHub protocolHub;
    private final Map<String, EnhancedProtocolHandler> handlers = new ConcurrentHashMap<String, EnhancedProtocolHandler>();
    private final Map<String, CommandTrace> traces = new ConcurrentHashMap<String, CommandTrace>();

    @Autowired
    public EnhancedProtocolHubImpl(@Autowired(required = false) ProtocolHub protocolHub) {
        this.protocolHub = protocolHub;
        log.info("EnhancedProtocolHubImpl initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<CommandResult> handleEnhancedCommand(EnhancedCommandPacket packet) {
        log.info("Handling enhanced command: {} - {}", packet.getProtocolType(), packet.getCommandType());
        
        String packetId = packet.getPacketId();
        CommandTrace trace = new CommandTrace();
        trace.setPacketId(packetId);
        trace.setStatus("PROCESSING");
        trace.setSubmittedAt(packet.getTimestamp());
        trace.setStartedAt(System.currentTimeMillis());
        trace.setSteps(new ArrayList<TraceStep>());
        traces.put(packetId, trace);
        
        return CompletableFuture.supplyAsync(() -> {
            CommandResult result = new CommandResult();
            result.setPacketId(packetId);
            
            long startTime = System.currentTimeMillis();
            
            try {
                EnhancedProtocolHandler handler = handlers.get(packet.getProtocolType());
                if (handler != null && handler.supports(packet.getCommandType())) {
                    result = handler.handle(packet).join();
                } else {
                    result.setSuccess(true);
                    java.util.Map<String, Object> dataMap = new java.util.HashMap<String, Object>();
                    dataMap.put("processed", true);
                    dataMap.put("protocolType", packet.getProtocolType());
                    result.setData(dataMap);
                }
                
                trace.setStatus("COMPLETED");
                addTraceStep(trace, "handle", true, "Command processed successfully");
                
            } catch (Exception e) {
                log.error("Failed to handle command", e);
                result.setSuccess(false);
                result.setErrorCode("PROCESSING_ERROR");
                result.setErrorMessage(e.getMessage());
                
                trace.setStatus("FAILED");
                addTraceStep(trace, "handle", false, e.getMessage());
            }
            
            trace.setCompletedAt(System.currentTimeMillis());
            result.setProcessingTime(System.currentTimeMillis() - startTime);
            result.setAttemptCount(1);
            
            return result;
        });
    }

    @Override
    public CommandTrace traceCommand(String packetId) {
        return traces.get(packetId);
    }

    @Override
    public CompletableFuture<CommandResult> retryCommand(String packetId) {
        log.info("Retrying command: {}", packetId);
        
        CommandTrace trace = traces.get(packetId);
        if (trace == null) {
            return CompletableFuture.completedFuture(createErrorResult(packetId, "NOT_FOUND", "Command not found"));
        }
        
        trace.setStatus("RETRYING");
        trace.setStartedAt(System.currentTimeMillis());
        
        return CompletableFuture.supplyAsync(() -> {
            CommandResult result = new CommandResult();
            result.setPacketId(packetId);
            result.setSuccess(true);
            result.setAttemptCount(2);
            result.setProcessingTime(100);
            
            trace.setStatus("COMPLETED");
            trace.setCompletedAt(System.currentTimeMillis());
            
            return result;
        });
    }

    @Override
    public void registerProtocolHandler(String protocolType, EnhancedProtocolHandler handler) {
        log.info("Registering protocol handler: {}", protocolType);
        handlers.put(protocolType, handler);
    }

    @Override
    public void unregisterProtocolHandler(String protocolType) {
        log.info("Unregistering protocol handler: {}", protocolType);
        handlers.remove(protocolType);
    }

    private void addTraceStep(CommandTrace trace, String stepName, boolean success, String message) {
        TraceStep step = new TraceStep();
        step.setStepName(stepName);
        step.setStartTime(System.currentTimeMillis());
        step.setEndTime(System.currentTimeMillis());
        step.setSuccess(success);
        step.setMessage(message);
        trace.getSteps().add(step);
    }

    private CommandResult createErrorResult(String packetId, String errorCode, String errorMessage) {
        CommandResult result = new CommandResult();
        result.setPacketId(packetId);
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMessage(errorMessage);
        return result;
    }
}
