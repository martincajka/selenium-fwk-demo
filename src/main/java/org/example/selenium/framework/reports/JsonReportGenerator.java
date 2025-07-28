package org.example.selenium.framework.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.selenium.framework.reports.dto.TestRunDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonReportGenerator implements ReportGenerator {
    private static final Logger log = LoggerFactory.getLogger(JsonReportGenerator.class);
    private static final String OUTPUT_FILE = "test-results.json";
    private final ObjectMapper objectMapper;

    public JsonReportGenerator() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    public void generate(TestRunDTO testRun) {
        try {
            String json = objectMapper.writeValueAsString(testRun);
            Path outputPath = Paths.get(OUTPUT_FILE);
            Files.writeString(outputPath, json);
            log.info("JSON report generated successfully: {}", OUTPUT_FILE);
        } catch (Exception e) {
            log.error("Failed to write JSON report to file", e);
        }
    }
}
