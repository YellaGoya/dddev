package com.d103.dddev.api.alert.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.d103.dddev.api.alert.dto.AlertListDto;
import com.d103.dddev.api.alert.dto.AlertUserKeyword;
import com.d103.dddev.api.alert.entity.AlertEntity;

@Repository
public interface AlertRepository extends JpaRepository<AlertEntity, Integer> {

	// 같은 레포와 연결된 같은 타입의 알림이(웹훅이) 이미 존재하는지 확인하기 위해 조회
	List<AlertEntity> findAllByRepositoryIdAndType(Integer repositoryId, String type);

	Optional<AlertEntity> findByUser_IdAndRepositoryIdAndType(Integer userId, Integer repositoryId, String type);

	// 알림 구독 중인 사용자, 키워드 조회
	List<AlertUserKeyword> findByRepositoryIdAndTypeAndUserIsNotNull(Integer repositoryId, String type);

	List<AlertEntity> findByUser_Id(Integer id);

	List<AlertEntity> findByWebhookId(Integer webhookId);

	@Query(value = "select a.id  from dddev.alert a join (select g.name, r.id from dddev.ground g join dddev.repository r on g.repository_id = r.id) gr on a.repository_id = gr.id where gr.id=? and a.user_id=?", nativeQuery = true)
	List<Integer> findByGroupdIdAndUser_Id(Integer groundId, Integer userId);

	List<AlertEntity> findByRepositoryId(Integer repositoryId);

	void deleteByRepositoryId(Integer repositoryId);

	@Query(value = "select a.id as id, a.type as type, a.user_id as userId, gr.name as groundName from dddev.alert a join (select g.name, r.id from dddev.ground g join dddev.repository r on g.repository_id = r.id) gr on a.repository_id = gr.id where a.user_id=?", nativeQuery = true)
	List<AlertListDto> findAlertEntityAndGroundName(Integer userId);

	@Query(value = "select a.id as id, a.type as type, a.user_id as userId, gr.name as groundName from dddev.alert a join (select g.name, r.id from dddev.ground g join dddev.repository r on g.repository_id = r.id and g.id = ?) gr on a.repository_id = gr.id where a.user_id=?", nativeQuery = true)
	List<AlertListDto> findAlertEntityAndGroundName(Integer groundId, Integer userId);

	Optional<AlertEntity> findByIdAndUser_Id(Integer alertId, Integer id);
}
