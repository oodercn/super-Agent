package net.ooder.sdk.llm;

import java.util.List;
import java.util.Map;

public class LlmResponse {
    private String status;
    private List<GeneratedCode> generatedCodes;
    private Metadata metadata;
    private String errorMessage;

    public LlmResponse() {
    }

    public LlmResponse(String status, List<GeneratedCode> generatedCodes, Metadata metadata, String errorMessage) {
        this.status = status;
        this.generatedCodes = generatedCodes;
        this.metadata = metadata;
        this.errorMessage = errorMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GeneratedCode> getGeneratedCodes() {
        return generatedCodes;
    }

    public void setGeneratedCodes(List<GeneratedCode> generatedCodes) {
        this.generatedCodes = generatedCodes;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }

    public static class GeneratedCode {
        private String agentId;
        private String language;
        private String content;
        private String checksum;
        private Map<String, String> endpoints;

        public GeneratedCode() {
        }

        public GeneratedCode(String agentId, String language, String content, String checksum, Map<String, String> endpoints) {
            this.agentId = agentId;
            this.language = language;
            this.content = content;
            this.checksum = checksum;
            this.endpoints = endpoints;
        }

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getChecksum() {
            return checksum;
        }

        public void setChecksum(String checksum) {
            this.checksum = checksum;
        }

        public Map<String, String> getEndpoints() {
            return endpoints;
        }

        public void setEndpoints(Map<String, String> endpoints) {
            this.endpoints = endpoints;
        }
    }

    public static class Metadata {
        private String modelUsed;
        private long timestamp;
        private int generationTimeMs;
        private String version;
        private Map<String, Object> extraInfo;

        public Metadata() {
        }

        public Metadata(String modelUsed, long timestamp, int generationTimeMs, String version, Map<String, Object> extraInfo) {
            this.modelUsed = modelUsed;
            this.timestamp = timestamp;
            this.generationTimeMs = generationTimeMs;
            this.version = version;
            this.extraInfo = extraInfo;
        }

        public String getModelUsed() {
            return modelUsed;
        }

        public void setModelUsed(String modelUsed) {
            this.modelUsed = modelUsed;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getGenerationTimeMs() {
            return generationTimeMs;
        }

        public void setGenerationTimeMs(int generationTimeMs) {
            this.generationTimeMs = generationTimeMs;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Map<String, Object> getExtraInfo() {
            return extraInfo;
        }

        public void setExtraInfo(Map<String, Object> extraInfo) {
            this.extraInfo = extraInfo;
        }
    }
}