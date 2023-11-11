package com.d103.dddev.api.general.repository.dto.responseDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
// tree 문서 구조로 보낼 때 사용하는 Dto
public class GeneralTreeResponseDto {
    private String id;
    private int step;
    private String title;
    private List<GeneralTreeResponseDto> children;
}
