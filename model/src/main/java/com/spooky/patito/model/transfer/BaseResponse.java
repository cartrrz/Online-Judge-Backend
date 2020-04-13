package com.spooky.patito.model.transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

    boolean success = true;

    String message;

}
