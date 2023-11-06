package com.d103.dddev.api.general.repository.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralInsertOneDto {
    private int step;
    private String title;
    private String parentId;
}
