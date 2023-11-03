package com.d103.dddev.api.general.repository.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class General1InsertDto {
    @ApiModelProperty(example = "공지사항")
    @NotNull
    private String title;
}
