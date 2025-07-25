package org.example.selenium.framework.assertions;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.StringDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Default implementation of HamcrestAssertionService.
 * This class uses Hamcrest matchers to perform assertions and notifies listeners about assertion events.
 */
public class DefaultHamcrestAssertionService implements HamcrestAssertionService {
    private final List<AssertionListener> listeners = new CopyOnWriteArrayList<>();
    
    @Override
    public <T> void assertThat(T actual, Matcher<? super T> matcher, String description) {
        // Notify listeners before the assertion
        notifyBeforeAssertion(actual, matcher, description);
        
        try {
            // Perform the assertion
            MatcherAssert.assertThat(description, actual, matcher);
            
            // Notify listeners about the successful assertion
            notifyAssertionSuccess(actual, matcher, description);
        } catch (AssertionError error) {
            // Notify listeners about the failed assertion
            notifyAssertionFailure(actual, matcher, description, error);
            
            // Re-throw the error
            throw error;
        }
    }
    
    @Override
    public <T> void assertThat(T actual, Matcher<? super T> matcher) {
        // Create a default description based on the matcher
        StringDescription description = new StringDescription();
        description.appendText("Expected: ")
                  .appendDescriptionOf(matcher);
        
        // Delegate to the other method
        assertThat(actual, matcher, description.toString());
    }
    
    @Override
    public void addListener(AssertionListener listener) {
        listeners.add(listener);
    }
    
    @Override
    public void removeListener(AssertionListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Notify all listeners before an assertion is made.
     */
    private <T> void notifyBeforeAssertion(T actual, Matcher<? super T> matcher, String description) {
        for (AssertionListener listener : listeners) {
            listener.beforeAssertion(actual, matcher, description);
        }
    }
    
    /**
     * Notify all listeners after a successful assertion.
     */
    private <T> void notifyAssertionSuccess(T actual, Matcher<? super T> matcher, String description) {
        for (AssertionListener listener : listeners) {
            listener.onAssertionSuccess(actual, matcher, description);
        }
    }
    
    /**
     * Notify all listeners after a failed assertion.
     */
    private <T> void notifyAssertionFailure(T actual, Matcher<? super T> matcher, String description, AssertionError error) {
        for (AssertionListener listener : listeners) {
            listener.onAssertionFailure(actual, matcher, description, error);
        }
    }
}