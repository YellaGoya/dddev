package com.d103.dddev.api.general.repository.dto.responseDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralResponseDto {
    private String id;
    private int step;
    private String title;
    private List<GeneralResponseDto> children;
}
