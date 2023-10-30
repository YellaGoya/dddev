package com.dddev.log.exception;

public class ElasticSearchException extends RuntimeException {
    public ElasticSearchException(String message) {
        super(message);
    }

    //질문이 없을 때
    public static class NoContentException extends ElasticSearchException{
        public NoContentException(String message) {
            super(message);
        }
    }

}
