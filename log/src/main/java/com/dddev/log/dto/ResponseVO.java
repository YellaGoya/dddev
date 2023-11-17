package com.dddev.log.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "응답")
public class ResponseVO<T> {

    @ApiModelProperty(value="code", example = "200")
    private Integer code;
    @ApiModelProperty(value="message", example = "메시지")
    private String message;
    @ApiModelProperty(value="data", example = "API 요청 완료")
    private T data;
}

