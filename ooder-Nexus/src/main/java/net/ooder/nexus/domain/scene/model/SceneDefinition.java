package net.ooder.nexus.domain.scene.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SceneDefinition {
    private String sceneId;
    private String name;
    private String description;
    private String type;
    private String category;
    private String version;
    private boolean required;
    private List<CapabilityRef> capabilities;
    private List<RoleDefinition> roles;
    private String visibility;
    private String status;
    private String ownerId;
    private String ownerName;
    private Date createdAt;
    private Date updatedAt;

    public SceneDefinition() {}

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public List<CapabilityRef> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<CapabilityRef> capabilities) {
        this.capabilities = capabilities;
    }

    public List<RoleDefinition> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDefinition> roles) {
        this.roles = roles;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
