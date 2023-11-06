package com.d103.dddev.api.alert.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.d103.dddev.api.alert.dto.AlertUserDto;
import com.d103.dddev.api.alert.entity.AlertEntity;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, Integer> {

	// 같은 레포와 연결된 같은 타입의 알림이(웹훅이) 이미 존재하는지 확인하기 위해 조회
	List<AlertEntity> findAllByRepositoryIdAndType(Integer repositoryId, String type);
	Optional<AlertEntity> findByUserDto_IdAndRepositoryIdAndType(Integer userId, Integer repositoryId, String type);


	// 알림 구독 중인 사용자, 키워드 조회
	List<AlertUserDto> findByRepositoryIdAndType(Integer repositoryId, String type);

	List<AlertEntity> findByUserDto_Id(Integer id);

	List<AlertEntity> findByWebhookId(Integer webhookId);

	Optional<AlertEntity> findByIdAndUserDto_Id(Integer alertId, Integer userId);
}
