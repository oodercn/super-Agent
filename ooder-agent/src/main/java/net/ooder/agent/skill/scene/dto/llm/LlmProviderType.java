package net.ooder.mvp.skill.scene.dto.llm;

import java.util.Arrays;
import java.util.List;

public enum LlmProviderType {

    ALIYUN_BAILIAN("aliyun-bailian", "阿里百联", "阿里云百炼平台，提供通义千问等大模型服务",
            "https://dashscope.aliyuncs.com/compatible-mode/v1",
            Arrays.asList(
                    new ModelInfo("qwen-turbo", "通义千问 Turbo", 8192, 0.7, true, false, false, 0.002),
                    new ModelInfo("qwen-plus", "通义千问 Plus", 128000, 0.7, true, true, true, 0.004),
                    new ModelInfo("qwen-max", "通义千问 Max", 32000, 0.7, true, true, true, 0.02),
                    new ModelInfo("qwen-long", "通义千问 Long", 10000, 0.7, false, false, false, 0.0005),
                    new ModelInfo("qwen-max-longcontext", "通义千问 Max长上下文", 28000, 0.7, true, false, false, 0.04)
            )),

    QIANWEN("qianwen", "通义千问", "通义千问大语言模型，支持文本、图像、音频多模态",
            "https://dashscope.aliyuncs.com/api/v1",
            Arrays.asList(
                    new ModelInfo("qwen-max", "通义千问 Max", 32000, 0.7, true, true, true, 0.02),
                    new ModelInfo("qwen-plus", "通义千问 Plus", 128000, 0.7, true, true, true, 0.004),
                    new ModelInfo("qwen-turbo", "通义千问 Turbo", 8192, 0.7, true, false, false, 0.002),
                    new ModelInfo("qwen-long", "通义千问 Long", 10000, 0.7, false, false, false, 0.0005),
                    new ModelInfo("qwen-vl-max", "通义千问 VL Max", 8192, 0.7, true, true, false, 0.02),
                    new ModelInfo("qwen-vl-plus", "通义千问 VL Plus", 8192, 0.7, true, true, false, 0.008),
                    new ModelInfo("qwen-audio-turbo", "通义千问 Audio", 8192, 0.7, false, false, false, 0.002)
            )),

    DEEPSEEK("deepseek", "DeepSeek", "DeepSeek深度求索，专注代码和推理的大模型",
            "https://api.deepseek.com",
            Arrays.asList(
                    new ModelInfo("deepseek-chat", "DeepSeek Chat", 64000, 0.7, true, false, false, 0.001),
                    new ModelInfo("deepseek-coder", "DeepSeek Coder", 16000, 0.3, true, false, false, 0.001),
                    new ModelInfo("deepseek-reasoner", "DeepSeek Reasoner", 64000, 0.7, true, false, false, 0.002),
                    new ModelInfo("deepseek-reasoner-r1", "DeepSeek R1", 64000, 0.7, true, false, false, 0.003)
            )),

    BAIDU("baidu", "百度千帆", "百度千帆大模型平台，提供文心一言系列模型",
            "https://qianfan.baidubce.com/v2",
            Arrays.asList(
                    new ModelInfo("ernie-4.0-8k", "ERNIE 4.0 8K", 8000, 0.7, true, false, false, 0.12),
                    new ModelInfo("ernie-4.0-turbo-8k", "ERNIE 4.0 Turbo", 8000, 0.7, true, false, false, 0.04),
                    new ModelInfo("ernie-3.5-8k", "ERNIE 3.5 8K", 8000, 0.7, true, false, false, 0.012),
                    new ModelInfo("ernie-speed-8k", "ERNIE Speed 8K", 8000, 0.7, false, false, false, 0.004),
                    new ModelInfo("ernie-lite-8k", "ERNIE Lite 8K", 8000, 0.7, false, false, false, 0.003)
            )),

    OPENAI("openai", "OpenAI", "OpenAI官方API，提供GPT系列模型",
            "https://api.openai.com/v1",
            Arrays.asList(
                    new ModelInfo("gpt-4o", "GPT-4o", 128000, 0.7, true, true, false, 0.005),
                    new ModelInfo("gpt-4o-mini", "GPT-4o Mini", 128000, 0.7, true, true, false, 0.00015),
                    new ModelInfo("gpt-4-turbo", "GPT-4 Turbo", 128000, 0.7, true, true, false, 0.01),
                    new ModelInfo("gpt-4", "GPT-4", 8192, 0.7, true, false, false, 0.03),
                    new ModelInfo("gpt-3.5-turbo", "GPT-3.5 Turbo", 16000, 0.7, true, false, false, 0.0005),
                    new ModelInfo("o1-preview", "O1 Preview", 128000, 0.7, false, false, false, 0.015),
                    new ModelInfo("o1-mini", "O1 Mini", 128000, 0.7, false, false, false, 0.003)
            )),

    OLLAMA("ollama", "Ollama", "本地部署大模型运行工具，支持多种开源模型",
            "http://localhost:11434",
            Arrays.asList(
                    new ModelInfo("llama3.1", "Llama 3.1", 128000, 0.7, false, false, false, 0.0),
                    new ModelInfo("llama3", "Llama 3", 8192, 0.7, false, false, false, 0.0),
                    new ModelInfo("qwen2", "Qwen 2", 32768, 0.7, false, false, false, 0.0),
                    new ModelInfo("mistral", "Mistral", 32768, 0.7, false, false, false, 0.0),
                    new ModelInfo("codellama", "Code Llama", 16384, 0.3, false, false, false, 0.0),
                    new ModelInfo("deepseek-coder-v2", "DeepSeek Coder V2", 32768, 0.3, false, false, false, 0.0),
                    new ModelInfo("gemma2", "Gemma 2", 8192, 0.7, false, false, false, 0.0)
            )),

    ANTHROPIC("anthropic", "Anthropic Claude", "Anthropic Claude系列模型，擅长长文本理解",
            "https://api.anthropic.com",
            Arrays.asList(
                    new ModelInfo("claude-3-5-sonnet-20241022", "Claude 3.5 Sonnet", 200000, 0.7, true, true, false, 0.003),
                    new ModelInfo("claude-3-opus-20240229", "Claude 3 Opus", 200000, 0.7, true, true, false, 0.015),
                    new ModelInfo("claude-3-sonnet-20240229", "Claude 3 Sonnet", 200000, 0.7, true, true, false, 0.003),
                    new ModelInfo("claude-3-haiku-20240307", "Claude 3 Haiku", 200000, 0.7, true, false, false, 0.00025)
            )),

    GOOGLE("google", "Google Gemini", "Google Gemini系列模型，多模态能力强",
            "https://generativelanguage.googleapis.com",
            Arrays.asList(
                    new ModelInfo("gemini-1.5-pro", "Gemini 1.5 Pro", 1000000, 0.7, true, true, false, 0.0035),
                    new ModelInfo("gemini-1.5-flash", "Gemini 1.5 Flash", 1000000, 0.7, true, true, false, 0.000075),
                    new ModelInfo("gemini-1.0-pro", "Gemini 1.0 Pro", 32760, 0.7, true, false, false, 0.0005),
                    new ModelInfo("gemini-2.0-flash-exp", "Gemini 2.0 Flash", 1000000, 0.7, true, true, false, 0.0)
            )),

    ZHIPU("zhipu", "智谱AI", "智谱AI GLM系列模型，中英双语能力强",
            "https://open.bigmodel.cn/api/paas/v4",
            Arrays.asList(
                    new ModelInfo("glm-4-plus", "GLM-4 Plus", 128000, 0.7, true, false, false, 0.05),
                    new ModelInfo("glm-4-0520", "GLM-4 0520", 128000, 0.7, true, false, false, 0.1),
                    new ModelInfo("glm-4-air", "GLM-4 Air", 128000, 0.7, true, false, false, 0.001),
                    new ModelInfo("glm-4-airx", "GLM-4 AirX", 8192, 0.7, true, false, false, 0.001),
                    new ModelInfo("glm-4-flash", "GLM-4 Flash", 128000, 0.7, true, false, false, 0.0001),
                    new ModelInfo("glm-4v-plus", "GLM-4V Plus", 8192, 0.7, true, true, false, 0.01),
                    new ModelInfo("glm-4v", "GLM-4V", 8192, 0.7, true, true, false, 0.05)
            )),

    MINIMAX("minimax", "MiniMax", "MiniMax大模型，擅长对话和创作",
            "https://api.minimax.chat/v1",
            Arrays.asList(
                    new ModelInfo("abab6.5s-chat", "ABAB 6.5s Chat", 245000, 0.7, true, false, false, 0.001),
                    new ModelInfo("abab6.5g-chat", "ABAB 6.5g Chat", 245000, 0.7, true, false, false, 0.002),
                    new ModelInfo("abab6.5-chat", "ABAB 6.5 Chat", 245000, 0.7, true, false, false, 0.03),
                    new ModelInfo("abab5.5-chat", "ABAB 5.5 Chat", 16384, 0.7, true, false, false, 0.015),
                    new ModelInfo("abab5.5s-chat", "ABAB 5.5s Chat", 8192, 0.7, true, false, false, 0.005)
            )),

    MOONSHOT("moonshot", "月之暗面", "月之暗面Kimi，超长上下文处理能力",
            "https://api.moonshot.cn/v1",
            Arrays.asList(
                    new ModelInfo("moonshot-v1-8k", "Moonshot V1 8K", 8192, 0.7, true, false, false, 0.012),
                    new ModelInfo("moonshot-v1-32k", "Moonshot V1 32K", 32768, 0.7, true, false, false, 0.024),
                    new ModelInfo("moonshot-v1-128k", "Moonshot V1 128K", 131072, 0.7, true, false, false, 0.06)
            )),

    SILICONFLOW("siliconflow", "SiliconFlow", "SiliconFlow模型云平台，提供多种开源模型",
            "https://api.siliconflow.cn/v1",
            Arrays.asList(
                    new ModelInfo("Qwen/Qwen2.5-72B-Instruct", "Qwen2.5 72B", 32768, 0.7, false, false, false, 0.0),
                    new ModelInfo("Qwen/Qwen2.5-32B-Instruct", "Qwen2.5 32B", 32768, 0.7, false, false, false, 0.0),
                    new ModelInfo("deepseek-ai/DeepSeek-V2.5", "DeepSeek V2.5", 32768, 0.7, false, false, false, 0.0),
                    new ModelInfo("meta-llama/Llama-3.1-70B-Instruct", "Llama 3.1 70B", 32768, 0.7, false, false, false, 0.0)
            )),

    AZURE("azure", "Azure OpenAI", "Azure OpenAI服务，企业级OpenAI部署",
            "https://YOUR_RESOURCE.openai.azure.com",
            Arrays.asList(
                    new ModelInfo("gpt-4o", "GPT-4o", 128000, 0.7, true, true, false, 0.005),
                    new ModelInfo("gpt-4-turbo", "GPT-4 Turbo", 128000, 0.7, true, true, false, 0.01),
                    new ModelInfo("gpt-4", "GPT-4", 8192, 0.7, true, false, false, 0.03),
                    new ModelInfo("gpt-35-turbo", "GPT-3.5 Turbo", 16000, 0.7, true, false, false, 0.0005)
            )),

    MOCK("mock", "Mock (测试)", "测试用Mock驱动，用于开发调试",
            "",
            Arrays.asList(
                    new ModelInfo("mock-model", "Mock Model", 4096, 0.7, false, false, false, 0.0)
            ));

    private final String code;
    private final String displayName;
    private final String description;
    private final String defaultBaseUrl;
    private final List<ModelInfo> models;

    LlmProviderType(String code, String displayName, String description, String defaultBaseUrl, List<ModelInfo> models) {
        this.code = code;
        this.displayName = displayName;
        this.description = description;
        this.defaultBaseUrl = defaultBaseUrl;
        this.models = models;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getDefaultBaseUrl() {
        return defaultBaseUrl;
    }

    public List<ModelInfo> getModels() {
        return models;
    }

    public static LlmProviderType fromCode(String code) {
        for (LlmProviderType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }

    public static List<String> getAllProviderCodes() {
        return Arrays.stream(values())
                .map(LlmProviderType::getCode)
                .collect(java.util.stream.Collectors.toList());
    }

    public static class ModelInfo {
        private final String modelId;
        private final String displayName;
        private final int maxTokens;
        private final double defaultTemperature;
        private final boolean supportsFunctionCalling;
        private final boolean supportsMultimodal;
        private final boolean supportsEmbedding;
        private final double costPer1kTokens;

        public ModelInfo(String modelId, String displayName, int maxTokens, double defaultTemperature,
                         boolean supportsFunctionCalling, boolean supportsMultimodal, boolean supportsEmbedding,
                         double costPer1kTokens) {
            this.modelId = modelId;
            this.displayName = displayName;
            this.maxTokens = maxTokens;
            this.defaultTemperature = defaultTemperature;
            this.supportsFunctionCalling = supportsFunctionCalling;
            this.supportsMultimodal = supportsMultimodal;
            this.supportsEmbedding = supportsEmbedding;
            this.costPer1kTokens = costPer1kTokens;
        }

        public String getModelId() {
            return modelId;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getMaxTokens() {
            return maxTokens;
        }

        public double getDefaultTemperature() {
            return defaultTemperature;
        }

        public boolean isSupportsFunctionCalling() {
            return supportsFunctionCalling;
        }

        public boolean isSupportsMultimodal() {
            return supportsMultimodal;
        }

        public boolean isSupportsEmbedding() {
            return supportsEmbedding;
        }

        public double getCostPer1kTokens() {
            return costPer1kTokens;
        }
    }
}
