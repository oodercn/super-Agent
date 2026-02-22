package net.ooder.nexus.skillcenter.dto.skill;

import java.util.List;
import java.util.Map;

/**
 * 技能清单DTO - 符合v0.7.0 skill-manifest-spec规范
 */
public class SkillManifestDTO {
    
    private String apiVersion;
    private String kind;
    private Metadata metadata;
    private Spec spec;

    public SkillManifestDTO() {
        this.apiVersion = "skill.ooder.net/v1";
        this.kind = "Skill";
    }

    public static class Metadata {
        private String id;
        private String name;
        private String version;
        private String description;
        private String author;
        private String license;
        private String homepage;
        private String repository;
        private String documentation;
        private String icon;
        private List<String> keywords;
        private List<Maintainer> maintainers;

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
        public String getHomepage() { return homepage; }
        public void setHomepage(String homepage) { this.homepage = homepage; }
        public String getRepository() { return repository; }
        public void setRepository(String repository) { this.repository = repository; }
        public String getDocumentation() { return documentation; }
        public void setDocumentation(String documentation) { this.documentation = documentation; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        public List<String> getKeywords() { return keywords; }
        public void setKeywords(List<String> keywords) { this.keywords = keywords; }
        public List<Maintainer> getMaintainers() { return maintainers; }
        public void setMaintainers(List<Maintainer> maintainers) { this.maintainers = maintainers; }
    }

    public static class Maintainer {
        private String name;
        private String email;
        private String github;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getGithub() { return github; }
        public void setGithub(String github) { this.github = github; }
    }

    public static class Spec {
        private String type;
        private Runtime runtime;
        private List<Capability> capabilities;
        private List<Scene> scenes;
        private Dependencies dependencies;
        private Config config;
        private List<Endpoint> endpoints;
        private Resources resources;
        private Deployment deployment;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Runtime getRuntime() { return runtime; }
        public void setRuntime(Runtime runtime) { this.runtime = runtime; }
        public List<Capability> getCapabilities() { return capabilities; }
        public void setCapabilities(List<Capability> capabilities) { this.capabilities = capabilities; }
        public List<Scene> getScenes() { return scenes; }
        public void setScenes(List<Scene> scenes) { this.scenes = scenes; }
        public Dependencies getDependencies() { return dependencies; }
        public void setDependencies(Dependencies dependencies) { this.dependencies = dependencies; }
        public Config getConfig() { return config; }
        public void setConfig(Config config) { this.config = config; }
        public List<Endpoint> getEndpoints() { return endpoints; }
        public void setEndpoints(List<Endpoint> endpoints) { this.endpoints = endpoints; }
        public Resources getResources() { return resources; }
        public void setResources(Resources resources) { this.resources = resources; }
        public Deployment getDeployment() { return deployment; }
        public void setDeployment(Deployment deployment) { this.deployment = deployment; }
    }

    public static class Runtime {
        private String language;
        private String javaVersion;
        private String pythonVersion;
        private String nodeVersion;
        private String framework;
        private String mainClass;
        private String entrypoint;
        private String jvmOpts;
        private String packageManager;

        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        public String getJavaVersion() { return javaVersion; }
        public void setJavaVersion(String javaVersion) { this.javaVersion = javaVersion; }
        public String getPythonVersion() { return pythonVersion; }
        public void setPythonVersion(String pythonVersion) { this.pythonVersion = pythonVersion; }
        public String getNodeVersion() { return nodeVersion; }
        public void setNodeVersion(String nodeVersion) { this.nodeVersion = nodeVersion; }
        public String getFramework() { return framework; }
        public void setFramework(String framework) { this.framework = framework; }
        public String getMainClass() { return mainClass; }
        public void setMainClass(String mainClass) { this.mainClass = mainClass; }
        public String getEntrypoint() { return entrypoint; }
        public void setEntrypoint(String entrypoint) { this.entrypoint = entrypoint; }
        public String getJvmOpts() { return jvmOpts; }
        public void setJvmOpts(String jvmOpts) { this.jvmOpts = jvmOpts; }
        public String getPackageManager() { return packageManager; }
        public void setPackageManager(String packageManager) { this.packageManager = packageManager; }
    }

    public static class Capability {
        private String id;
        private String name;
        private String description;
        private String category;
        private String version;
        private List<Parameter> parameters;
        private Returns returns;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public List<Parameter> getParameters() { return parameters; }
        public void setParameters(List<Parameter> parameters) { this.parameters = parameters; }
        public Returns getReturns() { return returns; }
        public void setReturns(Returns returns) { this.returns = returns; }
    }

    public static class Parameter {
        private String name;
        private String type;
        private boolean required;
        private Object defaultValue;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public Object getDefaultValue() { return defaultValue; }
        public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class Returns {
        private String type;
        private String description;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class Scene {
        private String name;
        private String description;
        private List<String> capabilities;
        private List<Role> roles;
        private String communicationProtocol;
        private String securityPolicy;
        private boolean allowParallel = true;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<String> getCapabilities() { return capabilities; }
        public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
        public List<Role> getRoles() { return roles; }
        public void setRoles(List<Role> roles) { this.roles = roles; }
        public String getCommunicationProtocol() { return communicationProtocol; }
        public void setCommunicationProtocol(String communicationProtocol) { this.communicationProtocol = communicationProtocol; }
        public String getSecurityPolicy() { return securityPolicy; }
        public void setSecurityPolicy(String securityPolicy) { this.securityPolicy = securityPolicy; }
        public boolean isAllowParallel() { return allowParallel; }
        public void setAllowParallel(boolean allowParallel) { this.allowParallel = allowParallel; }
    }

    public static class Role {
        private String roleId;
        private String name;
        private boolean required;
        private List<String> capabilities;

        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public List<String> getCapabilities() { return capabilities; }
        public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }
    }

    public static class Dependencies {
        private List<SkillDependency> skills;
        private List<LibraryDependency> libraries;

        public List<SkillDependency> getSkills() { return skills; }
        public void setSkills(List<SkillDependency> skills) { this.skills = skills; }
        public List<LibraryDependency> getLibraries() { return libraries; }
        public void setLibraries(List<LibraryDependency> libraries) { this.libraries = libraries; }
    }

    public static class SkillDependency {
        private String id;
        private String version;
        private boolean optional;
        private String description;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public boolean isOptional() { return optional; }
        public void setOptional(boolean optional) { this.optional = optional; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class LibraryDependency {
        private String group;
        private String artifact;
        private String version;
        private String scope;

        public String getGroup() { return group; }
        public void setGroup(String group) { this.group = group; }
        public String getArtifact() { return artifact; }
        public void setArtifact(String artifact) { this.artifact = artifact; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
    }

    public static class Config {
        private List<ConfigItem> required;
        private List<ConfigItem> optional;

        public List<ConfigItem> getRequired() { return required; }
        public void setRequired(List<ConfigItem> required) { this.required = required; }
        public List<ConfigItem> getOptional() { return optional; }
        public void setOptional(List<ConfigItem> optional) { this.optional = optional; }
    }

    public static class ConfigItem {
        private String name;
        private String type;
        private String description;
        private Object defaultValue;
        private boolean secret;
        private String validation;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Object getDefaultValue() { return defaultValue; }
        public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }
        public boolean isSecret() { return secret; }
        public void setSecret(boolean secret) { this.secret = secret; }
        public String getValidation() { return validation; }
        public void setValidation(String validation) { this.validation = validation; }
    }

    public static class Endpoint {
        private String path;
        private String method;
        private String description;
        private String capability;
        private boolean authentication = true;
        private int rateLimit;
        private List<EndpointParameter> parameters;

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
        public int getRateLimit() { return rateLimit; }
        public void setRateLimit(int rateLimit) { this.rateLimit = rateLimit; }
        public List<EndpointParameter> getParameters() { return parameters; }
        public void setParameters(List<EndpointParameter> parameters) { this.parameters = parameters; }
    }

    public static class EndpointParameter {
        private String name;
        private String in;
        private String type;
        private boolean required;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getIn() { return in; }
        public void setIn(String in) { this.in = in; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public boolean isRequired() { return required; }
        public void setRequired(boolean required) { this.required = required; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class Resources {
        private String cpu;
        private String memory;
        private String storage;
        private Network network;

        public String getCpu() { return cpu; }
        public void setCpu(String cpu) { this.cpu = cpu; }
        public String getMemory() { return memory; }
        public void setMemory(String memory) { this.memory = memory; }
        public String getStorage() { return storage; }
        public void setStorage(String storage) { this.storage = storage; }
        public Network getNetwork() { return network; }
        public void setNetwork(Network network) { this.network = network; }
    }

    public static class Network {
        private boolean ingress;
        private boolean egress;
        private List<Integer> ports;

        public boolean isIngress() { return ingress; }
        public void setIngress(boolean ingress) { this.ingress = ingress; }
        public boolean isEgress() { return egress; }
        public void setEgress(boolean egress) { this.egress = egress; }
        public List<Integer> getPorts() { return ports; }
        public void setPorts(List<Integer> ports) { this.ports = ports; }
    }

    public static class Deployment {
        private List<String> modes;
        private boolean singleton;
        private boolean requiresAuth;
        private HealthCheck healthCheck;
        private Startup startup;

        public List<String> getModes() { return modes; }
        public void setModes(List<String> modes) { this.modes = modes; }
        public boolean isSingleton() { return singleton; }
        public void setSingleton(boolean singleton) { this.singleton = singleton; }
        public boolean isRequiresAuth() { return requiresAuth; }
        public void setRequiresAuth(boolean requiresAuth) { this.requiresAuth = requiresAuth; }
        public HealthCheck getHealthCheck() { return healthCheck; }
        public void setHealthCheck(HealthCheck healthCheck) { this.healthCheck = healthCheck; }
        public Startup getStartup() { return startup; }
        public void setStartup(Startup startup) { this.startup = startup; }
    }

    public static class HealthCheck {
        private String path;
        private int interval;
        private int timeout;

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public int getInterval() { return interval; }
        public void setInterval(int interval) { this.interval = interval; }
        public int getTimeout() { return timeout; }
        public void setTimeout(int timeout) { this.timeout = timeout; }
    }

    public static class Startup {
        private int timeout;
        private int order;

        public int getTimeout() { return timeout; }
        public void setTimeout(int timeout) { this.timeout = timeout; }
        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }
    }

    public String getApiVersion() { return apiVersion; }
    public void setApiVersion(String apiVersion) { this.apiVersion = apiVersion; }
    public String getKind() { return kind; }
    public void setKind(String kind) { this.kind = kind; }
    public Metadata getMetadata() { return metadata; }
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }
    public Spec getSpec() { return spec; }
    public void setSpec(Spec spec) { this.spec = spec; }
}
