package net.ooder.skillcenter.ide;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI-IDE集成管理器，负责AI-IDE功能的集成
 * 包括代码生成、技能开发、调试等功能
 */
public class AIIDEIntegrationManager {
    // 单例实例
    private static AIIDEIntegrationManager instance;
    
    // IDE插件映射
    private Map<String, IDEPlugin> idePlugins;
    
    // 技能开发上下文映射
    private Map<String, SkillDevelopmentContext> developmentContexts;
    
    /**
     * 私有构造方法
     */
    private AIIDEIntegrationManager() {
        this.idePlugins = new ConcurrentHashMap<>();
        this.developmentContexts = new ConcurrentHashMap<>();
        
        // 初始化内置插件
        initializePlugins();
    }
    
    /**
     * 获取实例
     * @return AI-IDE集成管理器实例
     */
    public static synchronized AIIDEIntegrationManager getInstance() {
        if (instance == null) {
            instance = new AIIDEIntegrationManager();
        }
        return instance;
    }
    
    /**
     * 初始化内置插件
     */
    private void initializePlugins() {
        // 注册内置插件
        registerPlugin(new CodeGenerationPlugin());
        registerPlugin(new SkillDevelopmentPlugin());
        registerPlugin(new DebuggingPlugin());
        registerPlugin(new TestingPlugin());
    }
    
    /**
     * 注册IDE插件
     * @param plugin IDE插件
     */
    public void registerPlugin(IDEPlugin plugin) {
        if (plugin != null) {
            idePlugins.put(plugin.getPluginId(), plugin);
            System.out.println("Registered IDE plugin: " + plugin.getPluginName());
        }
    }
    
    /**
     * 卸载IDE插件
     * @param pluginId 插件ID
     */
    public void unregisterPlugin(String pluginId) {
        IDEPlugin plugin = idePlugins.remove(pluginId);
        if (plugin != null) {
            System.out.println("Unregistered IDE plugin: " + plugin.getPluginName());
        }
    }
    
    /**
     * 获取IDE插件
     * @param pluginId 插件ID
     * @return IDE插件
     */
    public IDEPlugin getPlugin(String pluginId) {
        return idePlugins.get(pluginId);
    }
    
    /**
     * 获取所有IDE插件
     * @return IDE插件映射
     */
    public Map<String, IDEPlugin> getAllPlugins() {
        return idePlugins;
    }
    
    /**
     * 生成代码
     * @param language 编程语言
     * @param prompt 代码生成提示
     * @param context 生成上下文
     * @return 生成的代码
     */
    public String generateCode(String language, String prompt, Map<String, Object> context) {
        CodeGenerationPlugin plugin = (CodeGenerationPlugin) idePlugins.get("code-generation-plugin");
        if (plugin != null) {
            return plugin.generateCode(language, prompt, context);
        }
        return "// Code generation plugin not available";
    }
    
    /**
     * 开始技能开发
     * @param skillId 技能ID
     * @return 开发上下文ID
     */
    public String startSkillDevelopment(String skillId) {
        String contextId = "dev-" + skillId + "-" + System.currentTimeMillis();
        SkillDevelopmentContext devContext = new SkillDevelopmentContext();
        devContext.setContextId(contextId);
        devContext.setSkillId(skillId);
        devContext.setStartTime(System.currentTimeMillis());
        
        developmentContexts.put(contextId, devContext);
        System.out.println("Started skill development context: " + contextId);
        
        return contextId;
    }
    
    /**
     * 结束技能开发
     * @param contextId 开发上下文ID
     * @return 是否成功结束
     */
    public boolean endSkillDevelopment(String contextId) {
        SkillDevelopmentContext context = developmentContexts.remove(contextId);
        if (context != null) {
            System.out.println("Ended skill development context: " + contextId);
            return true;
        }
        return false;
    }
    
    /**
     * 获取技能开发上下文
     * @param contextId 开发上下文ID
     * @return 开发上下文
     */
    public SkillDevelopmentContext getDevelopmentContext(String contextId) {
        return developmentContexts.get(contextId);
    }
    
    /**
     * 调试技能
     * @param skillId 技能ID
     * @param context 执行上下文
     * @return 调试结果
     * @throws SkillException 调试异常
     */
    public SkillResult debugSkill(String skillId, SkillContext context) throws SkillException {
        DebuggingPlugin plugin = (DebuggingPlugin) idePlugins.get("debugging-plugin");
        if (plugin != null) {
            return plugin.debugSkill(skillId, context);
        }
        throw new SkillException(skillId, "Debugging plugin not available", 
                                 SkillException.ErrorCode.EXECUTION_EXCEPTION);
    }
    
    /**
     * 测试技能
     * @param skillId 技能ID
     * @param testCases 测试用例
     * @return 测试结果
     */
    public SkillTestResult testSkill(String skillId, Map<String, Object> testCases) {
        TestingPlugin plugin = (TestingPlugin) idePlugins.get("testing-plugin");
        if (plugin != null) {
            return plugin.testSkill(skillId, testCases);
        }
        SkillTestResult result = new SkillTestResult();
        result.setSkillId(skillId);
        result.setSuccess(false);
        result.setMessage("Testing plugin not available");
        return result;
    }
    
    /**
     * 部署技能
     * @param skillId 技能ID
     * @param environment 部署环境
     * @return 部署结果
     */
    public boolean deploySkill(String skillId, String environment) {
        SkillDevelopmentPlugin plugin = (SkillDevelopmentPlugin) idePlugins.get("skill-development-plugin");
        if (plugin != null) {
            return plugin.deploySkill(skillId, environment);
        }
        return false;
    }
    
    /**
     * 导出技能
     * @param skillId 技能ID
     * @param format 导出格式
     * @return 导出的技能数据
     */
    public byte[] exportSkill(String skillId, String format) {
        SkillDevelopmentPlugin plugin = (SkillDevelopmentPlugin) idePlugins.get("skill-development-plugin");
        if (plugin != null) {
            return plugin.exportSkill(skillId, format);
        }
        return new byte[0];
    }
    
    /**
     * 导入技能
     * @param skillData 技能数据
     * @param format 导入格式
     * @return 导入的技能
     */
    public Skill importSkill(byte[] skillData, String format) {
        SkillDevelopmentPlugin plugin = (SkillDevelopmentPlugin) idePlugins.get("skill-development-plugin");
        if (plugin != null) {
            return plugin.importSkill(skillData, format);
        }
        return null;
    }
    
    /**
     * 优化技能
     * @param skill 技能实例
     * @return 优化后的技能
     */
    public Skill optimizeSkill(Skill skill) {
        SkillDevelopmentPlugin plugin = (SkillDevelopmentPlugin) idePlugins.get("skill-development-plugin");
        if (plugin != null) {
            return plugin.optimizeSkill(skill);
        }
        return skill;
    }
    
    /**
     * 分析技能
     * @param skillId 技能ID
     * @return 分析结果
     */
    public SkillAnalysisResult analyzeSkill(String skillId) {
        SkillDevelopmentPlugin plugin = (SkillDevelopmentPlugin) idePlugins.get("skill-development-plugin");
        if (plugin != null) {
            return plugin.analyzeSkill(skillId);
        }
        SkillAnalysisResult result = new SkillAnalysisResult();
        result.setSkillId(skillId);
        result.setAnalysisAvailable(false);
        result.setMessage("Skill analysis plugin not available");
        return result;
    }
    
    /**
     * 启动AI-IDE集成
     */
    public void start() {
        System.out.println("Starting AI-IDE Integration Manager...");
        
        // 启动所有插件
        for (IDEPlugin plugin : idePlugins.values()) {
            plugin.start();
        }
        
        System.out.println("AI-IDE Integration Manager started successfully");
    }
    
    /**
     * 停止AI-IDE集成
     */
    public void stop() {
        System.out.println("Stopping AI-IDE Integration Manager...");
        
        // 停止所有插件
        for (IDEPlugin plugin : idePlugins.values()) {
            plugin.stop();
        }
        
        // 清理开发上下文
        developmentContexts.clear();
        
        System.out.println("AI-IDE Integration Manager stopped successfully");
    }
}