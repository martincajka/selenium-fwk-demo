package org.example.selenium.framework.results;

import org.example.selenium.framework.listener.TestAction;

import java.lang.reflect.Method;
import java.util.List;

public record TestResult(Method testMethod, TestStatus status, long durationMilis, Throwable error, List<TestAction> testActions) {

    public String getTestName() {
        return testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName();
    }
}
