package com.d103.dddev.api.request.collection;

import com.d103.dddev.api.user.repository.dto.UserDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "comments")
public class Comment {
    @Id
    private String commentId;
    private UserDto author;
    private String comment;
    private String requestId;
    private LocalDateTime createdTime;
}
