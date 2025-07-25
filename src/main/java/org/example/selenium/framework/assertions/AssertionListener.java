package org.example.selenium.framework.assertions;

import org.hamcrest.Matcher;

/**
 * Listener for assertion events.
 * This interface defines callbacks for assertion lifecycle events.
 */
public interface AssertionListener {
    /**
     * Called before an assertion is made.
     *
     * @param actual The actual value
     * @param matcher The matcher to apply
     * @param description Description of the assertion
     * @param <T> Type of the actual value
     */
    <T> void beforeAssertion(T actual, Matcher<? super T> matcher, String description);
    
    /**
     * Called after a successful assertion.
     *
     * @param actual The actual value
     * @param matcher The matcher that was applied
     * @param description Description of the assertion
     * @param <T> Type of the actual value
     */
    <T> void onAssertionSuccess(T actual, Matcher<? super T> matcher, String description);
    
    /**
     * Called after a failed assertion.
     *
     * @param actual The actual value
     * @param matcher The matcher that was applied
     * @param description Description of the assertion
     * @param error The assertion error
     * @param <T> Type of the actual value
     */
    <T> void onAssertionFailure(T actual, Matcher<? super T> matcher, String description, AssertionError error);
}