package com.d103.dddev.api.issue.model.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.persistence.PrePersist;

public class Error {

    @Data
    @Builder
    public static class Response{
        private String message;
        private Integer status;
    }

    public static Response error(String message, HttpStatus status){
        return Response.builder()
                .message(message)
                .status(status.value())
                .build();
    }

    public static String NoSuchElementException() {
        return "해당 값을 검색할 수 없습니다.";
    }

    public static String NoResultException() {
        return "데이터베이스에 값이 존재하지 않습니다.";
    }

    public static String InternalServerError() {
        return "알 수 없는 오류입니다.";
    }

    public static String EmptyResultDataAccessException(){
        return "데이터베이스에 값이 존재하지 않습니다.";
    }

    public static String WrongStep(){return "단계가 다른 문서입니다.";}

    public static String NoSubDocument(){return "존재하지 않는 하위 문서입니다.";}
    public static String NoParentDocument(){return "존재하지 않는 상위 문서입니다.";}
}