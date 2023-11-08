package com.d103.dddev.api.issue.model.dto;

import com.d103.dddev.api.issue.model.document.Issue;
import com.d103.dddev.api.issue.model.message.TargetMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;


public class TargetDto {
    public static class Create{
        @Data
        @Builder
        @ApiModel(value="Create", description = "Create Success")
        public static class Response{
            @ApiModelProperty(value="message", example = "목표 문서 생성 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            @ApiModelProperty(value="data")
            private Issue data;
        }

    }

    public static class List{
        @Data
        @Builder
        @ApiModel(value="List", description = "List Success")
        public static class Response{
            @ApiModelProperty(value="message", example = "목표 문서 목록 조회 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            @ApiModelProperty(value="data")
            private ArrayList<Issue> data;
        }
    }

    public static class Detail{
        @Data
        @Builder
        @ApiModel(value="Detail", description = "Detail Success")
        public static class Response{
            @ApiModelProperty(value="message", example = "목표 문서 조회 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            @ApiModelProperty(value="data")
            private Issue data;
        }
    }

    public static class Delete{
        @Data
        @Builder
        @ApiModel(value="Delete", description = "Delete Success")
        public static class Response{
            @ApiModelProperty(value="message", example = "목표 문서 삭제 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
        }
    }

    public static class Update{
        @Data
        @Builder
        @ApiModel(value="목표 수정", description = "목표 문서의 제목 및 내용 수정")
        public static class Request{
            @ApiModelProperty(value="제목", example = "제목")
            private String title;
            @ApiModelProperty(value="내용", example = "내용")
            private String content;
        }

        @Data
        @Builder
        @ApiModel(value="Update", description = "Update Success")
        public static class Response{
            @ApiModelProperty(value="message", example = "목표 문서 수정 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            @ApiModelProperty(value="data")
            private Issue data;
        }
    }

    public static class Tree{
        @Data
        @Builder
        public static class Docs{
            private String id;
            private String title;
            private Integer step;
            private Integer sprintId;
            private java.util.List<Docs> children;
        }

        @Data
        @Builder
        @ApiModel(value="Tree", description = "Tree Success")
        public static class Response{
            @ApiModelProperty(value="message", example = "그라운드 전체 문서 트리")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            @ApiModelProperty(value="data")
            private Object data;
        }
    }
}
