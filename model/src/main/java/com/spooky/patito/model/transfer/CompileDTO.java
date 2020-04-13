package com.spooky.patito.model.transfer;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CompileDTO {

    private String log;
    private boolean success;
}
