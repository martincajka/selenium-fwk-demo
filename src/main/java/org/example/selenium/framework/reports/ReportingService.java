package org.example.selenium.framework.reports;

import org.example.selenium.framework.config.FrameworkConfig;
import org.example.selenium.framework.reports.dto.TestResultDTO;
import org.example.selenium.framework.reports.dto.TestRunDTO;
import org.example.selenium.framework.reports.mappers.TestResultMapper;
import org.example.selenium.framework.results.TestRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ReportingService {
    private static final Logger log = LoggerFactory.getLogger(ReportingService.class);
    private final List<ReportGenerator> reportGenerators = new ArrayList<>();

    public ReportingService() {
        if (FrameworkConfig.INSTANCE.getConfigAsBoolean("report.json")) {
            register(new JsonReportGenerator());
        }
    }

    public void register(ReportGenerator reportGenerator) {
        reportGenerators.add(reportGenerator);
    }

    public void process(TestRun testRun) {
        if (testRun == null || testRun.testResults().isEmpty()) {
            log.warn("No test results to process for reporting.");
            return;
        }

        if (reportGenerators.isEmpty()){
            log.warn("No report generators registered. Skipping reporting.");
            return;
        }

        log.info("Processing test run for reporting: {}", testRun);
        TestRunDTO testRunDTO = toDTO(testRun);

        reportGenerators.forEach(generator -> {
            try {
                generator.generate(testRunDTO);
            } catch (Exception e) {
                log.error("Failed to generate report using {}", generator.getClass().getSimpleName(), e);
            }
        });
    }

    private TestRunDTO toDTO(TestRun testRun) {
        List<TestResultDTO> testResultDTOs = testRun.testResults().stream()
                .map(TestResultMapper::toDTO)
                .toList();
        return new TestRunDTO(testResultDTOs);
    }


}
