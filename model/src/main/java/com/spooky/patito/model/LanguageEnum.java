package com.spooky.patito.model;

public enum LanguageEnum {

    CPP("cpp","c_plus_plus","cpp","g++ -O2 -s -Wall -std=c++11 -o {filename}.exe {filename}.cpp -lm","{filename}.exe");

    private String key;
    private String name;
    private String extension;
    private String compilePattern;
    private String runPattern;

    LanguageEnum(String key, String name, String extension, String compilePattern, String runPattern) {
        this.key = key;
        this.name = name;
        this.extension = extension;
        this.compilePattern = compilePattern;
        this.runPattern = runPattern;
    }

    public String getKey() {
        return key;
    }

    public String getNameValue() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public String getCompilePatternValue() {
        return compilePattern;
    }

    public String getRunPatternValue() {
        return runPattern;
    }
}
