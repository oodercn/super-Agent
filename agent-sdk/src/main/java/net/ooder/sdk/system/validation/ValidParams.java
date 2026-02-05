package net.ooder.sdk.system.validation;

import net.ooder.sdk.system.validation.ValidParam;

import java.lang.annotation.*;

/**
 * ValidParam注解的容器，用于支持重复注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidParams {
    ValidParam[] value();
}
