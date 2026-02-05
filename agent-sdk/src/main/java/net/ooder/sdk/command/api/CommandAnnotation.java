package net.ooder.sdk.command.api;


import net.ooder.sdk.command.model.CommandType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令注解，用于标记命令类型和元数据
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandAnnotation {
    /**
     * 命令ID
     */
    String id();
    
    /**
     * 命令名称
     */
    String name();
    
    /**
     * 命令描述
     */
    String desc();
    
    /**
     * 命令表达式
     */
    String[] expressionArr() default {};
    
    /**
     * 命令类型枚举值
     */
    CommandType commandType() default CommandType.COMMAND_RESPONSE;
    
    /**
     * 命令key，用于唯一标识命令
     */
    String key() default "";
    
}
