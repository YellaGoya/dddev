package com.dddev.log.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "로그 dto", description = "로그")
public class LogReq {

    @ApiModelProperty(value = "로그", example = "2023-11-03 09:35:13.099  INFO 7960 --- [           main] com.dddev.log.LogApplication", required = true)
    private String log;

}
