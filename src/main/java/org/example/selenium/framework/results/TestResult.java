package org.example.selenium.framework.results;

import java.lang.reflect.Method;

public record TestResult(Method testMethod, TestStatus status, long durationMilis, Throwable error) {

    public String getTestName() {
        return testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName();
    }
}
