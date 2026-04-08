package net.ooder.agent.dict;

public enum CapabilityCategoryEnum implements DictEnum {
    LLM("llm", "大模型", 1),
    KNOWLEDGE("knowledge", "知识库", 2),
    BIZ("biz", "业务场景", 3),
    ORG("org", "组织管理", 4),
    MSG("msg", "消息通知", 5),
    VFS("vfs", "文件存储", 6),
    UI("ui", "界面组件", 7),
    SYS("sys", "系统管理", 8),
    PAYMENT("payment", "支付", 9),
    MEDIA("media", "媒体", 10),
    UTIL("util", "工具", 11),
    TENANT("tenant", "租户管理", 12),
    AGENT("agent", "智能代理", 13),
    WORKFLOW("workflow", "工作流", 14),
    INTEGRATION("integration", "集成服务", 15),
    SECURITY("security", "安全认证", 16),
    DEMO("demo", "演示示例", 17),
    TEST("test", "测试能力", 18),
    DRIVER("driver", "驱动适配", 19),
    CONNECTOR("connector", "连接器", 20),
    OTHER("other", "其他", 99);

    private final String code;
    private final String name;
    private final int sort;

    CapabilityCategoryEnum(String code, String name, int sort) {
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

    public static CapabilityCategoryEnum fromCode(String code) {
        if (code == null) return OTHER;
        for (CapabilityCategoryEnum cat : values()) {
            if (cat.code.equalsIgnoreCase(code) || cat.name().equalsIgnoreCase(code)) {
                return cat;
            }
        }
        return OTHER;
    }

    public static CapabilityCategoryEnum fromName(String name) {
        if (name == null) return OTHER;
        for (CapabilityCategoryEnum cat : values()) {
            if (cat.name.equals(name)) {
                return cat;
            }
        }
        return OTHER;
    }
}
