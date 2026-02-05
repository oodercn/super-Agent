package net.ooder.sdk.system.compile;

import java.io.File;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CodeCompiler {
    /**
     * 获取支持的语言类型
     */
    String getLanguage();

    /**
     * 编译代码
     */
    CompletableFuture<CompilationResult> compile(String code, Map<String, Object> options);

    /**
     * 编译代码文件
     */
    CompletableFuture<CompilationResult> compileFile(File codeFile, Map<String, Object> options);

    /**
     * 运行测试
     */
    CompletableFuture<TestResult> runTests(String code, Map<String, Object> options);

    /**
     * 运行测试文件
     */
    CompletableFuture<TestResult> runTestsFile(File testFile, Map<String, Object> options);

    /**
     * 执行代码
     */
    CompletableFuture<ExecutionResult> execute(String code, Map<String, Object> options);

    /**
     * 执行编译后的代码
     */
    CompletableFuture<ExecutionResult> executeCompiled(CompilationResult compilationResult, Map<String, Object> options);

    /**
     * 检查是否支持该语言
     */
    boolean supportsLanguage(String language);

    /**
     * 检查编译器是否可用
     */
    boolean isAvailable();
}