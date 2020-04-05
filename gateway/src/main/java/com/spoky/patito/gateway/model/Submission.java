package com.spoky.patito.gateway.model;

public class Submission {
    private String code;
    private String standOutput;
    private String input;
    private String language;
    private String problemName;
    private int timeLimit;
    private  int memoryLimit;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStandOutput() {
        return standOutput;
    }

    public void setStandOutput(String standOutput) {
        this.standOutput = standOutput;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String inputl) {
        this.input = inputl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
    }
}
