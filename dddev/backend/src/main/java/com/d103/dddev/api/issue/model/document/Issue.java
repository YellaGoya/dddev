package com.d103.dddev.api.issue.model.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "issues")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Issue {
    @Id
    private String id;

    @Field("ground_id")
    private Integer groundId;
    @Field("sprint_id")
    private Integer sprintId;
    @Field("parent_id")
    private String parentId;
    @Field("children_id")
    private List<String> childrenId;

    @Field("step")
    private Integer step;
    private String type;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @Field("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // status changing time
    @Field("start_date")
    private LocalDateTime startDate;

    @Field("end_date")
    private LocalDateTime endDate;

    @Field("focus_time")
    private Integer focusTime;
    @Field("active_time")
    private Integer activeTime;

    @CreatedBy
    private String author; // user name
    @LastModifiedBy
    private String modifier; // modifier name

    private Integer status; // 0 : 백로그 , 1 : 진행 예정, 2 : 진행중, 3 : 완료
    private String title;
    private String content;
    private boolean unclassified;
}
