package net.ooder.sdk.system.factory;

import net.ooder.sdk.system.heartbeat.HeartbeatManager;
import net.ooder.sdk.network.RouteAgentManager;
import net.ooder.sdk.agent.scene.SceneManager;
import net.ooder.sdk.system.sleep.SleepManager;
import net.ooder.sdk.system.sleep.SleepStrategy;

/**
 * 系统工厂，用于创建系统相关组件
 */
public interface SystemFactory extends Factory {
    
    /**
     * 创建心跳管理器
     * @return 心跳管理器实例
     */
    HeartbeatManager createHeartbeatManager();
    
    /**
     * 创建睡眠管理器
     * @param strategy 睡眠策略
     * @return 睡眠管理器实例
     */
    SleepManager createSleepManager(SleepStrategy strategy);
    
    /**
     * 创建路由代理管理器
     * @return 路由代理管理器实例
     */
    RouteAgentManager createRouteAgentManager();
    
    /**
     * 创建场景管理器
     * @return 场景管理器实例
     */
    SceneManager createSceneManager();
}
