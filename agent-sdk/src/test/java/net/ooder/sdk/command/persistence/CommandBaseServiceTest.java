package net.ooder.sdk.command.persistence;

import net.ooder.sdk.system.enums.CommandStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.*;

public class CommandBaseServiceTest {
    private CommandBaseService commandBaseService;
    
    @BeforeEach
    public void setUp() {
        commandBaseService = new CommandBaseServiceImpl();
    }
    
    @Test
    public void testCreateCommand() throws ExecutionException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        
        CompletableFuture<String> future = commandBaseService.createCommand("test-command", params);
        String commandId = future.get();
        
        assertNotNull(commandId);
        assertTrue(commandId.startsWith("cmd_"));
    }
    
    @Test
    public void testGetCommandStatus() throws ExecutionException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        CompletableFuture<String> createFuture = commandBaseService.createCommand("test-command", params);
        String commandId = createFuture.get();
        
        CompletableFuture<CommandStatus> statusFuture = commandBaseService.getCommandStatus(commandId);
        CommandStatus status = statusFuture.get();
        
        assertEquals(CommandStatus.PENDING, status);
    }
    
    @Test
    public void testCancelCommand() throws ExecutionException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        CompletableFuture<String> createFuture = commandBaseService.createCommand("test-command", params);
        String commandId = createFuture.get();
        
        CompletableFuture<Boolean> cancelFuture = commandBaseService.cancelCommand(commandId);
        boolean cancelled = cancelFuture.get();
        
        assertTrue(cancelled);
        
        CompletableFuture<CommandStatus> statusFuture = commandBaseService.getCommandStatus(commandId);
        CommandStatus status = statusFuture.get();
        
        assertEquals(CommandStatus.CANCELLED, status);
    }
    
    @Test
    public void testUpdateCommandStatus() throws ExecutionException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        CompletableFuture<String> createFuture = commandBaseService.createCommand("test-command", params);
        String commandId = createFuture.get();
        
        CompletableFuture<Boolean> updateFuture = commandBaseService.updateCommandStatus(commandId, CommandStatus.EXECUTING);
        boolean updated = updateFuture.get();
        
        assertTrue(updated);
        
        CompletableFuture<CommandStatus> statusFuture = commandBaseService.getCommandStatus(commandId);
        CommandStatus status = statusFuture.get();
        
        assertEquals(CommandStatus.EXECUTING, status);
    }
    
    @Test
    public void testUpdateCommandStatusToCompleted() throws ExecutionException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        CompletableFuture<String> createFuture = commandBaseService.createCommand("test-command", params);
        String commandId = createFuture.get();
        
        CompletableFuture<Boolean> updateFuture = commandBaseService.updateCommandStatus(commandId, CommandStatus.COMPLETED);
        boolean updated = updateFuture.get();
        
        assertTrue(updated);
        
        CompletableFuture<CommandStatus> statusFuture = commandBaseService.getCommandStatus(commandId);
        CommandStatus status = statusFuture.get();
        
        assertEquals(CommandStatus.COMPLETED, status);
    }
    
    @Test
    public void testUpdateCommandStatusToFailed() throws ExecutionException, InterruptedException {
        Map<String, Object> params = new HashMap<>();
        CompletableFuture<String> createFuture = commandBaseService.createCommand("test-command", params);
        String commandId = createFuture.get();
        
        CompletableFuture<Boolean> updateFuture = commandBaseService.updateCommandStatus(commandId, CommandStatus.FAILED);
        boolean updated = updateFuture.get();
        
        assertTrue(updated);
        
        CompletableFuture<CommandStatus> statusFuture = commandBaseService.getCommandStatus(commandId);
        CommandStatus status = statusFuture.get();
        
        assertEquals(CommandStatus.FAILED, status);
    }
    
    @Test
    public void testNonExistentCommand() throws ExecutionException, InterruptedException {
        CompletableFuture<CommandStatus> statusFuture = commandBaseService.getCommandStatus("non-existent-command");
        CommandStatus status = statusFuture.get();
        
        assertEquals(CommandStatus.FAILED, status);
    }
    
    @Test
    public void testCancelNonExistentCommand() throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> cancelFuture = commandBaseService.cancelCommand("non-existent-command");
        boolean cancelled = cancelFuture.get();
        
        assertFalse(cancelled);
    }
    
    @Test
    public void testUpdateNonExistentCommandStatus() throws ExecutionException, InterruptedException {
        CompletableFuture<Boolean> updateFuture = commandBaseService.updateCommandStatus("non-existent-command", CommandStatus.COMPLETED);
        boolean updated = updateFuture.get();
        
        assertFalse(updated);
    }
}
