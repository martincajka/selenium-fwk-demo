package org.example.selenium.framework.assertions;

import org.example.selenium.framework.timing.TimingService;

/**
 * Factory for creating assertion services.
 * This class provides factory methods to create properly configured assertion services.
 */
public class AssertionFactory {
    /**
     * Create a HamcrestAssertionService that integrates with the TimingService.
     * The created service will track assertion timing and results using the TimingService.
     *
     * @param timingService The timing service to use for tracking assertions
     * @return A configured HamcrestAssertionService
     */
    public static HamcrestAssertionService createHamcrestAssertionService(TimingService timingService) {
        // Create the assertion service
        DefaultHamcrestAssertionService service = new DefaultHamcrestAssertionService();
        
        // Add a listener that integrates with the timing service
        service.addListener(new TimingServiceAssertionListener(timingService));
        
        return service;
    }
    
    /**
     * Initialize the HamcrestAssertions utility class with a TimingService.
     * This is a convenience method that creates an assertion service and initializes the utility class.
     *
     * @param timingService The timing service to use for tracking assertions
     */
    public static void initHamcrestAssertions(TimingService timingService) {
        HamcrestAssertionService service = createHamcrestAssertionService(timingService);
        HamcrestAssertions.init(service);
    }
    
    /**
     * Clean up the HamcrestAssertions utility class.
     * This is a convenience method that cleans up the utility class.
     */
    public static void cleanupHamcrestAssertions() {
        HamcrestAssertions.cleanup();
    }
}