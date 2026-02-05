package net.ooder.sdk.agent.model;

import net.ooder.sdk.command.factory.CommandFactory;
import net.ooder.sdk.command.factory.CommandFactoryImpl;
import net.ooder.sdk.network.factory.NetworkFactory;
import net.ooder.sdk.network.factory.NetworkFactoryImpl;
import net.ooder.sdk.network.udp.UDPSDK;
import net.ooder.sdk.network.udp.UDPConfig;
import net.ooder.sdk.network.udp.UDPMessageHandler;
import net.ooder.sdk.persistence.factory.PersistenceFactory;
import net.ooder.sdk.persistence.factory.PersistenceFactoryImpl;
import net.ooder.sdk.skill.factory.SkillFactory;
import net.ooder.sdk.skill.factory.SkillFactoryImpl;
import net.ooder.sdk.system.factory.SystemFactory;
import net.ooder.sdk.system.factory.SystemFactoryImpl;
import net.ooder.sdk.system.sleep.SleepManager;
import net.ooder.sdk.system.sleep.SleepStrategy;
import net.ooder.sdk.system.retry.RetryExecutor;
import net.ooder.sdk.system.retry.RetryStrategy;

/**
 * Agent管理器，作为所有factory的统一入口
 * 负责管理和提供所有factory实例
 */
public class AgentManager {
    
    private static AgentManager instance;
    
    private CommandFactory commandFactory;
    private SkillFactory skillFactory;
    private PersistenceFactory persistenceFactory;
    private NetworkFactory networkFactory;
    private SystemFactory systemFactory;
    
    private AgentManager() {
        // 私有构造函数
        initializeFactories();
    }
    
    /**
     * 获取Agent管理器实例
     * @return Agent管理器实例
     */
    public static synchronized AgentManager getInstance() {
        if (instance == null) {
            instance = new AgentManager();
        }
        return instance;
    }
    
    /**
     * 初始化所有工厂
     */
    private void initializeFactories() {
        this.commandFactory = new CommandFactoryImpl();
        this.skillFactory = new SkillFactoryImpl();
        this.persistenceFactory = new PersistenceFactoryImpl();
        this.networkFactory = new NetworkFactoryImpl();
        this.systemFactory = new SystemFactoryImpl();
    }
    
    /**
     * 获取命令工厂
     * @return 命令工厂实例
     */
    public CommandFactory getCommandFactory() {
        return commandFactory;
    }
    
    /**
     * 获取技能工厂
     * @return 技能工厂实例
     */
    public SkillFactory getSkillFactory() {
        return skillFactory;
    }
    
    /**
     * 获取持久化工厂
     * @return 持久化工厂实例
     */
    public PersistenceFactory getPersistenceFactory() {
        return persistenceFactory;
    }
    
    /**
     * 获取网络工厂
     * @return 网络工厂实例
     */
    public NetworkFactory getNetworkFactory() {
        return networkFactory;
    }
    
    /**
     * 获取系统工厂
     * @return 系统工厂实例
     */
    public SystemFactory getSystemFactory() {
        return systemFactory;
    }
    
    /**
     * 设置命令工厂
     * @param commandFactory 命令工厂实例
     */
    public void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }
    
    /**
     * 设置技能工厂
     * @param skillFactory 技能工厂实例
     */
    public void setSkillFactory(SkillFactory skillFactory) {
        this.skillFactory = skillFactory;
    }
    
    /**
     * 设置持久化工厂
     * @param persistenceFactory 持久化工厂实例
     */
    public void setPersistenceFactory(PersistenceFactory persistenceFactory) {
        this.persistenceFactory = persistenceFactory;
    }
    
    /**
     * 设置网络工厂
     * @param networkFactory 网络工厂实例
     */
    public void setNetworkFactory(NetworkFactory networkFactory) {
        this.networkFactory = networkFactory;
    }
    
    /**
     * 设置系统工厂
     * @param systemFactory 系统工厂实例
     */
    public void setSystemFactory(SystemFactory systemFactory) {
        this.systemFactory = systemFactory;
    }
    
    /**
     * 重置所有工厂
     */
    public void reset() {
        initializeFactories();
    }
}
