package net.ooder.agent.dto.config;

import java.util.List;

public class ConfigInheritanceDetailDTO {
    private List<ConfigInheritanceChainDTO> chain;
    private String targetType;
    private String targetId;
    private String currentLevel;

    public List<ConfigInheritanceChainDTO> getChain() {
        return chain;
    }

    public void setChain(List<ConfigInheritanceChainDTO> chain) {
        this.chain = chain;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }
}
