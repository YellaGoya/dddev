package com.d103.dddev.api.request.collection;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private String author;
    private String comment;
}
