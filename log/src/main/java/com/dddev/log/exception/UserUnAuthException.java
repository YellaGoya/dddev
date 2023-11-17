package com.dddev.log.exception;

public class UserUnAuthException extends RuntimeException {
    public UserUnAuthException(String message) {
        super(message);
    }

    //로그가 너무 많이 요청 될 때
    public static class UnusualRequest extends UserUnAuthException{
        public UnusualRequest(String message) {
            super(message);
        }
    }
}
