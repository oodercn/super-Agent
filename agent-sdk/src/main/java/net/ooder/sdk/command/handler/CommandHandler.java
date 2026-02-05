package net.ooder.sdk.command.handler;


import net.ooder.sdk.command.model.CommandType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令处理方法注解，用于标记处理特定命令类型的方法
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandHandler {
    /**
     * 命令类型
     */
    CommandType value();
}
