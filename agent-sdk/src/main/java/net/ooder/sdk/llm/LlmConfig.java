package net.ooder.sdk.llm;

import java.time.Duration;

public class LlmConfig {
    private String endpoint;
    private String apiKey;
    private String modelName;
    private Duration timeout = Duration.ofSeconds(30);
    private int maxRetries = 3;
    private Duration retryDelay = Duration.ofSeconds(1);
    private boolean enableCompression = false;

    private LlmConfig() {
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getModelName() {
        return modelName;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public Duration getRetryDelay() {
        return retryDelay;
    }

    public boolean isEnableCompression() {
        return enableCompression;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final LlmConfig config = new LlmConfig();

        public Builder endpoint(String endpoint) {
            config.endpoint = endpoint;
            return this;
        }

        public Builder apiKey(String apiKey) {
            config.apiKey = apiKey;
            return this;
        }

        public Builder modelName(String modelName) {
            config.modelName = modelName;
            return this;
        }

        public Builder timeout(Duration timeout) {
            config.timeout = timeout;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            config.maxRetries = maxRetries;
            return this;
        }

        public Builder retryDelay(Duration retryDelay) {
            config.retryDelay = retryDelay;
            return this;
        }

        public Builder enableCompression(boolean enableCompression) {
            config.enableCompression = enableCompression;
            return this;
        }

        public LlmConfig build() {
            return config;
        }
    }
}