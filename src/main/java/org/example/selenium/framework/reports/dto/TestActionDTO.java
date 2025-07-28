package org.example.selenium.framework.reports.dto;

public class TestActionDTO {
    private String action;
    private String target;
    private long startTimestamp;
    private long endTimestamp;
    private boolean success;
    private String detailMsg;

    public TestActionDTO(String action, String target, long startTimestamp, long endTimestamp, boolean success, String detailMsg) {
        this.action = action;
        this.target = target;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.success = success;
        this.detailMsg = detailMsg;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    @Override
    public String toString() {
        return "TestActionDTO{" +
                "action='" + action + '\'' +
                ", target='" + target + '\'' +
                ", startTimestamp=" + startTimestamp +
                ", endTimestamp=" + endTimestamp +
                ", success=" + success +
                ", detailMsg='" + detailMsg + '\'' +
                '}';
    }
}
