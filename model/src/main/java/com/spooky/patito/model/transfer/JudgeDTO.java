package com.spooky.patito.model.transfer;

public class JudgeDTO {
    private String runresult;
    private int usedTime;
    private  int usedMemory;

    public String getRunresult() {
        return runresult;
    }

    public void setRunresult(String runresult) {
        this.runresult = runresult;
    }

    public int getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(int usedTime) {
        this.usedTime = usedTime;
    }

    public int getUsedMemory() {
        return usedMemory;
    }

    public void setUsedMemory(int usedMemory) {
        this.usedMemory = usedMemory;
    }
}
