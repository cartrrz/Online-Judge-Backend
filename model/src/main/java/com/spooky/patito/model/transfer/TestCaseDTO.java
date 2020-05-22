package com.spooky.patito.model.transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestCaseDTO {

    private Long problemId;

    private String input;

    private String output;

    private String extId;
}
