package org.example.selenium.framework.config;


import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public enum FrameworkConfig {
    INSTANCE;

    private final Map<String, String> configuration;

    // Default configuration values
    private static final String DEFAULT_BROWSER = "chrome";
    private static final String DEFAULT_BROWSER_VERSION = "latest";
    private static final String DEFAULT_HEADLESS = "false";
    private static final String DEFAULT_PARALLEL = "true";
    private static final String DEFAULT_THREAD_COUNT = "4";
    private static final String DEFAULT_LOG_LEVEL = "INFO";
    private static final String DEFAULT_VIEWPORT = "desktop.medium";
    //    todo: ADD remaining later

    private static final String PROPERTIES_FILE = "/framework.properties";
    private static final String ENV_PREFIX = "FWK_";

    FrameworkConfig() {
        Map<String, String> mergedProperties = new HashMap<>();
        loadDefaultConfig(mergedProperties);
        loadFileConfig(mergedProperties);
        loadEnvConfig(mergedProperties);

        this.configuration = Collections.unmodifiableMap(mergedProperties);

    }

    private void loadDefaultConfig(Map<String, String> props) {
        props.put("browser", DEFAULT_BROWSER);
        props.put("browser.version", DEFAULT_BROWSER_VERSION);
        props.put("browser.headless", DEFAULT_HEADLESS);
        props.put("viewport", DEFAULT_VIEWPORT);
        props.put("execution.parallel", DEFAULT_PARALLEL);
        props.put("execution.threadCount", DEFAULT_THREAD_COUNT);
        props.put("logging.level", DEFAULT_LOG_LEVEL);
        //    todo: ADD remaining later
    }

    private void loadFileConfig(Map<String, String> props) {
        try (InputStream input = FrameworkConfig.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
//                log.warn("No properties file found: " + PROPERTIES_FILE);
                return;
            }
            Properties fileProps = new Properties();
            fileProps.load(input);
            fileProps.forEach((key, value) -> props.put(String.valueOf(key), String.valueOf(value)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties file from " + PROPERTIES_FILE, e);
        }
    }

    private void loadEnvConfig(Map<String, String> props) {
        System.getenv().forEach((key, value) -> {
            if (key.startsWith(ENV_PREFIX)) {
                String formatedKey = key.substring(ENV_PREFIX.length()).toLowerCase().replace("_", ".");
                props.put(formatedKey, value);
            }
        });
    }

    public String getConfig(String key, String defaultValue) {
        return configuration.getOrDefault(key, defaultValue);
    }

    public String getConfig(String key) {
        return getConfig(key, null);
    }

    public boolean getConfigAsBoolean(String key) {
        return Boolean.parseBoolean(getConfig(key, "false"));
    }

    public int getConfigAsInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(getConfig(key));
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

}
