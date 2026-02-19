package net.ooder.sdk.api.storage;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * TypeReference for generic type support
 *
 * <p>Usage example:</p>
 * <pre>
 * TypeReference&lt;List&lt;String&gt;&gt; typeRef = new TypeReference&lt;List&lt;String&gt;&gt;() {};
 * Optional&lt;List&lt;String&gt;&gt; result = storage.load("key", typeRef);
 * </pre>
 *
 * @param <T> the type being referenced
 * @author ooder Team
 * @since 0.7.1
 */
public abstract class TypeReference<T> {
    
    private final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            this.type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("TypeReference must be parameterized");
        }
    }

    public Type getType() {
        return type;
    }

    public Class<?> getRawType() {
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof Class) {
            return (Class<?>) type;
        }
        return Object.class;
    }
}
