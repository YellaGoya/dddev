package com.dddev.log.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroundAuth {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer userId;

    private Integer groundId;

    private String token;

    public GroundAuth(Integer userId, Integer groundId, String token) {
        this.userId = userId;
        this.groundId = groundId;
        this.token = token;
    }
}
