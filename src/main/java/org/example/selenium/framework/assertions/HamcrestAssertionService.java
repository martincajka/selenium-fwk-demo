package org.example.selenium.framework.assertions;

import org.hamcrest.Matcher;

/**
 * Service for making assertions using Hamcrest matchers and tracking results.
 * This interface defines the core functionality of the assertion service.
 */
public interface HamcrestAssertionService {
    /**
     * Assert that the actual value matches the given matcher.
     *
     * @param actual The actual value
     * @param matcher The matcher to apply
     * @param description Description of the assertion
     * @param <T> Type of the actual value
     * @throws AssertionError if the assertion fails
     */
    <T> void assertThat(T actual, Matcher<? super T> matcher, String description);
    
    /**
     * Assert that the actual value matches the given matcher.
     * Uses a default description based on the matcher.
     *
     * @param actual The actual value
     * @param matcher The matcher to apply
     * @param <T> Type of the actual value
     * @throws AssertionError if the assertion fails
     */
    <T> void assertThat(T actual, Matcher<? super T> matcher);
    
    /**
     * Add a listener for assertion events.
     *
     * @param listener The listener to add
     */
    void addListener(AssertionListener listener);
    
    /**
     * Remove a listener for assertion events.
     *
     * @param listener The listener to remove
     */
    void removeListener(AssertionListener listener);
}