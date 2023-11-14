package com.d103.dddev.api.general.repository.dto.responseDto;

import com.d103.dddev.api.general.collection.General;
import com.d103.dddev.api.user.repository.dto.UserDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralResponseDto {
    private String id;
    private int groundId;
    private String parentId;
    private List<General> children;
    private String title;
    private String content;
    private int step;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private GeneralUserResponseDto author;
    private GeneralUserResponseDto modifier;
    private boolean unclassified;
    private boolean isTemplate;
}
