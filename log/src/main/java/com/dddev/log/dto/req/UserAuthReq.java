package com.dddev.log.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthReq {
    @ApiModelProperty(value = "토큰", example = "cQrEiyUaLK8DEeSeCzG9u91FLC8Id5QelvKQ7wQEXXa1heD4WnM6N+i3clZu+bSu")
    private String token;
}
