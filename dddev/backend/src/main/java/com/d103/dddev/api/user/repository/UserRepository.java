package com.d103.dddev.api.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.user.repository.dto.UserDto;

public interface UserRepository extends JpaRepository<UserDto, Integer> {
	Optional<UserDto> findByGithubId(Integer githubId);
	Optional<UserDto> findByIdNotAndNickname(Integer id, String nickname);	// id != not and nickname = nickname

}
