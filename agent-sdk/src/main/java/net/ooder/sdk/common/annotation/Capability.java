
package net.ooder.sdk.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Capability {
    String id();
    String name() default "";
    String description() default "";
    String[] parameters() default {};
    String returnType() default "void";
}
