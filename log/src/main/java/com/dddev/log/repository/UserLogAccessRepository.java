package com.dddev.log.repository;

import com.dddev.log.dto.ElasticSearchLog;
import com.dddev.log.entity.UserLogAccess;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserLogAccessRepository extends CrudRepository<UserLogAccess, String> {
}
