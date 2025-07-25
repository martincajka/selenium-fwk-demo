package org.example.selenium.framework.listener;

import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class LoggingWebDriverListner implements WebDriverListener {
    private static final Logger log = LoggerFactory.getLogger(LoggingWebDriverListner.class);

    @Override
    public void beforeAnyCall(Object target, Method method, Object[] args) {
        log.trace("Webdriver call: {}.{}({})",
                target.getClass().getSimpleName(),
                method.getName(),
                args);
    }
}
