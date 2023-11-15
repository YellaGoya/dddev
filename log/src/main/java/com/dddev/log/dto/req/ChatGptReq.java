package com.dddev.log.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptReq {
    @ApiModelProperty(value = "질문", example = "~~~가 뭐야?")
    private String question;
}
