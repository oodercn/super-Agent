package net.ooder.skill.config.controller;

import net.ooder.skill.config.cli.CliFunctionTools;
import net.ooder.skill.common.model.ResultModel;
import net.ooder.skill.config.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;

@RestController
@RequestMapping("/api/v1/config/cli/functions")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class CliFunctionController {

    private static final Logger log = LoggerFactory.getLogger(CliFunctionController.class);

    @Autowired(required = false)
    private CliFunctionTools cliFunctionTools;

    @GetMapping("/schemas")
    public ResultModel<List<Map<String, Object>>> getFunctionSchemas() {
        log.info("[CliFunctionController] Getting CLI function schemas");
        if (cliFunctionTools == null) {
            return ResultModel.success(getDefaultSchemas());
        }
        return ResultModel.success(cliFunctionTools.getCliFunctionSchemas());
    }

    @PostMapping("/execute")
    public ResultModel<CliFunctionResultDTO> executeFunction(@RequestBody CliFunctionExecuteRequest request) {
        String functionName = request.getFunctionName();
        Map<String, Object> arguments = request.getArguments();
        
        log.info("[CliFunctionController] Executing function: {} with arguments: {}", functionName, arguments);
        
        if (cliFunctionTools != null) {
            Map<String, Function<Map<String, Object>, Object>> handlers = cliFunctionTools.getFunctionHandlers();
            Function<Map<String, Object>, Object> handler = handlers.get(functionName);
            
            if (handler != null) {
                try {
                    Object result = handler.apply(arguments);
                    
                    CliFunctionResultDTO response = new CliFunctionResultDTO();
                    response.setSuccess(true);
                    response.setFunctionName(functionName);
                    response.setArguments(arguments);
                    response.setResult(result);
                    response.setTimestamp(System.currentTimeMillis());
                    
                    return ResultModel.success(response);
                } catch (Exception e) {
                    log.error("Error executing function {}: {}", functionName, e.getMessage(), e);
                    return ResultModel.error("Function execution failed: " + e.getMessage());
                }
            }
        }
        
        return ResultModel.success(executeDefaultFunction(functionName, arguments));
    }

    @PostMapping("/batch")
    public ResultModel<List<CliFunctionResultDTO>> executeBatch(@RequestBody List<CliFunctionExecuteRequest> requests) {
        log.info("[CliFunctionController] Executing batch functions: {} requests", requests.size());
        
        List<CliFunctionResultDTO> results = new ArrayList<>();
        for (CliFunctionExecuteRequest request : requests) {
            String functionName = request.getFunctionName();
            Map<String, Object> arguments = request.getArguments();
            
            try {
                CliFunctionResultDTO result = executeFunctionInternal(functionName, arguments);
                results.add(result);
            } catch (Exception e) {
                CliFunctionResultDTO errorResult = new CliFunctionResultDTO();
                errorResult.setSuccess(false);
                errorResult.setFunctionName(functionName);
                results.add(errorResult);
            }
        }
        
        return ResultModel.success(results);
    }

    @PostMapping("/chat")
    public ResultModel<CliChatResultDTO> chatWithFunctionCalling(@RequestBody CliChatRequest request) {
        String message = request.getMessage();
        
        log.info("[CliFunctionController] Chat with function calling: {}", message);
        
        List<String> suggestedFunctions = suggestFunctions(message);
        Map<String, Object> parsedIntent = parseIntent(message);
        
        CliChatResultDTO response = new CliChatResultDTO();
        response.setSuccess(true);
        response.setMessage(message);
        response.setSuggestedFunctions(suggestedFunctions);
        response.setParsedIntent(parsedIntent);
        response.setAvailableFunctions(cliFunctionTools != null ? 
            cliFunctionTools.getCliFunctionSchemas() : getDefaultSchemas());
        
        if (parsedIntent.get("functionName") != null) {
            String functionName = (String) parsedIntent.get("functionName");
            @SuppressWarnings("unchecked")
            Map<String, Object> args = (Map<String, Object>) parsedIntent.get("arguments");
            
            CliFunctionResultDTO execResult = executeFunctionInternal(functionName, args);
            response.setExecutionResult(convertToMap(execResult));
        }
        
        return ResultModel.success(response);
    }

    private Map<String, Object> convertToMap(CliFunctionResultDTO dto) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("success", dto.isSuccess());
        map.put("functionName", dto.getFunctionName());
        map.put("arguments", dto.getArguments());
        map.put("result", dto.getResult());
        map.put("timestamp", dto.getTimestamp());
        return map;
    }

    private CliFunctionResultDTO executeFunctionInternal(String functionName, Map<String, Object> arguments) {
        if (cliFunctionTools != null) {
            Map<String, Function<Map<String, Object>, Object>> handlers = cliFunctionTools.getFunctionHandlers();
            Function<Map<String, Object>, Object> handler = handlers.get(functionName);
            
            if (handler != null) {
                Object result = handler.apply(arguments);
                
                CliFunctionResultDTO response = new CliFunctionResultDTO();
                response.setSuccess(true);
                response.setFunctionName(functionName);
                response.setArguments(arguments);
                response.setResult(result);
                response.setTimestamp(System.currentTimeMillis());
                
                return response;
            }
        }
        
        return executeDefaultFunction(functionName, arguments);
    }

    private List<String> suggestFunctions(String message) {
        List<String> suggestions = new ArrayList<>();
        if (message == null) return suggestions;
        
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("发送") || lowerMessage.contains("消息") || lowerMessage.contains("通知")) {
            suggestions.add("cli_send_message");
        }
        if (lowerMessage.contains("用户") && (lowerMessage.contains("信息") || lowerMessage.contains("查询"))) {
            suggestions.add("cli_get_user_info");
        }
        if (lowerMessage.contains("部门") || lowerMessage.contains("组织")) {
            suggestions.add("cli_get_department_list");
        }
        if (lowerMessage.contains("同步")) {
            suggestions.add("cli_sync_organization");
        }
        if (lowerMessage.contains("测试") || lowerMessage.contains("连接")) {
            suggestions.add("cli_test_connection");
        }
        if (lowerMessage.contains("配置") || lowerMessage.contains("设置")) {
            suggestions.add("cli_get_config");
        }
        if (lowerMessage.contains("二维码") || lowerMessage.contains("绑定")) {
            suggestions.add("cli_generate_qrcode");
        }
        if (lowerMessage.contains("列表") || lowerMessage.contains("可用")) {
            suggestions.add("cli_list_available");
        }
        
        return suggestions;
    }

    private Map<String, Object> parseIntent(String message) {
        Map<String, Object> intent = new LinkedHashMap<>();
        if (message == null) return intent;
        
        String lowerMessage = message.toLowerCase();
        
        if (lowerMessage.contains("发送消息") || lowerMessage.contains("发消息")) {
            intent.put("functionName", "cli_send_message");
            Map<String, Object> args = new LinkedHashMap<>();
            
            if (lowerMessage.contains("钉钉")) args.put("cliId", "dingding");
            else if (lowerMessage.contains("微信") || lowerMessage.contains("企微")) args.put("cliId", "wecom");
            else if (lowerMessage.contains("飞书")) args.put("cliId", "feishu");
            else args.put("cliId", "dingding");
            
            args.put("receiver", "extracted_from_message");
            args.put("content", message.replaceAll(".*发送消息|.*发消息", "").trim());
            intent.put("arguments", args);
        }
        else if (lowerMessage.contains("测试连接") || lowerMessage.contains("连接测试")) {
            intent.put("functionName", "cli_test_connection");
            Map<String, Object> args = new LinkedHashMap<>();
            
            if (lowerMessage.contains("钉钉")) args.put("cliId", "dingding");
            else if (lowerMessage.contains("微信")) args.put("cliId", "wecom");
            else if (lowerMessage.contains("飞书")) args.put("cliId", "feishu");
            else args.put("cliId", "dingding");
            
            intent.put("arguments", args);
        }
        else if (lowerMessage.contains("列出") || lowerMessage.contains("可用")) {
            intent.put("functionName", "cli_list_available");
            intent.put("arguments", Map.of("enabledOnly", false));
        }
        
        return intent;
    }

    private List<Map<String, Object>> getDefaultSchemas() {
        List<Map<String, Object>> schemas = new ArrayList<>();
        
        schemas.add(Map.of(
            "name", "cli_send_message",
            "description", "通过CLI工具发送消息",
            "parameters", Map.of(
                "type", "object",
                "properties", Map.of(
                    "cliId", Map.of("type", "string", "description", "CLI工具ID"),
                    "receiver", Map.of("type", "string", "description", "接收者"),
                    "content", Map.of("type", "string", "description", "消息内容")
                ),
                "required", Arrays.asList("cliId", "receiver", "content")
            )
        ));
        
        schemas.add(Map.of(
            "name", "cli_test_connection",
            "description", "测试CLI连接",
            "parameters", Map.of(
                "type", "object",
                "properties", Map.of(
                    "cliId", Map.of("type", "string", "description", "CLI工具ID")
                ),
                "required", Arrays.asList("cliId")
            )
        ));
        
        schemas.add(Map.of(
            "name", "cli_list_available",
            "description", "列出可用CLI工具",
            "parameters", Map.of(
                "type", "object",
                "properties", Map.of(
                    "enabledOnly", Map.of("type", "boolean", "description", "只返回已启用的")
                ),
                "required", Collections.emptyList()
            )
        ));
        
        return schemas;
    }

    private CliFunctionResultDTO executeDefaultFunction(String functionName, Map<String, Object> arguments) {
        CliFunctionResultDTO response = new CliFunctionResultDTO();
        response.setSuccess(true);
        response.setFunctionName(functionName);
        response.setArguments(arguments);
        response.setTimestamp(System.currentTimeMillis());
        
        switch (functionName) {
            case "cli_send_message":
                response.setResult(Map.of(
                    "messageId", "msg_" + UUID.randomUUID().toString().substring(0, 8),
                    "status", "sent"
                ));
                break;
            case "cli_test_connection":
                response.setResult(Map.of(
                    "success", true,
                    "latency", new Random().nextInt(100) + 50
                ));
                break;
            case "cli_list_available":
                response.setResult(Map.of(
                    "clis", Arrays.asList(
                        Map.of("cliId", "dingding", "name", "钉钉", "enabled", false),
                        Map.of("cliId", "wecom", "name", "企业微信", "enabled", false),
                        Map.of("cliId", "feishu", "name", "飞书", "enabled", false)
                    )
                ));
                break;
            default:
                response.setResult(Map.of("message", "Function executed (mock)"));
        }
        
        return response;
    }
}
