package net.ooder.sdk.system.factory;

import net.ooder.sdk.command.factory.CommandFactory;
import net.ooder.sdk.command.factory.CommandFactoryImpl;
import net.ooder.sdk.network.factory.NetworkFactory;
import net.ooder.sdk.network.factory.NetworkFactoryImpl;
import net.ooder.sdk.persistence.factory.PersistenceFactory;
import net.ooder.sdk.persistence.factory.PersistenceFactoryImpl;
import net.ooder.sdk.skill.factory.SkillFactory;
import net.ooder.sdk.skill.factory.SkillFactoryImpl;

/**
 * 工厂管理器，用于管理所有工厂实例
 */
public class FactoryManager {
    
    private static FactoryManager instance;
    
    private CommandFactory commandFactory;
    private SkillFactory skillFactory;
    private PersistenceFactory persistenceFactory;
    private NetworkFactory networkFactory;
    private SystemFactory systemFactory;
    
    private FactoryManager() {
        // 私有构造函数
    }
    
    /**
     * 获取工厂管理器实例
     * @return 工厂管理器实例
     */
    public static synchronized FactoryManager getInstance() {
        if (instance == null) {
            instance = new FactoryManager();
        }
        return instance;
    }
    
    /**
     * 获取命令工厂
     * @return 命令工厂实例
     */
    public CommandFactory getCommandFactory() {
        if (commandFactory == null) {
            commandFactory = new CommandFactoryImpl();
        }
        return commandFactory;
    }
    
    /**
     * 获取技能工厂
     * @return 技能工厂实例
     */
    public SkillFactory getSkillFactory() {
        if (skillFactory == null) {
            skillFactory = new SkillFactoryImpl();
        }
        return skillFactory;
    }
    
    /**
     * 获取持久化工厂
     * @return 持久化工厂实例
     */
    public PersistenceFactory getPersistenceFactory() {
        if (persistenceFactory == null) {
            persistenceFactory = new PersistenceFactoryImpl();
        }
        return persistenceFactory;
    }
    
    /**
     * 获取网络工厂
     * @return 网络工厂实例
     */
    public NetworkFactory getNetworkFactory() {
        if (networkFactory == null) {
            networkFactory = new NetworkFactoryImpl();
        }
        return networkFactory;
    }
    
    /**
     * 获取系统工厂
     * @return 系统工厂实例
     */
    public SystemFactory getSystemFactory() {
        if (systemFactory == null) {
            systemFactory = new SystemFactoryImpl();
        }
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
}
