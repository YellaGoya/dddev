package com.dddev.log.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Document(indexName = "group_id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ElasticSearchLog {

    @Id
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime localDateTime;

    @Field(type = FieldType.Text)
    private String log;
}
