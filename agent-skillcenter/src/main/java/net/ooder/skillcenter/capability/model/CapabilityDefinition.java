package net.ooder.skillcenter.capability.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 能力定义 - 符合v0.7.0协议规范
 */
public class CapabilityDefinition {
    
    private String id;
    private String name;
    private String description;
    private CapabilityCategory category;
    private String version;
    private List<CapabilityParameter> parameters;
    private List<CapabilityReturn> returns;
    private List<String> examples;
    private boolean async;
    private int timeout;

    public CapabilityDefinition() {
        this.parameters = new ArrayList<>();
        this.returns = new ArrayList<>();
        this.examples = new ArrayList<>();
        this.async = false;
        this.timeout = 30000;
    }

    public static CapabilityDefinition of(String id, String name, String description) {
        CapabilityDefinition cap = new CapabilityDefinition();
        cap.setId(id);
        cap.setName(name);
        cap.setDescription(description);
        return cap;
    }

    public static CapabilityDefinition dataAccess(String id, String name, String description) {
        CapabilityDefinition cap = of(id, name, description);
        cap.setCategory(CapabilityCategory.DATA_ACCESS);
        return cap;
    }

    public static CapabilityDefinition authentication(String id, String name, String description) {
        CapabilityDefinition cap = of(id, name, description);
        cap.setCategory(CapabilityCategory.AUTHENTICATION);
        return cap;
    }

    public static CapabilityDefinition communication(String id, String name, String description) {
        CapabilityDefinition cap = of(id, name, description);
        cap.setCategory(CapabilityCategory.COMMUNICATION);
        return cap;
    }

    public static CapabilityDefinition integration(String id, String name, String description) {
        CapabilityDefinition cap = of(id, name, description);
        cap.setCategory(CapabilityCategory.INTEGRATION);
        return cap;
    }

    public static CapabilityDefinition processing(String id, String name, String description) {
        CapabilityDefinition cap = of(id, name, description);
        cap.setCategory(CapabilityCategory.PROCESSING);
        return cap;
    }

    public static CapabilityDefinition storage(String id, String name, String description) {
        CapabilityDefinition cap = of(id, name, description);
        cap.setCategory(CapabilityCategory.STORAGE);
        return cap;
    }

    public void addParameter(CapabilityParameter param) {
        parameters.add(param);
    }

    public void addReturn(CapabilityReturn ret) {
        returns.add(ret);
    }

    public void addExample(String example) {
        examples.add(example);
    }

    public boolean validateParameters(java.util.Map<String, Object> params) {
        for (CapabilityParameter param : parameters) {
            if (param.isRequired() && !params.containsKey(param.getName())) {
                return false;
            }
            if (params.containsKey(param.getName())) {
                if (!param.validate(params.get(param.getName()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public CapabilityCategory getCategory() { return category; }
    public void setCategory(CapabilityCategory category) { this.category = category; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public List<CapabilityParameter> getParameters() { return parameters; }
    public void setParameters(List<CapabilityParameter> parameters) { this.parameters = parameters; }

    public List<CapabilityReturn> getReturns() { return returns; }
    public void setReturns(List<CapabilityReturn> returns) { this.returns = returns; }

    public List<String> getExamples() { return examples; }
    public void setExamples(List<String> examples) { this.examples = examples; }

    public boolean isAsync() { return async; }
    public void setAsync(boolean async) { this.async = async; }

    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }
}
