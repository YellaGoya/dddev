package com.d103.dddev.api.general.collection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Document(collection = "general")
public class General {
    @Id
    private String id;
    @Field("parent_id")
    private int parentId;
    @Field("children_id")
    private int[] childrenId;
    @Field("ground_id")
    private int groundId;
    private int type;
    private int step;
    private String title;
    private String content;
    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Field("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
