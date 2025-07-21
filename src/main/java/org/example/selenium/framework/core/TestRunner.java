package org.example.selenium.framework.core;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.example.selenium.framework.browser.BrowserFactory;
import org.example.selenium.framework.browser.BrowserManager;
import org.example.selenium.framework.config.FrameworkConfig;
import org.example.selenium.framework.results.TestResult;
import org.example.selenium.framework.results.TestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestRunner {
    private static final Logger log = LoggerFactory.getLogger(TestRunner.class);
    private static final String TARGET_PACKAGE = "org.example.selenium.framework.tests";
    private static final int MAX_CONCURRENT_SESSIONS = FrameworkConfig.INSTANCE.getConfigAsInt("execution.threadCount", 2);
    private final Semaphore browserSessionLimiter = new Semaphore(MAX_CONCURRENT_SESSIONS);
    private final List<Method> parallelTests = new ArrayList<>();
    private final List<Method> singleThreadedTests = new ArrayList<>();


    public void run() {
        scanForTests();
        executeTests();
    }

    private void executeTests() {
        log.info("Stating test execution...");
        List<Future<TestResult>> allFutures = new ArrayList<>();


        // Execute parallel tests using Virtual Threads
        if (!parallelTests.isEmpty()) {
            allFutures.addAll(executeParallelTests());
        }

        // Execute single-threaded tests sequentially
        if (!singleThreadedTests.isEmpty()) {
            allFutures.addAll(executeSingleThreadedTests());
        }

        log.info("All tests have completed. Processing results...");
        processResults(allFutures);
        log.info("Test execution finished.");
    }

    private List<Future<TestResult>> executeSingleThreadedTests() {
        log.info("Executing {} single-threaded tests...", singleThreadedTests.size());
        // Use a single-thread executor to run these tests one by one.
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            List<Callable<TestResult>> tasks = createTasksFor(singleThreadedTests);
            return executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            log.error("Single-threaded test execution was interrupted", e);
            Thread.currentThread().interrupt();
            return List.of();
        }
    }

    private List<Future<TestResult>> executeParallelTests() {
        log.info("Executing {} parallel tests with Virtual Threads...", parallelTests.size());
        // Use a virtual-thread-per-task executor for maximum concurrency.
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Callable<TestResult>> tasks = createTasksFor(parallelTests);
            // invokeAll blocks until all submitted tasks are complete.
            return executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            log.error("Parallel test execution was interrupted", e);
            Thread.currentThread().interrupt();
            return List.of();
        }
    }

    private List<Callable<TestResult>> createTasksFor(List<Method> methods) {
        return methods.stream()
                .<Callable<TestResult>>map(method -> () -> {
                    log.info("Waiting for permit to run test: {}.{}()",
                            method.getDeclaringClass().getSimpleName(),
                            method.getName());
                    browserSessionLimiter.acquire();
                    try {
                        log.info("🔄 Starting test: {}.{}() on thread {}",
                                method.getDeclaringClass().getSimpleName(),
                                method.getName(),
                                Thread.currentThread().threadId());
                        long startTestExecution = System.currentTimeMillis();
                        try {
                            Object testInstance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
                            method.invoke(testInstance);
                            long endTestExecution = System.currentTimeMillis();
                            return new TestResult(method, TestStatus.PASSED, endTestExecution - startTestExecution, null);
                        } catch (Throwable e) {
                            long endTestExecution = System.currentTimeMillis();
                            return new TestResult(method, TestStatus.FAILED, endTestExecution - startTestExecution, e);
                        }
                    } finally {
                        BrowserManager.quit();
                        browserSessionLimiter.release();
                        log.info("Permit released for test: {}.{}()",
                                method.getDeclaringClass().getSimpleName(),
                                method.getName());
                    }
                })
                .toList();
    }

    private void processResults(List<Future<TestResult>> futures) {
        int success = 0;
        int failed = 0;

        for (Future<TestResult> future : futures) {
            try {
                // .get() is instant here because invokeAll already waited.
                TestResult result = future.get();
                if (result.status() == TestStatus.PASSED) {
                    success++;
                    log.info("✅ PASSED: {}", result.getTestName());
                } else {
                    failed++;
                    log.error("❌ FAILED: {} - Reason: {}", result.getTestName(), result.error().getMessage(), result.error());
                }
            } catch (Exception e) {
                failed++;
                log.error("❌ A test task failed to execute correctly.", e);
            }
        }
        log.info("--- Summary --- Passed: {}, Failed: {}", success, failed);
    }

    private void scanForTests() {
        log.info("Scanning for tests in package: " + TARGET_PACKAGE + "...");
        // Use a try-with-resources block to ensure the scanner is closed
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo() // Enables method, field, and annotation info
                .acceptPackages(TARGET_PACKAGE) // Specifies the package to scan
                .scan()) { // Executes the scan

            // Find all classes that have a method annotated with @Test
            for (var classInfo : scanResult.getClassesWithMethodAnnotation(Test.class.getName())) {
                for (Method method : classInfo.loadClass().getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Test.class)) {
                        if (method.isAnnotationPresent(SingleThreaded.class)) {
                            log.debug("✅ Found single-threaded test: {}.{}()",
                                    classInfo.getSimpleName(),
                                    method.getName());
                            singleThreadedTests.add(method);
                        } else {
                            log.debug("✅ Found parallel test: {}.{}()",
                                    classInfo.getSimpleName(),
                                    method.getName());
                            parallelTests.add(method);
                        }
                    }
                }
            }
        }
        log.info("Scanning for tests finished");
    }
}
