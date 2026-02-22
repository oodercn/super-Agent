package net.ooder.skillcenter.runtime.model;

/**
 * 运行时配置 - 符合v0.7.0协议规范
 */
public class RuntimeConfig {
    
    private String language;
    private String version;
    private String framework;
    private String entrypoint;
    private String jvmOpts;
    private String requirements;
    private String packageJson;
    private String mainClass;
    
    public RuntimeConfig() {}
    
    public static RuntimeConfig java(String javaVersion, String framework) {
        RuntimeConfig config = new RuntimeConfig();
        config.setLanguage("java");
        config.setVersion(javaVersion);
        config.setFramework(framework);
        return config;
    }
    
    public static RuntimeConfig python(String pythonVersion, String framework) {
        RuntimeConfig config = new RuntimeConfig();
        config.setLanguage("python");
        config.setVersion(pythonVersion);
        config.setFramework(framework);
        return config;
    }
    
    public static RuntimeConfig nodejs(String nodeVersion, String framework) {
        RuntimeConfig config = new RuntimeConfig();
        config.setLanguage("nodejs");
        config.setVersion(nodeVersion);
        config.setFramework(framework);
        return config;
    }
    
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getFramework() { return framework; }
    public void setFramework(String framework) { this.framework = framework; }
    
    public String getEntrypoint() { return entrypoint; }
    public void setEntrypoint(String entrypoint) { this.entrypoint = entrypoint; }
    
    public String getJvmOpts() { return jvmOpts; }
    public void setJvmOpts(String jvmOpts) { this.jvmOpts = jvmOpts; }
    
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    
    public String getPackageJson() { return packageJson; }
    public void setPackageJson(String packageJson) { this.packageJson = packageJson; }
    
    public String getMainClass() { return mainClass; }
    public void setMainClass(String mainClass) { this.mainClass = mainClass; }
}
