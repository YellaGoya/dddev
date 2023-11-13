package com.dddev.log.dto.req;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAuthReq {
    private String token;
}
