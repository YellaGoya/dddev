package com.d103.dddev.api.issue.model.dto;

import com.d103.dddev.api.issue.model.document.Issue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

public class CheckDto {
    public static class Create{

        @Data
        @ApiModel(value="체크포인트 생성", description = "체크포인트 생성 dto")
        public static class Request{
            @ApiModelProperty(value="상위문서 ID")
            private String parentId;
        }

        @Data
        @Builder
        public static class Response{
            private String message;
            private Integer status;
            private Issue check;
        }
    }

    public static class List{
        @Data
        @Builder
        public static class Response{
            private String message;
            private Integer status;
            private ArrayList<Issue> checkList;
        }
    }

    public static class Detail{
        @Data
        @Builder
        public static class Response{
            private String message;
            private Integer status;
            private Issue check;
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
        public static class Request{
            private String title;
            private String content;
        }

        @Data
        @Builder
        public static class Response{
            private Integer status;
            private String message;
            private Issue check;
        }
    }

    public static class Connect{

        @Data
        public static class Request{
            private String checkId;
            private String targetId;
        }

        @Data
        @Builder
        public static class Response{
            private Integer status;
            private String message;
            private Issue check;
        }
    }
}
