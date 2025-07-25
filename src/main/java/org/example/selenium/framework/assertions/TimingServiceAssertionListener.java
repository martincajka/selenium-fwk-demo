package org.example.selenium.framework.assertions;

import org.example.selenium.framework.timing.TimingService;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

/**
 * Assertion listener that integrates with the TimingService.
 * This listener tracks assertion timing and results using the TimingService.
 */
public class TimingServiceAssertionListener implements AssertionListener {
    private final TimingService timingService;

    /**
     * Create a new TimingServiceAssertionListener.
     *
     * @param timingService The timing service to use for tracking assertions
     */
    public TimingServiceAssertionListener(TimingService timingService) {
        this.timingService = timingService;
    }

    @Override
    public <T> void beforeAssertion(T actual, Matcher<? super T> matcher, String description) {
        // Start timing the assertion
        timingService.startTiming("Assertion", description);
    }

    @Override
    public <T> void onAssertionSuccess(T actual, Matcher<? super T> matcher, String description) {
        // End timin
        StringDescription successDescription = new StringDescription();
        successDescription.appendText("Expected: ")
                .appendDescriptionOf(matcher);

        timingService.endTiming("Assertion", description, true, successDescription.toString());
    }

    @Override
    public <T> void onAssertionFailure(T actual, Matcher<? super T> matcher, String description, AssertionError error) {
        // Create a detailed error message
        StringDescription errorDescription = new StringDescription();
        errorDescription.appendText("Expected: ")
                .appendDescriptionOf(matcher)
                .appendText("\n");


        // If the error has a message, use it; otherwise, describe the mismatch
        if (error.getMessage() != null) {
            errorDescription.appendText(error.getMessage());
        } else {
            matcher.describeMismatch(actual, errorDescription);
        }

        // End timing with failure and the error message
        timingService.endTiming("Assertion", description, false, errorDescription.toString());
    }
}