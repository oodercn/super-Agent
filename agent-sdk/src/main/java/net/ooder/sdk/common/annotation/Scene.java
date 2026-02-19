
package net.ooder.sdk.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scene {
    String id();
    String name() default "";
    String description() default "";
    String version() default "1.0.0";
    SceneType type() default SceneType.PRIMARY;
    String[] dependencies() default {};
    
    enum SceneType {
        PRIMARY,
        COLLABORATIVE
    }
}
