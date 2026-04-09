package net.ooder.enexus.dict;

public enum SkillFormEnum implements DictEnum {
    SERVICE("SERVICE", "服务能力", 1),
    TOOL("TOOL", "工具能力", 2),
    KNOWLEDGE("KNOWLEDGE", "知识能力", 3),
    WORKFLOW("WORKFLOW", "工作流能力", 4),
    LLM("LLM", "大模型能力", 5),
    PROVIDER("PROVIDER", "服务提供者", 6),
    DRIVER("DRIVER", "驱动适配器", 7),
    SCENE("SCENE", "场景应用", 8),
    AGENT("AGENT", "智能代理", 9),
    INTEGRATION("INTEGRATION", "集成服务", 10),
    TRIGGER("TRIGGER", "触发器", 11),
    SCHEDULER("SCHEDULER", "调度器", 12),
    CONNECTOR("CONNECTOR", "连接器", 13),
    ADAPTER("ADAPTER", "适配器", 14),
    PLUGIN("PLUGIN", "插件", 15),
    EXTENSION("EXTENSION", "扩展", 16),
    MIDDLEWARE("MIDDLEWARE", "中间件", 17),
    UTILITY("UTILITY", "工具类", 18),
    DEMO("DEMO", "演示示例", 19),
    TEST("TEST", "测试能力", 20),
    DEV("DEV", "开发中", 21);

    private final String code;
    private final String name;
    private final int sort;

    SkillFormEnum(String code, String name, int sort) {
        this.code = code;
        this.name = name;
        this.sort = sort;
    }

    @Override
    public String getCode() { return code; }

    @Override
    public String getName() { return name; }

    @Override
    public int getSort() { return sort; }

    public static SkillFormEnum fromCode(String code) {
        if (code == null) return PROVIDER;
        for (SkillFormEnum form : values()) {
            if (form.code.equalsIgnoreCase(code) || form.name().equalsIgnoreCase(code)) {
                return form;
            }
        }
        return PROVIDER;
    }

    public static SkillFormEnum fromName(String name) {
        if (name == null) return PROVIDER;
        for (SkillFormEnum form : values()) {
            if (form.name.equals(name)) {
                return form;
            }
        }
        return PROVIDER;
    }
}
