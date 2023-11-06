package com.d103.dddev.api.issue.model.dto;

import com.d103.dddev.api.issue.model.document.Issue;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

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
        public static class Request{
            private String title;
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
}
