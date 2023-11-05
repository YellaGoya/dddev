package com.d103.dddev.api.request.repository.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestMoveDto {
    private String id;
    private String parentId;
}
