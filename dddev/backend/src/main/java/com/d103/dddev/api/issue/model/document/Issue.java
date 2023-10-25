package com.d103.dddev.api.issue.model.document;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Builder
@Document(collection = "issues")
public class Issue {
    @Id
    private String id;
    private String sprintId;
    private String parentId;
    private ArrayList<String> childrenId = new ArrayList<>();
    private Integer step;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer workTime;
    private Integer studyTime;
    private String author;
    private String modifier;
    private Integer status;
    private String uri;
    private String title;
    private String content;
}
