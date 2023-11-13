package com.d103.dddev.api.general.repository.dto.responseDto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
// step1만 보내주는 Dto
public class GeneralTitleResponseDto {
    String id;
    String title;
}
