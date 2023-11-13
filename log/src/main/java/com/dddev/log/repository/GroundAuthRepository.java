package com.dddev.log.repository;

import com.dddev.log.entity.GroundAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroundAuthRepository extends JpaRepository<GroundAuth, Long> {
}
