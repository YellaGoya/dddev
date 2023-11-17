package com.dddev.log.dto.res;

import com.dddev.log.dto.Message;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ChatRes {

    private List<Choice> choices;
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Choice {

        private int index;
        private Message message;

    }
}
