package com.d103.dddev.api.request.collection;

import com.d103.dddev.api.user.repository.entity.User;
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
@Document(collection = "request")
public class Request {
    @Id
    private String id;
    @Field("ground_id")
    private int groundId;
    @Field("parent_id")
    private String parentId;
    private List<Request> children;
    private String title;
    private String content;
    private int step;
    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Field("updated_at")
    @CreatedDate
    private LocalDateTime updatedAt;
    // 요청을 보내는 사람
    private User sendUser;
    // 요청을 받는 사람
    private User receiveUser;
    // 작성한 사람
    private String author;
    // 마지막으로 수정한 사람
    private String modifier;
    // status 0이면 요청 보내기전, 1이면 요청 보낸후 수정 불가
    private int status;
    private List<Comment> comments;
    private boolean unclassified;
}
