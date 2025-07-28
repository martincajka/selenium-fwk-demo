package org.example.selenium.framework.reports;

import org.example.selenium.framework.reports.dto.TestRunDTO;

@FunctionalInterface
public interface ReportGenerator {
    void generate(TestRunDTO testRun);
}
