package net.ooder.sdk.command.impl;

import net.ooder.sdk.command.api.CommandAnnotation;
import net.ooder.sdk.command.api.CommandTask;
import net.ooder.sdk.command.factory.CommandTaskFactory;
import net.ooder.sdk.command.model.CommandType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * 命令任务扫描器，用于自动扫描并注册带有@CommandAnnotation注解的命令任务类
 */
public class CommandTaskScanner {
    private static final Logger log = LoggerFactory.getLogger(CommandTaskScanner.class);
    private final CommandTaskFactory commandTaskFactory;
    
    public CommandTaskScanner(CommandTaskFactory commandTaskFactory) {
        this.commandTaskFactory = commandTaskFactory;
    }
    
    /**
     * 扫描指定包下的所有命令任务类
     * @param packageName 要扫描的包名
     */
    public void scanPackage(String packageName) {
        try {
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
            
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    scanDirectory(new File(resource.toURI()), packageName);
                }
            }
        } catch (Exception e) {
            log.error("Failed to scan package: {}", packageName, e);
        }
    }
    
    /**
     * 扫描指定目录下的所有类文件
     * @param directory 要扫描的目录
     * @param packageName 包名
     */
    private void scanDirectory(File directory, String packageName) {
        if (!directory.exists()) {
            return;
        }
        
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                processClass(className);
            }
        }
    }
    
    /**
     * 处理扫描到的类，检查是否带有@CommandAnnotation注解
     * @param className 类名
     */
    private void processClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            
            // 检查类是否带有@CommandAnnotation注解
            if (clazz.isAnnotationPresent(CommandAnnotation.class)) {
               CommandAnnotation annotation = clazz.getAnnotation(CommandAnnotation.class);
                CommandType commandType = annotation.commandType();
                String commandKey = annotation.key();
                
                // 如果没有指定key，使用commandType作为key
                if (commandKey == null || commandKey.isEmpty()) {
                    commandKey = commandType.getValue();
                }
                
                // 检查类是否实现了CommandTask接口
                if (CommandTask.class.isAssignableFrom(clazz)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends CommandTask> taskClass = (Class<? extends CommandTask>) clazz;
                    
                    // 检查类是否有合适的构造方法
                    boolean hasValidConstructor = hasValidConstructor(taskClass);
                    if (hasValidConstructor) {
                        commandTaskFactory.registerCommandTask(commandType, taskClass);
                        log.info("Registered command task: {} for type: {}, key: {}", className, commandType, commandKey);
                    } else {
                        log.warn("Command task {} has no valid constructor, skipping registration", className);
                    }
                } else {
                    log.warn("Class {} has @CommandAnnotation but does not implement CommandTask, skipping registration", className);
                }
            }
        } catch (Exception e) {
            log.error("Failed to process class: {}", className, e);
        }
    }
    
    /**
     * 检查类是否有合适的构造方法
     * @param taskClass 命令任务类
     * @return 如果有合适的构造方法则返回true，否则返回false
     */
    private boolean hasValidConstructor(Class<? extends CommandTask> taskClass) {
        try {
            // 检查是否有带CommandType参数的构造方法
            Constructor<? extends CommandTask> constructor = taskClass.getConstructor(CommandType.class);
            return constructor != null;
        } catch (NoSuchMethodException e) {
            // 检查是否有默认构造方法
            try {
                Constructor<? extends CommandTask> constructor = taskClass.getConstructor();
                return constructor != null;
            } catch (NoSuchMethodException e1) {
                return false;
            }
        }
    }
}