package com.d103.dddev.api.sprint.controller.error;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    @ApiModelProperty(example = "200")
    private String status;
    @ApiModelProperty("편지 생성 성공")
    private String message;
}
