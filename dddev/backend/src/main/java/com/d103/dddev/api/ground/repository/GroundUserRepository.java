package com.d103.dddev.api.ground.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.d103.dddev.api.ground.repository.dto.GroundUserDto;
import com.d103.dddev.api.user.repository.dto.UserDto;

public interface GroundUserRepository extends JpaRepository<GroundUserDto, Integer> {
	List<GroundUserDto> findByGroundDto_Id(Integer groundId);

	List<GroundUserDto> findByUserDto_Id(Integer userId);

	Optional<GroundUserDto> findByGroundDto_IdAndUserDto_IdAndIsOwnerIsTrue(Integer groundId, Integer userId);

	Optional<GroundUserDto> findByGroundDto_IdAndUserDto_Id(Integer groundId, Integer userId);

	@Query(value = "SELECT * FROM user u \n"
		+ "WHERE u.id NOT IN ( SELECT g.user_id FROM ground_user g WHERE ground_id = :groundId) \n"
		+ "AND u.role != \"ADMIN\" AND u.email = :email", nativeQuery = true)
	List<Map<String, String>> findUserByGroundIdNotAndEmail(@Param("groundId") Integer groundId,
		@Param("email") String email);
}
