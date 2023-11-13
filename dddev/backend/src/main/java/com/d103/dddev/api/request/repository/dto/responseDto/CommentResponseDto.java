package com.d103.dddev.api.request.repository.dto.responseDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {
    private String id;
    private String author;
    private String fileName;
    private String comment;
    private LocalDateTime createdTime;
}
