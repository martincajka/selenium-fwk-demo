package org.example.selenium.framework.reports.dto;

import java.util.List;

public class TestResultDTO {
    private String testName;
    private String status;
    private long startTimestamp;
    private long endTimestamp;
    private String error;
    private List<TestActionDTO> testActions;

    public TestResultDTO(String testName, String status, long startTimestamp, long endTimestamp, String error, List<TestActionDTO> testActions) {
        this.testName = testName;
        this.status = status;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.error = error;
        this.testActions = testActions;
    }

    public String getTestName() {
        return testName;
    }

    public String getStatus() {
        return status;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public String getError() {
        return error;
    }

    public List<TestActionDTO> getTestActions() {
        return testActions;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setTestActions(List<TestActionDTO> testActions) {
        this.testActions = testActions;
    }

    @Override
    public String toString() {
        return "TestResultDTO{" +
                "testName='" + testName + '\'' +
                ", status='" + status + '\'' +
                ", startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                ", error='" + error + '\'' +
                ", testActions=" + testActions +
                '}';
    }
}
