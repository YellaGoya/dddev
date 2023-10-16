package com.d103.dddev.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.user.repository.dto.ProfileDto;

public interface ProfileRepository extends JpaRepository<ProfileDto, Integer> {

}
