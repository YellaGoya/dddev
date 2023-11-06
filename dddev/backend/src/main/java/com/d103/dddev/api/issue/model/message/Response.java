package com.d103.dddev.api.issue.model.message;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.beans.ExceptionListener;

public class Response {

    public static ResponseEntity success(Object response){
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public static ResponseEntity error(Error.Response response){
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
