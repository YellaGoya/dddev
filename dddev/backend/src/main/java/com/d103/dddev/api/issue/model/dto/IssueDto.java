package com.d103.dddev.api.issue.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class IssueDto {

    @Data
    @Builder
    public static class Request{
        private String sprintId;
        private String parentId;
        private ArrayList<String> childrenId = new ArrayList<>();
        private Integer step;
        private String type;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private Integer workTime;
        private Integer studyTime;
        private String author;
        private String modifier;
        private Integer status;
        private String uri;
        private String title;
        private String content;
    }

    @Data
    @Builder
    public class Response {
        private String message;
        private Integer status;
        private boolean success;
    }
}
