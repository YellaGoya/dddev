package com.dddev.log.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@Builder
@RedisHash(value = "userGptAccess")
public class UserGptAccess {
    @Id
    private String groundId;
    private Integer count;

    @TimeToLive
    private Long expiration;

    public void increase(){
        this.count += 1;
    }
}
