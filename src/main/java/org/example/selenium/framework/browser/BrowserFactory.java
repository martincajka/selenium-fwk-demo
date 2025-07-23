package org.example.selenium.framework.browser;

import org.example.selenium.framework.config.FrameworkConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.http.ClientConfig;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariDriverService;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
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
        String browserVersion = FrameworkConfig.INSTANCE.getConfig("browser.version", "latest");
        boolean headless = FrameworkConfig.INSTANCE.getConfigAsBoolean("browser.headless");
        String viewport = FrameworkConfig.INSTANCE.getConfig("viewport", "desktop.medium");
        MutableCapabilities commonCapabilities = new MutableCapabilities();
        commonCapabilities.setCapability(CapabilityType.BROWSER_VERSION, browserVersion);

        log.debug("Creating a new '{}' (Version: {}) WebDriver instance. Headless: {}, Viewport: {}",
                browserName, browserVersion, headless, viewport);

        // Create the WebDriver instance based on browser type
        WebDriver driver;
        switch (browserName.toLowerCase()) {
            case "chrome":
                driver = createChromeDriver(headless, commonCapabilities);
                break;
            case "firefox":
                driver = createFirefoxDriver(headless, commonCapabilities);
                break;
            case "edge":
                driver = createEdgeDriver(headless, commonCapabilities);
                break;
            case "safari":
                driver = createSafariDriver(commonCapabilities);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser specified: " + browserName);
        }

        // to open browser in top-left corner of the screen
        driver.manage().window().setPosition(new Point(0, 0));

        // Set the viewport size
        Dimension viewportSize = getViewportSize(viewport);
        driver.manage().window().setSize(viewportSize);
        log.debug("Set viewport size to: {}x{}", viewportSize.getWidth(), viewportSize.getHeight());

        return driver;
    }

    /**
     * Creates a Chrome WebDriver with specified options.
     * Supprted versions include "STABLE", "BETA", "DEV", "NIGHTLY" and versions form 115.
     */
    private static WebDriver createChromeDriver(boolean headless, MutableCapabilities commonCapabilities) {
        ChromeOptions options = new ChromeOptions();

        // Configure headless mode
        if (headless) {
            options.addArguments("--headless=new");
        }

        // Add additional Chrome-specific options
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");

        return new ChromeDriver(options.merge(commonCapabilities));
    }

    /**
     * Creates a Firefox WebDriver with specified options.
     */
    private static WebDriver createFirefoxDriver(boolean headless, MutableCapabilities commonCapabilities) {
        FirefoxOptions options = new FirefoxOptions();

        // Configure headless mode
        if (headless) {
            options.addArguments("--headless");
        }

        return new FirefoxDriver(options.merge(commonCapabilities));
    }

    /**
     * Creates an Edge WebDriver with specified options.
     */
    private static WebDriver createEdgeDriver(boolean headless, MutableCapabilities commonCapabilities) {
        EdgeOptions options = new EdgeOptions();

        // Configure headless mode
        if (headless) {
            options.addArguments("--headless");
        }

        return new EdgeDriver(options.merge(commonCapabilities));
    }

    /**
     * Creates a Safari WebDriver with specified options.
     * Note: Safari does not support headless mode.
     * Safari does not support WebDriverManager to handle its versions so any Safari version is ignored.
     */
    private static WebDriver createSafariDriver(MutableCapabilities commonCapabilities) {
        SafariOptions options = new SafariOptions();

        MutableCapabilities filteredCapabilities = commonCapabilities.getCapabilityNames().stream()
                .filter(capability -> !capability.equals(CapabilityType.BROWSER_VERSION))
                .collect(MutableCapabilities::new, (caps, name) -> caps.setCapability(name, commonCapabilities.getCapability(name)), MutableCapabilities::merge);

        return new SafariDriver(options.merge(filteredCapabilities));
    }

    /**
     * Returns a Dimension object representing the viewport size based on the provided viewport name.
     * Supports predefined viewport sizes and custom dimensions.
     *
     * @param viewport The viewport name or custom dimensions
     * @return Dimension object with the specified width and height
     */
    private static Dimension getViewportSize(String viewport) {
        // Check if custom viewport dimensions are provided in config
        String customWidth = FrameworkConfig.INSTANCE.getConfig("viewport.width");
        String customHeight = FrameworkConfig.INSTANCE.getConfig("viewport.height");

        if (customWidth != null && customHeight != null) {
            try {
                int width = Integer.parseInt(customWidth);
                int height = Integer.parseInt(customHeight);
                return new Dimension(width, height);
            } catch (NumberFormatException e) {
                log.warn("Invalid custom viewport dimensions. Using predefined viewport instead.");
            }
        }

        return switch (viewport.toLowerCase()) {
            // Mobile viewports
            case "mobile.small" -> new Dimension(375, 667);    // iPhone 8
            case "mobile.medium" -> new Dimension(390, 844);   // iPhone 12/13
            case "mobile.large" -> new Dimension(428, 926);    // iPhone 13 Pro Max

            // Tablet viewports
            case "tablet.small" -> new Dimension(768, 1024);   // iPad Mini
            case "tablet.medium" -> new Dimension(834, 1112);  // iPad Air
            case "tablet.large" -> new Dimension(1024, 1366);  // iPad Pro

            // Desktop viewports
            case "desktop.small" -> new Dimension(1024, 768);  // Small laptop
            case "desktop.medium" -> new Dimension(1280, 800); // Medium laptop
            case "desktop.large" -> new Dimension(1920, 1080); // Large desktop

            // Default size
            default -> {
                log.warn("Unknown viewport: {}. Using desktop.medium as default.", viewport);
                yield new Dimension(1280, 800);
            }
        };
    }

//    todo add driver download using webdriver manager
}