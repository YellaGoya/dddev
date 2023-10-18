package com.d103.dddev.api.file.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.d103.dddev.api.file.repository.ProfileRepository;
import com.d103.dddev.api.file.repository.dto.ProfileDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

	@Value("${spring.servlet.multipart.location}")
	private String FILE_PATH;

	@Value("${file.folder.profile}")
	private String PROFILE_FOLDER;

	private final ProfileRepository profileRepository;

	@Override
	public ProfileDto saveDefaultProfileImg(MultipartFile defaultFile) throws Exception {
		if (defaultFile.isEmpty())
			return null;

		// 파일 정보
		String originalFilename = defaultFile.getOriginalFilename();
		String uuid = createRandomFileName();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String savedName = uuid + extension;
		String savedPath = FILE_PATH + PROFILE_FOLDER + savedName;

		// 파일 객체 생성
		File file = new File(savedPath);

		// 서버에 저장
		defaultFile.transferTo(file);

		// content-type
		String type = Files.probeContentType(file.toPath());

		// dto build
		ProfileDto profileDto = ProfileDto.builder()
			.id(1)
			.filePath(savedPath)
			.contentType(type)
			.build();

		// db에 저장
		return profileRepository.save(profileDto);
	}

	@Override
	public ProfileDto saveProfile(MultipartFile newFile) throws Exception {
		if (newFile.isEmpty())
			return null;

		// 파일 정보
		String originalFilename = newFile.getOriginalFilename();
		String uuid = createRandomFileName();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String savedName = uuid + extension;
		String savedPath = FILE_PATH + PROFILE_FOLDER + savedName;

		// 파일 객체 생성
		File file = new File(savedPath);

		// 서버에 저장
		newFile.transferTo(file.toPath());

		// content-type
		String type = Files.probeContentType(file.toPath());

		// dto build
		ProfileDto profileDto = ProfileDto.builder()
			.filePath(savedPath)
			.contentType(type)
			.build();

		// db에 저장
		return profileRepository.save(profileDto);
	}

	@Override
	public ProfileDto getProfileDto(Integer profileId) throws Exception {
		return profileRepository.findById(profileId).orElseThrow(() -> new Exception("존재하지 않는 프로필 사진입니다."));
	}

	@Override
	public byte[] getProfileById(Integer profileId) throws Exception {
		ProfileDto profileDto = profileRepository.findById(profileId)
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 프로필사진입니다."));
		return getProfileByPath(profileDto.getFilePath());
	}

	@Override
	public byte[] getProfileByPath(String filePath) throws Exception {
		InputStream inputStream = new FileInputStream(filePath);
		byte[] imageByteArray = IOUtils.toByteArray(inputStream);
		inputStream.close();
		return imageByteArray;
	}

	@Override
	public void deleteProfile(ProfileDto profileDto) throws Exception {
		// 서버에 저장된 이미지 파일 삭제
		String prevFilePath = profileDto.getFilePath();
		File prevFile = new File(prevFilePath);
		if (prevFile.delete()) {
			log.info("EC2 :: 프로필 사진 삭제 성공!");
		} else {
			log.error("EC2 :: 프로필 사진 삭제 실패..");
		}

		// db에 저장된 이미지 삭제
		profileRepository.deleteById(profileDto.getId());
	}

	@Override
	public String createRandomFileName() throws Exception {
		return UUID.randomUUID().toString();
	}
}
