package com.dddev.log.service;

import com.dddev.log.exception.ChatGptException;
import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatgptService chatgptService;

    public String getChatResponse(String question) {
        try{
            String responseMessage = chatgptService.sendMessage(question + "This is the log I'm curious about. Tell me about this log in detail and how to solve it as an example. " +
                    "Answer me in Korean.");
            return responseMessage;
        }catch (ChatGptException.GptExcetpion e){
            throw new ChatGptException.GptExcetpion("Gpt를 호출하는 중 오류가 발생했습니다.");
        }
    }

    public String getreqexpResponse(String question) {
        try{
            String responseMessage = chatgptService.sendMessage(" \"" + question + "\" " + "This is a regular expression. Turn this into a regular " +
                    "expression for ElasticSearch. However, don't use any other rhetoric when answering, just tell me the correct answer. ");
            return responseMessage;
        }catch (ChatGptException.GptExcetpion e){
            throw new ChatGptException.GptExcetpion("Gpt를 호출하는 중 오류가 발생했습니다.");
        }
    }
}
