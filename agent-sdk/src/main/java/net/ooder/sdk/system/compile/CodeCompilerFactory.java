package net.ooder.sdk.system.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class CodeCompilerFactory {
    private static final Logger log = LoggerFactory.getLogger(CodeCompilerFactory.class);
    private static final Map<String, CodeCompiler> compilerRegistry = new HashMap<>();
    private static CodeCompilerFactory instance;

    static {
        // 初始化编译器注册表
        instance = new CodeCompilerFactory();
        // 加载所有实现了CodeCompiler接口的服务
        ServiceLoader<CodeCompiler> loaders = ServiceLoader.load(CodeCompiler.class);
        for (CodeCompiler compiler : loaders) {
            instance.registerCompiler(compiler);
        }
    }

    private CodeCompilerFactory() {
    }

    public static CodeCompilerFactory getInstance() {
        return instance;
    }

    /**
     * 注册编译器
     */
    public void registerCompiler(CodeCompiler compiler) {
        if (compiler != null) {
            String language = compiler.getLanguage().toLowerCase();
            compilerRegistry.put(language, compiler);
            log.info("Registered compiler for language: {}", language);
        }
    }

    /**
     * 获取指定语言的编译器
     */
    public CodeCompiler getCompiler(String language) {
        if (language == null) {
            return null;
        }
        CodeCompiler compiler = compilerRegistry.get(language.toLowerCase());
        if (compiler == null) {
            log.warn("No compiler found for language: {}", language);
        } else if (!compiler.isAvailable()) {
            log.warn("Compiler for language {} is not available", language);
            return null;
        }
        return compiler;
    }

    /**
     * 检查是否支持指定语言
     */
    public boolean supportsLanguage(String language) {
        if (language == null) {
            return false;
        }
        CodeCompiler compiler = compilerRegistry.get(language.toLowerCase());
        return compiler != null && compiler.isAvailable();
    }

    /**
     * 获取所有支持的语言
     */
    public String[] getSupportedLanguages() {
        return compilerRegistry.keySet().toArray(new String[0]);
    }
}