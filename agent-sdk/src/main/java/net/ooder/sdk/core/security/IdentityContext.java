package net.ooder.sdk.core.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IdentityContext {
    
    private static final ThreadLocal<CoreIdentity> currentIdentity = new ThreadLocal<CoreIdentity>();
    
    public static void setCurrentIdentity(CoreIdentity identity) {
        currentIdentity.set(identity);
    }
    
    public static CoreIdentity getCurrentIdentity() {
        return currentIdentity.get();
    }
    
    public static void clear() {
        currentIdentity.remove();
    }
    
    public static boolean hasIdentity() {
        return currentIdentity.get() != null;
    }
}
