
package net.ooder.sdk.infra.retry;

import java.util.concurrent.Callable;

public interface RetryStrategy {
    
    <T> T execute(Callable<T> task) throws Exception;
    
    <T> T execute(Callable<T> task, int maxRetries) throws Exception;
    
    String getName();
    
    int getMaxRetries();
    
    long getInitialInterval();
    
    long getMaxInterval();
}
