package com.d103.dddev.api.sprint.repository.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintUpdateDto {
    private String name;
    private String goal;
}
