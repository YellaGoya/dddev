package com.d103.dddev.api.sprint.repository.dto.responseDto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintResponseDto {
    private int id;
    private String name;
    private String goal;
    private Integer status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalFocusTime;
}
