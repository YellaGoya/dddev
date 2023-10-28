package com.d103.dddev.api.issue.model.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Builder
@Document(collection = "issues")
public class Issue {
    @Id
    private String id;

    @Field("groud_id")
    private String groundId;
    @Field("sprint_id")
    private String sprintId;
    @Field("parent_id")
    private String parentId;
    @Field("children_id")
    private ArrayList<String> childrenId;

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
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Field("work_time")
    private Integer workTime;
    @Field("study_time")
    private Integer studyTime;

    @CreatedBy
    private String author; // user name
    @LastModifiedBy
    private String modifier; // modifier name

    private Integer status;
    private String title;
    private String content;
}
