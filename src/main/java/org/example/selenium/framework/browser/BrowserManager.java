package org.example.selenium.framework.browser;

import org.openqa.selenium.WebDriver;

public class BrowserManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * Returns the WebDriver instance for the current thread.
     * If an instance does not exist, it creates a new one.
     *
     * @return The thread-safe WebDriver instance.
     */
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            driver.set(BrowserFactory.createDriver());
        }
        return driver.get();
    }

    /**
     * Quits the WebDriver and removes it from the current thread.
     */
    public static void quit() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}