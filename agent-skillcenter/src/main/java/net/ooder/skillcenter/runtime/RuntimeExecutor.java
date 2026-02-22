package net.ooder.skillcenter.runtime;

import net.ooder.skillcenter.runtime.model.RuntimeConfig;
import net.ooder.skillcenter.runtime.model.RuntimeStatus;

import java.util.Map;

/**
 * 运行时执行器接口 - 符合v0.7.0协议规范
 */
public interface RuntimeExecutor {
    
    String getLanguage();
    
    boolean isSupported(String version);
    
    RuntimeStatus initialize(String runtimeId, RuntimeConfig config);
    
    RuntimeStatus start(String runtimeId);
    
    RuntimeStatus stop(String runtimeId);
    
    RuntimeStatus getStatus(String runtimeId);
    
    Object execute(String runtimeId, String method, Map<String, Object> params);
    
    void destroy(String runtimeId);
}
