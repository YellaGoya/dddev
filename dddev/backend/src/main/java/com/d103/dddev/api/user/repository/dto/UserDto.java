package com.d103.dddev.api.user.repository.dto;

import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.d103.dddev.api.user.repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private ProfileDto profileDto;
    private Integer lastGroundId;
    private Integer githubId;
    private String nickname;
    private String email;
    private String statusMsg;
    private String personalAccessToken;

    public User convertToEntity() {
        return User.builder()
                .id(this.id)
                .profileDto(this.profileDto)
                .lastGroundId(this.lastGroundId)
                .githubId(this.githubId)
                .nickname(this.nickname)
                .email(this.email)
                .statusMsg(this.statusMsg)
                .build();
    }
}
