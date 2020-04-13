package com.spooky.patito.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Submission {
    private String source;
    private String language;
    private Long problemId;
    private int timeLimit;
    private  int memoryLimit;

}
