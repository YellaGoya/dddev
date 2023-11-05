package com.d103.dddev.api.file.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.common.ResponseVO;
import com.d103.dddev.api.file.repository.dto.ProfileDto;
import com.d103.dddev.api.file.service.ProfileService;

import lombok.RequiredArgsConstructor;

// TODO : ADMIN 계정 생각해보기
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
	private final ProfileService profileService;

	@PostMapping("/default/user")
	ResponseEntity<ResponseVO<ProfileDto>> defaultUserProfile(@RequestHeader String Authorization, @RequestPart MultipartFile file) {
		try {
			ProfileDto profileDto = profileService.saveDefaultUserProfileImg(file);
			ResponseVO<ProfileDto> responseVO = ResponseVO.<ProfileDto>builder()
				.code(HttpStatus.OK.value())
				.message("디폴트 유저 프로필 이미지 저장 성공!")
				.data(profileDto)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			return null;
		}
	}

	@PostMapping("/default/ground")
	ResponseEntity<ResponseVO<ProfileDto>> defaultGroundProfile(@RequestHeader String Authorization, @RequestPart MultipartFile file) {
		try {
			ProfileDto profileDto = profileService.saveDefaultGroundProfileImg(file);
			ResponseVO<ProfileDto> responseVO = ResponseVO.<ProfileDto>builder()
				.code(HttpStatus.OK.value())
				.message("디폴트 그라운드 프로필 이미지 저장 성공!")
				.data(profileDto)
				.build();

			return new ResponseEntity<>(responseVO, HttpStatus.OK);
		} catch (Exception e) {
			return null;
		}
	}
}
