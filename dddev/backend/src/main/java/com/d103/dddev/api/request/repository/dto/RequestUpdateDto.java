package com.d103.dddev.api.request.repository.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUpdateDto {
    private String id;
    private String title;
    private String content;
    private int sendUserId;
    private int receiveUserId;
}
