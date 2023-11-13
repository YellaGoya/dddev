package com.dddev.log.exception;

public class UserUnAuthGptException extends RuntimeException {
    public UserUnAuthGptException(String message) {
        super(message);
    }

    //로그가 너무 많이 요청 될 때
    public static class UnusualRequest extends UserUnAuthGptException {
        public UnusualRequest(String message) {
            super(message);
        }
    }
}
