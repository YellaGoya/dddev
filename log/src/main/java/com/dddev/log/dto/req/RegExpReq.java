package com.dddev.log.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "로그 dto", description = "로그")public class RegExpReq {

    private String regExp;
}
