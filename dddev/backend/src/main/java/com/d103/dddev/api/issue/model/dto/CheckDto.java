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
        @ApiModel(value="체크 포인트 생성", description = "체크 포인트 생성 dto")
        public static class Request{
            @ApiModelProperty(value="상위문서 ID")
            private String parentId;
        }

        @Data
        @Builder
        @ApiModel(value="체크 포인트 생성 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "체크 포인트 문서 생성 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }

    public static class List{
        @Data
        @Builder
        @ApiModel(value="채크 포인트 목록 조회 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "체크 포인트 문서 목록 조회 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private ArrayList<Issue> data;
        }
    }

    public static class Detail{
        @Data
        @Builder
        @ApiModel(value="체크 포인트 상세 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "체크 포인트 문서 상세 조회 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }

    public static class Delete{
        @Data
        @Builder
        @ApiModel(value="체크 포인트 삭제 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "체크 포인트 문서 삭제 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
        }
    }

    public static class Update{
        @Data
        @Builder
        @ApiModel(value="체크 문서 수정", description = "체크 문서의 제목 및 내용 수정")
        public static class Request{
            @ApiModelProperty(value="제목", example = "제목")
            private String title;
            @ApiModelProperty(value="내용", example = "내용")
            private String content;
        }

        @Data
        @Builder
        @ApiModel(value="체크 포인트 문서 수정 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "체크 포인트 문서 수정 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }

    public static class Connect{

        @Data
        @ApiModel(value="체크 포인트 문서 연결 변경", description = "체크 포인트 문서 연결 변경")
        public static class Request{
            @ApiModelProperty(value="목표 문서 ID", example = "부모 문서 ID")
            private String parentId;
        }

        @Data
        @Builder
        @ApiModel(value="체크 포인트 문서 연결 변경 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "체크 포인트 문서 연결 변경 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }

    public static class Title{
        @Data
        @ApiModel(value="체크 포인트 문서 제목 변경", description = "체크 포인트 문서 제목 변경 API")
        public static class Request{
            @ApiModelProperty(value="제목", example = "제목")
            private String title;
        }

        @Data
        @Builder
        @ApiModel(value="체크 포인트 문서 제목 변경 응답")
        public static class Response{
            @ApiModelProperty(value="message", example = "체크 포인트 문서 제목 수정 완료")
            private String message;
            @ApiModelProperty(value="code", example = "200")
            private Integer code;
            private Issue data;
        }
    }
}
