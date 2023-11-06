package com.d103.dddev.api.issue.model.dto;

import com.d103.dddev.api.issue.model.document.Issue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class TargetDto {
    public static class Create{

        @Data
        @Builder
        public static class Response{
            private String message;
            private Integer status;
            private Issue target;
        }
    }

    public static class List{
        @Data
        @Builder
        public static class Response{
            private String message;
            private Integer status;
            private ArrayList<Issue> targetList;
        }
    }

    public static class Detail{
        @Data
        @Builder
        public static class Response{
            private String message;
            private Integer status;
            private Issue target;
        }
    }

    public static class Delete{
        @Data
        @Builder
        public static class Response{
            private String message;
            private Integer status;
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
        public static class Response{
            private Integer status;
            private String message;
            private Issue target;
        }
    }

    public static class Tree{
        @Data
        @Builder
        public static class Docs{
            private String id;
            private String title;
            private Integer step;
            private java.util.List<Docs> children;
        }

        @Data
        @Builder
        public static class Response{
            private Integer status;
            private String message;
            private Object tree;
        }
    }
}
