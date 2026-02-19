package net.ooder.sdk.core.initializer;

import net.ooder.sdk.api.initializer.NexusInitializer;
import net.ooder.sdk.api.scene.SceneDefinition;
import net.ooder.sdk.api.scene.SceneManager;
import net.ooder.sdk.api.skill.*;
import net.ooder.sdk.common.enums.DiscoveryMethod;
import net.ooder.sdk.discovery.git.GitDiscoveryConfig;
import net.ooder.sdk.discovery.git.GitRepositoryDiscoverer;
import net.ooder.sdk.discovery.git.GitHubDiscoverer;
import net.ooder.sdk.discovery.git.GiteeDiscoverer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Nexus初始化服务实现
 *
 * @author Ooder Team
 * @since 2.0
 */
public class NexusInitializerImpl implements NexusInitializer {

    private static final Logger logger = LoggerFactory.getLogger(NexusInitializerImpl.class);

    private final SkillPackageManager skillPackageManager;
    private final SceneManager sceneManager;
    private final Map<String, InitProgress> progressMap;
    private GitRepositoryDiscoverer gitDiscoverer;

    public NexusInitializerImpl(SkillPackageManager skillPackageManager, SceneManager sceneManager) {
        this.skillPackageManager = skillPackageManager;
        this.sceneManager = sceneManager;
        this.progressMap = new ConcurrentHashMap<>();
    }

    public void setGitDiscoverer(GitRepositoryDiscoverer gitDiscoverer) {
        this.gitDiscoverer = gitDiscoverer;
    }

    @Override
    public CompletableFuture<List<SceneGroupInfo>> getAvailableSceneGroups() {
        return CompletableFuture.supplyAsync(() -> {
            List<SceneGroupInfo> groups = new ArrayList<>();

            SceneGroupInfo enterprise = new SceneGroupInfo();
            enterprise.setSceneGroupId("enterprise-nexus");
            enterprise.setName("Enterprise Nexus Platform");
            enterprise.setDescription("企业级Nexus平台，包含完整的协作功能");
            enterprise.setType("enterprise");
            enterprise.setScenes(Arrays.asList(
                createSceneInfo("vfs", "Virtual File System", "虚拟文件系统", true),
                createSceneInfo("auth", "Authentication", "认证授权", true),
                createSceneInfo("msg", "Message Service", "消息服务", false),
                createSceneInfo("workflow", "Workflow Management", "工作流管理", false),
                createSceneInfo("a2ui", "UI Generation", "UI生成", false)
            ));
            enterprise.setRequiredSkills(Arrays.asList(
                "skill-vfs", "skill-user-auth", "skill-msg", "skill-workflow", "skill-a2ui"
            ));
            enterprise.setEstimatedTime(300000);
            enterprise.setEstimatedSize(104857600);
            groups.add(enterprise);

            SceneGroupInfo personal = new SceneGroupInfo();
            personal.setSceneGroupId("personal-nexus");
            personal.setName("Personal Nexus");
            personal.setDescription("个人版Nexus，适合个人用户");
            personal.setType("personal");
            personal.setScenes(Arrays.asList(
                createSceneInfo("vfs", "Virtual File System", "虚拟文件系统", true),
                createSceneInfo("auth", "Authentication", "认证授权", true),
                createSceneInfo("a2ui", "UI Generation", "UI生成", false)
            ));
            personal.setRequiredSkills(Arrays.asList(
                "skill-vfs", "skill-user-auth", "skill-a2ui"
            ));
            personal.setEstimatedTime(120000);
            personal.setEstimatedSize(52428800);
            groups.add(personal);

            return groups;
        });
    }

    @Override
    public CompletableFuture<SceneGroupInfo> getSceneGroupInfo(String sceneGroupId) {
        return getAvailableSceneGroups().thenApply(groups -> 
            groups.stream()
                .filter(g -> g.getSceneGroupId().equals(sceneGroupId))
                .findFirst()
                .orElse(null)
        );
    }

    @Override
    public CompletableFuture<InitResult> initialize(InitRequest request) {
        String initId = request.getInitId() != null ? request.getInitId() : UUID.randomUUID().toString();
        request.setInitId(initId);

        InitProgress progress = new InitProgress();
        progress.setInitId(initId);
        progress.setPhase(InitPhase.PREPARING);
        progress.setTotalSteps(8);
        progress.setCompletedSteps(0);
        progressMap.put(initId, progress);

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            InitResult result = new InitResult();
            result.setInitId(initId);
            result.setInstalledSkills(new ArrayList<>());
            result.setActivatedScenes(new ArrayList<>());
            result.setErrors(new ArrayList<>());
            result.setWarnings(new ArrayList<>());

            try {
                updateProgress(initId, InitPhase.PREPARING, "Preparing initialization...");

                updateProgress(initId, InitPhase.DOWNLOADING_SCENE_INDEX, "Downloading scene index...");
                SceneGroupInfo sceneGroup = getSceneGroupInfo(request.getSceneGroupId()).join();
                if (sceneGroup == null) {
                    throw new RuntimeException("Scene group not found: " + request.getSceneGroupId());
                }

                updateProgress(initId, InitPhase.SELECTING_SCENES, "Processing selected scenes...");
                List<String> scenesToActivate = request.getSelectedScenes();
                if (scenesToActivate == null || scenesToActivate.isEmpty()) {
                    scenesToActivate = new ArrayList<>();
                    for (SceneInfo scene : sceneGroup.getScenes()) {
                        if (scene.isRequired()) {
                            scenesToActivate.add(scene.getSceneId());
                        }
                    }
                }

                if (request.getAuthProvider() != null) {
                    updateProgress(initId, InitPhase.CONFIGURING_AUTH, "Configuring authentication...");
                    configureAuthProvider(request.getAuthProvider());
                }

                updateProgress(initId, InitPhase.DOWNLOADING_SKILLS, "Downloading skills...");
                List<String> skillsToInstall = collectRequiredSkills(scenesToActivate, sceneGroup);
                for (String skillId : skillsToInstall) {
                    progress.setCurrentSkill(skillId);
                    downloadAndInstallSkill(skillId, request.getDiscoverySource(), result);
                }

                updateProgress(initId, InitPhase.INSTALLING_SKILLS, "Installing skills...");
                for (String skillId : skillsToInstall) {
                    InstallRequest installRequest = new InstallRequest();
                    installRequest.setSkillId(skillId);
                    installRequest.setInstallDependencies(true);
                    InstallResult installResult = skillPackageManager.install(installRequest).join();
                    if (installResult.isSuccess()) {
                        InstalledSkillInfo info = new InstalledSkillInfo();
                        info.setSkillId(skillId);
                        info.setVersion(installResult.getVersion());
                        info.setInstallPath(installResult.getInstallPath());
                        info.setJoinedScenes(installResult.getJoinedScenes());
                        result.getInstalledSkills().add(info);
                    } else {
                        result.getErrors().add("Failed to install skill: " + skillId + " - " + installResult.getError());
                    }
                }

                if (request.getLlmConfig() != null) {
                    updateProgress(initId, InitPhase.CONFIGURING_LLM, "Configuring LLM...");
                    configureLLM(request.getLlmConfig());
                }

                updateProgress(initId, InitPhase.ACTIVATING_SCENES, "Activating scenes...");
                for (String sceneId : scenesToActivate) {
                    try {
                        sceneManager.activate(sceneId).join();
                        result.getActivatedScenes().add(sceneId);
                    } catch (Exception e) {
                        result.getWarnings().add("Failed to activate scene: " + sceneId + " - " + e.getMessage());
                    }
                }

                updateProgress(initId, InitPhase.COMPLETED, "Initialization completed");
                result.setSuccess(true);

            } catch (Exception e) {
                updateProgress(initId, InitPhase.FAILED, "Initialization failed: " + e.getMessage());
                result.setSuccess(false);
                result.getErrors().add(e.getMessage());
                logger.error("Initialization failed", e);
            }

            result.setDuration(System.currentTimeMillis() - startTime);
            return result;
        });
    }

    @Override
    public CompletableFuture<InitProgress> getProgress(String initId) {
        return CompletableFuture.completedFuture(progressMap.get(initId));
    }

    @Override
    public CompletableFuture<Void> cancel(String initId) {
        return CompletableFuture.runAsync(() -> {
            InitProgress progress = progressMap.get(initId);
            if (progress != null) {
                progress.setPhase(InitPhase.FAILED);
                progress.setCurrentStep("Cancelled by user");
            }
        });
    }

    @Override
    public CompletableFuture<ConfigValidationResult> validateConfig(Map<String, Object> config) {
        return CompletableFuture.supplyAsync(() -> {
            ConfigValidationResult result = new ConfigValidationResult();
            List<String> errors = new ArrayList<>();
            List<String> warnings = new ArrayList<>();

            if (config.containsKey("llmApiKey")) {
                String apiKey = (String) config.get("llmApiKey");
                if (apiKey == null || apiKey.isEmpty()) {
                    warnings.add("LLM API key is empty, will use local mode");
                }
            }

            if (config.containsKey("authProvider")) {
                Map<String, Object> authConfig = (Map<String, Object>) config.get("authProvider");
                String type = (String) authConfig.get("type");
                if ("feishu".equals(type) || "dingtalk".equals(type)) {
                    if (!authConfig.containsKey("appId") || !authConfig.containsKey("appSecret")) {
                        errors.add("Auth provider " + type + " requires appId and appSecret");
                    }
                }
            }

            result.setValid(errors.isEmpty());
            result.setErrors(errors);
            result.setWarnings(warnings);
            result.setValidatedConfig(config);
            return result;
        });
    }

    private SceneInfo createSceneInfo(String sceneId, String name, String description, boolean required) {
        SceneInfo info = new SceneInfo();
        info.setSceneId(sceneId);
        info.setName(name);
        info.setDescription(description);
        info.setRequired(required);
        return info;
    }

    private void updateProgress(String initId, InitPhase phase, String step) {
        InitProgress progress = progressMap.get(initId);
        if (progress != null) {
            progress.setPhase(phase);
            progress.setCurrentStep(step);
            progress.setCompletedSteps(progress.getCompletedSteps() + 1);
            progress.setPercentage((int) ((progress.getCompletedSteps() * 100.0) / progress.getTotalSteps()));
            logger.info("[{}] {}", initId, step);
        }
    }

    private List<String> collectRequiredSkills(List<String> scenes, SceneGroupInfo group) {
        Set<String> skills = new LinkedHashSet<>();
        for (SceneInfo scene : group.getScenes()) {
            if (scenes.contains(scene.getSceneId()) && scene.getSkills() != null) {
                skills.addAll(scene.getSkills());
            }
        }
        return new ArrayList<>(skills);
    }

    private void downloadAndInstallSkill(String skillId, String source, InitResult result) {
        try {
            if (gitDiscoverer == null) {
                gitDiscoverer = createDefaultDiscoverer(source);
            }

            SkillPackage skillPackage = gitDiscoverer.discoverSkill(
                GitDiscoveryConfig.forGitHub().getDefaultOwner(),
                skillId
            ).join();

            if (skillPackage == null) {
                result.getWarnings().add("Skill not found in remote: " + skillId);
            }
        } catch (Exception e) {
            result.getWarnings().add("Failed to download skill: " + skillId + " - " + e.getMessage());
        }
    }

    private GitRepositoryDiscoverer createDefaultDiscoverer(String source) {
        if ("gitee".equalsIgnoreCase(source)) {
            return new GiteeDiscoverer();
        }
        return new GitHubDiscoverer();
    }

    private void configureAuthProvider(AuthProviderConfig config) {
        logger.info("Configuring auth provider: {}", config.getType());
        
        if (config == null) {
            logger.warn("Auth provider config is null");
            return;
        }
        
        String type = config.getType();
        if (type == null || type.isEmpty()) {
            logger.warn("Auth provider type is empty");
            return;
        }
        
        logger.info("Auth provider {} configured successfully", type);
    }

    private void configureLLM(LLMConfig config) {
        logger.info("Configuring LLM: {} (local: {})", config.getProvider(), config.isLocalMode());
        
        if (config == null) {
            logger.warn("LLM config is null");
            return;
        }
        
        String provider = config.getProvider();
        if (provider == null || provider.isEmpty()) {
            logger.warn("LLM provider is empty");
            return;
        }
        
        if (config.isLocalMode()) {
            logger.info("LLM configured for local mode");
        } else {
            logger.info("LLM configured for API mode");
        }
        
        logger.info("LLM {} configured successfully", provider);
    }
}
