package com.dddev.log.exception;

public class ChatGptException extends RuntimeException {
    public ChatGptException(String message) {
        super(message);
    }

    //질문이 없을 때
    public static class IncorrectQuestion extends ChatGptException{
        public IncorrectQuestion(String message) {
            super(message);
        }
    }

    //Gpt에서 오류가 발생할 때
    public static class GptExcetpion extends ChatGptException{
        public GptExcetpion(String message) {
            super(message);
        }
    }
}
