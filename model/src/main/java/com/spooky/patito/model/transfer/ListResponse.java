package com.spooky.patito.model.transfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListResponse<T> extends BaseResponse{
    List<T> response;

    int total;
}
