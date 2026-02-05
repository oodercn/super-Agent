package net.ooder.skillcenter.shell;

import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.skillcenter.market.SkillStorage;
import net.ooder.skillcenter.execution.SkillExecutorEngine;

/**
 * 抽象命令类，提供命令的通用实现
 */
public abstract class AbstractCommand implements Command {
    
    // 技能管理器
    protected SkillManager skillManager;
    
    // 技能市场管理器
    protected SkillMarketManager marketManager;
    
    // 技能执行引擎
    protected SkillExecutorEngine executorEngine;
    
    // 技能存储
    protected SkillStorage skillStorage;
    
    // 命令输出
    protected CommandOutput output;
    
    /**
     * 构造方法，初始化依赖
     */
    public AbstractCommand() {
        this.skillManager = SkillManager.getInstance();
        this.marketManager = SkillMarketManager.getInstance();
        this.executorEngine = SkillExecutorEngine.getInstance();
        this.skillStorage = new net.ooder.skillcenter.market.SDKSkillStorage();
        this.skillStorage.initialize();
        this.output = new CommandOutput();
    }
    
    /**
     * 获取命令名称
     * @return 命令名称
     */
    @Override
    public abstract String getName();
    
    /**
     * 获取命令描述
     * @return 命令描述
     */
    @Override
    public abstract String getDescription();
    
    /**
     * 执行命令
     * @param args 命令参数
     * @throws Exception 执行异常
     */
    @Override
    public abstract void execute(String[] args) throws Exception;
    
    /**
     * 输出信息
     * @param message 信息内容
     */
    protected void println(String message) {
        output.println(message);
    }
    
    /**
     * 输出错误信息
     * @param message 错误信息
     */
    protected void error(String message) {
        output.error(message);
    }
    
    /**
     * 输出成功信息
     * @param message 成功信息
     */
    protected void success(String message) {
        output.success(message);
    }
    
    /**
     * 输出警告信息
     * @param message 警告信息
     */
    protected void warn(String message) {
        output.warn(message);
    }
}
