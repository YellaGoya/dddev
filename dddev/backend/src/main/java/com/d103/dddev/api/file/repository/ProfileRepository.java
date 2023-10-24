package com.d103.dddev.api.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d103.dddev.api.file.repository.dto.ProfileDto;

public interface ProfileRepository extends JpaRepository<ProfileDto, Integer> {

}
