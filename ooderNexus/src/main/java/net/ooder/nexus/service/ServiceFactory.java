package net.ooder.nexus.service;

import net.ooder.nexus.service.config.IConfigService;
import net.ooder.nexus.service.health.IHealthService;
import net.ooder.nexus.service.log.ILogService;
import net.ooder.nexus.service.network.INetworkService;
import net.ooder.nexus.service.protocol.IProtocolService;
import net.ooder.nexus.service.security.ISecurityService;
import net.ooder.nexus.service.system.ISystemService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务工厂类
 * 负责管理所有服务实例，提供统一的服务获取方式
 */
public class ServiceFactory {
    
    private static final ServiceFactory instance = new ServiceFactory();
    private final Map<Class<?>, Object> serviceMap = new ConcurrentHashMap<>();
    
    private ServiceFactory() {
        // 私有构造方法，防止外部实例化
    }
    
    /**
     * 获取服务工厂实例
     * @return 服务工厂实例
     */
    public static ServiceFactory getInstance() {
        return instance;
    }
    
    /**
     * 注册服务实例
     * @param serviceClass 服务接口类
     * @param serviceInstance 服务实例
     * @param <T> 服务类型
     */
    public <T> void registerService(Class<T> serviceClass, T serviceInstance) {
        serviceMap.put(serviceClass, serviceInstance);
    }
    
    /**
     * 获取服务实例
     * @param serviceClass 服务接口类
     * @param <T> 服务类型
     * @return 服务实例
     */
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        return (T) serviceMap.get(serviceClass);
    }
    
    /**
     * 获取系统管理服务
     * @return 系统管理服务实例
     */
    public ISystemService getSystemService() {
        return getService(ISystemService.class);
    }
    
    /**
     * 获取网络管理服务
     * @return 网络管理服务实例
     */
    public INetworkService getNetworkService() {
        return getService(INetworkService.class);
    }
    
    /**
     * 获取安全管理服务
     * @return 安全管理服务实例
     */
    public ISecurityService getSecurityService() {
        return getService(ISecurityService.class);
    }
    
    /**
     * 获取配置管理服务
     * @return 配置管理服务实例
     */
    public IConfigService getConfigService() {
        return getService(IConfigService.class);
    }
    
    /**
     * 获取日志管理服务
     * @return 日志管理服务实例
     */
    public ILogService getLogService() {
        return getService(ILogService.class);
    }
    
    /**
     * 获取健康检查服务
     * @return 健康检查服务实例
     */
    public IHealthService getHealthService() {
        return getService(IHealthService.class);
    }
    
    /**
     * 获取协议管理服务
     * @return 协议管理服务实例
     */
    public IProtocolService getProtocolService() {
        return getService(IProtocolService.class);
    }
}
