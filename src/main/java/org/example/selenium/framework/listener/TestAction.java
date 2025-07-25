package org.example.selenium.framework.listener;

public record TestAction(
        String action,
        String target,
        long startTimestamp,
        long endTimestamp,
        boolean success,
        String detailMsg
) {
    public TestAction(String action, String target, long startTimestamp, long endTimestamp, boolean success) {
        this(action, target, startTimestamp, endTimestamp, success, null);
    }

    @Override
    public String toString() {
        String status = success ? "✅" : "❌";
        String timing = String.format("%dms", (endTimestamp - startTimestamp));
        String detailInfo = detailMsg != null ? " | " + detailMsg : "";

        return String.format("  %s %s: '%s' | %s%s", status, action, target, timing, detailInfo);
    }
}
