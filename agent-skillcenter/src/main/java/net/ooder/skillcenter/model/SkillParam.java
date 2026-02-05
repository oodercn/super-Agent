package net.ooder.skillcenter.model;

/**
 * 技能参数描述，定义技能所需的参数信息
 */
public class SkillParam {
    private String name;
    private String description;
    private Class<?> type;
    private boolean required;
    private Object defaultValue;

    /**
     * 创建技能参数对象
     * @param name 参数名称
     * @param description 参数描述
     * @param type 参数类型
     * @param required 是否必填
     */
    public SkillParam(String name, String description, Class<?> type, boolean required) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.required = required;
    }

    /**
     * 创建带有默认值的技能参数对象
     * @param name 参数名称
     * @param description 参数描述
     * @param type 参数类型
     * @param required 是否必填
     * @param defaultValue 默认值
     */
    public SkillParam(String name, String description, Class<?> type, boolean required, Object defaultValue) {
        this(name, description, type, required);
        this.defaultValue = defaultValue;
    }

    // Getters and setters
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

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }
}
