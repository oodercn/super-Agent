package net.ooder.sdk.system.validation;


import net.ooder.sdk.system.validation.ValidParams;

import java.lang.annotation.*;

/**
 * 参数验证注解，用于标记需要验证的参数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(ValidParams.class)
public @interface ValidParam {
    /**
     * 参数名称
     */
    String name();
    
    /**
     * 是否必填
     */
    boolean required() default true;
    
    /**
     * 参数类型
     */
    Class<?> type() default Object.class;
    
    /**
     * 集合元素类型（适用于Collection和Map类型）
     */
    Class<?> elementType() default Object.class;
    
    /**
     * Map键类型（适用于Map类型）
     */
    Class<?> keyType() default Object.class;
    
    /**
     * 最小长度（适用于String类型）
     */
    int minLength() default 0;
    
    /**
     * 最大长度（适用于String类型）
     */
    int maxLength() default Integer.MAX_VALUE;
    
    /**
     * 最小值（适用于数字类型）
     */
    long min() default Long.MIN_VALUE;
    
    /**
     * 最大值（适用于数字类型）
     */
    long max() default Long.MAX_VALUE;
    
    /**
     * 正则表达式（适用于String类型）
     */
    String pattern() default "";
    
    /**
     * 枚举类型（适用于枚举类型验证）
     */
    Class<?> enumClass() default Void.class;
    
    /**
     * 允许的枚举值（适用于枚举类型验证）
     */
    String[] enumValues() default {};
    
    /**
     * 错误消息
     */
    String message() default "";
}
