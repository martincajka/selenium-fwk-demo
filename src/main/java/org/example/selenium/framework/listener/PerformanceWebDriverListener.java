package org.example.selenium.framework.listener;

import org.example.selenium.framework.timing.TimingService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Listener that tracks performance metrics for WebDriver operations.
 * Uses ThreadLocal to track when operations are part of an explicit wait.
 */
public class PerformanceWebDriverListener implements WebDriverListener {
    private final TimingService timingService;
    
    // Using ThreadLocal instead of ConcurrentMap for better performance and cleaner code
    private final ThreadLocal<Boolean> inExplicitWait = ThreadLocal.withInitial(() -> false);

    public PerformanceWebDriverListener(TimingService timingService) {
        this.timingService = timingService;
    }

    public List<TestAction> getTimings() {
        return timingService.getTimings();
    }

    private void before(String operationType, String target) {
        timingService.startTiming(operationType, target);
    }

    private void after(String operationType, String target, boolean success) {
        timingService.endTiming(operationType, target, success, null);
    }


    private void after(String operationType, String target, boolean success, String errorMessage) {
        timingService.endTiming(operationType, target, success, errorMessage);
    }

    @Override
    public void beforeAnyCall(Object target, Method method, Object[] args) {
        // Detect when WebDriverWait.until() is called
        if (target instanceof WebDriverWait && "until".equals(method.getName())) {
            inExplicitWait.set(true);
            String waitDescription = extractWaitDescription(args);
            before("Explicit Wait", waitDescription);
        }
    }

    @Override
    public void afterAnyCall(Object target, Method method, Object[] args, Object result) {
        // Detect when WebDriverWait.until() completes
        if (target instanceof WebDriverWait && "until".equals(method.getName())) {
            inExplicitWait.set(false);

            // Try to extract meaningful information about what was being waited for
            String waitDescription = extractWaitDescription(args);
            after("Explicit Wait", waitDescription, true);
        }
    }

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        // Handle WebDriverWait timeout errors
        if (target instanceof WebDriverWait && "until".equals(method.getName())) {
            inExplicitWait.set(false);
            String errorMessage = e.getTargetException().toString();
            String waitDescription = extractWaitDescription(args);
            after("Explicit Wait", waitDescription + " - " + e.getTargetException().getClass().getSimpleName(), false, errorMessage);
        }
        // Handle WebDriver errors
        else if (target instanceof WebDriver) {
            String operation = method.getName();
            String targetDesc = args != null && args.length > 0 ? args[0].toString() : "unknown";
            after(operation, targetDesc, false, e.getTargetException().toString());
        }
        // Handle WebElement errors
        else if (target instanceof WebElement) {
            String operation = method.getName();
            String targetDesc = target.toString();
            String errorMessage = e.getTargetException().toString();
            after(operation, targetDesc + " - " + e.getTargetException().getClass().getSimpleName(), false, errorMessage);
        }
    }

    @Override
    public void beforeGet(WebDriver driver, String url) {
        before("Navigate to URL", url);
    }

    @Override
    public void afterGet(WebDriver driver, String url) {
        after("Navigate to URL", url, true);
    }

    @Override
    public void beforeFindElements(WebDriver driver, By locator) {
        if (!inExplicitWait.get()) {
            before("Find Elements", locator.toString());
        }
    }

    @Override
    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
        if (!inExplicitWait.get()) {
            after("Find Elements", locator.toString(), true);
        }
    }

    @Override
    public void beforeClick(WebElement element) {
        before("Click Element", element.toString());
    }

    @Override
    public void afterClick(WebElement element) {
        after("Click Element", element.toString(), true);
    }

    @Override
    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        before("Send Keys", element.toString());
    }

    @Override
    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        after("Send Keys", element.toString() + " - " + String.join("", keysToSend), true);
    }

    @Override
    public void beforeGetText(WebElement element) {
        before("Get Text", element.toString());
    }

    @Override
    public void afterGetText(WebElement element, String result) {
        after("Get Text", element.toString(), true);
    }

    private String extractWaitDescription(Object[] args) {
        if (args != null && args.length > 0) {
            // The first argument to until() is typically the ExpectedCondition or Function
            String functionString = args[0].toString();

            // Try to extract meaningful information from common wait patterns
            if (functionString.contains("findElement")) {
                // Extract locator information if possible
                return "Wait for element";
            } else if (functionString.contains("ExpectedConditions")) {
                return "Expected condition";
            } else {
                return "Custom condition";
            }
        }
        return "Unknown wait condition";
    }
}