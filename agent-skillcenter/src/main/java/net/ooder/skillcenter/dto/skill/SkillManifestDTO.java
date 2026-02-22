package net.ooder.skillcenter.dto.skill;

import net.ooder.skillcenter.capability.model.CapabilityDefinition;
import net.ooder.skillcenter.config.model.ConfigItem;
import net.ooder.skillcenter.lifecycle.deployment.model.DeploymentConfig;
import net.ooder.skillcenter.resources.model.ResourceRequest;
import net.ooder.skillcenter.runtime.model.RuntimeConfig;
import net.ooder.skillcenter.scene.model.SceneDefinition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 技能清单DTO - 符合v0.7.0协议规范
 * 统一的技能定义模型
 */
public class SkillManifestDTO {
    
    private String id;
    private String name;
    private String version;
    private String description;
    private String author;
    private String license;
    
    private String type;
    private RuntimeConfig runtime;
    private List<CapabilityDefinition> capabilities;
    private List<SceneDefinition> scenes;
    private List<Dependency> dependencies;
    private List<ConfigItem> config;
    private List<Endpoint> endpoints;
    private ResourceRequest resources;
    private DeploymentConfig deployment;
    
    private Map<String, Object> metadata;
    private Date createdAt;
    private Date updatedAt;
    private String status;

    public SkillManifestDTO() {
        this.capabilities = new ArrayList<>();
        this.scenes = new ArrayList<>();
        this.dependencies = new ArrayList<>();
        this.config = new ArrayList<>();
        this.endpoints = new ArrayList<>();
    }

    public static SkillManifestDTO of(String id, String name, String version) {
        SkillManifestDTO manifest = new SkillManifestDTO();
        manifest.setId(id);
        manifest.setName(name);
        manifest.setVersion(version);
        return manifest;
    }

    public void addCapability(CapabilityDefinition capability) {
        capabilities.add(capability);
    }

    public void addScene(SceneDefinition scene) {
        scenes.add(scene);
    }

    public void addDependency(Dependency dependency) {
        dependencies.add(dependency);
    }

    public void addConfig(ConfigItem configItem) {
        config.add(configItem);
    }

    public void addEndpoint(Endpoint endpoint) {
        endpoints.add(endpoint);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getLicense() { return license; }
    public void setLicense(String license) { this.license = license; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public RuntimeConfig getRuntime() { return runtime; }
    public void setRuntime(RuntimeConfig runtime) { this.runtime = runtime; }

    public List<CapabilityDefinition> getCapabilities() { return capabilities; }
    public void setCapabilities(List<CapabilityDefinition> capabilities) { this.capabilities = capabilities; }

    public List<SceneDefinition> getScenes() { return scenes; }
    public void setScenes(List<SceneDefinition> scenes) { this.scenes = scenes; }

    public List<Dependency> getDependencies() { return dependencies; }
    public void setDependencies(List<Dependency> dependencies) { this.dependencies = dependencies; }

    public List<ConfigItem> getConfig() { return config; }
    public void setConfig(List<ConfigItem> config) { this.config = config; }

    public List<Endpoint> getEndpoints() { return endpoints; }
    public void setEndpoints(List<Endpoint> endpoints) { this.endpoints = endpoints; }

    public ResourceRequest getResources() { return resources; }
    public void setResources(ResourceRequest resources) { this.resources = resources; }

    public DeploymentConfig getDeployment() { return deployment; }
    public void setDeployment(DeploymentConfig deployment) { this.deployment = deployment; }

    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static class Dependency {
        private String name;
        private String version;
        private String type;
        private String scope;

        public static Dependency of(String name, String version) {
            Dependency dep = new Dependency();
            dep.setName(name);
            dep.setVersion(version);
            return dep;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
    }

    public static class Endpoint {
        private String path;
        private String method;
        private String description;
        private String capability;
        private boolean authentication;

        public static Endpoint of(String path, String method) {
            Endpoint ep = new Endpoint();
            ep.setPath(path);
            ep.setMethod(method);
            return ep;
        }

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }

        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getCapability() { return capability; }
        public void setCapability(String capability) { this.capability = capability; }

        public boolean isAuthentication() { return authentication; }
        public void setAuthentication(boolean authentication) { this.authentication = authentication; }
    }
}
