package com.d103.dddev.api.request.repository.dto.responseDto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestResponseDto {
    private String id;
    private int step;
    private String title;
    private List<RequestResponseDto> children;
}
