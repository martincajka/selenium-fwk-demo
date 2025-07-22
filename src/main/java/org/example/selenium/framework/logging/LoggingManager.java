package org.example.selenium.framework.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.example.selenium.framework.config.FrameworkConfig;
import org.slf4j.LoggerFactory;

public class LoggingManager {
    /**
     * Initializes the logging system by setting the root logger's level
     * based on the value in FrameworkConfig.
     * <p>
     * This method requires the Logback Classic library to be on the classpath.
     * </p>
     */
    public static void initialize() {
        try {
            Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
            String logLevel = FrameworkConfig.INSTANCE.getConfig("logging.level", "INFO");
            root.setLevel(Level.toLevel(logLevel, Level.INFO));
            root.info("Logging level set to: {}", root.getLevel());
        } catch (ClassCastException e) {
            System.err.println("Failed to cast to Logback logger. Make sure logback-classic is on the classpath.");
        }
    }
}
