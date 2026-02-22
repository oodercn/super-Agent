package net.ooder.nexus.common.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 审计日志注解
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
    
    String action() default "";
    
    String resourceType() default "";
    
    String description() default "";
    
    boolean logParams() default true;
    
    boolean logResult() default false;
}
