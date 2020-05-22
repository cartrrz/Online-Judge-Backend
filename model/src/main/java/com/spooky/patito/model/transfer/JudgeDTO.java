package com.spooky.patito.model.transfer;


import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JudgeDTO {
    private String runresult;
    private int usedTime;
    private  int usedMemory;
    private String extId;

}
