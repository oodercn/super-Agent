package net.ooder.skillcenter.runtime;

import net.ooder.skillcenter.runtime.impl.JavaRuntimeExecutor;
import net.ooder.skillcenter.runtime.impl.NodeRuntimeExecutor;
import net.ooder.skillcenter.runtime.impl.PythonRuntimeExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 运行时工厂 - 符合v0.7.0协议规范
 */
@Component
public class RuntimeFactory {

    private static final Logger log = LoggerFactory.getLogger(RuntimeFactory.class);

    private final Map<String, RuntimeExecutor> executors = new HashMap<>();

    public RuntimeFactory() {
        registerExecutor(new JavaRuntimeExecutor());
        registerExecutor(new PythonRuntimeExecutor());
        registerExecutor(new NodeRuntimeExecutor());
        log.info("RuntimeFactory initialized with {} executors", executors.size());
    }

    public void registerExecutor(RuntimeExecutor executor) {
        executors.put(executor.getLanguage().toLowerCase(), executor);
        log.debug("Registered runtime executor: {}", executor.getLanguage());
    }

    public RuntimeExecutor getExecutor(String language) {
        if (language == null) {
            return null;
        }
        return executors.get(language.toLowerCase());
    }

    public boolean isLanguageSupported(String language) {
        return language != null && executors.containsKey(language.toLowerCase());
    }

    public Map<String, RuntimeExecutor> getAllExecutors() {
        return new HashMap<>(executors);
    }
}
