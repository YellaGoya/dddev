package com.d103.dddev.api.general.repository.dto.requestDto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralInsertOneDto {
    private String title;
    private String parentId;
}
