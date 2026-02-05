package net.ooder.sdk.system.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单的IOC容器实现，用于管理对象实例
 */
public class SimpleIOCContainer {
    private static final Logger log = LoggerFactory.getLogger(SimpleIOCContainer.class);
    private static final SimpleIOCContainer INSTANCE = new SimpleIOCContainer();
    
    private final Map<Class<?>, Object> instances = new ConcurrentHashMap<>();
    private final Map<String, Object> namedInstances = new ConcurrentHashMap<>();
    private final Map<Class<?>, Class<?>> typeMappings = new ConcurrentHashMap<>();
    
    private SimpleIOCContainer() {
        // 私有构造方法，实现单例模式
    }
    
    public static SimpleIOCContainer getInstance() {
        return INSTANCE;
    }
    
    /**
     * 注册类型映射
     * @param interfaceType 接口类型
     * @param implementationType 实现类型
     */
    public <T> void registerType(Class<T> interfaceType, Class<? extends T> implementationType) {
        typeMappings.put(interfaceType, implementationType);
        log.info("Registered type mapping: {} -> {}", interfaceType.getName(), implementationType.getName());
    }
    
    /**
     * 注册对象实例
     * @param type 对象类型
     * @param instance 对象实例
     */
    @SuppressWarnings("unchecked")
    public <T> void registerInstance(Class<?> type, T instance) {
        instances.put(type, instance);
        log.info("Registered instance: {} -> {}", type.getName(), instance.getClass().getName());
    }
    
    /**
     * 注册命名对象实例
     * @param name 对象名称
     * @param instance 对象实例
     */
    public <T> void registerNamedInstance(String name, T instance) {
        namedInstances.put(name, instance);
        log.info("Registered named instance: {} -> {}", name, instance.getClass().getName());
    }
    
    /**
     * 获取对象实例
     * @param type 对象类型
     * @return 对象实例
     * @throws Exception 获取对象实例失败时抛出异常
     */
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> type) throws Exception {
        // 检查是否已有实例
        Object instance = instances.get(type);
        if (instance != null) {
            return (T) instance;
        }
        
        // 检查是否有类型映射
        Class<?> implementationType = typeMappings.get(type);
        if (implementationType != null) {
            // 创建实例
            instance = implementationType.newInstance();
            // 注册实例
            instances.put(type, instance);
            log.info("Created and registered instance: {} -> {}", type.getName(), implementationType.getName());
            return (T) instance;
        }
        
        // 如果是接口或抽象类，抛出异常
        if (type.isInterface() || Modifier.isAbstract(type.getModifiers())) {
            throw new IllegalArgumentException("No implementation registered for interface/abstract class: " + type.getName());
        }
        
        // 创建实例
        instance = type.newInstance();
        // 注册实例
        instances.put(type, instance);
        log.info("Created and registered instance: {}", type.getName());
        return (T) instance;
    }
    
    /**
     * 获取命名对象实例
     * @param name 对象名称
     * @return 对象实例
     */
    @SuppressWarnings("unchecked")
    public <T> T getNamedInstance(String name) {
        Object instance = namedInstances.get(name);
        if (instance == null) {
            throw new IllegalArgumentException("No named instance registered: " + name);
        }
        return (T) instance;
    }
    
    /**
     * 检查是否已注册指定类型的实例
     * @param type 对象类型
     * @return 如果已注册则返回true，否则返回false
     */
    public boolean hasInstance(Class<?> type) {
        return instances.containsKey(type) || typeMappings.containsKey(type);
    }
    
    /**
     * 检查是否已注册指定名称的实例
     * @param name 对象名称
     * @return 如果已注册则返回true，否则返回false
     */
    public boolean hasNamedInstance(String name) {
        return namedInstances.containsKey(name);
    }
    
    /**
     * 清理容器，移除所有实例
     */
    public void clear() {
        instances.clear();
        namedInstances.clear();
        typeMappings.clear();
        log.info("IOC container cleared");
    }
}