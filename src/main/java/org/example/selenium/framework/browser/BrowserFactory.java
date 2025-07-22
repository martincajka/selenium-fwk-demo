package org.example.selenium.framework.browser;

import org.example.selenium.framework.config.FrameworkConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariDriverService;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

public class BrowserFactory {

    private static final Logger log = LoggerFactory.getLogger(BrowserFactory.class);

   /**
     * Creates a WebDriver instance based on configuration settings.
     * Supports headless mode and video recording.
     *
     * @return Configured WebDriver instance
     */
    public static WebDriver createDriver() {
        String browserName = FrameworkConfig.INSTANCE.getConfig("browser", "chrome");
        boolean headless = FrameworkConfig.INSTANCE.getConfigAsBoolean("browser.headless");

        log.debug("Creating a new '{}' WebDriver instance. Headless: {}",
                browserName, headless);

        switch (browserName.toLowerCase()) {
            case "chrome":
                return createChromeDriver(headless);
            case "firefox":
                return createFirefoxDriver(headless);
            case "edge":
                return createEdgeDriver(headless);
            case "safari":
                return createSafariDriver();
            default:
                throw new IllegalArgumentException("Unsupported browser specified: " + browserName);
        }
    }
    
    /**
     * Creates a Chrome WebDriver with specified options.
     */
    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        
        // Configure headless mode
        if (headless) {
            options.addArguments("--headless=new");
        }
        
        // Add additional Chrome-specific options
        options.addArguments("--start-maximized");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        
        return new ChromeDriver(options);
    }
    
    /**
     * Creates a Firefox WebDriver with specified options.
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        
        // Configure headless mode
        if (headless) {
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }
    
    /**
     * Creates an Edge WebDriver with specified options.
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        
        // Configure headless mode
        if (headless) {
            options.addArguments("--headless");
        }
        
        return new EdgeDriver(options);
    }
    
    /**
     * Creates a Safari WebDriver with specified options.
     * Note: Safari does not support headless mode.
     */
    private static WebDriver createSafariDriver() {
        SafariOptions options = new SafariOptions();
        
        return new SafariDriver(options);
    }

//    todo add driver download using webdriver manager
}