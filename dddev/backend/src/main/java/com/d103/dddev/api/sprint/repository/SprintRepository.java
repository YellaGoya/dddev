package com.d103.dddev.api.sprint.repository;

import com.d103.dddev.api.sprint.repository.entity.SprintEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SprintRepository extends JpaRepository<SprintEntity, Integer> {
    Optional<List<SprintEntity>> findByGround_Id(int groundId);
}
