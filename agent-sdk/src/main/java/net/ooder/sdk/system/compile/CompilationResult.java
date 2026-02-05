package net.ooder.sdk.system.compile;

import java.util.List;

public class CompilationResult {
    private boolean success;
    private String language;
    private String outputPath;
    private List<String> warnings;
    private List<String> errors;
    private long compilationTimeMs;
    private String compilationOutput;

    public CompilationResult() {
    }

    public CompilationResult(boolean success, String language, String outputPath, 
                           List<String> warnings, List<String> errors, 
                           long compilationTimeMs, String compilationOutput) {
        this.success = success;
        this.language = language;
        this.outputPath = outputPath;
        this.warnings = warnings;
        this.errors = errors;
        this.compilationTimeMs = compilationTimeMs;
        this.compilationOutput = compilationOutput;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public long getCompilationTimeMs() {
        return compilationTimeMs;
    }

    public void setCompilationTimeMs(long compilationTimeMs) {
        this.compilationTimeMs = compilationTimeMs;
    }

    public String getCompilationOutput() {
        return compilationOutput;
    }

    public void setCompilationOutput(String compilationOutput) {
        this.compilationOutput = compilationOutput;
    }
}