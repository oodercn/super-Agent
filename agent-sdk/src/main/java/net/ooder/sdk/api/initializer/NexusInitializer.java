package net.ooder.sdk.api.initializer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Nexus初始化服务接口
 *
 * @author Ooder Team
 * @since 2.0
 */
public interface NexusInitializer {

    /**
     * 获取可用的场景组列表
     */
    CompletableFuture<List<SceneGroupInfo>> getAvailableSceneGroups();

    /**
     * 获取场景组详情
     */
    CompletableFuture<SceneGroupInfo> getSceneGroupInfo(String sceneGroupId);

    /**
     * 开始初始化流程
     */
    CompletableFuture<InitResult> initialize(InitRequest request);

    /**
     * 获取初始化进度
     */
    CompletableFuture<InitProgress> getProgress(String initId);

    /**
     * 取消初始化
     */
    CompletableFuture<Void> cancel(String initId);

    /**
     * 验证配置
     */
    CompletableFuture<ConfigValidationResult> validateConfig(Map<String, Object> config);

    /**
     * 场景组信息
     */
    class SceneGroupInfo {
        private String sceneGroupId;
        private String name;
        private String description;
        private String type;
        private List<SceneInfo> scenes;
        private List<String> requiredSkills;
        private long estimatedTime;
        private long estimatedSize;

        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public List<SceneInfo> getScenes() { return scenes; }
        public void setScenes(List<SceneInfo> scenes) { this.scenes = scenes; }
        public List<String> getRequiredSkills() { return requiredSkills; }
        public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills; }
        public long getEstimatedTime() { return estimatedTime; }
        public void setEstimatedTime(long estimatedTime) { this.estimatedTime = estimatedTime; }
        public long getEstimatedSize() { return estimatedSize; }
        public void setEstimatedSize(long estimatedSize) { this.estimatedSize = estimatedSize; }
    }

    /**
     * 场景信息
     */
    class SceneInfo {
        private String sceneId;
        private String name;
        private String description;
        private boolean required;
        private List<String> capabilities;
        private List<String> skills;

        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public List<String> getCapabilities() { return capabilities; }
        public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
        public List<String> getSkills() { return skills; }
        public void setSkills(List<String> skills) { this.skills = skills; }
    }

    /**
     * 初始化请求
     */
    class InitRequest {
        private String initId;
        private String sceneGroupId;
        private List<String> selectedScenes;
        private Map<String, Object> config;
        private LLMConfig llmConfig;
        private AuthProviderConfig authProvider;
        private String discoverySource;

        public String getInitId() { return initId; }
        public void setInitId(String initId) { this.initId = initId; }
        public String getSceneGroupId() { return sceneGroupId; }
        public void setSceneGroupId(String sceneGroupId) { this.sceneGroupId = sceneGroupId; }
        public List<String> getSelectedScenes() { return selectedScenes; }
        public void setSelectedScenes(List<String> selectedScenes) { this.selectedScenes = selectedScenes; }
        public Map<String, Object> getConfig() { return config; }
        public void setConfig(Map<String, Object> config) { this.config = config; }
        public LLMConfig getLlmConfig() { return llmConfig; }
        public void setLlmConfig(LLMConfig llmConfig) { this.llmConfig = llmConfig; }
        public AuthProviderConfig getAuthProvider() { return authProvider; }
        public void setAuthProvider(AuthProviderConfig authProvider) { this.authProvider = authProvider; }
        public String getDiscoverySource() { return discoverySource; }
        public void setDiscoverySource(String discoverySource) { this.discoverySource = discoverySource; }
    }

    /**
     * LLM配置
     */
    class LLMConfig {
        private String provider;
        private String apiKey;
        private String baseUrl;
        private String model;
        private boolean localMode;

        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public boolean isLocalMode() { return localMode; }
        public void setLocalMode(boolean localMode) { this.localMode = localMode; }
    }

    /**
     * 认证提供者配置
     */
    class AuthProviderConfig {
        private String type;
        private String appId;
        private String appSecret;
        private String corpId;
        private Map<String, Object> extraConfig;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getAppId() { return appId; }
        public void setAppId(String appId) { this.appId = appId; }
        public String getAppSecret() { return appSecret; }
        public void setAppSecret(String appSecret) { this.appSecret = appSecret; }
        public String getCorpId() { return corpId; }
        public void setCorpId(String corpId) { this.corpId = corpId; }
        public Map<String, Object> getExtraConfig() { return extraConfig; }
        public void setExtraConfig(Map<String, Object> extraConfig) { this.extraConfig = extraConfig; }
    }

    /**
     * 初始化结果
     */
    class InitResult {
        private String initId;
        private boolean success;
        private List<InstalledSkillInfo> installedSkills;
        private List<String> activatedScenes;
        private List<String> errors;
        private List<String> warnings;
        private long duration;

        public String getInitId() { return initId; }
        public void setInitId(String initId) { this.initId = initId; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public List<InstalledSkillInfo> getInstalledSkills() { return installedSkills; }
        public void setInstalledSkills(List<InstalledSkillInfo> installedSkills) { this.installedSkills = installedSkills; }
        public List<String> getActivatedScenes() { return activatedScenes; }
        public void setActivatedScenes(List<String> activatedScenes) { this.activatedScenes = activatedScenes; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
    }

    /**
     * 已安装技能信息
     */
    class InstalledSkillInfo {
        private String skillId;
        private String version;
        private String installPath;
        private List<String> joinedScenes;

        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getInstallPath() { return installPath; }
        public void setInstallPath(String installPath) { this.installPath = installPath; }
        public List<String> getJoinedScenes() { return joinedScenes; }
        public void setJoinedScenes(List<String> joinedScenes) { this.joinedScenes = joinedScenes; }
    }

    /**
     * 初始化进度
     */
    class InitProgress {
        private String initId;
        private InitPhase phase;
        private int totalSteps;
        private int completedSteps;
        private String currentStep;
        private String currentSkill;
        private int percentage;
        private long elapsedTime;
        private long estimatedRemaining;

        public String getInitId() { return initId; }
        public void setInitId(String initId) { this.initId = initId; }
        public InitPhase getPhase() { return phase; }
        public void setPhase(InitPhase phase) { this.phase = phase; }
        public int getTotalSteps() { return totalSteps; }
        public void setTotalSteps(int totalSteps) { this.totalSteps = totalSteps; }
        public int getCompletedSteps() { return completedSteps; }
        public void setCompletedSteps(int completedSteps) { this.completedSteps = completedSteps; }
        public String getCurrentStep() { return currentStep; }
        public void setCurrentStep(String currentStep) { this.currentStep = currentStep; }
        public String getCurrentSkill() { return currentSkill; }
        public void setCurrentSkill(String currentSkill) { this.currentSkill = currentSkill; }
        public int getPercentage() { return percentage; }
        public void setPercentage(int percentage) { this.percentage = percentage; }
        public long getElapsedTime() { return elapsedTime; }
        public void setElapsedTime(long elapsedTime) { this.elapsedTime = elapsedTime; }
        public long getEstimatedRemaining() { return estimatedRemaining; }
        public void setEstimatedRemaining(long estimatedRemaining) { this.estimatedRemaining = estimatedRemaining; }
    }

    /**
     * 初始化阶段
     */
    enum InitPhase {
        PREPARING,
        DOWNLOADING_SCENE_INDEX,
        SELECTING_SCENES,
        CONFIGURING_AUTH,
        DOWNLOADING_SKILLS,
        INSTALLING_SKILLS,
        CONFIGURING_LLM,
        ACTIVATING_SCENES,
        COMPLETED,
        FAILED
    }

    /**
     * 配置验证结果
     */
    class ConfigValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;
        private Map<String, Object> validatedConfig;

        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
        public List<String> getWarnings() { return warnings; }
        public void setWarnings(List<String> warnings) { this.warnings = warnings; }
        public Map<String, Object> getValidatedConfig() { return validatedConfig; }
        public void setValidatedConfig(Map<String, Object> validatedConfig) { this.validatedConfig = validatedConfig; }
    }
}
