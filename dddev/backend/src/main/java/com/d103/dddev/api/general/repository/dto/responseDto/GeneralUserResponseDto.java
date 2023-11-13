package com.d103.dddev.api.general.repository.dto.responseDto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralUserResponseDto {
    private Integer id;
    private Integer githubId;
    private String nickname;
    private String email;
    private String statusMsg;
    private String fileName;
}
