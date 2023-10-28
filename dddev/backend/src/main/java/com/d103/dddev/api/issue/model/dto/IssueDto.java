package com.d103.dddev.api.issue.model.dto;

import com.d103.dddev.api.issue.model.document.Issue;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class IssueDto {

    @Data
    @Builder
    public static class Request{
        private String sprintId;
        private String groundId;
        private String parentId;
        private Integer step;
    }

    @Data
    @Builder
    public static class Response {
        private String message;
        private Integer status;
        private Issue issue;
        private boolean success;
    }
}
