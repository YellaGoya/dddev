package com.dddev.log.dto.req;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthReq {
    private String token;
}
