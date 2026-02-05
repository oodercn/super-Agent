package net.ooder.sdk.system.compile;

import java.util.List;

public class TestResult {
    private boolean success;
    private String language;
    private int passedTests;
    private int failedTests;
    private int totalTests;
    private long testTimeMs;
    private List<TestCase> testCases;
    private String testOutput;

    public TestResult() {
    }

    public TestResult(boolean success, String language, int passedTests, 
                    int failedTests, int totalTests, long testTimeMs, 
                    List<TestCase> testCases, String testOutput) {
        this.success = success;
        this.language = language;
        this.passedTests = passedTests;
        this.failedTests = failedTests;
        this.totalTests = totalTests;
        this.testTimeMs = testTimeMs;
        this.testCases = testCases;
        this.testOutput = testOutput;
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

    public int getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(int passedTests) {
        this.passedTests = passedTests;
    }

    public int getFailedTests() {
        return failedTests;
    }

    public void setFailedTests(int failedTests) {
        this.failedTests = failedTests;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public long getTestTimeMs() {
        return testTimeMs;
    }

    public void setTestTimeMs(long testTimeMs) {
        this.testTimeMs = testTimeMs;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public String getTestOutput() {
        return testOutput;
    }

    public void setTestOutput(String testOutput) {
        this.testOutput = testOutput;
    }

    public static class TestCase {
        private String name;
        private boolean passed;
        private String errorMessage;
        private long executionTimeMs;

        public TestCase() {
        }

        public TestCase(String name, boolean passed, String errorMessage, long executionTimeMs) {
            this.name = name;
            this.passed = passed;
            this.errorMessage = errorMessage;
            this.executionTimeMs = executionTimeMs;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isPassed() {
            return passed;
        }

        public void setPassed(boolean passed) {
            this.passed = passed;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public long getExecutionTimeMs() {
            return executionTimeMs;
        }

        public void setExecutionTimeMs(long executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
        }
    }
}