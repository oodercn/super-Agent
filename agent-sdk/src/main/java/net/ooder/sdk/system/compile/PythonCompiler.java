package net.ooder.sdk.system.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PythonCompiler implements CodeCompiler {
    private static final Logger log = LoggerFactory.getLogger(PythonCompiler.class);
    private static final String LANGUAGE = "python";
    private static final String PYTHON_COMMAND = "python3";
    private static final String PIP_COMMAND = "pip3";
    private static final String TEST_COMMAND = "pytest";

    @Override
    public String getLanguage() {
        return LANGUAGE;
    }

    @Override
    public CompletableFuture<CompilationResult> compile(String code, Map<String, Object> options) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            List<String> warnings = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            try {
                // 创建临时文件
                Path tempDir = Files.createTempDirectory("python_compile");
                Path codeFile = tempDir.resolve("code.py");
                Files.write(codeFile, code.getBytes(StandardCharsets.UTF_8));

                // 检查Python语法
                ProcessBuilder pb = new ProcessBuilder(PYTHON_COMMAND, "-m", "py_compile", codeFile.toString());
                pb.redirectErrorStream(true);
                Process process = pb.start();

                String output = new String(readAllBytes(process.getInputStream()), StandardCharsets.UTF_8);
                int exitCode = process.waitFor();

                if (exitCode != 0) {
                    // 解析语法错误
                    errors.add(output);
                    return new CompilationResult(false, LANGUAGE, null, warnings, errors, 
                                               System.currentTimeMillis() - startTime, output);
                }

                // 检查是否有警告
                pb = new ProcessBuilder(PYTHON_COMMAND, "-W", "all", "-c", "import py_compile; py_compile.compile('" + codeFile.toString() + "')");
                pb.redirectErrorStream(true);
                process = pb.start();

                String checkOutput = new String(readAllBytes(process.getInputStream()), StandardCharsets.UTF_8);
                process.waitFor();

                if (!checkOutput.isEmpty()) {
                    warnings.add(checkOutput);
                }

                log.info("Python code compiled successfully");
                return new CompilationResult(true, LANGUAGE, tempDir.toString(), warnings, errors, 
                                           System.currentTimeMillis() - startTime, output);

            } catch (Exception e) {
                log.error("Error compiling Python code: {}", e.getMessage());
                errors.add(e.getMessage());
                return new CompilationResult(false, LANGUAGE, null, warnings, errors, 
                                           System.currentTimeMillis() - startTime, e.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<CompilationResult> compileFile(File codeFile, Map<String, Object> options) {
        try {
            String code = new String(Files.readAllBytes(codeFile.toPath()), StandardCharsets.UTF_8);
            return compile(code, options);
        } catch (IOException e) {
            CompletableFuture<CompilationResult> future = new CompletableFuture<>();
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            future.complete(new CompilationResult(false, LANGUAGE, null, null, errors, 0, e.getMessage()));
            return future;
        }
    }

    @Override
    public CompletableFuture<TestResult> runTests(String code, Map<String, Object> options) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            List<TestResult.TestCase> testCases = new ArrayList<>();

            try {
                // 创建临时文件
                Path tempDir = Files.createTempDirectory("python_test");
                Path testFile = tempDir.resolve("test_code.py");
                Files.write(testFile, code.getBytes(StandardCharsets.UTF_8));

                // 运行pytest
                ProcessBuilder pb = new ProcessBuilder(PYTHON_COMMAND, "-m", TEST_COMMAND, testFile.toString(), "-v");
                pb.redirectErrorStream(true);
                Process process = pb.start();

                String output = new String(readAllBytes(process.getInputStream()), StandardCharsets.UTF_8);
                int exitCode = process.waitFor();

                // 解析测试结果
                // 这里是简化的解析，实际应该使用更复杂的解析逻辑
                String[] lines = output.split("\n");
                int passedTests = 0;
                int failedTests = 0;

                for (String line : lines) {
                    if (line.startsWith("test_")) {
                        if (line.contains("PASSED")) {
                            passedTests++;
                            testCases.add(new TestResult.TestCase(line.split(" ")[0], true, null, 0));
                        } else if (line.contains("FAILED")) {
                            failedTests++;
                            testCases.add(new TestResult.TestCase(line.split(" ")[0], false, "Test failed", 0));
                        }
                    }
                }

                int totalTests = passedTests + failedTests;
                boolean success = exitCode == 0;

                log.info("Python tests completed: {} passed, {} failed, {} total", passedTests, failedTests, totalTests);
                return new TestResult(success, LANGUAGE, passedTests, failedTests, totalTests, 
                                    System.currentTimeMillis() - startTime, testCases, output);

            } catch (Exception e) {
                log.error("Error running Python tests: {}", e.getMessage());
                return new TestResult(false, LANGUAGE, 0, 1, 1, 
                                    System.currentTimeMillis() - startTime, null, e.getMessage());
            }
        });
    }

    @Override
    public CompletableFuture<TestResult> runTestsFile(File testFile, Map<String, Object> options) {
        try {
            String code = new String(Files.readAllBytes(testFile.toPath()), StandardCharsets.UTF_8);
            return runTests(code, options);
        } catch (IOException e) {
            CompletableFuture<TestResult> future = new CompletableFuture<>();
            future.complete(new TestResult(false, LANGUAGE, 0, 1, 1, 0, null, e.getMessage()));
            return future;
        }
    }

    @Override
    public CompletableFuture<ExecutionResult> execute(String code, Map<String, Object> options) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                // 创建临时文件
                Path tempDir = Files.createTempDirectory("python_execute");
                Path codeFile = tempDir.resolve("code.py");
                Files.write(codeFile, code.getBytes(StandardCharsets.UTF_8));

                // 执行Python代码
                ProcessBuilder pb = new ProcessBuilder(PYTHON_COMMAND, codeFile.toString());
                pb.redirectErrorStream(true);
                Process process = pb.start();

                String output = new String(readAllBytes(process.getInputStream()), StandardCharsets.UTF_8);
                int exitCode = process.waitFor();

                boolean success = exitCode == 0;
                String errorOutput = success ? "" : output;

                log.info("Python code executed: {}", success ? "success" : "failure");
                return new ExecutionResult(success, LANGUAGE, success ? output : "", errorOutput, 
                                         exitCode, System.currentTimeMillis() - startTime);

            } catch (Exception e) {
                log.error("Error executing Python code: {}", e.getMessage());
                return new ExecutionResult(false, LANGUAGE, "", e.getMessage(), -1, 
                                         System.currentTimeMillis() - startTime);
            }
        });
    }

    @Override
    public CompletableFuture<ExecutionResult> executeCompiled(CompilationResult compilationResult, Map<String, Object> options) {
        if (!compilationResult.isSuccess() || compilationResult.getOutputPath() == null) {
            CompletableFuture<ExecutionResult> future = new CompletableFuture<>();
            future.complete(new ExecutionResult(false, LANGUAGE, "", "Compilation failed", -1, 0));
            return future;
        }

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                // 执行编译后的Python代码
                File compiledFile = new File(compilationResult.getOutputPath(), "code.pyc");
                if (!compiledFile.exists()) {
                    compiledFile = new File(compilationResult.getOutputPath(), "__pycache__/code.cpython-*.pyc");
                }

                ProcessBuilder pb = new ProcessBuilder(PYTHON_COMMAND, compiledFile.toString());
                pb.redirectErrorStream(true);
                Process process = pb.start();

                String output = new String(readAllBytes(process.getInputStream()), StandardCharsets.UTF_8);
                int exitCode = process.waitFor();

                boolean success = exitCode == 0;
                String errorOutput = success ? "" : output;

                log.info("Compiled Python code executed: {}", success ? "success" : "failure");
                return new ExecutionResult(success, LANGUAGE, success ? output : "", errorOutput, 
                                         exitCode, System.currentTimeMillis() - startTime);

            } catch (Exception e) {
                log.error("Error executing compiled Python code: {}", e.getMessage());
                return new ExecutionResult(false, LANGUAGE, "", e.getMessage(), -1, 
                                         System.currentTimeMillis() - startTime);
            }
        });
    }

    @Override
    public boolean supportsLanguage(String language) {
        return LANGUAGE.equalsIgnoreCase(language);
    }

    @Override
    public boolean isAvailable() {
        try {
            ProcessBuilder pb = new ProcessBuilder(PYTHON_COMMAND, "--version");
            Process process = pb.start();
            process.waitFor();
            return true;
        } catch (Exception e) {
            // 尝试使用python命令
            try {
                ProcessBuilder pb = new ProcessBuilder("python", "--version");
                Process process = pb.start();
                process.waitFor();
                return true;
            } catch (Exception ex) {
                log.warn("Python is not available");
                return false;
            }
        }
    }
    
    /**
     * Java 8 compatible implementation of InputStream.readAllBytes()
     */
    private static byte[] readAllBytes(InputStream input) throws IOException {
        java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }
}