package org.example.selenium.framework.timing;

import org.example.selenium.framework.listener.TestAction;

import java.util.List;

/**
 * Interface for a service that tracks timing information for operations.
 * This decouples timing tracking from specific implementations like WebdriverWrapper and PerformanceWebDriverListener.
 */
public interface TimingService {
    /**
     * Start timing an operation.
     *
     * @param operationType The type of operation being timed (e.g., "Wait for Element", "Find Element")
     * @param target The target of the operation (e.g., locator string)
     */
    void startTiming(String operationType, String target);
    
    /**
     * End timing an operation and record the result.
     *
     * @param operationType The type of operation that was timed
     * @param target The target of the operation
     * @param success Whether the operation was successful
     */
    void endTiming(String operationType, String target, boolean success, String detailMsg);

    void endTiming(String operationType, String target, boolean success);

    /**
     * Get all recorded timing information.
     *
     * @return List of ActionTiming records
     */
    List<TestAction> getTimings();
}