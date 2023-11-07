package com.d103.dddev.api.general.repository.dto.requestDto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
// step1 문서의 다중 생성을 지원하는 Dto
public class GeneralInsertManyDto {
    private String[] titles;
}
