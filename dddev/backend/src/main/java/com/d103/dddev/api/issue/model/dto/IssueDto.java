package com.d103.dddev.api.issue.model.dto;

import com.d103.dddev.api.issue.model.document.Issue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class IssueDto {

    public static class Create{

        @Data
        @ApiModel(value="이슈 생성 요청", description = "모든 값이 들어가지 않아도 생성 가능(부모문서 => 미분류, 스프린트 ID => 0)")
        public static class Request{
            @ApiModelProperty(value="상위문서 ID")
            private String parentId;
            @ApiModelProperty(value="스프린트 ID")
            private Integer sprintId;
        }

        @Data
        @Builder
        @ApiModel(value="이슈 생성 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "이슈 문서 생성 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }

    public static class List{
        @Data
        @Builder
        @ApiModel(value="이슈 목록 조회 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "이슈 문서 목록 조회 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private ArrayList<Issue> data;
        }
    }

    public static class Detail{
        @Data
        @Builder
        @ApiModel(value="이슈 상세 조회 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "이슈 문서 상세 조회 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }

    public static class Delete{
        @Data
        @Builder
        @ApiModel(value="이슈 삭제 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "이슈 문서 삭제 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
        }
    }

    public static class Content{
        @Data
        @Builder
        @ApiModel(value="이슈 문서 수정", description = "이슈 문서 수정 => 들어오는 값 그대로 저장")
        public static class Request{
            @ApiModelProperty(value="title", example = "제목")
            private String title;
            @ApiModelProperty(value="content", example = "내용")
            private String content;
        }

        @Data
        @Builder
        @ApiModel(value="이슈 문서 수정 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "이슈 문서 수정 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }

    public static class Status{

        @Data
        @ApiModel(value="이슈 상태 변경", description = "이슈 진행 상태 변경 API")
        public static class Request{
            @ApiModelProperty(value="상태", example = "1")
            private Integer status;
        }

        @Data
        @Builder
        @ApiModel(value="이슈 상태 변경")
        public static class Response{
            @ApiModelProperty(value="message", example = "이슈 문서 상태 수정 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }

    public static class Connect{
        @Data
        @ApiModel(value ="상위 문서 연결 변경", description = "이슈 문서 연결 변경 API")
        public static class Request{
            @ApiModelProperty(value = "부모 문서 ID")
            private String parentId;
        }

        @Data
        @Builder
        @ApiModel(value="상위 문서 연결 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "연결 변경이 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }

    public static class Time{
        @Data
        @ApiModel(value="이슈 문서 시간 변경", description = "이슈 문서 시간 변경 API")
        public static class Request{
            @ApiModelProperty(value="집중 시간", example = "1")
            private Integer focusTime;
            @ApiModelProperty(value="연구 시간", example = "2")
            private Integer activeTime;
        }

        @Data
        @Builder
        @ApiModel(value="이슈 문서 시간 변경 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "이슈 문서 시간 수정 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }

    public static class Sprint{
        @Data
        @ApiModel(value="이슈 문서 스프린트 연결", description = "이슈 문서 스프린트 연결 API")
        public static class Request{
            @ApiModelProperty(value = "스프린트 ID" , example = "1")
            private Integer sprintId;
        }

        @Data
        @Builder
        @ApiModel(value="이슈 문서 스프린트 연결 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "이슈 문서 스프린트 연결 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }
}
