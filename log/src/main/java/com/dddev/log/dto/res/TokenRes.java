package com.dddev.log.dto.res;

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
@ApiModel(value = "그라운드 토큰")
public class TokenRes {

    @ApiModelProperty(value = "유저 id", example = "3")
    private Integer userId;
    @ApiModelProperty(value = "그라운드 id", example = "1")
    private Integer groundId;
    @ApiModelProperty(value = "발급 받았던 토큰", example = "cQrEiyUaLK8DEeSeCzG9u91FLC8Id5QelvKQ7wQEXXa1heD4WnM6N+i3clZu+bSu")
    private String token;

}
