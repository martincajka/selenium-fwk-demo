package org.example.selenium.framework.browser;

import org.example.selenium.framework.config.FrameworkConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserFactory {

    private static final Logger log = LoggerFactory.getLogger(BrowserFactory.class);

    public static WebDriver createDriver() {
        String browserName = FrameworkConfig.INSTANCE.getConfig("browser", "chrome");
        log.info("Creating a new '{}' WebDriver instance.", browserName);

        switch (browserName.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                if (FrameworkConfig.INSTANCE.getConfigAsBoolean("browser.headless")) {
                    chromeOptions.addArguments("--headless");
                }
                // Add other options from config as needed
                return new ChromeDriver(chromeOptions);
             case "firefox":
                return new FirefoxDriver(); // Example for other browsers
            default:
                throw new IllegalArgumentException("Unsupported browser specified: " + browserName);
        }
    }

//    todo add driver download using webdriver manager
}