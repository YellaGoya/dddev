package com.d103.dddev.api.request.repository.dto.responseDto;

import com.d103.dddev.api.file.repository.dto.ProfileDto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestUserResponseDto {
    private Integer id;
    private Integer githubId;
    private String nickname;
    private String email;
    private String statusMsg;
    private String fileName;
}

