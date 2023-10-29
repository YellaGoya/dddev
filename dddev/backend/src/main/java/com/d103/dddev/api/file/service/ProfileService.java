package com.d103.dddev.api.file.service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.file.repository.dto.ProfileDto;

public interface ProfileService {
	ProfileDto saveDefaultProfileImg(MultipartFile defaultFile) throws Exception;

	ProfileDto saveProfile(MultipartFile newFile) throws Exception;

	ProfileDto getProfileDto(Integer profileId) throws Exception;

	byte[] getProfileById(Integer profileId) throws Exception;

	byte[] getProfileByPath(String filePath) throws Exception;

	void deleteProfile(ProfileDto profileDto) throws Exception;

	String createRandomFileName() throws Exception;
}
