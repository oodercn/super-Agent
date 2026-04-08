package net.ooder.skill.chat.controller;

import net.ooder.skill.chat.service.SkillsContextService;
import net.ooder.skill.chat.service.impl.SkillsContextServiceImpl;
import net.ooder.skill.chat.service.impl.KnowledgeServiceImpl;
import net.ooder.skill.common.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Skills Context Controller
 * Skills 上下文控制器
 * 
 * 提供 AI 助手上下文注册和管理�?API 端点
 */
@RestController
@RequestMapping("/api/v1/context")
@CrossOrigin(originPatterns = "*", allowedHeaders = "*")
public class SkillsContextController {

    private static final Logger log = LoggerFactory.getLogger(SkillsContextController.class);

    private SkillsContextService contextService;

    public SkillsContextController() {
        this.contextService = new SkillsContextServiceImpl(new KnowledgeServiceImpl());
    }

    /**
     * 初始化用户上下文
     */
    @PostMapping("/initialize")
    public ResultModel<Boolean> initializeContext(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String sessionId = request.get("sessionId");
        
        log.info("[initializeContext] userId: {}, sessionId: {}", userId, sessionId);
        
        contextService.initializeContext(userId, sessionId);
        return ResultModel.success(true);
    }

    /**
     * 获取用户身份信息
     */
    @GetMapping("/identity/{userId}")
    public ResultModel<Map<String, Object>> getUserIdentity(@PathVariable String userId) {
        log.info("[getUserIdentity] userId: {}", userId);
        
        Map<String, Object> identity = contextService.getUserIdentity(userId);
        return ResultModel.success(identity);
    }

    /**
     * 获取模块信息
     */
    @GetMapping("/module/{skillId}")
    public ResultModel<List<Map<String, Object>>> getModuleInfo(@PathVariable String skillId) {
        log.info("[getModuleInfo] skillId: {}", skillId);
        
        List<Map<String, Object>> info = contextService.getModuleInfo(skillId);
        return ResultModel.success(info);
    }

    /**
     * 获取当前模块上下文
     */
    @GetMapping("/current/{sessionId}")
    public ResultModel<Map<String, Object>> getCurrentModuleContext(@PathVariable String sessionId) {
        log.info("[getCurrentModuleContext] sessionId: {}", sessionId);
        
        Map<String, Object> context = contextService.getCurrentModuleContext(sessionId);
        return ResultModel.success(context);
    }

    /**
     * 注册技能上下文
     */
    @PostMapping("/register")
    public ResultModel<Boolean> registerSkillContext(@RequestBody Map<String, Object> request) {
        String skillId = (String) request.get("skillId");
        
        log.info("[registerSkillContext] skillId: {}", skillId);
        
        contextService.registerSkillContext(skillId, request);
        return ResultModel.success(true);
    }

    /**
     * 更新页面状�?     */
    @PostMapping("/page-state")
    public ResultModel<Boolean> updatePageState(@RequestBody Map<String, Object> request) {
        String sessionId = (String) request.get("sessionId");
        String module = (String) request.get("module");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> state = (Map<String, Object>) request.get("state");
        
        log.info("[updatePageState] sessionId: {}, module: {}", sessionId, module);
        
        contextService.updatePageState(sessionId, module, state);
        return ResultModel.success(true);
    }

    /**
     * 获取页面状态
     */
    @GetMapping("/page-state/{sessionId}/{module}")
    public ResultModel<Map<String, Object>> getPageState(
            @PathVariable String sessionId,
            @PathVariable String module) {
        log.info("[getPageState] sessionId: {}, module: {}", sessionId, module);
        
        Map<String, Object> state = contextService.getPageState(sessionId, module);
        return ResultModel.success(state);
    }

    /**
     * 获取知识库上下文
     */
    @PostMapping("/knowledge")
    public ResultModel<List<String>> getKnowledgeContext(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");
        Integer limit = (Integer) request.get("limit");
        if (limit == null) limit = 5;
        
        log.info("[getKnowledgeContext] query: {}, limit: {}", query, limit);
        
        List<String> context = contextService.getKnowledgeContext(query, limit);
        return ResultModel.success(context);
    }

    /**
     * 构建 AI 助手系统提示
     */
    @PostMapping("/system-prompt")
    public ResultModel<String> buildSystemPrompt(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        String skillId = request.get("skillId");
        
        log.info("[buildSystemPrompt] userId: {}, skillId: {}", userId, skillId);
        
        String prompt = contextService.buildSystemPrompt(userId, skillId);
        return ResultModel.success(prompt);
    }
}
