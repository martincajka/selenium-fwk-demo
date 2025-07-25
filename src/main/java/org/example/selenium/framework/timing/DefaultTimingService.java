package org.example.selenium.framework.timing;

import org.example.selenium.framework.listener.TestAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Default implementation of the TimingService interface.
 * Tracks timing information for operations in a thread-safe manner.
 */
public class DefaultTimingService implements TimingService {
    private final List<TestAction> timings = Collections.synchronizedList(new ArrayList<>());
    private final ConcurrentMap<String, Long> startTimes = new ConcurrentHashMap<>();

    @Override
    public void startTiming(String operationType, String target) {
        String key = generateKey(operationType, target);
        startTimes.put(key, System.currentTimeMillis());
    }

    @Override
    public void endTiming(String operationType, String target, boolean success, String detailMsg) {
        String key = generateKey(operationType, target);
        Long startTime = startTimes.remove(key);
        
        if (startTime != null) {
            String action = success ? operationType : operationType + " (Failed)";
            timings.add(new TestAction(action, target, startTime, System.currentTimeMillis(), success, detailMsg));
        }
    }

    @Override
    public void endTiming(String operationType, String target, boolean success) {
        endTiming(operationType, target, success, null);
    }

    @Override
    public List<TestAction> getTimings() {
        return new ArrayList<>(timings);
    }
    
    /**
     * Generates a unique key for the timing operation based on thread ID, operation type, and target.
     * This ensures that concurrent operations in different threads don't interfere with each other.
     */
    private String generateKey(String operationType, String target) {
        return Thread.currentThread().getId() + ":" + operationType + ":" + target;
    }
}