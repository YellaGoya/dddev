package com.d103.dddev.api.general.repository.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralMoveDto {
    private String id;
    private String parentId;
}
