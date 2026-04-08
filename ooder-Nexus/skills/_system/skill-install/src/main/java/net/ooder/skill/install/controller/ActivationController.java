package net.ooder.skill.install.controller;

import net.ooder.skill.install.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/activations")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class ActivationController {

    private static final Logger log = LoggerFactory.getLogger(ActivationController.class);
    
    private static final Map<String, ActivationProcess> activeProcesses = new ConcurrentHashMap<>();

    @PostMapping("/{installId}/start-with-template")
    public ResultModel<Map<String, Object>> startWithTemplate(
            @PathVariable String installId,
            @RequestBody Map<String, Object> request) {
        
        log.info("[startWithTemplate] Starting activation with installId: {}, request: {}", installId, request);
        
        String templateId = (String) request.get("templateId");
        String roleName = (String) request.get("roleName");
        String activator = (String) request.getOrDefault("activator", "default-user");
        
        ActivationProcess process = new ActivationProcess();
        process.setInstallId(installId);
        process.setTemplateId(templateId);
        process.setRoleName(roleName);
        process.setActivator(activator);
        process.setStatus("PENDING");
        process.setCurrentStep(0);
        process.setTotalSteps(5);
        process.setCreateTime(System.currentTimeMillis());
        
        List<ActivationStep> steps = createDefaultSteps();
        process.setSteps(steps);
        
        activeProcesses.put(installId, process);
        
        Map<String, Object> result = new HashMap<>();
        result.put("installId", installId);
        result.put("status", "PENDING");
        result.put("message", "Activation initialized successfully");
        
        return ResultModel.success(result);
    }

    @PostMapping("/{installId}/auto-activate")
    public ResultModel<Map<String, Object>> autoActivate(@PathVariable String installId) {
        log.info("[autoActivate] Auto-activating: {}", installId);
        
        ActivationProcess process = activeProcesses.get(installId);
        if (process == null) {
            process = new ActivationProcess();
            process.setInstallId(installId);
            process.setStatus("IN_PROGRESS");
            process.setCurrentStep(0);
            process.setTotalSteps(5);
            process.setSteps(createDefaultSteps());
            activeProcesses.put(installId, process);
        }
        
        startActivationAsync(installId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("installId", installId);
        result.put("status", "IN_PROGRESS");
        result.put("message", "Auto-activation started");
        
        return ResultModel.success(result);
    }

    @GetMapping("/{installId}/process")
    public ResultModel<ActivationProcess> getProcess(@PathVariable String installId) {
        log.debug("[getProcess] Getting process for: {}", installId);
        
        ActivationProcess process = activeProcesses.get(installId);
        if (process == null) {
            process = new ActivationProcess();
            process.setInstallId(installId);
            process.setStatus("PENDING");
            process.setCurrentStep(0);
            process.setTotalSteps(5);
            process.setSteps(createDefaultSteps());
            activeProcesses.put(installId, process);
        }
        
        return ResultModel.success(process);
    }

    @GetMapping("/{installId}/stream")
    public SseEmitter streamProcess(@PathVariable String installId) {
        log.info("[streamProcess] Starting SSE stream for: {}", installId);
        
        SseEmitter emitter = new SseEmitter(300000L);
        
        emitter.onCompletion(() -> log.info("SSE completed for: {}", installId));
        emitter.onTimeout(() -> {
            log.info("SSE timeout for: {}", installId);
            emitter.complete();
        });
        emitter.onError(e -> log.error("SSE error for: {}", installId, e));
        
        new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    ActivationProcess process = activeProcesses.get(installId);
                    if (process != null) {
                        if (process.getStatus().equals("COMPLETED") || 
                            process.getStatus().equals("FAILED")) {
                            emitter.send(SseEmitter.event()
                                .name("complete")
                                .data(process));
                            emitter.complete();
                            break;
                        }
                        emitter.send(SseEmitter.event()
                            .name("update")
                            .data(process));
                    }
                    Thread.sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                log.error("Error in SSE stream", e);
                emitter.completeWithError(e);
            }
        }).start();
        
        return emitter;
    }

    @PostMapping("/{installId}/cancel")
    public ResultModel<Map<String, Object>> cancelActivation(@PathVariable String installId) {
        log.info("[cancelActivation] Cancelling: {}", installId);
        
        ActivationProcess process = activeProcesses.get(installId);
        if (process != null) {
            process.setStatus("CANCELLED");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("installId", installId);
        result.put("status", "CANCELLED");
        result.put("message", "Activation cancelled");
        
        return ResultModel.success(result);
    }

    private void startActivationAsync(String installId) {
        new Thread(() -> {
            try {
                ActivationProcess process = activeProcesses.get(installId);
                if (process == null) return;
                
                process.setStatus("IN_PROGRESS");
                List<ActivationStep> steps = process.getSteps();
                
                for (int i = 0; i < steps.size(); i++) {
                    Thread.sleep(1500);
                    
                    if (process.getStatus().equals("CANCELLED")) {
                        break;
                    }
                    
                    ActivationStep step = steps.get(i);
                    step.setStatus("IN_PROGRESS");
                    process.setCurrentStep(i + 1);
                    
                    Thread.sleep(1000);
                    
                    step.setStatus("COMPLETED");
                }
                
                if (!process.getStatus().equals("CANCELLED")) {
                    process.setStatus("COMPLETED");
                    process.setCompleteTime(System.currentTimeMillis());
                }
                
            } catch (InterruptedException e) {
                log.error("Activation interrupted", e);
                ActivationProcess process = activeProcesses.get(installId);
                if (process != null) {
                    process.setStatus("FAILED");
                }
            }
        }).start();
    }

    private List<ActivationStep> createDefaultSteps() {
        List<ActivationStep> steps = new ArrayList<>();
        
        steps.add(new ActivationStep("step-1", "初始化环境", "PENDING", "准备激活环境"));
        steps.add(new ActivationStep("step-2", "加载配置", "PENDING", "加载技能配置"));
        steps.add(new ActivationStep("step-3", "验证依赖", "PENDING", "验证依赖项"));
        steps.add(new ActivationStep("step-4", "执行激活", "PENDING", "执行激活操作"));
        steps.add(new ActivationStep("step-5", "完成注册", "PENDING", "完成注册流程"));
        
        return steps;
    }

    public static class ActivationProcess {
        private String installId;
        private String templateId;
        private String roleName;
        private String activator;
        private String status;
        private int currentStep;
        private int totalSteps;
        private List<ActivationStep> steps;
        private long createTime;
        private long completeTime;

        public String getInstallId() { return installId; }
        public void setInstallId(String installId) { this.installId = installId; }
        
        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        
        public String getRoleName() { return roleName; }
        public void setRoleName(String roleName) { this.roleName = roleName; }
        
        public String getActivator() { return activator; }
        public void setActivator(String activator) { this.activator = activator; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public int getCurrentStep() { return currentStep; }
        public void setCurrentStep(int currentStep) { this.currentStep = currentStep; }
        
        public int getTotalSteps() { return totalSteps; }
        public void setTotalSteps(int totalSteps) { this.totalSteps = totalSteps; }
        
        public List<ActivationStep> getSteps() { return steps; }
        public void setSteps(List<ActivationStep> steps) { this.steps = steps; }
        
        public long getCreateTime() { return createTime; }
        public void setCreateTime(long createTime) { this.createTime = createTime; }
        
        public long getCompleteTime() { return completeTime; }
        public void setCompleteTime(long completeTime) { this.completeTime = completeTime; }
    }

    public static class ActivationStep {
        private String stepId;
        private String name;
        private String status;
        private String description;

        public ActivationStep() {}

        public ActivationStep(String stepId, String name, String status, String description) {
            this.stepId = stepId;
            this.name = name;
            this.status = status;
            this.description = description;
        }

        public String getStepId() { return stepId; }
        public void setStepId(String stepId) { this.stepId = stepId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
