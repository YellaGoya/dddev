package com.dddev.log.dto.req;


import com.dddev.log.dto.Message;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ChatReq {

    private String model;
    private List<Message> messages;
    private int max_tokens;
    private double temperature;

    public ChatReq(String model, String prompt, String role) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
        this.messages.add(new Message("system", role));
        this.temperature = 1.0;
        this.max_tokens = 4000;
    }
}
