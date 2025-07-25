package org.example.selenium.framework.assertions;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * Static utility class for making assertions using Hamcrest matchers.
 * This class provides a convenient way to use the assertion service in tests.
 */
public class HamcrestAssertions {
    private static final ThreadLocal<HamcrestAssertionService> ASSERTION_SERVICE = new ThreadLocal<>();
    
    /**
     * Initialize the assertions with an assertion service.
     * This should be called at the beginning of each test.
     *
     * @param assertionService The assertion service to use
     */
    public static void init(HamcrestAssertionService assertionService) {
        ASSERTION_SERVICE.set(assertionService);
    }
    
    /**
     * Clean up the thread local resources.
     * This should be called at the end of each test.
     */
    public static void cleanup() {
        ASSERTION_SERVICE.remove();
    }
    
    /**
     * Assert that the actual value matches the given matcher.
     *
     * @param actual The actual value
     * @param matcher The matcher to apply
     * @param <T> Type of the actual value
     * @throws AssertionError if the assertion fails
     */
    public static <T> void assertThat(T actual, Matcher<? super T> matcher) {
        getAssertionService().assertThat(actual, matcher);
    }
    
    /**
     * Assert that the actual value matches the given matcher.
     *
     * @param actual The actual value
     * @param matcher The matcher to apply
     * @param description Description of the assertion
     * @param <T> Type of the actual value
     * @throws AssertionError if the assertion fails
     */
    public static <T> void assertThat(T actual, Matcher<? super T> matcher, String description) {
        getAssertionService().assertThat(actual, matcher, description);
    }
    
    /**
     * Assert that the actual value is equal to the expected value.
     *
     * @param actual The actual value
     * @param expected The expected value
     * @param <T> Type of the values
     * @throws AssertionError if the assertion fails
     */
    public static <T> void assertEqual(T actual, T expected) {
        assertThat(actual, Matchers.equalTo(expected), "Expected values to be equal");
    }
    
    /**
     * Assert that the actual value is equal to the expected value.
     *
     * @param actual The actual value
     * @param expected The expected value
     * @param description Description of the assertion
     * @param <T> Type of the values
     * @throws AssertionError if the assertion fails
     */
    public static <T> void assertEqual(T actual, T expected, String description) {
        assertThat(actual, Matchers.equalTo(expected), description);
    }
    
    /**
     * Assert that the condition is true.
     *
     * @param condition The condition to check
     * @throws AssertionError if the condition is false
     */
    public static void assertTrue(boolean condition) {
        assertThat(condition, Matchers.is(true), "Expected condition to be true");
    }
    
    /**
     * Assert that the condition is true.
     *
     * @param condition The condition to check
     * @param description Description of the assertion
     * @throws AssertionError if the condition is false
     */
    public static void assertTrue(boolean condition, String description) {
        assertThat(condition, Matchers.is(true), description);
    }
    
    /**
     * Assert that the condition is false.
     *
     * @param condition The condition to check
     * @throws AssertionError if the condition is true
     */
    public static void assertFalse(boolean condition) {
        assertThat(condition, Matchers.is(false), "Expected condition to be false");
    }
    
    /**
     * Assert that the condition is false.
     *
     * @param condition The condition to check
     * @param description Description of the assertion
     * @throws AssertionError if the condition is true
     */
    public static void assertFalse(boolean condition, String description) {
        assertThat(condition, Matchers.is(false), description);
    }
    
    /**
     * Assert that the object is null.
     *
     * @param object The object to check
     * @throws AssertionError if the object is not null
     */
    public static void assertNull(Object object) {
        assertThat(object, Matchers.nullValue(), "Expected object to be null");
    }
    
    /**
     * Assert that the object is null.
     *
     * @param object The object to check
     * @param description Description of the assertion
     * @throws AssertionError if the object is not null
     */
    public static void assertNull(Object object, String description) {
        assertThat(object, Matchers.nullValue(), description);
    }
    
    /**
     * Assert that the object is not null.
     *
     * @param object The object to check
     * @throws AssertionError if the object is null
     */
    public static void assertNotNull(Object object) {
        assertThat(object, Matchers.notNullValue(), "Expected object to be not null");
    }
    
    /**
     * Assert that the object is not null.
     *
     * @param object The object to check
     * @param description Description of the assertion
     * @throws AssertionError if the object is null
     */
    public static void assertNotNull(Object object, String description) {
        assertThat(object, Matchers.notNullValue(), description);
    }
    
    /**
     * Get the current assertion service.
     * This will throw an IllegalStateException if init() has not been called.
     *
     * @return The current assertion service
     * @throws IllegalStateException if init() has not been called
     */
    private static HamcrestAssertionService getAssertionService() {
        HamcrestAssertionService service = ASSERTION_SERVICE.get();
        if (service == null) {
            throw new IllegalStateException("HamcrestAssertions has not been initialized. Call init() first.");
        }
        return service;
    }
}