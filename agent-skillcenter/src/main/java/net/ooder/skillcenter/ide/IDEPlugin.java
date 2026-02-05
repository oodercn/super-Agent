package net.ooder.skillcenter.ide;

/**
 * IDE插件接口，所有IDE插件的基础接口
 */
public interface IDEPlugin {
    /**
     * 获取插件ID
     * @return 插件ID
     */
    String getPluginId();
    
    /**
     * 获取插件名称
     * @return 插件名称
     */
    String getPluginName();
    
    /**
     * 获取插件描述
     * @return 插件描述
     */
    String getDescription();
    
    /**
     * 启动插件
     */
    void start();
    
    /**
     * 停止插件
     */
    void stop();
    
    /**
     * 检查插件是否可用
     * @return 是否可用
     */
    boolean isAvailable();
}