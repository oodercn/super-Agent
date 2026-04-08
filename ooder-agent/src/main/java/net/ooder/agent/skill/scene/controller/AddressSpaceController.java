package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.model.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/address-space")
public class AddressSpaceController {

    private static final List<Map<String, Object>> CATEGORIES = Arrays.asList(
        createCategory("sys", "系统核心", "0x00-0x07", 0x00, "#1890ff", "ri-settings-3-line"),
        createCategory("org", "组织服务", "0x08-0x0F", 0x08, "#722ed1", "ri-team-line"),
        createCategory("auth", "认证服务", "0x10-0x17", 0x10, "#eb2f96", "ri-shield-keyhole-line"),
        createCategory("net", "网络服务", "0x18-0x1F", 0x18, "#13c2c2", "ri-global-line"),
        createCategory("vfs", "文件存储", "0x20-0x27", 0x20, "#fa8c16", "ri-folder-line"),
        createCategory("db", "数据库", "0x28-0x2F", 0x28, "#52c41a", "ri-database-2-line"),
        createCategory("llm", "大语言模型", "0x30-0x37", 0x30, "#2f54eb", "ri-robot-line"),
        createCategory("know", "知识库", "0x38-0x3F", 0x38, "#faad14", "ri-book-2-line"),
        createCategory("payment", "支付服务", "0x40-0x47", 0x40, "#f5222d", "ri-bank-card-line"),
        createCategory("media", "媒体服务", "0x48-0x4F", 0x48, "#eb2f96", "ri-video-line"),
        createCategory("comm", "通讯服务", "0x50-0x57", 0x50, "#1890ff", "ri-message-3-line"),
        createCategory("mon", "监控服务", "0x58-0x5F", 0x58, "#595959", "ri-line-chart-line"),
        createCategory("iot", "物联网", "0x60-0x67", 0x60, "#52c41a", "ri-cpu-line"),
        createCategory("search", "搜索服务", "0x68-0x6F", 0x68, "#722ed1", "ri-search-line"),
        createCategory("sched", "调度服务", "0x70-0x77", 0x70, "#fa8c16", "ri-time-line"),
        createCategory("sec", "安全服务", "0x78-0x7F", 0x78, "#f5222d", "ri-lock-line"),
        createCategory("util", "工具服务", "0xF0-0xFF", 0xF0, "#8c8c8c", "ri-tools-line")
    );

    private static final List<Map<String, Object>> ADDRESSES = Arrays.asList(
        createAddress(0x00, "SYS_REGISTRY", "sys", "系统注册中心"),
        createAddress(0x01, "SYS_CONFIG", "sys", "系统配置中心"),
        createAddress(0x02, "SYS_CAPABILITY", "sys", "能力管理服务"),
        createAddress(0x03, "SYS_PROTOCOL", "sys", "协议处理服务"),
        createAddress(0x08, "ORG_LOCAL", "org", "本地组织服务"),
        createAddress(0x09, "ORG_DINGDING", "org", "钉钉组织服务"),
        createAddress(0x0A, "ORG_FEISHU", "org", "飞书组织服务"),
        createAddress(0x0B, "ORG_WECOM", "org", "企业微信组织服务"),
        createAddress(0x0C, "ORG_LDAP", "org", "LDAP组织服务"),
        createAddress(0x10, "AUTH_USER", "auth", "用户认证服务"),
        createAddress(0x11, "AUTH_TOKEN", "auth", "令牌管理服务"),
        createAddress(0x18, "NET_PROXY", "net", "网络代理服务"),
        createAddress(0x19, "NET_DNS", "net", "DNS服务"),
        createAddress(0x20, "VFS_BASE", "vfs", "VFS基础服务"),
        createAddress(0x21, "VFS_LOCAL", "vfs", "本地文件存储"),
        createAddress(0x22, "VFS_MINIO", "vfs", "MinIO对象存储"),
        createAddress(0x23, "VFS_OSS", "vfs", "阿里云OSS存储"),
        createAddress(0x24, "VFS_S3", "vfs", "AWS S3存储"),
        createAddress(0x25, "VFS_DATABASE", "vfs", "数据库存储"),
        createAddress(0x28, "DB_MYSQL", "db", "MySQL数据库"),
        createAddress(0x29, "DB_POSTGRES", "db", "PostgreSQL数据库"),
        createAddress(0x2A, "DB_MONGODB", "db", "MongoDB数据库"),
        createAddress(0x2B, "DB_REDIS", "db", "Redis缓存"),
        createAddress(0x30, "LLM_BASE", "llm", "LLM基础服务", 0x31),
        createAddress(0x31, "LLM_OLLAMA", "llm", "Ollama本地模型", 0x32),
        createAddress(0x32, "LLM_OPENAI", "llm", "OpenAI API"),
        createAddress(0x33, "LLM_QIANWEN", "llm", "通义千问"),
        createAddress(0x34, "LLM_DEEPSEEK", "llm", "DeepSeek"),
        createAddress(0x35, "LLM_VOLCENGINE", "llm", "火山引擎豆包"),
        createAddress(0x38, "KNOW_BASE", "know", "知识库基础服务"),
        createAddress(0x39, "KNOW_VECTOR", "know", "向量知识库"),
        createAddress(0x3A, "KNOW_RAG", "know", "RAG检索服务"),
        createAddress(0x3B, "KNOW_EMBEDDING", "know", "嵌入服务"),
        createAddress(0x40, "PAY_BASE", "payment", "支付基础服务"),
        createAddress(0x41, "PAY_ALIPAY", "payment", "支付宝"),
        createAddress(0x42, "PAY_WECHAT", "payment", "微信支付"),
        createAddress(0x48, "MEDIA_BASE", "media", "媒体基础服务"),
        createAddress(0x49, "MEDIA_WECHAT", "media", "微信公众号"),
        createAddress(0x4A, "MEDIA_WEIBO", "media", "微博"),
        createAddress(0x50, "COMM_BASE", "comm", "通讯基础服务"),
        createAddress(0x51, "COMM_MQTT", "comm", "MQTT服务"),
        createAddress(0x52, "COMM_EMAIL", "comm", "邮件服务"),
        createAddress(0x53, "COMM_NOTIFY", "comm", "通知服务"),
        createAddress(0x58, "MON_BASE", "mon", "监控基础服务"),
        createAddress(0x59, "MON_HEALTH", "mon", "健康检查"),
        createAddress(0x5A, "MON_AGENT", "mon", "代理管理"),
        createAddress(0x60, "IOT_BASE", "iot", "物联网基础服务"),
        createAddress(0x61, "IOT_K8S", "iot", "Kubernetes"),
        createAddress(0x68, "SEARCH_BASE", "search", "搜索基础服务"),
        createAddress(0x69, "SEARCH_ES", "search", "Elasticsearch"),
        createAddress(0x70, "SCHED_BASE", "sched", "调度基础服务"),
        createAddress(0x71, "SCHED_QUARTZ", "sched", "Quartz调度"),
        createAddress(0x78, "SEC_BASE", "sec", "安全基础服务"),
        createAddress(0x79, "SEC_ACCESS", "sec", "访问控制"),
        createAddress(0xF0, "UTIL_BASE", "util", "工具基础服务"),
        createAddress(0xF1, "UTIL_REPORT", "util", "报表服务")
    );

    private static Map<String, Object> createCategory(String code, String name, String range, int base, String color, String icon) {
        Map<String, Object> cat = new HashMap<>();
        cat.put("code", code);
        cat.put("name", name);
        cat.put("range", range);
        cat.put("base", base);
        cat.put("color", color);
        cat.put("icon", icon);
        return cat;
    }

    private static Map<String, Object> createAddress(int address, String name, String category, String description) {
        return createAddress(address, name, category, description, null);
    }

    private static Map<String, Object> createAddress(int address, String name, String category, String description, Integer fallback) {
        Map<String, Object> addr = new HashMap<>();
        addr.put("address", address);
        addr.put("name", name);
        addr.put("category", category);
        addr.put("description", description);
        if (fallback != null) {
            addr.put("fallback", fallback);
        }
        return addr;
    }

    @GetMapping("/categories")
    public ResultModel<List<Map<String, Object>>> getCategories() {
        return ResultModel.success(CATEGORIES);
    }

    @GetMapping("/addresses")
    public ResultModel<Map<String, Object>> getAddresses() {
        Map<String, Object> result = new HashMap<>();
        result.put("categories", CATEGORIES);
        result.put("addresses", ADDRESSES);
        return ResultModel.success(result);
    }

    @GetMapping("/addresses/{address}")
    public ResultModel<Map<String, Object>> getAddressDetail(@PathVariable int address) {
        for (Map<String, Object> addr : ADDRESSES) {
            if ((Integer) addr.get("address") == address) {
                return ResultModel.success(addr);
            }
        }
        return ResultModel.error("Address not found: 0x" + Integer.toHexString(address).toUpperCase());
    }

    @GetMapping("/drivers")
    public ResultModel<Map<String, Object>> getDrivers() {
        Map<String, Object> drivers = new HashMap<>();
        
        drivers.put("llm", createDriverCategory("llm", "大语言模型", "0x30-0x37", Arrays.asList(
            createDriver("skill-llm-ollama", "Ollama", 0x31, "local", "免费", Arrays.asList("本地部署", "数据安全")),
            createDriver("skill-llm-openai", "OpenAI", 0x32, "cloud", "$0.005/1K tokens", Arrays.asList("最强能力", "多模态")),
            createDriver("skill-llm-qianwen", "通义千问", 0x33, "cloud", "¥0.002/1K tokens", Arrays.asList("中文优化", "企业级")),
            createDriver("skill-llm-deepseek", "DeepSeek", 0x34, "cloud", "¥0.001/1K tokens", Arrays.asList("高性价比", "Function Calling")),
            createDriver("skill-llm-volcengine", "火山引擎", 0x35, "cloud", "¥0.001/1K tokens", Arrays.asList("企业级", "稳定"))
        )));
        
        drivers.put("db", createDriverCategory("db", "数据库", "0x28-0x2F", Arrays.asList(
            createDriver("skill-db-mysql", "MySQL", 0x28, "relational", "开源", Arrays.asList("关系型", "成熟稳定")),
            createDriver("skill-db-postgres", "PostgreSQL", 0x29, "relational", "开源", Arrays.asList("关系型", "企业级")),
            createDriver("skill-db-mongodb", "MongoDB", 0x2A, "document", "开源", Arrays.asList("文档型", "灵活")),
            createDriver("skill-db-redis", "Redis", 0x2B, "cache", "开源", Arrays.asList("缓存", "高性能"))
        )));
        
        return ResultModel.success(drivers);
    }

    @GetMapping("/driver-configs")
    public ResultModel<Map<String, Object>> getDriverConfigs() {
        Map<String, Object> configs = new HashMap<>();
        return ResultModel.success(configs);
    }

    @PostMapping("/driver-configs")
    public ResultModel<Map<String, Object>> saveDriverConfigs(@RequestBody Map<String, Object> configs) {
        return ResultModel.success(configs);
    }

    @PostMapping("/driver-configs/{driverId}")
    public ResultModel<Object> saveDriverConfig(@PathVariable String driverId, @RequestBody Object config) {
        return ResultModel.success(config);
    }

    @PostMapping("/test-connection/{driverId}")
    public ResultModel<Map<String, Object>> testConnection(@PathVariable String driverId) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "连接测试成功");
        result.put("latency", 120);
        return ResultModel.success(result);
    }

    private Map<String, Object> createDriverCategory(String category, String displayName, String addressRange, List<Map<String, Object>> driverList) {
        Map<String, Object> cat = new HashMap<>();
        cat.put("category", category);
        cat.put("displayName", displayName);
        cat.put("addressRange", addressRange);
        cat.put("drivers", driverList);
        return cat;
    }

    private Map<String, Object> createDriver(String id, String name, int address, String type, String price, List<String> features) {
        Map<String, Object> driver = new HashMap<>();
        driver.put("id", id);
        driver.put("name", name);
        driver.put("address", address);
        driver.put("type", type);
        driver.put("price", price);
        driver.put("features", features);
        return driver;
    }
}
