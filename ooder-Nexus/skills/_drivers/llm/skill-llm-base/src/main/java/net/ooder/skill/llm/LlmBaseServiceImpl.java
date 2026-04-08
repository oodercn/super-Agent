package net.ooder.skill.llm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class LlmBaseServiceImpl implements LlmService {

    private static final Logger log = LoggerFactory.getLogger(LlmBaseServiceImpl.class);

    private final Map<String, LlmProviderDTO> providers = new LinkedHashMap<>();
    private final Map<String, LlmModelDTO> models = new LinkedHashMap<>();
    private LlmConfigDTO config;

    public LlmBaseServiceImpl() {
        initDefaultProviders();
        initDefaultConfig();
        log.info("[LlmBaseService] Initialized with {} providers", providers.size());
    }

    private void initDefaultProviders() {
        LlmProviderDTO deepseek = new LlmProviderDTO();
        deepseek.setId("deepseek");
        deepseek.setName("DeepSeek");
        deepseek.setType("chat");
        deepseek.setEnabled(true);
        deepseek.setModels(Arrays.asList(
            createModel("deepseek-chat", "DeepSeek Chat", "deepseek", "通用对话模型", 64000),
            createModel("deepseek-coder", "DeepSeek Coder", "deepseek", "代码生成模型", 16000),
            createModel("deepseek-reasoner", "DeepSeek Reasoner", "deepseek", "推理模型", 64000)
        ));
        providers.put("deepseek", deepseek);

        LlmProviderDTO qianwen = new LlmProviderDTO();
        qianwen.setId("qianwen");
        qianwen.setName("通义千问");
        qianwen.setType("chat");
        qianwen.setEnabled(false);
        qianwen.setModels(Arrays.asList(
            createModel("qwen-turbo", "通义千问 Turbo", "qianwen", "快速响应模型", 8192),
            createModel("qwen-plus", "通义千问 Plus", "qianwen", "增强模型", 32768),
            createModel("qwen-max", "通义千问 Max", "qianwen", "旗舰模型", 32768)
        ));
        providers.put("qianwen", qianwen);

        LlmProviderDTO ollama = new LlmProviderDTO();
        ollama.setId("ollama");
        ollama.setName("Ollama (本地)");
        ollama.setType("local");
        ollama.setEnabled(false);
        ollama.setModels(Arrays.asList(
            createModel("llama3", "Llama 3", "ollama", "Meta开源模型", 8192),
            createModel("qwen2", "Qwen 2", "ollama", "阿里开源模型", 8192)
        ));
        providers.put("ollama", ollama);

        for (LlmProviderDTO provider : providers.values()) {
            if (provider.getModels() != null) {
                for (LlmModelDTO model : provider.getModels()) {
                    models.put(model.getId(), model);
                }
            }
        }
    }

    private LlmModelDTO createModel(String id, String name, String providerId, String description, int maxTokens) {
        LlmModelDTO model = new LlmModelDTO();
        model.setId(id);
        model.setName(name);
        model.setProviderId(providerId);
        model.setDescription(description);
        model.setMaxTokens(maxTokens);
        model.setSupportsStreaming(true);
        model.setSupportsFunctionCalling(true);
        return model;
    }

    private void initDefaultConfig() {
        config = new LlmConfigDTO();
        config.setProvider("deepseek");
        config.setModel("deepseek-chat");
        config.setTemperature(0.7);
        config.setMaxTokens(4096);
        config.setStreamEnabled(true);
    }

    @Override
    public String getSkillId() {
        return "skill-llm-base";
    }

    @Override
    public List<LlmProviderDTO> getProviders() {
        return new ArrayList<>(providers.values());
    }

    @Override
    public List<LlmModelDTO> getModels() {
        return new ArrayList<>(models.values());
    }

    @Override
    public LlmModelDTO getModel(String modelId) {
        return models.get(modelId);
    }

    @Override
    public LlmConfigDTO getConfig() {
        return config;
    }

    @Override
    public void updateConfig(LlmConfigDTO config) {
        this.config = config;
        log.info("[LlmBaseService] Updated config: provider={}, model={}", config.getProvider(), config.getModel());
    }

    @Override
    public String getDefaultProvider() {
        return config != null ? config.getProvider() : "deepseek";
    }

    @Override
    public String getDefaultModel() {
        return config != null ? config.getModel() : "deepseek-chat";
    }
}
