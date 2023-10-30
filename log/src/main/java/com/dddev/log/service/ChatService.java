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
            String responseMessage = chatgptService.sendMessage(question + "는 내가 궁금해 하는 로그야. 이 로그에 대해서" +
                    " 자세하게 그리고 어떻게 해결해야 하는지 에시로 알려줘.");
            return responseMessage;
        }catch (ChatGptException.GptExcetpion e){
            throw new ChatGptException.GptExcetpion("Gpt를 호출하는 중 오류가 발생했습니다.");
        }
    }
}
