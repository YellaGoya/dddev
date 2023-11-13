package com.d103.dddev.api.request.collection;

import com.d103.dddev.api.user.repository.dto.UserDto;
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
    private Integer step;
    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Field("updated_at")
    @CreatedDate
    private LocalDateTime updatedAt;
    // 요청을 보내는 사람
    private UserDto sendUser;
    // 요청을 받는 사람
    private UserDto receiveUser;
    // 작성한 사람
    private UserDto author;
    // 마지막으로 수정한 사람
    private UserDto modifier;
    // status 0이면 요청 보내기전, 1이면 요청 보낸후 수정 불가
    private int status; // 0: 해야 할 일, 1: 진행 중, 2 완료
    private List<String> commentIdList;
    private boolean unclassified;
    private boolean isTemplate;
}
