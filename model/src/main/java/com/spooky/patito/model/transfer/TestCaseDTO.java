package com.spooky.patito.model.transfer;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TestCaseDTO {

    private Long problemId;

    private String input;

    private String output;

    private Long extId;
}
