package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.llmscript.ActionExecuteRequestDTO;
import net.ooder.mvp.skill.scene.dto.llmscript.ActionExecuteResultDTO;
import net.ooder.mvp.skill.scene.dto.llmscript.ScriptExecuteRequestDTO;
import net.ooder.mvp.skill.scene.dto.llmscript.ScriptGenerateRequestDTO;
import net.ooder.mvp.skill.scene.llm.ModuleApiRegistry;
import net.ooder.mvp.skill.scene.llm.SceneActionExecutor;
import net.ooder.mvp.skill.scene.llm.SceneActionExecutor.ActionRequest;
import net.ooder.mvp.skill.scene.llm.SceneActionExecutor.ActionResult;
import net.ooder.mvp.skill.scene.llm.SceneActionExecutor.ScriptRequest;
import net.ooder.mvp.skill.scene.llm.SceneActionExecutor.ScriptResult;
import net.ooder.mvp.skill.scene.llm.assistant.ScriptGenerationResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/llm-script")
public class LlmScriptController {

    private static final Logger log = LoggerFactory.getLogger(LlmScriptController.class);

    private final ModuleApiRegistry moduleApiRegistry;
    private final SceneActionExecutor actionExecutor;

    @Autowired
    public LlmScriptController(ModuleApiRegistry moduleApiRegistry, 
                               SceneActionExecutor actionExecutor) {
        this.moduleApiRegistry = moduleApiRegistry;
        this.actionExecutor = actionExecutor;
    }

    @PostMapping("/execute")
    public ResponseEntity<ActionExecuteResultDTO> executeAction(@RequestBody ActionExecuteRequestDTO request) {
        log.info("[LlmScriptController] Received execute request: {}", request);

        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setAction(request.getAction());
        actionRequest.setModule(request.getModule());
        if (request.getParams() != null) {
            actionRequest.setParams(request.getParams());
        }
        if (request.getRequireConfirm() != null) {
            actionRequest.setRequireConfirm(request.getRequireConfirm());
        }
        if (request.getSyncContext() != null) {
            actionRequest.setSyncContext(request.getSyncContext());
        }

        ActionResult result = actionExecutor.execute(actionRequest);

        ActionExecuteResultDTO response = new ActionExecuteResultDTO();
        response.setSuccess(result.isSuccess());
        response.setNeedConfirm(result.isNeedConfirm());
        
        if (result.isNeedConfirm()) {
            response.setConfirmMessage(result.getConfirmMessage());
            Map<String, Object> pendingAction = new HashMap<>();
            pendingAction.put("action", actionRequest.getAction());
            pendingAction.put("module", actionRequest.getModule());
            pendingAction.put("params", actionRequest.getParams());
            response.setPendingAction(pendingAction);
        } else if (result.isSuccess()) {
            response.setResult(result.getResult());
        } else {
            response.setError(result.getError());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/execute-script")
    public ResponseEntity<ActionExecuteResultDTO> executeScript(@RequestBody ScriptExecuteRequestDTO request) {
        log.info("[LlmScriptController] Received script execution request");

        ScriptRequest scriptRequest = new ScriptRequest();
        scriptRequest.setScript(request.getScript());
        scriptRequest.setScriptType(request.getScriptType() != null ? request.getScriptType() : "mvel");
        scriptRequest.setModule(request.getModule());
        if (request.getRequireConfirm() != null) {
            scriptRequest.setRequireConfirm(request.getRequireConfirm());
        }
        if (request.getSyncContext() != null) {
            scriptRequest.setSyncContext(request.getSyncContext());
        }

        ScriptResult result = actionExecutor.executeScript(scriptRequest);

        ActionExecuteResultDTO response = new ActionExecuteResultDTO();
        response.setSuccess(result.isSuccess());
        response.setNeedConfirm(result.isNeedConfirm());
        
        if (result.isNeedConfirm()) {
            response.setConfirmMessage(result.getConfirmMessage());
            Map<String, Object> pendingScript = new HashMap<>();
            pendingScript.put("script", scriptRequest.getScript());
            pendingScript.put("scriptType", scriptRequest.getScriptType());
            pendingScript.put("module", scriptRequest.getModule());
            response.setPendingAction(pendingScript);
        } else if (result.isSuccess()) {
            response.setResult(result.getResult());
        } else {
            response.setError(result.getError());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/generate-script")
    public ResponseEntity<ActionExecuteResultDTO> generateScript(@RequestBody ScriptGenerateRequestDTO request) {
        log.info("[LlmScriptController] Received script generation request");

        String intent = request.getIntent();
        Map<String, Object> context = request.getContext();
        if (context == null) {
            context = new HashMap<>();
        }

        ScriptGenerationResultDto result = actionExecutor.generateScriptForIntent(intent, context);

        ActionExecuteResultDTO response = new ActionExecuteResultDTO();
        response.setSuccess(result.isSuccess());
        
        if (result.isSuccess()) {
            Map<String, Object> scriptResult = new HashMap<>();
            scriptResult.put("script", result.getScript());
            scriptResult.put("scriptType", result.getScriptType());
            scriptResult.put("module", result.getModule());
            scriptResult.put("explanation", result.getExplanation());
            response.setResult(scriptResult);
        } else {
            response.setError(result.getError());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<ActionExecuteResultDTO> confirmAction(@RequestBody ActionExecuteRequestDTO request) {
        log.info("[LlmScriptController] Received confirm request");

        String module = request.getModule();
        moduleApiRegistry.setCurrentModule(module);

        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setAction(request.getAction());
        actionRequest.setModule(module);
        if (request.getParams() != null) {
            actionRequest.setParams(request.getParams());
        }
        actionRequest.setRequireConfirm(false);
        actionRequest.setSyncContext(true);

        ActionResult result = actionExecutor.execute(actionRequest);

        ActionExecuteResultDTO response = new ActionExecuteResultDTO();
        response.setSuccess(result.isSuccess());
        
        if (result.isSuccess()) {
            response.setResult(result.getResult());
        } else {
            response.setError(result.getError());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/module/set")
    public ResponseEntity<ActionExecuteResultDTO> setCurrentModule(@RequestBody Map<String, String> request) {
        String module = request.get("module");
        
        if (module == null || module.isEmpty()) {
            ActionExecuteResultDTO errorResponse = new ActionExecuteResultDTO();
            errorResponse.setSuccess(false);
            errorResponse.setError("Module name is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        moduleApiRegistry.setCurrentModule(module);

        ActionExecuteResultDTO response = new ActionExecuteResultDTO();
        response.setSuccess(true);
        Map<String, Object> result = new HashMap<>();
        result.put("currentModule", module);
        result.put("availableApis", moduleApiRegistry.getAvailableApis(module));
        response.setResult(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/module/current")
    public ResponseEntity<ActionExecuteResultDTO> getCurrentModule() {
        String currentModule = moduleApiRegistry.getCurrentModule();
        Set<String> availableApis = moduleApiRegistry.getCurrentAvailableApis();

        ActionExecuteResultDTO response = new ActionExecuteResultDTO();
        response.setSuccess(true);
        Map<String, Object> result = new HashMap<>();
        result.put("currentModule", currentModule);
        result.put("availableApis", availableApis);
        response.setResult(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/module/{module}/apis")
    public ResponseEntity<ActionExecuteResultDTO> getModuleApis(@PathVariable String module) {
        Set<String> apis = moduleApiRegistry.getAvailableApis(module);
        Map<String, Object> moduleInfo = moduleApiRegistry.getModuleInfo(module);

        ActionExecuteResultDTO response = new ActionExecuteResultDTO();
        response.setSuccess(true);
        Map<String, Object> result = new HashMap<>();
        result.put("module", module);
        result.put("apis", apis);
        result.put("info", moduleInfo);
        response.setResult(result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/modules")
    public ResponseEntity<ActionExecuteResultDTO> getAllModules() {
        List<Map<String, Object>> modules = moduleApiRegistry.getAllModulesInfo();

        ActionExecuteResultDTO response = new ActionExecuteResultDTO();
        response.setSuccess(true);
        Map<String, Object> result = new HashMap<>();
        result.put("modules", modules);
        response.setResult(result);
        return ResponseEntity.ok(response);
    }
}
