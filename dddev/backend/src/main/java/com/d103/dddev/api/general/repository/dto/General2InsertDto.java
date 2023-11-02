package com.d103.dddev.api.general.repository.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class General2InsertDto {
    @ApiModelProperty(example = "공지사항")
    private String title;
    private String parentId;
}
