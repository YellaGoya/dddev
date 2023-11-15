package com.dddev.log.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "ground_id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ElasticSearchLog {

    @Id
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'")
    @ApiModelProperty(value="로그 저장 시간", example = "2023-11-14T23:45:26.458Z")
    private LocalDateTime localDateTime;

    @Field(type = FieldType.Text)
    @ApiModelProperty(value="로그", example = "2023-11-03 09:35:13.099 INFO 7960 --- [ main] com.dddev.log.LogApplication")
    private String log;
}
