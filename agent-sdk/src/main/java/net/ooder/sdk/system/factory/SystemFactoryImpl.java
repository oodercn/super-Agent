package net.ooder.sdk.system.factory;

import net.ooder.sdk.agent.model.AgentManager;
import net.ooder.sdk.network.factory.NetworkFactory;
import net.ooder.sdk.system.factory.SystemFactory;
import net.ooder.sdk.system.heartbeat.HeartbeatManager;
import net.ooder.sdk.network.RouteAgentManager;
import net.ooder.sdk.agent.scene.SceneManager;
import net.ooder.sdk.network.udp.UDPConfig;
import net.ooder.sdk.network.udp.UDPSDK;
import net.ooder.sdk.system.sleep.SleepManager;
import net.ooder.sdk.system.sleep.SleepStrategy;

import java.util.HashMap;

/**
 * 系统工厂实现类
 */
public class SystemFactoryImpl implements SystemFactory {
    
    @Override
    public HeartbeatManager createHeartbeatManager() {
        // 创建默认的心跳管理器，实际使用时需要通过构造函数传入参数
        return new HeartbeatManager("default", 30000, 60000, 3);
    }
    
    @Override
    public SleepManager createSleepManager(SleepStrategy strategy) {
        return new SleepManager(strategy);
    }
    
    @Override
    public RouteAgentManager createRouteAgentManager() {
        // 创建默认的路由代理管理器，实际使用时需要通过构造函数传入参数
        try {
            NetworkFactory networkFactory = AgentManager.getInstance().getNetworkFactory();
            UDPConfig udpConfig = networkFactory.createUDPConfig();
            UDPSDK udpSDK = networkFactory.createUDPSDK(udpConfig);
            return new RouteAgentManager(udpSDK, "default-route", "Default Route Agent", new HashMap<>());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create RouteAgentManager", e);
        }
    }
    
    @Override
    public SceneManager createSceneManager() {
        return new SceneManager();
    }
}
