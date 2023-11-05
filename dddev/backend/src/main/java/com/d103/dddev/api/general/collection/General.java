package com.d103.dddev.api.general.collection;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "general")
public class General {
    @Id
    private String id;
    @Field("ground_id")
    private int groundId;
    @Field("parent_id")
    private String parentId;
    private List<General> children;
    private String title;
    private String content;
    private int step;
    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Field("updated_at")
    @CreatedDate
    private LocalDateTime updatedAt;
}
