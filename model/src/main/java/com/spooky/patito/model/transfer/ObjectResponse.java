package com.spooky.patito.model.transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ObjectResponse<T> extends BaseResponse{
    T response;
}
