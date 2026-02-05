package net.ooder.examples.skilla;

import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.examples.skilla.vfs.StorageService;
import net.ooder.skillvfs.VfsService;
import net.ooder.skillvfs.VfsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkillCommandHandlerTest {
    @Mock
    private StorageService mockStorageService;
    
    @Mock
    private VfsService mockVfsService;
    
    @Mock
    private VfsConfig mockVfsConfig;
    
    private SkillCommandHandler skillCommandHandler;
    
    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        
        // Setup mock VFS configuration
        when(mockVfsConfig.getVfsServerUrl()).thenReturn("http://localhost:8080/vfs");
        when(mockVfsConfig.getGroupName()).thenReturn("test-group");
        when(mockVfsConfig.isEnableVfs()).thenReturn(true);
        
        // Use spy to override the StorageService initialization
        skillCommandHandler = Mockito.spy(new SkillCommandHandler(
                "http://localhost:8080/vfs",
                "test-group",
                5000L,
                3,
                true
        ));
        
        // Replace the real StorageService with our mock
        try {
            // Use reflection to set the private storageService field
            java.lang.reflect.Field storageServiceField = SkillCommandHandler.class.getDeclaredField("storageService");
            storageServiceField.setAccessible(true);
            storageServiceField.set(skillCommandHandler, mockStorageService);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to set up test: " + e.getMessage());
        }
    }
    
    @Test
    void testCommandHandlerInitialization() {
        // Verify the handler was created successfully
        assertNotNull(skillCommandHandler, "SkillCommandHandler should be initialized");
        
        // Verify storage service was started
        verify(mockStorageService).start();
    }
    
    @Test
    void testHandleSkillInvokeCommand() {
        // Create a skill.invoke command packet
        Map<String, Object> params = new HashMap<>();
        params.put("query", "test query");
        
        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.SKILL_INVOKE)
                .params(params)
                .build();
        
        // Test handling the command
        skillCommandHandler.handleSkillInvoke(packet);
        
        // Verify the command was processed
        // Note: Since processQuery is private and just returns the query, we can't easily verify it was called
        // but we can verify the log messages were generated
    }
    
    @Test
    void testHandleRouteForwardCommand_VfsSync() {
        // Create a route.forward command packet with vfs-sync action
        Map<String, Object> params = new HashMap<>();
        params.put("action", "vfs-sync");
        params.put("vfs_id", "vfs-001");
        params.put("vfs_url", "http://localhost:8080/vfs");
        
        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.ROUTE_FORWARD)
                .params(params)
                .build();
        
        // Test handling the vfs-sync command
        skillCommandHandler.handleRouteForward(packet);
        
        // Verify storage service sync methods were called
        verify(mockStorageService).syncAllToVfs();
        verify(mockStorageService).syncAllFromVfs();
    }
    
    @Test
    void testHandleRouteForwardCommand_UnknownAction() {
        // Create a route.forward command packet with unknown action
        Map<String, Object> params = new HashMap<>();
        params.put("action", "unknown-action");
        
        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.ROUTE_FORWARD)
                .params(params)
                .build();
        
        // Test handling the unknown action command
        skillCommandHandler.handleRouteForward(packet);
        
        // Verify storage service sync methods were NOT called
        verify(mockStorageService, never()).syncAllToVfs();
        verify(mockStorageService, never()).syncAllFromVfs();
    }
    
    @Test
    void testHandleRouteForwardCommand_MissingAction() {
        // Create a route.forward command packet with missing action parameter
        Map<String, Object> params = new HashMap<>();
        // No action parameter
        
        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.ROUTE_FORWARD)
                .params(params)
                .build();
        
        // Test handling the command - should throw an exception due to missing required parameter
        assertThrows(Exception.class, () -> {
            skillCommandHandler.handleRouteForward(packet);
        });
    }
    
    @Test
    void testVfsSyncCommandProcessing() {
        // Create a command packet that simulates a VFS sync command from the route agent
        Map<String, Object> params = new HashMap<>();
        params.put("action", "vfs-sync");
        params.put("vfs_id", "vfs-001");
        params.put("vfs_url", "http://localhost:8080/vfs");
        params.put("group_name", "test-group");
        params.put("sync_type", "both"); // sync both ways
        
        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.ROUTE_FORWARD)
                .params(params)
                .build();
        
        // Mock the sync operations
        doNothing().when(mockStorageService).syncAllToVfs();
        doNothing().when(mockStorageService).syncAllFromVfs();
        
        // Process the command
        skillCommandHandler.handleRouteForward(packet);
        
        // Verify that both sync operations were performed
        verify(mockStorageService).syncAllToVfs();
        verify(mockStorageService).syncAllFromVfs();
        
        // Verify sync was called once for each direction
        verify(mockStorageService, times(1)).syncAllToVfs();
        verify(mockStorageService, times(1)).syncAllFromVfs();
    }
    
    @Test
    void testVfsSyncCommandWithSwitchToVfs() {
        // Create a command packet that includes a directive to switch to VFS storage
        Map<String, Object> params = new HashMap<>();
        params.put("action", "vfs-sync");
        params.put("switch_to_vfs", true);
        
        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.ROUTE_FORWARD)
                .params(params)
                .build();
        
        // Mock the storage service methods
        doNothing().when(mockStorageService).syncAllToVfs();
        doNothing().when(mockStorageService).syncAllFromVfs();
        doNothing().when(mockStorageService).switchJsonStorageImplementation(true);
        
        // Process the command
        skillCommandHandler.handleRouteForward(packet);
        
        // Verify that sync operations were performed
        verify(mockStorageService).syncAllToVfs();
        verify(mockStorageService).syncAllFromVfs();
        
        // Verify that JSON storage implementation was switched to VFS
        // Note: The current implementation doesn't use the switch_to_vfs parameter
        // but this test shows how it would be tested if implemented
    }
    
    @Test
    void testCommandHandlerWithDisabledVfs() {
        // Create a command handler with VFS disabled
        SkillCommandHandler handlerWithDisabledVfs = new SkillCommandHandler(
                "http://localhost:8080/vfs",
                "test-group",
                5000L,
                3,
                false
        );
        
        // Create a route.forward command packet with vfs-sync action
        Map<String, Object> params = new HashMap<>();
        params.put("action", "vfs-sync");
        
        CommandPacket packet = CommandPacket.builder()
                .command(CommandType.ROUTE_FORWARD)
                .params(params)
                .build();
        
        // Test handling the command - should not throw an exception even with VFS disabled
        assertDoesNotThrow(() -> {
            handlerWithDisabledVfs.handleRouteForward(packet);
        });
    }
}
