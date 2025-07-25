package org.example.selenium.framework.browser;

import org.example.selenium.framework.timing.TimingService;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class WebdriverWrapper implements WebDriver {
    private final WebDriver driver;
    private final Duration defaultTimeout;
    private static final Duration DEFAULT_POLLING_INTERVAL = Duration.ofMillis(500);
    private final TimingService timingService;

    public WebdriverWrapper(WebDriver driver, Duration defaultTimeout) {
        this(driver, defaultTimeout, null);
    }
    
    public WebdriverWrapper(WebDriver driver, Duration defaultTimeout, TimingService timingService) {
        this.driver = driver;
        this.defaultTimeout = defaultTimeout;
        this.timingService = timingService;
    }

    @Override
    public WebElement findElement(By by) {
        if (timingService != null) {
            timingService.startTiming("Wait for Element", by.toString());
        }
        
        try {
            WebElement element = new WebDriverWait(driver, defaultTimeout)
                    .pollingEvery(DEFAULT_POLLING_INTERVAL)
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.presenceOfElementLocated(by));
            
            if (timingService != null) {
                timingService.endTiming("Wait for Element", by.toString(), true);
            }
            
            return element;
        } catch (TimeoutException e) {
            if (timingService != null) {
                timingService.endTiming("Wait for Element", by.toString(), false, e.getMessage());
            }
            
            throw new NoSuchElementException("Element not found within timeout: " + by, e);
        }
    }

    @Override
    public List<WebElement> findElements(By by) {
        if (timingService != null) {
            timingService.startTiming("Wait for Elements", by.toString());
        }
        
        try {
            List<WebElement> elements = new WebDriverWait(driver, defaultTimeout)
                    .pollingEvery(DEFAULT_POLLING_INTERVAL)
                    .ignoring(StaleElementReferenceException.class)
                    .until(driver -> {
                        List<WebElement> foundElements = driver.findElements(by);
                        return !foundElements.isEmpty() ? foundElements : null;
                    });
            
            if (timingService != null) {
                timingService.endTiming("Wait for Elements", by.toString(), true);
            }
            
            return elements;
        } catch (TimeoutException e) {
            if (timingService != null) {
                timingService.endTiming("Wait for Elements", by.toString(), false);
            }
            
            return List.of();
        }
    }

    @Override
    public void get(String url) {
        driver.get(url);
    }

    @Override
    public @Nullable String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public @Nullable String getTitle() {
        return driver.getTitle();
    }

    @Override
    public @Nullable String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

}
