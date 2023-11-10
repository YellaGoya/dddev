package com.d103.dddev.api.request.repository.dto.requestDto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUpdateDto {
    private String title;
    private String content;
    private int sendUserId;
    private int receiveUserId;
}
