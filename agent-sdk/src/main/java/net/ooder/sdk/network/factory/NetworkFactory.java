package net.ooder.sdk.network.factory;

import net.ooder.sdk.network.udp.UDPSDK;
import net.ooder.sdk.network.udp.UDPConfig;
import net.ooder.sdk.network.udp.UDPMessageHandler;
import net.ooder.sdk.system.factory.Factory;

/**
 * 网络工厂，用于创建网络通信相关组件
 */
public interface NetworkFactory extends Factory {
    
    /**
     * 创建UDP SDK实例
     * @param config UDP配置
     * @return UDP SDK实例
     * @throws Exception 初始化异常
     */
    UDPSDK createUDPSDK(UDPConfig config) throws Exception;
    
    /**
     * 创建UDP配置
     * @return UDP配置实例
     */
    UDPConfig createUDPConfig();
    
    /**
     * 创建UDP消息处理器
     * @return UDP消息处理器实例
     */
    UDPMessageHandler createUDPMessageHandler();
}
