package com.d103.dddev.api.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.user.repository.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByGithubId(Integer githubId);
	Optional<User> findByIdNotAndNickname(Integer id, String nickname);	// id != not and nickname = nickname

	Optional<User> findByGithubName(String userName);
}
