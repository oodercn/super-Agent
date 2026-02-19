
package net.ooder.sdk.common.constants;

public final class Defaults {
    
    public static final int DEFAULT_RETRY_COUNT = 3;
    
    public static final long DEFAULT_RETRY_INTERVAL_MS = 1000;
    
    public static final double DEFAULT_RETRY_MULTIPLIER = 2.0;
    
    public static final long DEFAULT_RETRY_MAX_INTERVAL_MS = 30000;
    
    public static final int DEFAULT_THREAD_POOL_SIZE = 10;
    
    public static final int DEFAULT_CACHE_SIZE = 1000;
    
    public static final long DEFAULT_CACHE_EXPIRE_MS = 3600000;
    
    public static final int DEFAULT_QUEUE_CAPACITY = 10000;
    
    public static final String DEFAULT_ENCODING = "UTF-8";
    
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public static final String DEFAULT_TIMEZONE = "UTC";
    
    private Defaults() {
    }
}
