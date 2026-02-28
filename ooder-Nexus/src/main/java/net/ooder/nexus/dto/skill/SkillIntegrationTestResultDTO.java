package net.ooder.nexus.dto.skill;

import java.io.Serializable;
import java.util.List;

public class SkillIntegrationTestResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean success;
    private List<String> testResults;
    private Integer totalTests;
    private Integer passedTests;
    private String error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<String> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<String> testResults) {
        this.testResults = testResults;
    }

    public Integer getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(Integer totalTests) {
        this.totalTests = totalTests;
    }

    public Integer getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(Integer passedTests) {
        this.passedTests = passedTests;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
