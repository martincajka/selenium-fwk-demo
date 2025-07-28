package org.example.selenium.framework.reports.dto;

import java.util.List;

public class TestRunDTO {
    private List<TestResultDTO> testResults;

    public TestRunDTO(List<TestResultDTO> testResults) {
        this.testResults = testResults;
    }

    public List<TestResultDTO> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResultDTO> testResults) {
        this.testResults = testResults;
    }
}
