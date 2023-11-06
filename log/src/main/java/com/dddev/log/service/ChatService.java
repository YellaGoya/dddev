package com.dddev.log.service;

import com.dddev.log.dto.req.ChatReq;
import com.dddev.log.dto.res.ChatRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    @Qualifier("openaiRestTemplate")
    private final RestTemplate restTemplate;
    private final Environment env;

    public String chatGptLog(String prompt){

        ChatReq request = new ChatReq(env.getProperty("openai.model"), prompt, "You're a log analysis expert. " +
                "I know all the logs of any program. " +
                "Kind and easy for developers trying to catch this error, let me know the solution. Tell me in Korean.");
        // call the API
        ChatRes response = restTemplate.postForObject(env.getProperty("openai.api.url"), request, ChatRes.class);
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        // return the first response
        return response.getChoices().get(0).getMessage().getContent();
    }

    public String chatGptExp(String prompt){
        // create a request
        ChatReq request = new ChatReq(env.getProperty("openai.model"), prompt,"You are a regular expression expert in elasticsearch. " +
                "Please change the regular expression to the elasticserach regular expression. " +
                "However, the answer does not include any other rhetoric, just tell me the answer.");
        // call the API
        ChatRes response = restTemplate.postForObject(env.getProperty("openai.api.url"), request, ChatRes.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return "No response";
        }
        // return the first response
        return response.getChoices().get(0).getMessage().getContent();
    }
}
