package net.ooder.sdk.command.tasks;


import net.ooder.sdk.command.api.CommandAnnotation;
import net.ooder.sdk.command.impl.AbstractCommandTask;
import net.ooder.sdk.command.model.CommandResult;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.params.EndExecuteParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommandAnnotation(
        id = "end_execute",
        name = "End Agent Execute Command",
        desc = "Execute a command on an End Agent",
        commandType = CommandType.END_EXECUTE,
        key = "end.execute"
)
public class EndExecuteCommandTask extends AbstractCommandTask<EndExecuteParams> {
    private static final Logger log = LoggerFactory.getLogger(EndExecuteCommandTask.class);

    public EndExecuteCommandTask() {
        super(CommandType.END_EXECUTE);
    }

    @Override
    protected CommandResult doExecute(CommandPacket<EndExecuteParams> packet) throws Exception {
        log.info("Handling END_EXECUTE command with params: {}", packet.getPayload());
        
        Object payload = packet.getPayload();
        String command = null;
        Map<String, Object> args = new HashMap<>();
        
        try {
            // 从参数中获取命令内容
            if (payload instanceof EndExecuteParams) {
                // 处理特定参数类的情况
                EndExecuteParams params = (EndExecuteParams) payload;
                command = params.getCommand();
                args = params.getArgs() != null ? params.getArgs() : new HashMap<>();
            } else if (payload instanceof Map) {
                // 处理Map参数的情况（向后兼容）
                Map<String, Object> params = (Map<String, Object>) payload;
                command = (String) params.get("command");
                args = (Map<String, Object>) params.getOrDefault("args", new HashMap<>());
            } else {
                throw new IllegalArgumentException("Invalid payload type: " + (payload != null ? payload.getClass().getName() : "null"));
            }
            
            if (command == null) {
                throw new IllegalArgumentException("Command parameters cannot be null");
            }
            
            // 参数验证
            if (command.trim().isEmpty()) {
                throw new IllegalArgumentException("Command cannot be empty");
            }
            
            // 安全检查：限制可执行的命令范围
            List<String> allowedCommands = Arrays.asList("echo", "ls", "dir", "pwd", "whoami");
            if (!allowedCommands.contains(command.toLowerCase())) {
                throw new SecurityException("Command not allowed: " + command);
            }
            
            // 构建完整命令
            StringBuilder fullCommand = new StringBuilder(command);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                fullCommand.append(" ").append(entry.getValue());
            }
            
            log.info("Executing command: {}", fullCommand);
            
            // 实际执行命令
            Process process;
            String os = System.getProperty("os.name").toLowerCase();
            
            if (os.contains("win")) {
                // 在Windows系统上，使用cmd /c来执行内置命令
                process = Runtime.getRuntime().exec(new String[]{"cmd", "/c", fullCommand.toString()});
            } else {
                // 在非Windows系统上，直接执行命令
                process = Runtime.getRuntime().exec(fullCommand.toString());
            }
            
            // 收集命令输出
            String output = readInputStream(process.getInputStream());
            String error = readInputStream(process.getErrorStream());
            int exitCode = process.waitFor();
            
            // 构建执行结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("command", command);
            resultData.put("args", args);
            resultData.put("exitCode", exitCode);
            resultData.put("output", output);
            resultData.put("error", error);
            resultData.put("timestamp", System.currentTimeMillis());
            
            CommandResult result;
            if (exitCode == 0) {
                resultData.put("status", "success");
                result = CommandResult.success(resultData, "Command executed successfully");
                log.info("Command executed successfully: {}, exitCode: {}", command, exitCode);
            } else {
                resultData.put("status", "failed");
                result = CommandResult.failed("Command execution failed", resultData);
                log.warn("Command executed with error: {}, exitCode: {}, error: {}", command, exitCode, error);
            }
            
            return result;
            
        } catch (SecurityException e) {
            log.error("Security violation when executing command: {}", e.getMessage(), e);
            
            // 构建安全错误结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("status", "security_error");
            resultData.put("error", e.getMessage());
            resultData.put("timestamp", System.currentTimeMillis());
            
            return CommandResult.securityError("Security violation", resultData);
        } catch (IllegalArgumentException e) {
            log.error("Invalid command parameters: {}", e.getMessage(), e);
            
            // 构建参数错误结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("status", "parameter_error");
            resultData.put("error", e.getMessage());
            resultData.put("timestamp", System.currentTimeMillis());
            
            return CommandResult.parameterError("Invalid parameters", resultData);
        } catch (Exception e) {
            log.error("Failed to execute command: {}", e.getMessage(), e);
            
            // 构建执行错误结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("status", "execution_error");
            resultData.put("error", e.getMessage());
            resultData.put("timestamp", System.currentTimeMillis());
            
            return CommandResult.executionError("Execution failed", resultData);
        }
    }
    
    /**
     * 读取输入流内容
     */
    private String readInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append(System.lineSeparator());
            }
            return result.toString().trim();
        }
    }
}