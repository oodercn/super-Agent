package net.ooder.examples.skilla.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "scene")
public class SceneConfig {
    private String name;
    private String type;
    private String description;
    private boolean autoLeaveAfterCompletion;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAutoLeaveAfterCompletion() {
        return autoLeaveAfterCompletion;
    }

    public void setAutoLeaveAfterCompletion(boolean autoLeaveAfterCompletion) {
        this.autoLeaveAfterCompletion = autoLeaveAfterCompletion;
    }
}
