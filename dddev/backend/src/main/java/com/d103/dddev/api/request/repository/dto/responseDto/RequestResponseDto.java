package com.d103.dddev.api.request.repository.dto.responseDto;

import com.d103.dddev.api.request.collection.Request;
import com.d103.dddev.api.user.repository.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestResponseDto {
    private String id;
    private int groundId;
    private String parentId;
    private List<Request> children;
    private String title;
    private String content;
    private int step;
    private LocalDateTime createdAt;
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
    private int status; // 0: 해야 할 일, 1: 진행 중, 2 완료
    private List<CommentResponseDto> comments;
    private boolean unclassified;
    private boolean isTemplate;
}
