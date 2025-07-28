package org.example.selenium.framework.reports.mappers;

import org.example.selenium.framework.reports.dto.TestActionDTO;
import org.example.selenium.framework.reports.dto.TestResultDTO;
import org.example.selenium.framework.results.TestResult;

import java.util.List;
import java.util.stream.Collectors;

public class TestResultMapper {
    public static TestResultDTO toDTO(TestResult testResult) {
        if (testResult == null) {
            return null;
        }

        String testName = testResult.getTestName();
        String status = testResult.status().name();
        long testStartTimestamp = testResult.testStartTimestamp();
        long testEndTimestamp = testResult.testEndTimestamp();
        String error = testResult.error();
        List<TestActionDTO> testActions =
            testResult.testActions().stream()
                .map(action -> new org.example.selenium.framework.reports.dto.TestActionDTO(
                        action.action(),
                        action.target(),
                        action.startTimestamp(),
                        action.endTimestamp(),
                        action.success(),
                        action.detailMsg()))
                .collect(Collectors.toList());

        return new TestResultDTO(testName, status, testStartTimestamp, testEndTimestamp, error, testActions);
    }
}
