package com.dddev.log.repository;

import com.dddev.log.entity.GroundAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroundAuthRepository extends JpaRepository<GroundAuth, Long> {
    @Query("SELECT g.token FROM GroundAuth g WHERE g.userId = :userId AND g.groundId  = :groundId")
    List<String> findToken( @Param("groundId") Integer groundId, @Param("userId") Integer userId);

    @Query("SELECT g FROM GroundAuth g WHERE g.groundId  = :groundId ORDER BY  g.localDateTime ASC")
    List<GroundAuth> findByGroundId(@Param("groundId") Integer groundId);
}
