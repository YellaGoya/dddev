package com.d103.dddev.api.request.collection;

import com.d103.dddev.api.general.collection.General2;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
@Document(collection = "request1")
public class Request1 {
    @Id
    private String id;
    private List<Request2> children;
    @Field("ground_id")
    private int groundId;
    private String title;
    private String content;
    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Field("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
