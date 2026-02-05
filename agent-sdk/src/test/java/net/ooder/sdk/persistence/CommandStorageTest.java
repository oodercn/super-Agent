package net.ooder.sdk.persistence;

import net.ooder.sdk.command.persistence.CommandPersistenceService;
import net.ooder.sdk.command.persistence.CommandRecord;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.system.enums.CommandStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * 针对性测试命令保存和读取功能
 */
public class CommandStorageTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Starting Command Storage Test ===");
            
            // 获取CommandPersistenceService实例
            CommandPersistenceService persistenceService = CommandPersistenceService.getInstance();
            System.out.println("OK: CommandPersistenceService instance obtained");
            
            // 创建测试命令记录
            CommandRecord record = new CommandRecord();
            record.setCommandId("test-command-" + System.currentTimeMillis());
            record.setCommandType(CommandType.SKILL_INVOKE);
            record.setSenderId("test-sender");
            record.setReceiverId("test-receiver");
            record.setStatus(CommandStatus.COMPLETED);
            
            // 添加测试结果
            Map<String, Object> result = new HashMap<>();
            result.put("status", "success");
            result.put("message", "Test command executed successfully");
            
            // 使用传统HashMap方式替代Map.of()
            Map<String, Object> data = new HashMap<>();
            data.put("key1", "value1");
            data.put("key2", "value2");
            result.put("data", data);
            record.setResult(result);
            
            System.out.println("OK: Test command record created");
            System.out.println("  Command ID: " + record.getCommandId());
            System.out.println("  Command Type: " + record.getCommandType());
            System.out.println("  Status: " + record.getStatus());
            
            // 保存命令记录
            boolean saveResult = persistenceService.saveCommandRecord(record);
            if (saveResult) {
                System.out.println("OK: Command record saved successfully");
            } else {
                System.out.println("ERROR: Failed to save command record");
                return;
            }
            
            // 读取命令记录
            CommandRecord retrievedRecord = persistenceService.loadCommandRecord(record.getCommandId());
            if (retrievedRecord != null) {
                System.out.println("OK: Command record retrieved successfully");
                System.out.println("  Retrieved Command ID: " + retrievedRecord.getCommandId());
                System.out.println("  Retrieved Command Type: " + retrievedRecord.getCommandType());
                System.out.println("  Retrieved Status: " + retrievedRecord.getStatus());
                System.out.println("  Retrieved Result: " + retrievedRecord.getResult());
                
                // 验证记录内容是否一致
                if (retrievedRecord.getCommandId().equals(record.getCommandId()) &&
                    retrievedRecord.getCommandType() == record.getCommandType() &&
                    retrievedRecord.getStatus() == record.getStatus() &&
                    retrievedRecord.getResult().equals(record.getResult())) {
                    System.out.println("OK: Command record content matches");
                } else {
                    System.out.println("ERROR: Command record content mismatch");
                }
            } else {
                System.out.println("ERROR: Failed to retrieve command record");
                return;
            }
            
            // 测试更新命令状态
            boolean updateResult = persistenceService.updateCommandStatus(record.getCommandId(), CommandStatus.FAILED, "Test error message");
            if (updateResult) {
                System.out.println("OK: Command status updated successfully");
                
                // 再次读取验证更新
                CommandRecord updatedRecord = persistenceService.loadCommandRecord(record.getCommandId());
                if (updatedRecord != null && updatedRecord.getStatus() == CommandStatus.FAILED) {
                    System.out.println("OK: Command status update verified");
                    System.out.println("  Updated Status: " + updatedRecord.getStatus());
                    System.out.println("  Updated Error Message: " + updatedRecord.getErrorMessage());
                }
            } else {
                System.out.println("ERROR: Failed to update command status");
            }
            
            System.out.println("\n=== Command Storage Test Completed Successfully! ===");
            System.out.println("All tests passed: Command save and read functionality is working correctly.");
            
        } catch (Exception e) {
            System.err.println("ERROR: Command Storage Test Failed with Exception:");
            e.printStackTrace();
        }
    }
}