package com.dddev.log.dto.req;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeReq {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}
