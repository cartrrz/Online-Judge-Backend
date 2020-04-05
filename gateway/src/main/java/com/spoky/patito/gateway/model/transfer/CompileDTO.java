package com.spoky.patito.gateway.model.transfer;

public class CompileDTO {
    private String log;
    private boolean success;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
