package com.d103.dddev.api.general.repository.dto.requestDto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GeneralUpdateDto {
    private String id;
    private String title;
    private String content;
}
