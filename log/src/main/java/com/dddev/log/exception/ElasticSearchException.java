package com.dddev.log.exception;

public class ElasticSearchException extends RuntimeException {
    public ElasticSearchException(String message) {
        super(message);
    }

    //해당 하는 로그가 없을 때
    public static class NoContentException extends ElasticSearchException{
        public NoContentException(String message) {
            super(message);
        }
    }

    //해당 하는 인덱스가 없을 때
    public static class NoIndexException extends ElasticSearchException{
        public NoIndexException(String message) {
            super(message);
        }
    }

}
