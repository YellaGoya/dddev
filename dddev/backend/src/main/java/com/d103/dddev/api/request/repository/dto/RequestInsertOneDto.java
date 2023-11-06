package com.d103.dddev.api.request.repository.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestInsertOneDto {
    private int step;
    private String title;
    private String parentId;
}
