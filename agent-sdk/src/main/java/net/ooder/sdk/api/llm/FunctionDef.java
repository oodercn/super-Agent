package net.ooder.sdk.api.llm;

/**
 * Function Definition for LLM Function Calling
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class FunctionDef {

    private String name;
    private String description;
    private Parameters parameters;

    public FunctionDef() {
        this.parameters = new Parameters();
    }

    public static FunctionDef of(String name, String description) {
        FunctionDef def = new FunctionDef();
        def.setName(name);
        def.setDescription(description);
        return def;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Parameters getParameters() { return parameters; }
    public void setParameters(Parameters parameters) { this.parameters = parameters; }

    public FunctionDef addParameter(String name, String type, String description, boolean required) {
        this.parameters.addProperty(name, type, description, required);
        return this;
    }

    public static class Parameters {
        private String type = "object";
        private java.util.Map<String, Property> properties = new java.util.HashMap<String, Property>();
        private java.util.List<String> required = new java.util.ArrayList<String>();

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public java.util.Map<String, Property> getProperties() { return properties; }
        public void setProperties(java.util.Map<String, Property> properties) { this.properties = properties; }

        public java.util.List<String> getRequired() { return required; }
        public void setRequired(java.util.List<String> required) { this.required = required; }

        public void addProperty(String name, String type, String description, boolean isRequired) {
            Property prop = new Property();
            prop.setType(type);
            prop.setDescription(description);
            properties.put(name, prop);
            if (isRequired) {
                required.add(name);
            }
        }
    }

    public static class Property {
        private String type;
        private String description;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
