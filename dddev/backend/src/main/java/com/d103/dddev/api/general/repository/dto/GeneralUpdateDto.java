package com.d103.dddev.api.general.repository.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
// General1, General2 공통 업데이트 Dto
public class GeneralUpdateDto {
    private String id;
    private String title;
    private String content;
}
